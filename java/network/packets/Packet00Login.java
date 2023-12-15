package network.packets;


import network.server.*;

public class Packet00Login extends Packet {
    private final String username;
    private final int score;

    /**
     * When retrieving data
     *
     * @param data data retrieved (username)
     */
    public Packet00Login(byte[] data) {
        super(0);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.score = Integer.parseInt(dataArray[1]);
    }

    /**
     * When sending from the client
     *
     * @param username to log in
     */
    public Packet00Login(String username, int score) {
        super(0);
        this.username = username;
        this.score = score;
    }

    public int getScore() {
        return score;
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
        return ("00" + this.username + "," + this.score).getBytes();
    }

    public String getUsername() {
        return username;
    }
}
