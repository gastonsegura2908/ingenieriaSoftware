package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import math.Vector2D;

/**
 * Clase Text, contiene un único método que dibuja un texto.
 */
public class Text {
	/**
	 * Método drawText, dibuja un texto, con una fuente, color y posición.
	 * @param g Objeto que permitirá dibujar.
	 * @param text String que contiene el texto.
	 * @param pos Posición del texto.
	 * @param center Booleano para indicar si el texto va en el centro de pos (1) o en la esquina superior izquierda (0).
	 * @param color Color del texto.
	 * @param font Fuente del texto.
	 */
	public static void drawText(Graphics g, String text, Vector2D pos, boolean center, Color color, Font font) {
		g.setColor(color);
		g.setFont(font);
		Vector2D position = new Vector2D(pos.getX(), pos.getY());
		
		if(center) {
			FontMetrics fm = g.getFontMetrics();
			position.setX(position.getX() - fm.stringWidth(text) / 2);
			position.setY(position.getY() - fm.getHeight() / 2);
			
		}
		
		g.drawString(text, (int)position.getX(), (int)position.getY());
		
	}
}
