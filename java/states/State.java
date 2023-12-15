package states;

import java.awt.Graphics;

/**
 * clase que reune los datos en comun de cada estado
 */
public abstract class State {

    private static State currentState = null;

    /**
     * getter del estado actual
     *
     * @return currentState estado actual
     */
    public static State getCurrentState() {
        return currentState;
    }

    /**
     * cambia el estado
     *
     * @param newState estado al cual queremos cambiar nuestro actual estado
     */
    public static void changeState(State newState) {
        currentState = newState;
    }

    /**
     * actualizar
     *
     * @param dt
     */
    public abstract void update(float dt);

    /**
     * dibujar
     *
     * @param g
     */
    public abstract void draw(Graphics g);

}
