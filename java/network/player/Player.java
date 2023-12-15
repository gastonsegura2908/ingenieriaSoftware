package network.player;

public class Player implements Comparable<Player> {
    public int score;
    public String username;
    private int x, y;

    public Player(int score, String username, int x, int y) {
        this.score = score;
        this.username = username;
        this.x = x;
        this.y = y;
    }

    public String getUsername() {
        return username;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Compara 2 Player segun el puntaje
     */
    @Override
    public int compareTo(Player p) {
        return Integer.compare(score, p.score);
    }
}
