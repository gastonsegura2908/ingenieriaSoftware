package gameObjects;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import constants.Constants;
import graphics.Animation;
import graphics.Assets;
import graphics.Sound;
import map.Map;
import map.Room;
import math.Vector2D;
import states.GameState;

/**
 * Representa un game object que puede moverse
 * por el espacio y que por ende goza de propiedades como
 * velocidad y angulo
 */
public abstract class MovingObject extends GameObject {

    protected Vector2D velocity;
    protected AffineTransform at;
    protected double angle;
    protected double maxVel;
    protected int width;
    protected int height;
    protected GameState gameState;

    protected Room room;

    private Sound explosion = null;

    protected boolean Dead;

    public MovingObject(Vector2D position, Room room, Vector2D velocity, double maxVel, BufferedImage texture, GameState gameState) {
        super(position, texture);
        this.room = room;
        room.addObject(this);
        this.velocity = velocity;
        this.maxVel = maxVel;
        this.gameState = gameState;
        angle = 0;
        Dead = false;
        try {
            width = texture.getWidth();
            height = texture.getHeight();
            explosion = new Sound(Assets.explosion);
        } catch (Exception ex){
            width = 50;
            height = 50;
        }
    }

    /**
     * Verifica la colision con otros objetos del juego
     */
    protected void collidesWith() {

        ArrayList<MovingObject> movingObjects = room.getMovingObjects();

        for (int i = 0; i < movingObjects.size(); i++) {

            MovingObject m = movingObjects.get(i);

            if (m.equals(this))
                continue;

            double distance = m.getCenter().subtract(getCenter()).getMagnitude();

            if (distance < (double) m.width / 2 + (double) width / 2 && movingObjects.contains(this) && !m.Dead && !Dead) {
                //verificamos quien es el owner del Laser. Si es el Player entonces debe sumar puntaje la colision del Laser
                if(this instanceof Laser && ((Laser) this).getOwner() instanceof Player){
                    objectCollision(this, m,true);
                }else{
                    objectCollision(this, m,false);
                }

            }
        }
    }

    /**
     * Accion a realizar cuando colisiona con otro objeto
     *
     * @param a objeto actual
     * @param b objeto con el cual colisiona
     * @param points boolean para informar si la colision deber sumar puntaje(true) o no(false)
     */
    private void objectCollision(MovingObject a, MovingObject b,boolean points) {

        Player p = null;

        if (a instanceof Player)
            p = (Player) a;
        else if (b instanceof Player)
            p = (Player) b;

        // no detectamos colision si el jugador esta reapareciendo
        if (p != null && p.isSpawning())
            return;

        // no hay colision entre dos meteoros
        if (a instanceof Meteor && b instanceof Meteor)
            return;

        if (!(a instanceof PowerUp || b instanceof PowerUp)) {
            if(points){
                a.Destroy(true);
                b.Destroy(true);
            }else{
                a.Destroy(false);
                b.Destroy(false);
            }

            return;
        }

        if (p != null) {
            // consumir power ups
            if (a instanceof Player) {
                ((PowerUp) b).executeAction();
                if(points){
                    b.Destroy(true);
                }else{
                    b.Destroy(false);
                }

            } else {
                ((PowerUp) a).executeAction();
                if(points){
                    a.Destroy(true);
                }else {
                    a.Destroy(false);
                }

            }
        }

    }
    /**
     * Accion a realizar para destruir el objeto
     *
     * @param points boolean para informar si la colision deber sumar puntaje(true) o no(false)
     */
    protected void Destroy(boolean points) {
        Dead = true;
        if (!(this instanceof Laser) && !(this instanceof PowerUp)) {
            room.addAnimation(new Animation(
                    Assets.exp,
                    50,
                    position.subtract(new Vector2D((double) Assets.exp[0].getWidth() / 2, (double) Assets.exp[0].getHeight() / 2))
            ));
            if (room.equals(gameState.getPlayer().getRoom()))
                explosion.play();
        }
    }

    /**
     * Obtiene la posicion del centro del objeto
     *
     * @return nuevo vector
     */
    public Vector2D getCenter() {
        return new Vector2D(position.getX() + (double) width / 2, position.getY() + (double) height / 2);
    }

    public boolean isDead() {
        return Dead;
    }

    /**
     * Limita la posicion en el mapa
     */
    protected void limitPosition() {
        if (position.getX() > Constants.WIDTH) {
            position.setX(-width);
            room.removeObject(this);
            room = Map.getMap().getRoomRight(room);
            room.addObject(this);
        }
        if (position.getY() > Constants.HEIGHT) {
            position.setY(-height);
            room.removeObject(this);
            room = Map.getMap().getRoomBottom(room);
            room.addObject(this);
        }

        if (position.getX() < -width) {
            position.setX(Constants.WIDTH);
            room.removeObject(this);
            room = Map.getMap().getRoomLeft(room);
            room.addObject(this);
        }
        if (position.getY() < -height) {
            position.setY(Constants.HEIGHT);
            room.removeObject(this);
            room = Map.getMap().getRoomTop(room);
            room.addObject(this);
        }
    }

    public Room getRoom() {
        return room;
    }

    public double getWidth() {
        return width;
    }
}
