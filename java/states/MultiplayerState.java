package states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import graphics.Text;
import network.Server;
import network.player.PlayerMP;
import observer.*;
import gameObjects.Message;
import graphics.Assets;
import math.Vector2D;

/**
 * Clase que informa el estado del juego
 * AmovingObjects: actualiza y dibuja todos los objetos que se muevan en el juego
 * meteors: cant de meteoros iniciales en una oleada
 * waves: cant de oleadas
 */
public class MultiplayerState extends GameState implements Observador {
    private final ArrayList<Message> messages = new ArrayList<>();

    private ArrayList<PlayerMP> connectedPlayers = new ArrayList<>();
    private final Server server;
    /**
     * constructor
     */
    public MultiplayerState(String playerName, Server server)  {
        super(playerName);
        this.server = server;
        server.setGame(this);
        server.start();
    }

    @Override
    public void addScore(int value, Vector2D position) {

        Color c = Color.WHITE;
        String text = "+" + value + " score";

        if (player.isDoubleScoreOn()) {
            c = Color.YELLOW;
            value = value * 2;
            text = "+" + value + " score" + " (X2)";
        }

        score += value;

        if (!server.isDisconnected())
            server.updateScore(score);
        else {
            gameOver();
        }

        messages.add(new Message(position, true, text, c, false, Assets.fontMed));
    }


    /**
     * dibuja:animaciones,score,cant de oleadas,
     * con g2d mejoramos el tema de los pixeles de la nave al girar.
     *
     * @param g objeto de graficos
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        player.drawRoom(g);

        for (int i = 0; i < messages.size(); i++) {
            messages.get(i).draw(g2d);
            if (messages.get(i).isDead()) {
                messages.remove(i);
                i--;
            }
        }
        drawScore(g);
        drawMPScores(g);
        drawLives(g);
    }

    public void drawMPScores(Graphics g) {
        Vector2D pos = new Vector2D(850, 75);
        Graphics2D g2d = (Graphics2D) g;

        int i = 0;
        for (PlayerMP p : connectedPlayers){
            if (i == 0) {
                Text.drawText(g2d, (i+1) + ". " + p.username+" "+ p.score, pos, false, Color.YELLOW, Assets.fontMed);
                i++;
                continue;
            }
            pos.setY(pos.getY() + 50);
            Text.drawText(g2d, (i+1) + ". " + p.username+" "+ p.score, pos, false, Color.MAGENTA, Assets.fontMed);
            i++;
        }

    }

    /**
     * despliega el mensaje de que el juego termino
     */
    @Override
    public void gameOver() {
        Message gameOverMsg = new Message(
                PLAYER_START_POSITION.add(new Vector2D(0, -100)),
                true,
                "GAME OVER",
                Color.WHITE,
                true,
                Assets.fontBig);

        this.messages.add(gameOverMsg);

        if (connectedPlayers.size() == 0) {
            closeServer();
            gameOver = true;
            return;
        }

        // mostrar al ganador
        int highestConnectedPlayerScore = connectedPlayers.get(0).score;

        String winner = player.getName();
        int winnerScore = score;

        if (highestConnectedPlayerScore > score){
            winner = connectedPlayers.get(0).username;
            winnerScore = highestConnectedPlayerScore;
        }

        Message winnerMsg = new Message(
                PLAYER_START_POSITION,
                true,
                winner+" WINS",
                Color.YELLOW,
                true,
                Assets.fontBig);

        Message winnerPointsMsg = new Message(
                PLAYER_START_POSITION.add(new Vector2D(0, 50)),
                true,
                " "+winnerScore+" POINTS",
                Color.GREEN,
                true,
                Assets.fontBig);


        this.messages.add(winnerMsg);
        this.messages.add(winnerPointsMsg);

        closeServer();
        gameOver = true;
    }

    @Override
    public void update(){
        if (server.isDisconnected()) {
            gameOver();
            return;
        }
        connectedPlayers = server.getConnectedPlayers();
    }

    public void closeServer() {
        if (server.isDisconnected()) return;
        server.disconnect();
    }
}
