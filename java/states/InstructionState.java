package states;

import java.awt.Color;
import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

import constants.Constants;
import graphics.Assets;
import graphics.Text;
import input.KeyBoard;
import io.JSONParser;
import io.ScoreData;
import math.Vector2D;
import ui.Action;
import ui.Button;
import input.Timer;

/**
 * clase que muestra los controles. hereda de State
 */
public class InstructionState extends State {

    private Button goMenuButton;

    /**
     * constructor
     */
    public InstructionState() {
        goMenuButton = new Button(
                Assets.greyBtn,
                Assets.blueBtn,
                Constants.WIDTH - 250,
                Constants.HEIGHT - 150,
                Constants.MENU,
                (params) -> {
                    State.changeState(new MenuState());
                }
        );
        goMenuButton.setSelected(true);
    }

    /**
     * actualiza cambios de estado de iconos y botones
     *
     * @param dt delta de tiempo
     */
    @Override
    public void update(float dt) {
        goMenuButton.update();
    }

    /**
     * grafica la pantalla
     *
     * @param g
     */
    @Override
    public void draw(Graphics g) {

        g.drawImage(Assets.fondoInstrucions,0,0,Constants.WIDTH,Constants.HEIGHT,null);

        g.drawImage(Assets.instructions,200,200,600,250,null);
        g.drawImage(Assets.ufo,80,450,40,40,null);
        g.drawImage(Assets.meds[1],120,180,40,40,null);
        g.drawImage(Assets.bigs[1],850,360,60,60,null);
        g.drawImage(Assets.ufo,700,80,40,40,null);

        goMenuButton.draw(g);

        Vector2D textPos = new Vector2D(
                Constants.HALF_WIDTH,
                Constants.HALF_HEIGHT - Assets.greyBtn.getHeight() * 4
        );

        Text.drawText(g, "Instrucciones", textPos, true, Color.BLUE, Assets.fontBig);

    }

}
