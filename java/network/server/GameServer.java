package network.server;

import network.packets.*;
import network.player.*;
import observer.*;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

import static network.packets.Packet.PacketTypes.*;

/**
 * GameServer class, provee el servicio de servidor,
 * solo se ejecuta en la maquina del host de la partida
 */
public class GameServer extends Thread {
    private DatagramSocket socket;
    private ArrayList<PlayerMP> connectedPlayers = new ArrayList<>();
    private boolean disconnected = false;

    public GameServer() {
        try {
            this.socket = new DatagramSocket(3001);
        } catch (SocketException e) {
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
                // throw new RuntimeException(e);
                System.out.println("Error: " + e.getMessage());
                break;
            }

            this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
        }
        disconnected = true;
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet = null;

        if (type == INVALID) {
            return;
        }

//        System.out.print("server received: ");

        if (type == LOGIN) {
            packet = new Packet00Login(data);
//            System.out.println("[" + address.getHostAddress() + ":" + port + "]" + ((Packet00Login) packet).getUsername() + " has connected!");
            PlayerMP player = new PlayerMP(((Packet00Login) packet).getScore(), ((Packet00Login) packet).getUsername(), address, port);
            this.addConnection(player, (Packet00Login) packet);
        }

        if (type == DISCONNECT) {
            packet = new Packet01Disconnect(data);
//            System.out.println("[" + address.getHostAddress() + ":" + port + "]" + ((Packet01Disconnect) packet).getUsername() + " has left!");
            this.removeConnection((Packet01Disconnect) packet);
        }

        if (type == SCORE) {
            packet = new Packet02Score(data);
//            System.out.println(((Packet02Score) packet).getUsername() + " has scored " + ((Packet02Score) packet).getScore() );
            this.handleScore((Packet02Score) packet);
        }

    }

    private void handleScore(Packet02Score packet) {
        int index = getPlayerMpIndex(packet.getUsername());

        if (index == -1)
            return;

        this.connectedPlayers.get(index).score = packet.getScore();

        packet.writeData(this); // notify other clients

    }

    public void removeConnection(Packet01Disconnect packet) {
        int playerIndex = getPlayerMpIndex(packet.getUsername());

        if (playerIndex != -1) {
            this.connectedPlayers.remove(playerIndex);
            packet.writeData(this); // notify al clients of disconnection
        }
    }

    public int getPlayerMpIndex(String username) {
        int index = 0;
        for (PlayerMP p : this.connectedPlayers) {
            if (p.getUsername().equals(username))
                return index;
            index++;
        }
        return -1;
    }

    public void addConnection(PlayerMP player, Packet00Login packet) {
        boolean alreadyConnected = false;
        for (PlayerMP p : this.connectedPlayers) {
            if (player.getUsername().equalsIgnoreCase(p.getUsername())) {
                // already connected, update info
                if (p.ipAddress == null)
                    p.ipAddress = player.ipAddress;
                if (p.port == -1)
                    p.port = player.port;
                alreadyConnected = true;
            } else {
                // notify other players who joined the game
                sendData(packet.getData(), p.ipAddress, p.port);

                // notify new player about other connected players
                Packet00Login packetForNewPlayer = new Packet00Login(p.getUsername(), p.score);
                sendData(packetForNewPlayer.getData(), player.ipAddress, player.port);
            }

        }
        if (!alreadyConnected) {
            this.connectedPlayers.add(player);
        }
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        if (disconnected) return;
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            // throw new RuntimeException(e);
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void sendToAllClients(byte[] data) {
        for (PlayerMP p : connectedPlayers) {
            sendData(data, p.ipAddress, p.port);
        }
    }

    public void disconnect() {
        if (socket == null) return;
        if (socket.isClosed()) return;
        socket.close();
    }

    public boolean isDisconnected() {
        return disconnected;
    }
}

