package graphics;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
	
	private Clip clip;
	private FloatControl volume;

	public static boolean mute = false;

	/**
	 * Constructor de la clase.
	 * @param clip El archivo de audio.
	 */
	public Sound(Clip clip) {

		this.clip = clip;
		volume = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
	}

	/**
	 * Método para iniciar la reproducción del audio desde el inicio.
	 */
	public void play() {
		clip.setFramePosition(0);
		clip.start();
	}

	/**
	 * Reproduce el sonido de manera contínua hasta que se llame al método para detenerlo.
	 */
	public void loop() {
		clip.setFramePosition(0);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	/**
	 * Método para detener la reproducción el audio.
	 */
	public void stop() {
		clip.stop();
	}

	/**
	 * Método que retorna el estado de reproducción del sonido.
	 * @return 1 si se reproduce sonido, 0 si no lo hace.
	 */
	public boolean is_playing() {
		return clip.isRunning();
	}

	/**
	 * Método para obtener la posición de tiempo en la que se encuentra el sonido.
	 * @return la posición de sonido.
	 */
	public int getFramePosition() {
		return clip.getFramePosition();
	}

	/**
	 * Método para cambiar el volumen del sonido.
	 * @param value volumen nuevo del sonido.
	 */
	public void changeVolume(float value) {
		volume.setValue(value);
	}

}
