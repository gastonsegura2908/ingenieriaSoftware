package states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import constants.Constants;
import gameObjects.Message;
import gameObjects.Meteor;
import gameObjects.Player;
import gameObjects.PowerUp;
import gameObjects.PowerUpTypes;
import gameObjects.Size;
import gameObjects.Ufo;
import graphics.Assets;
import graphics.Sound;
import io.JSONParser;
import io.ScoreData;
import main.Window;
import map.Map;
import math.Vector2D;
import ui.Action;
import input.KeyBoard;
import input.Timer;
import ui.Button;

/**
 * Clase que informa el estado del juego
 * AmovingObjects: actualiza y dibuja todos los objetos que se muevan en el juego
 * meteors: cant de meteoros iniciales en una oleada
 * waves: cant de oleadas
 */
public class GameState extends State {
    public static final Vector2D PLAYER_START_POSITION = new Vector2D(Constants.HALF_WIDTH - (double) Assets.player.getWidth() / 2,
            Constants.HALF_HEIGHT - (double) Assets.player.getHeight() / 2);

    protected final Player player;
    private final ArrayList<Message> messages = new ArrayList<>();
    protected int score = 0;
    protected int lives = 3;
    private int meteors;
    private int waves = 1;
    private final Sound backgroundMusic;
    private long gameOverTimer;
    protected boolean gameOver;

    private long ufoSpawner;
    private long powerUpSpawner;
    private final Map map;
    private final Timer retardoPause;
    private boolean ApplyretardoPause=true;

    /**
     * constructor
     */
    public GameState(String playerName)  {
        map = Map.getMap();
        //timer para controlar retardo al pulsar la tecla pausa
        retardoPause=new Timer(250);

        player = new Player(PLAYER_START_POSITION, new Vector2D(),
                Constants.PLAYER_MAX_VEL, Assets.player, playerName, this);

        gameOver = false;

        meteors = 1;

        startWave();

        backgroundMusic = new Sound(Assets.backgroundMusic);
        backgroundMusic.changeVolume(-20.0f);

        if(backgroundMusic.is_playing() && Sound.mute) {
            backgroundMusic.stop();
        } else if (!backgroundMusic.is_playing() && !Sound.mute) {
            backgroundMusic.loop();
        }

        gameOverTimer = 0;
        ufoSpawner = 0;
        powerUpSpawner = 0;

        gameOver = false;

    }

    /**
     * le agrega valor a la variable score(puntaje) cada vez que se destruya un ufo o meteoro
     *
     * @param value    valor segun si se destruyo un ufo o un meteoro
     * @param position posicion del texto
     */
    public void addScore(int value, Vector2D position) {

        Color c = Color.WHITE;
        String text = "+" + value + " score";
        if (player.isDoubleScoreOn()) {
            c = Color.YELLOW;
            value = value * 2;
            text = "+" + value + " score" + " (X2)";
        }

        score += value;
//        server.updateScore(score);
        messages.add(new Message(position, true, text, c, false, Assets.fontMed));
    }

    /**
     * inicia cada oleada de meteoros,le pone las posiciones. mas oleadas,mas cant de meteoritos
     */
    private void startWave() {

        messages.add(new Message(new Vector2D(Constants.HALF_WIDTH, Constants.HALF_HEIGHT), false,
                "WAVE " + waves, Color.WHITE, true, Assets.fontBig));

        double x, y;

        for (int i = 0; i < meteors; i++) {

            x = i % 2 == 0 ? Math.random() * Constants.WIDTH : 0;
            y = i % 2 == 0 ? 0 : Math.random() * Constants.HEIGHT;

            BufferedImage texture = Assets.bigs[(int) (Math.random() * Assets.bigs.length)];

            new Meteor(
                    new Vector2D(x, y),
                    player.getRoom(),
                    new Vector2D(0, 1).setDirection(Math.random() * Math.PI * 2),
                    Constants.METEOR_INIT_VEL * Math.random() + 1,
                    texture,
                    this,
                    Size.BIG
            );

        }
        meteors++;
        waves++;
    }

    /**
     * nos permite crear el UFO, ademas ponemos la posicion de los 4 nodos que sigue el ufo
     */
    private void spawnUfo() {

        int rand = (int) (Math.random() * 2);

        double x = rand == 0 ? (Math.random() * Constants.WIDTH) : Constants.WIDTH;
        double y = rand == 0 ? Constants.HEIGHT : (Math.random() * Constants.HEIGHT);

        new Ufo(
                new Vector2D(x, y),
                new Vector2D(),
                Constants.UFO_MAX_VEL,
                Assets.ufo,
                this
        );
    }


