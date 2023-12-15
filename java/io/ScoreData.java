package io;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 Clase ScoreData, sirve para almacenar el puntaje obtenido en el juego una vez finalizada la partida.
 Se llama a su constructor indicando el puntaje obtenido y este lo almacena con su respectiva fecha.
 */
public class ScoreData {

    private String date;
    private int score;

    /************************************************/
    private String name;

    private char[] nameChars;

    /************************************************/
    public ScoreData(int score, String name) {
        this.name = name;
        this.score = score;

        Date today = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        date = format.format(today);

        nameChars = name.toCharArray();

    }

    public ScoreData() {

    }
    /**
	 Devuelve la fecha.
	 @return date Fecha almacenada.
	 */
    public String getDate() {
        return date;
    }
    /**

	 Establece la fecha actual.
	 @param  date Fecha actual.
	 */
    public void setDate(String date) {
        this.date = date;
    }
    /**
	 Devuelve el puntaje.
	 @return  score Puntaje almacenado.
	 */
    public int getScore() {
        return score;
    }
    /**
	 Establece el puntaje.
	 @param  score Puntaje a almacenar.
	 */
    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    /************************************************/
    public int getNameSize() {
        return name.length();
    }

    public char[] getNameChars() {
        return name.toCharArray();
    }
    /************************************************/
}
