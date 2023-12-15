package graphics;

import java.awt.image.BufferedImage;

import math.Vector2D;

/**
 * Esta clase contiene la informacion sobre cualquier tipo de animacion que suceda en el juego
 */
public class Animation {

    private BufferedImage[] frames;
    private int velocity;
    private int index;
    private boolean running;
    private Vector2D position;
    private long time;

    /**
     * constructor
     *
     * @param frames   num total de fotogramas
     * @param velocity vel con la q cambia de un fotograma al siguiente
     * @param position posicion	en la que voy a dibujar la animacion
     */
    public Animation(BufferedImage[] frames, int velocity, Vector2D position) {
        this.frames = frames;
        this.velocity = velocity;
        this.position = position;
        index = 0;
        running = true;
    }

    /**
     * cambia/actualiza los fotogramas cada vez que pase la cantidad de tiempo que colocamos como parametro
     *
     * @param dt delta de tiempo
     */
    public void update(float dt) {

        time += dt;

        if (time > velocity) {
            time = 0;
            index++;
            if (index >= frames.length) {
                running = false;
                index = 0;
            }
        }
    }

    /**
     * nos dice si la animacion esta corriendo
     *
     * @return running si es false es porque la animacion acabo
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * metodo getter que nos da la posicion actual
     *
     * @return position  posicion
     */
    public Vector2D getPosition() {
        return position;
    }

    /**
     * obtiene el fotograma actual en el arreglo de programas
     *
     * @return frames retorna los frames en la posicion en la que se encuentra el indice
     */
    public BufferedImage getCurrentFrame() {
        return frames[index];
    }


}
