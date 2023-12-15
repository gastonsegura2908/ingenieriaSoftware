package network.server;
import network.packets.*;
import network.player.*;
import observer.*;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;

import static network.packets.Packet.PacketTypes.*;

public class GameClient extends Thread implements Sujeto {
    private InetAddress ipAddress = null;
    private DatagramSocket socket = null;
    private final ArrayList<Observador> observadores = new ArrayList<>();
    private final ArrayList<PlayerMP> connectedPlayers = new ArrayList<>();
    private final Object playersLock = new Object();

    private boolean disconnected = false;

    public GameClient(String ipAddress) {
        try {
            this.socket = new DatagramSocket();
            this.ipAddress = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException | SocketException e) {
            // throw new RuntimeException(e);
            System.out.println("Error: " + e.getMessage());
            disconnected = true;
        }

    }

    public void run() {
        if (socket == null || disconnected)
            return;

        //        listen for incoming packets
        while (!disconnected && !socket.isClosed()) {

            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
                break;
            }

            this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());

        }

        disconnected = true;
        notifyObservadores();

    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet;

        if (type == INVALID) {
            return;
        }

//        System.out.print("client received: ");

        if (type == LOGIN) {
            // join player to connectedPlayers list
            packet = new Packet00Login(data);

            System.out.println("[" + address.getHostAddress() + ":" + port + "]" + ((Packet00Login) packet).getUsername() + " has joined!");

            handleLogin((Packet00Login) packet, address, port);
        }

        if (type == DISCONNECT) {
            // remove player from connectedPlayers list
            packet = new Packet01Disconnect(data);
//            System.out.println("[" + address.getHostAddress() + ":" + port + "]" + ((Packet01Disconnect) packet).getUsername() + " has left!");

            handleDisconnect((Packet01Disconnect) packet);
        }

        if (type == SCORE) {
            // update score of player with username
            packet = new Packet02Score(data);
//            System.out.println(((Packet02Score) packet).getUsername() + " scored " + ((Packet02Score) packet).getScore());
            handleScore((Packet02Score) packet);
        }

        // cambio el estado, notificar observadores
        notifyObservadores();

    }

    /**
     * Add player to connectedPlayers list
     * @param packet    -   Packet00Login, received packet
     * @param address   -   InetAddress, address of sender
     * @param port      -   int
     */
    private void handleLogin(Packet00Login packet, InetAddress address, int port) {
        // join the game client side -> add player to list
        PlayerMP player = new PlayerMP(packet.getScore(), packet.getUsername(), address, port);
        synchronized (playersLock) {
            connectedPlayers.add(player);
        }
    }

    /**
     * Remove player from connectedPlayers list
     * @param packet -  Packet01Disconnect, received packet
     */
    private void handleDisconnect(Packet01Disconnect packet) {
        // join the game client side -> add player to list
        String username = packet.getUsername();

        // remove player with username
        synchronized (playersLock) {
            for (PlayerMP player : connectedPlayers) {
                if (player.getUsername().equals(username)) {
                    connectedPlayers.remove(player);
                    break;
                }
            }
        }
    }

    /**
     * Update score of player with username
     * @param packet -  Packet02Score, received packet
     */
    private void handleScore(Packet02Score packet) {
        String username = packet.getUsername();
        int score = packet.getScore();

        // update score player with username
        synchronized (playersLock) {
            for (PlayerMP player : connectedPlayers) {
                if (player.getUsername().equals(username)) {
                    player.score = score;
                    break;
                }
            }
            connectedPlayers.sort(Collections.reverseOrder());
        }
    }

    public void sendData(byte[] data) {
        if (disconnected) return;
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 3001);
        try {
            socket.send(packet);
        } catch (IOException e) {
            // throw new RuntimeException(e);
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void addObservador(Observador o) {
        observadores.add(o);
    }

    @Override
    public void removeObservador(Observador o) {
        observadores.remove(o);
    }

    @Override
    public void notifyObservadores() {
        for (Observador o : observadores) {
            o.update();
        }
    }

    /**
     * Get players state, used by Observers
     * @return ArrayList<PlayerMP> - list of connected players
     */
    public ArrayList<PlayerMP> getConnectedPlayers() {
        ArrayList<PlayerMP> players = new ArrayList<>();
        synchronized (playersLock) {
            for (PlayerMP p : connectedPlayers)
                players.add(new PlayerMP(p));
        }
        return players;
    }

    /**
     * Disconnect client and close socket
     */
    public void disconnect() {
        // desconectar cliente
        if (socket == null) return;
        if (socket.isClosed()) return;
        socket.close();
    }

    public boolean isDisconnected() {
        return disconnected;
    }
}

