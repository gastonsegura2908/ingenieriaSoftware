package map;

import constants.Constants;
import gameObjects.Meteor;
import gameObjects.MovingObject;
import graphics.Animation;
import graphics.Assets;

import java.awt.*;
import java.util.ArrayList;

/**
 * Clase que representa una pantalla entera que ve el jugador
 * que puede ser solo una parte del mapa.
 * Se entiende como una sala del mapa en la que el jugador puede entrar.
 */
public class Room {

    // objetos que hay en esta sala
    private final ArrayList<MovingObject> movingObjects = new ArrayList<>();
    final ArrayList<Animation> animations = new ArrayList<>();
    private final int row;
    private final int col;

    public Room(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    /**
     * Dibuja los objetos que hay en la sala
     *
     * @param g objeto Graphics para dibujar los elementos
     */
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        drawFondo(g);

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        for (MovingObject movingObject : movingObjects) movingObject.draw(g);
        for (Animation anim : animations) {
            g2d.drawImage(anim.getCurrentFrame(), (int) anim.getPosition().getX(), (int) anim.getPosition().getY(),
                    null);
        }
    }

    private void drawFondo(Graphics g) {
        if(Map.getSize() == Map.Size.BIG){
            g.drawImage(Assets.fondoGame3x3[row][col],0,0,Constants.WIDTH,Constants.HEIGHT,null);
        } else if (Map.getSize() == Map.Size.MEDIUM) {
            g.drawImage(Assets.fondoGame2x2[row][col],0,0,Constants.WIDTH,Constants.HEIGHT,null);
        }else{
            g.drawImage(Assets.fondoGame,0,0,Constants.WIDTH,Constants.HEIGHT,null);
        }
    }

    /**
     * Añade un objeto en la sala
     *
     * @param object Objeto a agregar
     */
    public void addObject(MovingObject object) {
        movingObjects.add(object);
    }

    public void removeObject(MovingObject object) {
        movingObjects.remove(object);
    }

    public void update(float dt) {
        // update moving objects
        for (int i = 0; i < movingObjects.size(); i++) {

            MovingObject mo = movingObjects.get(i);

            mo.update(dt);
            if (mo.isDead()) {
                movingObjects.remove(i);
                i--;
            }

        }
        // update animations
        for (int i = 0; i < animations.size(); i++) {
            Animation anim = animations.get(i);
            anim.update(dt);
            if (!anim.isRunning()) {
                animations.remove(i);
                i--;
            }
        }
    }

    /**
     * agrega animaciones al arreglo
     *
     * @param animation animacion a agregar
     */
    public void addAnimation(Animation animation) {
        animations.add(animation);
    }

    public ArrayList<MovingObject> getMovingObjects() {
        return movingObjects;
    }

    public void clear() {
        movingObjects.clear();
        animations.clear();
    }

    public int countMeteors() {
        return (int) movingObjects.stream().filter(obj -> obj instanceof Meteor).count();
    }
}
