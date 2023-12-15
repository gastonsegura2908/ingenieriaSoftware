package network;
import network.packets.*;
import network.server.*;
import network.player.*;
import states.GameState;
import states.MultiplayerState;

import java.util.ArrayList;

/**
 * Server class
 * Handles Server and Client Connections
 * */
public class Server {

    // servicio de cliente
    private static GameClient socketClient = null;

    // servicio de servidor
    private static GameServer socketServer = null;

    private final PlayerMP player;        // instancia de jugador multijugador
    private MultiplayerState game;    // instancia de estado de juego

    private String ipAddress;
    private boolean isHost;

    public Server (String playerName, String ipAddress, boolean isHost) {
        this.ipAddress = ipAddress;
        this.isHost = isHost;
        player = new PlayerMP(0, playerName, null, -1);
    }

    /**
     * Starts socket threads, client and server
     */
    public void start() {

        if (isHost) {
            socketServer = new GameServer();
            socketServer.start();
        }


        socketClient = new GameClient(ipAddress);
        socketClient.start();

        login();

        socketClient.addObservador(game);
    }

    /**
     * Login to server
     */
    public void login() {
        Packet00Login loginPacket = new Packet00Login(player.getUsername(), player.score);
        if (socketServer != null) {
            // running server locally
            socketServer.addConnection(player, loginPacket);
        }
        loginPacket.writeData(socketClient);
    }

    /**
     * Update player's score
     * @param score - int, new score
     */
    public void updateScore(int score) {
        player.score = score;
        Packet02Score packet = new Packet02Score(player.getUsername(), score);
        packet.writeData(socketClient);
    }

    /**
     * Disconnect from server and close sockets
     */
    public void disconnect() {
        Packet01Disconnect packet = new Packet01Disconnect(player.getUsername());
        packet.writeData(socketClient);
        socketClient.disconnect();

        if (socketServer != null) {
            // running server locally
            socketServer.disconnect();
        }

        socketClient = null;
        socketServer = null;
    }

    public ArrayList<PlayerMP> getConnectedPlayers() {
        return socketClient.getConnectedPlayers();
    }

    public void setGame(MultiplayerState game) {
        this.game = game;
    }

    /**
     * Checks if socket is connected
     * @return boolean
     */
    public boolean isDisconnected() {
        if (socketClient == null)
            return true;

        if (socketServer == null && isHost)
            return true;

        if (isHost)
            return socketClient.isDisconnected() || socketServer.isDisconnected();

        return socketClient.isDisconnected();
    }
}
