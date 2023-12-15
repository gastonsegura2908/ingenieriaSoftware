package gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import constants.Constants;
import graphics.Assets;
import graphics.Sound;
import map.Map;
import map.Room;
import math.Vector2D;
import states.GameState;

/**
 * Clase de Nave enemiga
 * Utiliza el algoritmo de seek behavior para
 * realizar un recorrido determinado
 */
public class Ufo extends MovingObject {

    private ArrayList<Vector2D> path;

    private int index;

    private boolean following;

    private long fireRate;

    private final Sound shoot;

    public Ufo(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, GameState gameState) {
        super(position, Map.getMap().getRoom(0, 0), velocity, maxVel, texture, gameState);
        path = getRandomPath();
        index = 0;
        following = true;
        fireRate = 0;
        shoot = new Sound(Assets.ufoShoot);
    }

    private Vector2D pathFollowing() {
        Vector2D currentNode = path.get(index);

        double distanceToNode = currentNode.subtract(getCenter()).getMagnitude();

        if (distanceToNode < Constants.NODE_RADIUS) {
            index++;
            if (index >= path.size()) {
                following = false;
            }
        }

        return seekForce(currentNode);

    }

    private Vector2D seekForce(Vector2D target) {
        Vector2D desiredVelocity = target.subtract(getCenter());
        desiredVelocity = desiredVelocity.normalize().scale(maxVel);
        return desiredVelocity.subtract(velocity);
    }

    @Override
    public void update(float dt) {

        fireRate += dt;

        Vector2D pathFollowing;

        if (following)
            pathFollowing = pathFollowing();
        else
            pathFollowing = new Vector2D();

        pathFollowing = pathFollowing.scale(1 / Constants.UFO_MASS);

        velocity = velocity.add(pathFollowing);

        velocity = velocity.limit(maxVel);

        position = position.add(velocity);

        limitPosition();

        // shoot
        // dispara en direccion del player, con una componente aleatoria

        if (fireRate > Constants.UFO_FIRE_RATE) {
            if (room.equals(gameState.getPlayer().getRoom())) {

                Vector2D toPlayer = gameState.getPlayer().getCenter().subtract(getCenter());

                toPlayer = toPlayer.normalize();

                double currentAngle = toPlayer.getAngle();

                currentAngle += Math.random() * Constants.UFO_ANGLE_RANGE - Constants.UFO_ANGLE_RANGE / 2;

                if (toPlayer.getX() < 0)
                    currentAngle = -currentAngle + Math.PI;

                toPlayer = toPlayer.setDirection(currentAngle);

                new Laser(
                        getCenter().add(toPlayer.scale(width)),
                        room,
                        toPlayer,
                        Constants.LASER_VEL,
                        currentAngle + Math.PI / 2,
                        Assets.redLaser,
                        gameState,
                        this
                );


                fireRate = 0;

                shoot.play();

            }
        }

        if (shoot.getFramePosition() > 8500) {
            shoot.stop();
        }

        angle += 0.05;

        collidesWith();

    }

    @Override
    protected void limitPosition() {
        Room prevRoom = room;
        super.limitPosition();

        if (prevRoom.equals(room))
            return;

        // restart path if changed room
        following = true;
        index = 0;
        path = getRandomPath();
    }

    @Override
    public void Destroy(boolean points) {

        //si points es true significa que la destruccion de este objeto debe sumar puntaje
        if(points)
        gameState.addScore(Constants.UFO_SCORE, position);

        super.Destroy(points);
    }

    @Override
    public void draw(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        at = AffineTransform.getTranslateInstance(position.getX(), position.getY());

        at.rotate(angle, (double) width / 2, (double) height / 2);

        g2d.drawImage(texture, at, null);

    }

    public static ArrayList<Vector2D> getRandomPath() {
        ArrayList<Vector2D> path = new ArrayList<>();

        double posX, posY;

        posX = Math.random() * Constants.HALF_WIDTH;
        posY = Math.random() * Constants.HALF_HEIGHT;
        path.add(new Vector2D(posX, posY));

        posX = Math.random() * Constants.HALF_WIDTH + Constants.HALF_WIDTH;
        posY = Math.random() * Constants.HEIGHT / 2;
        path.add(new Vector2D(posX, posY));

        posX = Math.random() * Constants.HALF_WIDTH;
        posY = Math.random() * Constants.HALF_HEIGHT + Constants.HALF_HEIGHT;
        path.add(new Vector2D(posX, posY));

        posX = Math.random() * Constants.HALF_WIDTH + Constants.HALF_WIDTH;
        posY = Math.random() * Constants.HALF_HEIGHT + Constants.HALF_HEIGHT;
        path.add(new Vector2D(posX, posY));
        return path;
    }
}