    /**
     * definimos los casos  en los cuales aparecen los super poderes,como son el doble disparo,escudo,vida,puntaje
     */
    private void spawnPowerUp() {

        final int x = (int) ((Constants.WIDTH - Assets.orb.getWidth()) * Math.random());
        final int y = (int) ((Constants.HEIGHT - Assets.orb.getHeight()) * Math.random());

        int index = (int) (Math.random() * (PowerUpTypes.values().length));

        PowerUpTypes p = PowerUpTypes.values()[index];

        final String text = p.text;
        Action action = null;
        Vector2D position = new Vector2D(x, y);

        switch (p) {
            case LIFE:
                action = (params) -> {
                    lives++;
                    messages.add(new Message(
                            position,
                            false,
                            text,
                            Color.GREEN,
                            false,
                            Assets.fontMed
                    ));
                };
                break;
            case SHIELD:
                action = (params) -> {
                    player.setShield();
                    messages.add(new Message(
                            position,
                            false,
                            text,
                            Color.DARK_GRAY,
                            false,
                            Assets.fontMed
                    ));
                };
                break;
            case SCORE_X2:
                action = (params) -> {
                    player.setDoubleScore();
                    messages.add(new Message(
                            position,
                            false,
                            text,
                            Color.YELLOW,
                            false,
                            Assets.fontMed
                    ));
                };
                break;
            case FASTER_FIRE:
                action = (params) -> {
                    player.setFastFire();
                    messages.add(new Message(
                            position,
                            false,
                            text,
                            Color.BLUE,
                            false,
                            Assets.fontMed
                    ));
                };
                break;
            case SCORE_STACK:
                action = (params) -> {
                    score += 1000;
                    messages.add(new Message(
                            position,
                            false,
                            text,
                            Color.MAGENTA,
                            false,
                            Assets.fontMed
                    ));
                };
                break;
            case DOUBLE_GUN:
                action = (params) -> {
                    player.setDoubleGun();
                    messages.add(new Message(
                            position,
                            false,
                            text,
                            Color.ORANGE,
                            false,
                            Assets.fontMed
                    ));
                };
                break;
            default:
                break;
        }

        new PowerUp(
                position,
                player.getRoom(),
                p.texture,
                action,
                this
        );


    }

    /**
     * Actualiza la posición del jugador,objetos,animaciones.
     *
     * @param dt tiempo desde el ultimo update en ns
     */
    public void update(float dt) {
        if(backgroundMusic.is_playing() && Sound.mute) {
            backgroundMusic.stop();
        } else if (!backgroundMusic.is_playing() && !Sound.mute) {
            backgroundMusic.loop();
        }

        //si el tiempo de retardo se cumplio se ejecuta la accion de la tecla
        if (KeyBoard.PAUSE && Window.keyBoardTimer.isTime()){
            backgroundMusic.stop();
            Window.keyBoardTimer.reset();
            State.changeState(new PauseState(this));
        }

        if (gameOver)
            gameOverTimer += dt;

        powerUpSpawner += dt;
        ufoSpawner += dt;

        map.update(dt);

        if (gameOverTimer > Constants.GAME_OVER_TIME) {

            try {
                ArrayList<ScoreData> dataList = JSONParser.readFile();
                dataList.add(new ScoreData(score, player.getName()));
                JSONParser.writeFile(dataList);

            } catch (IOException e) {
                e.printStackTrace();
            }

            backgroundMusic.stop();

            //State.changeState(new MenuState());
            State.changeState(new ScoreState());

            // 'delete' map
            map.clear();
        }

        if (powerUpSpawner > Constants.POWER_UP_SPAWN_TIME) {
            spawnPowerUp();
            powerUpSpawner = 0;
        }

        if (ufoSpawner > Constants.UFO_SPAWN_RATE) {
            spawnUfo();
            ufoSpawner = 0;
        }


        // verifico que queden meteoros, si no queda ninguno iniciamos otra ola
        int meteorsLeft = map.countMeteors();

        if (meteorsLeft == 0)
            startWave();

    }

    /**
     * dibuja:animaciones,score,cant de oleadas,
     * con g2d mejoramos el tema de los pixeles de la nave al girar.
     *
     * @param g objeto de graficos
     */
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
        drawLives(g);
    }

    /**
     * dibuja el puntaje.
     * creamos un vector para dar la posicion en la que estara el puntaje.
     *
     * @param g objeto de graficos
     */
    public void drawScore(Graphics g) {

        Vector2D pos = new Vector2D(850, 25);

        String scoreToString = Integer.toString(score);

        for (int i = 0; i < scoreToString.length(); i++) {

            g.drawImage(Assets.numbers[Integer.parseInt(scoreToString.substring(i, i + 1))],
                    (int) pos.getX(), (int) pos.getY(), null);
            pos.setX(pos.getX() + 20);

        }

    }

    /**
     * dibuja la cantidad de vidas que tiene la nave
     *
     * @param g objeto de graficos
     */
    public void drawLives(Graphics g) {

        if (lives < 1)
            return;

        Vector2D livePosition = new Vector2D(25, 25);

        g.drawImage(Assets.life, (int) livePosition.getX(), (int) livePosition.getY(), null);

        g.drawImage(Assets.numbers[10], (int) livePosition.getX() + 40,
                (int) livePosition.getY() + 5, null);

        String livesToString = Integer.toString(lives);

        Vector2D pos = new Vector2D(livePosition.getX(), livePosition.getY());

        for (int i = 0; i < livesToString.length(); i++) {
            int number = Integer.parseInt(livesToString.substring(i, i + 1));

            if (number <= 0)
                break;
            g.drawImage(Assets.numbers[number],
                    (int) pos.getX() + 60, (int) pos.getY() + 5, null);
            pos.setX(pos.getX() + 20);
        }

    }

    /**
     * metodo get que nos da el jugador actual
     *
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * resta una vida a la cantidad total
     *
     * @param position posicion del texto
     * @return lives true si las vidas son mayores a 0,sino false
     */
    public boolean subtractLife(Vector2D position) {
        lives--;

        Message lifeLostMesg = new Message(
                position,
                false,
                "-1 LIFE",
                Color.RED,
                false,
                Assets.fontMed
        );
        messages.add(lifeLostMesg);

        return lives > 0;
    }

    /**
     * despliega el mensaje de que el juego termino
     */
    public void gameOver() {
        Message gameOverMsg = new Message(
                PLAYER_START_POSITION,
                true,
                "GAME OVER",
                Color.WHITE,
                true,
                Assets.fontBig);

        this.messages.add(gameOverMsg);
        gameOver = true;
    }

    /**
     * nos da la cantidad de oleadas actuales
     *
     * @return waves cantidad de oleadas
     */
    public int getWaves() {
        return waves;
    }

}
