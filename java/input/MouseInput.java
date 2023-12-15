package input;

import observer.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Esta clase nos da informacion actual sobre el mouse
 */
public class MouseInput extends MouseAdapter implements Sujeto {

    public static int X, Y;
    public static boolean MLB;
    private final ArrayList<Observador> observadores = new ArrayList<>();

    /**
     * informa cuando se presiona uno de los botones del mouse(boton izquierdo)
     *
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            MLB = true;
        }
    }

    /**
     * informa cuando se suelta uno de los botones del mouse(boton izquierdo)
     *
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            notifyObservadores();                   // notifico a los observadores que se solto el boton
            MLB = false;
        }
    }

    /**
     * informa cuando se mueve el mouse y a la vez tengo presionado un boton.osea estoy arrastrando.
     *
     * @param e
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        X = e.getX();
        Y = e.getY();
    }

    /**
     * informa cuando se mueve el mouse,guarda posicion actual
     *
     * @param e
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        X = e.getX();
        Y = e.getY();
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
