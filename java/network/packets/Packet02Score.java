package network.packets;


import network.server.*;

public class Packet02Score extends Packet {
    private final String username;
    private final int score;

    /**
     * When retrieving data
     *
     * @param data data retrieved (username)
     */
    public Packet02Score(byte[] data) {
        super(2);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.score = Integer.parseInt(dataArray[1]);
    }

    /**
     * When sending from the client
     *
     * @param username to log in
     */
    public Packet02Score(String username, int score) {
        super(2);
        this.username = username;
        this.score = score;
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
        return ("02" + this.username + "," + this.score).getBytes();
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }
}

