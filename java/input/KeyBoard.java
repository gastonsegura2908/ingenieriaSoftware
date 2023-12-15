package input;

import observer.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * Permite la entrada de comandos por el teclado del jugador.
 */
public class KeyBoard implements KeyListener, Sujeto {

    private boolean[] keys = new boolean[1024];

    public static boolean UP, DOWN, LEFT, RIGHT, SHOOT, PAUSE, ENTER, MUTE, HOST, TAB;

    private ArrayList<Observador> observadores = new ArrayList<>();

    /**
     * Contiene el estado de cada tecla.
     */
    public KeyBoard() {
        UP = false;
        LEFT = false;
        RIGHT = false;
        SHOOT = false;
        PAUSE = false;
        DOWN = false;
        ENTER = false;
        MUTE = false;
        HOST =false;
        TAB =false;
    }

    /**
     * Actualiza el estado de cada tecla.
     */
    public void update() {
        UP = keys[KeyEvent.VK_UP];
        DOWN = keys[KeyEvent.VK_DOWN];
        LEFT = keys[KeyEvent.VK_LEFT];
        RIGHT = keys[KeyEvent.VK_RIGHT];
        SHOOT = keys[KeyEvent.VK_P];
        PAUSE = keys[KeyEvent.VK_ESCAPE];
        ENTER = keys[KeyEvent.VK_ENTER];
        MUTE = keys[KeyEvent.VK_M];
        HOST = keys[KeyEvent.VK_H];
    }

    /**
     * Métodos de la clase KeyListener:
     * Detectan el accionamiento de las teclas
     * y la misma que se está alterando.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
        notifyObservadores(); // notifico a los observadores que se solto el boton
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void addObservador(Observador o) {
        observadores.add(o);
    }

    @Override
    public void removeObservador(Observador o) {
        observadores.remove(o);
    }

    @Override
    public void notifyObservadores() {
        for (Observador o: observadores) {
            o.update();
        }
    }
}
