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
import io.JSONParser;
import io.ScoreData;
import math.Vector2D;
import ui.Button;

/**
 * clase que informa el estado de puntajes. hereda de State
 */
public class ScoreState extends State {

    private final Button returnButton;

    private final PriorityQueue<ScoreData> highScores;

    private final Comparator<ScoreData> scoreComparator;

    /**
     * constructor
     */
    public ScoreState() {
        returnButton = new Button(
                Assets.greyBtn,
                Assets.blueBtn,
                Assets.greyBtn.getHeight(),
                Constants.HEIGHT - Assets.greyBtn.getHeight() * 2,
                Constants.RETURN,
                (params) -> State.changeState(new MenuState())
        );

        returnButton.setSelected(true);

        scoreComparator = new Comparator<ScoreData>() {

            /**
             * define el orden de los datos(puntajes). el objetivo es que los puntajes
             * esten de mayor a menor
             * @param e1 - ScoreData 1
             * @param e2 - ScoreData 2
             * @return int -1 o 1 o 0
             */
            @Override
            public int compare(ScoreData e1, ScoreData e2) {
                return Integer.compare(e1.getScore(), e2.getScore());
            }
        };

        highScores = new PriorityQueue<>(10, scoreComparator);

        try {
            ArrayList<ScoreData> dataList = JSONParser.readFile();

            highScores.addAll(dataList);

            while (highScores.size() > 10) {
                highScores.poll();
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * actualiza returnButton
     *
     * @param dt delta de tiempo
     */
    @Override
    public void update(float dt) {
        returnButton.update();
    }

    /**
     * dibuja el ranking de scores de mayor a menor puntaje
     *
     * @param g - Objeto de graficos
     */
    @Override
    public void draw(Graphics g) {

        g.drawImage(Assets.fondoScore,0,0,Constants.WIDTH,Constants.HEIGHT,null);

        returnButton.draw(g);

        ScoreData[] auxArray = highScores.toArray(new ScoreData[0]);

        Arrays.sort(auxArray, scoreComparator);


        Vector2D scorePos = new Vector2D(
                Constants.HALF_WIDTH - 300,
                100
        );
        Vector2D datePos = new Vector2D(
                Constants.HALF_WIDTH + 300,
                100
        );
        Vector2D namePos = new Vector2D(
                Constants.HALF_WIDTH,
                100
        );

        Text.drawText(g, Constants.SCORE, scorePos, true, Color.BLUE, Assets.fontBig);
        Text.drawText(g, Constants.NAME, namePos, true, Color.BLUE, Assets.fontBig);
        Text.drawText(g, Constants.DATE, datePos, true, Color.BLUE, Assets.fontBig);

        int offset = 40;

        scorePos.setY(scorePos.getY() + offset);
        datePos.setY(datePos.getY() + offset);
        namePos.setY(namePos.getY() + offset);

        for (int i = auxArray.length - 1; i > -1; i--) {

            ScoreData d = auxArray[i];

            Text.drawText(g, Integer.toString(d.getScore()), scorePos, true, Color.RED, Assets.fontMed);
            Text.drawText(g, d.getName(), namePos, true, Color.RED, Assets.fontMed);
            Text.drawText(g, d.getDate(), datePos, true, Color.RED, Assets.fontMed);

            scorePos.setY(scorePos.getY() + offset);
            namePos.setY(namePos.getY() + offset);
            datePos.setY(datePos.getY() + offset);

        }

    }

}
