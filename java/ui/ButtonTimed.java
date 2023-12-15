package ui;

import input.KeyBoard;
import main.Window;
import input.MouseInput;
import observer.Observador;

import java.awt.image.BufferedImage;

/**
 * Clase que representa un boton que esta atado a un Timer
 * que limita la cantidad de clicks que puede recibir en un determinado tiempo
 */
public class ButtonTimed extends Button implements Observador {
	public ButtonTimed(
			BufferedImage mouseOutImg,
			BufferedImage mouseInImg,
			int x, int y,
			String text,
			Action action
			) {
		super(mouseOutImg, mouseInImg, x, y, text, action);

		// me subscribo al timer del mouse
		Window.mouseTimer.addObservador(this);
		Window.keyBoardTimer.addObservador(this);
	}

    /**
     * Metodo que se encarga de actualizar el estado del boton
	 * cuando es notificado por el Sujeto, en este caso el Timer
	 */
	@Override
	public void update() {
		mouseIn = boundingBox.contains(MouseInput.X, MouseInput.Y);
		if(mouseIn && MouseInput.MLB) {
			action.doAction(this);
			return;
		}
		if (isSelected && KeyBoard.ENTER){
			action.doAction(this);
		}
	}

	public void close() {
		// desuscribo al boton del timer
		Window.mouseTimer.removeObservador(this);
		Window.keyBoardTimer.removeObservador(this);
	}
}
