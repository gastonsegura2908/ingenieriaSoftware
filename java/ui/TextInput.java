package ui;

import constants.Constants;
import graphics.Assets;
import graphics.Text;
import input.KeyBoard;
import input.Timer;
import main.Window;
import math.Vector2D;
import observer.*;

import java.awt.*;

public class TextInput  implements Observador {
    private int caracter = 65;
    private int letterIndex = 0;
    private String input;
    private final int size;
    private final Timer flickerTimer;
    private boolean showSelected = true;

    public TextInput(int size, String initialText) {
        this.size = size;
        flickerTimer = new Timer(500);
        input = initialText.substring(0, size);

        // me subscribo al timer del keyboard
        Window.keyBoardTimer.addObservador(this);
    }

    public void update(float dt) {
        flicker(dt);
    }

    public void update(){
        checkTecla(KeyBoard.UP, (params)->{
            if (caracter == 90)
                caracter = 65;
            else
                caracter++;

            updateInput();
        });

        checkTecla(KeyBoard.DOWN, (params)->{
            if (caracter == 65)
                caracter = 90;
            else
                caracter--;

            updateInput();
        });

        checkTecla(KeyBoard.RIGHT, (params)->{
            letterIndex++;
            if (letterIndex == size)
                letterIndex = 0;
            caracter = input.toCharArray()[letterIndex];
        });

        checkTecla(KeyBoard.LEFT, (params)->{
            letterIndex--;
            if (letterIndex == -1)
                letterIndex = size-1;
            caracter = input.toCharArray()[letterIndex];
        });
    }

    public void drawInput(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        String indicador = "-";
        int offset = 30;

        Vector2D charPosition = new Vector2D((double) Constants.WIDTH / 2 - offset * 2, (double) Constants.HEIGHT / 2);

        char[] chars = input.toCharArray();

        char c;

        for (int i = 0; i < chars.length; i++) {
            charPosition.setX(charPosition.getX() + offset);

            if (i == letterIndex && !showSelected)
                continue;

            c = chars[i];
            Text.drawText(g2d, String.valueOf(c), charPosition, true, Color.WHITE, Assets.fontBig);
        }


        Vector2D indicadorPos = new Vector2D((double) Constants.WIDTH / 2 - offset, (double) Constants.HEIGHT / 2 + 20);

        if (letterIndex == 1)
            indicadorPos.setX(indicadorPos.getX() + offset);
        if (letterIndex == 2)
            indicadorPos.setX(indicadorPos.getX() + offset * 2);
        if (letterIndex == 3)
            indicadorPos.setX(indicadorPos.getX() + offset * 3);

        Text.drawText(g2d, indicador, indicadorPos, true, Color.WHITE, Assets.fontBig);
    }

    /**
     * Hace que la letra parpadee
     * @param dt - tiempo entre frames
     */
    private void flicker(float dt) {
        flickerTimer.update(dt);

        if(!flickerTimer.isTime()) return;

        flickerTimer.reset();

        showSelected = !showSelected;
    }

    /**
     * Verifica si la tecla esta presionada y si paso el delay
     * Ejecuta la accion correspondiente a la tecla
     * @param tecla     - tecla a verificar
     * @param action    - accion a ejecutar
     */
    private void checkTecla(boolean tecla, Action action) {
        if (!tecla) return;
        action.doAction();
    }

    /**
     * Actualiza el string de input con el caracter seleccionado
     */
    private void updateInput() {
        char[] inputArr = input.toCharArray();
        inputArr[letterIndex] = (char) caracter;
        input = String.valueOf(inputArr);
    }

    public void close(){
        // desuscribo al input del timer
        Window.keyBoardTimer.removeObservador(this);
    }

    public String getInput() {
        return input;
    }
}
