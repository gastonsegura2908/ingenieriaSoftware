package gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import constants.Constants;
import math.Vector2D;
import states.GameState;
import map.Room;

public class Laser extends MovingObject {
    private MovingObject owner;

    public Laser(Vector2D position, Room room, Vector2D velocity, double maxVel, double angle, BufferedImage texture, GameState gameState,MovingObject owner) {
        super(position, room, velocity, maxVel, texture, gameState);
        this.owner=owner;
        this.angle = angle;
        this.velocity = velocity.scale(maxVel);
    }

    @Override
    public void update(float dt) {
        position = position.add(velocity);
        if (position.getX() < 0 || position.getX() > Constants.WIDTH ||
                position.getY() < 0 || position.getY() > Constants.HEIGHT) {
            Destroy(false);
        }

        collidesWith();

    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        at = AffineTransform.getTranslateInstance(position.getX() - width / 2, position.getY());

        at.rotate(angle, width / 2, 0);

        g2d.drawImage(texture, at, null);

    }

    @Override
    public Vector2D getCenter() {
        return new Vector2D(position.getX() + width / 2, position.getY() + width / 2);
    }

    /**
     * Metodo getter que devuelve el objeto que instancio el laser. Puede ser Player o Ufo
     */
    public MovingObject getOwner(){
        return owner;
    }
}
