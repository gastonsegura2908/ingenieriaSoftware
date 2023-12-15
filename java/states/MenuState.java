package states;

import java.awt.Graphics;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;

import constants.Constants;
import graphics.Assets;
import graphics.Sound;
import input.KeyBoard;
import main.Window;
import map.Map;
import ui.Button;
import ui.ButtonTimed;

public class MenuState extends State {

    private final ArrayList<Button> buttons;
    private final ArrayList<Button> allButtons;
    private int sizeIndex = 0;
    private final Map.Size[] mapSizes = {Map.Size.SMALL, Map.Size.MEDIUM, Map.Size.BIG};
    private final ButtonTimed muteButton;
    private final ButtonTimed mapsButton;

    private int buttonIndex = 0;

    /**
     * Constructor de la clase, crea 3 botones: PLAY, HIGHEST SCORES y EXIT.
     */
    public MenuState() {

        allButtons= new ArrayList<>(); // almacena todos los botones
        buttons = new ArrayList<>();   // no almacena los buttonTimed

        // boton map sizes
        mapsButton = new ButtonTimed(
                Assets.greyBtn,
                Assets.blueBtn,
                Constants.WIDTH / 2 - Assets.greyBtn.getWidth() / 2,
                Constants.HEIGHT / 2 + Assets.greyBtn.getHeight() * 2,
                mapSizes[sizeIndex].text,
                (params) -> {
                    sizeIndex++;
                    if (sizeIndex >= mapSizes.length)
                        sizeIndex = 0;
                    Button button = (Button) params[0];
                    button.setText(mapSizes[sizeIndex].text);
                }
        );
        // boton mute
        muteButton = new ButtonTimed(
                Assets.greyBtn,
                Assets.blueBtn,
                Constants.WIDTH - 250,
                80,
                Sound.mute ? "Music: OFF" : "Music: ON",
                (params) -> {
                    Sound.mute = !Sound.mute;
                    Button button = (Button) params[0];
                    button.setText(Sound.mute ? "Music: OFF" : "Music: ON");
                });


        // boton play
        Button playButton = new Button(
                Assets.greyBtn,
                Assets.blueBtn,
                Constants.WIDTH / 2 - Assets.greyBtn.getWidth() / 2,
                Constants.HEIGHT / 2 - Assets.greyBtn.getHeight() * 2,
                Constants.PLAY,
                (params) -> {
                    Map.setSize(mapSizes[sizeIndex]);

                    // close resources
                    muteButton.close();
                    mapsButton.close();

                    State.changeState(new NameState(false));
                }
        );

        // boton high scores
        Button highScoresButton = new Button(
                Assets.greyBtn,
                Assets.blueBtn,
                Constants.WIDTH / 2 - Assets.greyBtn.getWidth() / 2,
                Constants.HEIGHT / 2,
                Constants.HIGH_SCORES,
                (params) -> {
                    // close resources
                    muteButton.close();
                    mapsButton.close();
                    State.changeState(new ScoreState());
                }
        );

        // boton multiplayer
        Button multiplayerButton = new Button(
                Assets.greyBtn,
                Assets.blueBtn,
                20,
                80,
                "MULTIPLAYER",
                (params) -> {
                    Map.setSize(mapSizes[sizeIndex]);
                    // close resources
                    muteButton.close();
                    mapsButton.close();
                    State.changeState(new NameState(true));
                }
        );

        // boton exit
        Button exitButton = new Button(
                Assets.greyBtn,
                Assets.blueBtn,
                Constants.WIDTH / 2 - Assets.greyBtn.getWidth() / 2,
                Constants.HEIGHT / 2 + Assets.greyBtn.getHeight() * 4,
                Constants.EXIT,
                (params) -> System.exit(0)
        );

        /*
        Estrucutra del Menu
            - boton play
            - boton high scores
            - boton map sizes
            - boton exit
            - boton multiplayer
            - boton mute
        */

        allButtons.add(playButton);
        allButtons.add(highScoresButton);
        allButtons.add(mapsButton);
        allButtons.add(exitButton);
        allButtons.add(multiplayerButton);
        allButtons.add(muteButton);

        buttons.add(playButton);
        buttons.add(highScoresButton);
        buttons.add(exitButton);
        buttons.add(multiplayerButton);

        allButtons.get(buttonIndex).setSelected(true); // seleccionamos un boton
    }

    /**
     * Método para verificar constantemente si se posiciona el mouse sobre el botón e interactuar.
     *
     * @param dt Tiempo desde el último update en ns.
     */
    @Override
    public void update(float dt) {
        // update buttons
        for (Button b : buttons) {
            b.update();
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

        // verificar mute
        if (KeyBoard.MUTE && Window.keyBoardTimer.isTime()){
            Window.keyBoardTimer.reset();
            muteButton.doAction();
        }
    }

    /**
     * Método para graficar los botones en pantalla.
     *
     * @param g objeto a graficar.
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(Assets.fondoMenu,0,0,Constants.WIDTH,Constants.HEIGHT,null);

        muteButton.draw(g);
        mapsButton.draw(g);

        for (Button b : buttons) {
            b.draw(g);
        }
    }
}
