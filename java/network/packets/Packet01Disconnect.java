package network.packets;


import network.server.*;

public class Packet01Disconnect extends Packet {
    private final String username;

    /**
     * When retrieving data
     *
     * @param data data retrieved (username)
     */
    public Packet01Disconnect(byte[] data) {
        super(1);
        this.username = readData(data);
    }

    /**
     * When sending from the client
     *
     * @param username to log in
     */
    public Packet01Disconnect(String username) {
        super(1);
        this.username = username;
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendToAllClients(getData());
    }

    @Override
    public byte[] getData() {
        return ("01" + this.username).getBytes();
    }

    public String getUsername() {
        return username;
    }
}
