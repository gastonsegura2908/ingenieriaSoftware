package states;

import constants.Constants;
import graphics.Assets;
import graphics.Sound;
import graphics.Text;
import input.KeyBoard;
import input.Timer;
import map.Map;
import math.Vector2D;
import ui.Action;
import ui.Button;
import ui.ButtonTimed;
import main.Window;

import java.awt.*;
import java.util.ArrayList;

/**
 * Estado de Pausa donde se guarda el estado
 * del juego pudiendose reanudar luego
 */
public class PauseState extends State {
    private final ArrayList<Button> buttons;
    private final ArrayList<Button> allButtons;
    private final GameState gameState;
    private MultiplayerState multiplayerState;
    private ButtonTimed muteButton;
    private boolean multiplayer = false;
    private int buttonIndex = 0;
    public PauseState(GameState gameState) {

        this.gameState = gameState;
        allButtons= new ArrayList<>(); // lista con todos los botones
        buttons = new ArrayList<>();   // no tiene los botones temporizados

        if (gameState instanceof MultiplayerState) {
            multiplayer = true;
            multiplayerState = (MultiplayerState) gameState;
        }

        // boton mute
        muteButton = new ButtonTimed(
                Assets.greyBtn,
                Assets.blueBtn,
                20,
                80,
                Sound.mute ? "Music: OFF" : "Music: ON",
                (params) -> {
                    Sound.mute = !Sound.mute;
                    Button button = (Button) params[0];
                    button.setText(Sound.mute ? "Music: OFF" : "Music: ON");
                });



        // boton resume
        Button resumeButton = new Button(
                Assets.greyBtn,
                Assets.blueBtn,
                Constants.WIDTH / 2 - Assets.greyBtn.getWidth() / 2,
                Constants.HEIGHT / 2 - Assets.greyBtn.getHeight() * 2,
                Constants.RESUME,
                (params) -> {
                    muteButton.close();
                    State.changeState(gameState);
                }
        );

        // boton exit

        Button exitButton = new Button(
                Assets.greyBtn,
                Assets.blueBtn,
                Constants.WIDTH / 2 - Assets.greyBtn.getWidth() / 2,
                Constants.HEIGHT / 2,
                Constants.EXIT,
                (params) -> {
                    Map.getMap().clear();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    muteButton.close();

                    // close server
                    if (multiplayer) multiplayerState.closeServer();

                    //State.changeState(new MenuState());
                    State.changeState(new MenuState());
                }
        );

        /*
            Estructura de Botones:
            - boton resume
            - boton exit
            - boton mute
        */
        allButtons.add(resumeButton);
        allButtons.add(exitButton);
        allButtons.add(muteButton);
        allButtons.get(buttonIndex).setSelected(true);

        buttons.add(resumeButton);
        buttons.add(exitButton);

    }

    @Override
    public void update(float dt) {
        for (Button b : buttons) {
            b.update();
        }
        if (KeyBoard.PAUSE && Window.keyBoardTimer.isTime()) {
            Window.keyBoardTimer.reset();
            allButtons.get(0).doAction();
        }

        // verificar mute
        if (KeyBoard.MUTE && Window.keyBoardTimer.isTime()){
            Window.keyBoardTimer.reset();
            muteButton.doAction();
        }

        // verificar si se cambia de boton seleccionado
        if (KeyBoard.DOWN && Window.keyBoardTimer.isTime()) {
            Window.keyBoardTimer.reset();

            allButtons.get(buttonIndex).setSelected(false);

            buttonIndex++;
            if (buttonIndex >= allButtons.size())
                buttonIndex = 0;

            allButtons.get(buttonIndex).setSelected(true);
        }

        if (KeyBoard.UP && Window.keyBoardTimer.isTime()) {
            Window.keyBoardTimer.reset();

            allButtons.get(buttonIndex).setSelected(false);

            buttonIndex--;
            if (buttonIndex < 0)
                buttonIndex =  allButtons.size() - 1;

            allButtons.get(buttonIndex).setSelected(true);
        }
    }

    @Override
    public void draw(Graphics g) {

        g.drawImage(Assets.fondoPause,0,0,Constants.WIDTH,Constants.HEIGHT,null);

        // draw buttons
        muteButton.draw(g);
        for (Button b : buttons) {
            b.draw(g);
        }

        // draw wave
        Vector2D textPos = new Vector2D(
                Constants.HALF_WIDTH,
                Constants.HALF_HEIGHT - Assets.greyBtn.getHeight() * 4
        );

        Text.drawText(g, "Wave " + gameState.getWaves(), textPos, true, Color.RED, Assets.fontBig);

        gameState.drawScore(g);
        if (multiplayer) multiplayerState.drawMPScores(g);

        gameState.drawLives(g);
    }
}
