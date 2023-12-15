package network.player;

import java.net.InetAddress;

public class PlayerMP extends Player {
    public InetAddress ipAddress;
    public int port;

    public PlayerMP(int score, String username, InetAddress ipAddress, int port) {
        super(score, username, 0, 0);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public PlayerMP(PlayerMP p) {
        super(p.score, p.username, p.getX(), p.getY());
        ipAddress = p.ipAddress;
        port = p.port;
    }
}