package graphics;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Clase sin instancias la cual immplementa sus metodos de forma static
 * para ser accedidos desde cualquier clase cuando les haga falta.
 * Su principal uso es importar desde la carpeta /res el contenido
 *  grafico, fuente de escritura y sonidos
 */
public class Loader {

	/**
	 Es utilizado para extraer de una direccion ó path una imagen y retornarla
	 en forma de objeto BufferedImage.
	 @param path direccion del directorio que contiene la imagen
	 @return retorna un objeto BufferedImage el cual esta asociado a la imagen apuntada por el path
	 */
	public static BufferedImage ImageLoader(String path)
	{
		try {
			return ImageIO.read(Loader.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 Es utilizado para extraer de una direccion ó path una fuente y retornarla
	 en forma de objeto Font.
	 @param path direccion del directorio que contiene la fuente
	 @return retorna un objeto Font el cual esta asociado a la fuente apuntada por el path
	 */
	public static Font loadFont(String path, int size) {
			try {
				return Font.createFont(Font.TRUETYPE_FONT, Loader.class.getResourceAsStream(path)).deriveFont(Font.PLAIN, size);
			} catch (FontFormatException | IOException e) {
				e.printStackTrace();
				return null;
			}
	}

	/**
	 Es utilizado para extraer de una direccion ó path un sonido ó track y retornarla
	 en forma de un objeto predefinido llamado Clip.
	 @param path direccion del directorio que contiene el track
	 @return retorna un objeto Clip el cual esta asociado al track apuntada por el path
	 */
	public static Clip loadSound(String path) {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(Loader.class.getResource(path)));
			return clip;
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
