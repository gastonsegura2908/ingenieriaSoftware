package gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import constants.Constants;
import map.*;
import math.Vector2D;
import states.GameState;

public class Meteor extends MovingObject {

    private Size size;

    public Meteor(Vector2D position, Room room, Vector2D velocity, double maxVel, BufferedImage texture, GameState gameState, Size size) {
        super(position, room, velocity, maxVel, texture, gameState);
        this.size = size;
        this.velocity = velocity.scale(maxVel);
    }

    @Override
    public void update(float dt) {

        Vector2D playerPos = new Vector2D(gameState.getPlayer().getCenter());

        int distanceToPlayer = (int) playerPos.subtract(getCenter()).getMagnitude();

        if (distanceToPlayer < Constants.SHIELD_DISTANCE / 2 + width / 2) {

            if (gameState.getPlayer().isShieldOn()) {
                Vector2D fleeForce = fleeForce();
                velocity = velocity.add(fleeForce);
            }


        }

        if (velocity.getMagnitude() >= this.maxVel) {
            Vector2D reversedVelocity = new Vector2D(-velocity.getX(), -velocity.getY());
            velocity = velocity.add(reversedVelocity.normalize().scale(0.01f));
        }

        velocity = velocity.limit(Constants.METEOR_MAX_VEL);

        position = position.add(velocity);

        limitPosition();

        angle += Constants.DELTAANGLE / 2;

    }

    private Vector2D fleeForce() {
        Vector2D desiredVelocity = gameState.getPlayer().getCenter().subtract(getCenter());
        desiredVelocity = (desiredVelocity.normalize()).scale(Constants.METEOR_MAX_VEL);
        Vector2D v = new Vector2D(velocity);
        return v.subtract(desiredVelocity);
    }

    @Override
    public void Destroy(boolean points) {
        divideMeteor();

        //si points es true significa que la destruccion de este objeto debe sumar puntaje
        if(points)
            gameState.addScore(Constants.METEOR_SCORE, position);

        super.Destroy(points);
    }


    @Override
    public void draw(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        at = AffineTransform.getTranslateInstance(position.getX(), position.getY());

        at.rotate(angle, width / 2, height / 2);

        g2d.drawImage(texture, at, null);

    }

    public Size getSize() {
        return size;
    }

    /**
     * divide meteoro en mas pequeños cuando son alcanzados por el laser de la nave,o por la propia nave
     */
    public void divideMeteor() {

        Size size = getSize();

        BufferedImage[] textures = size.textures;

        Size newSize;

        switch (size) {
            case BIG:
                newSize = Size.MED;
                break;
            case MED:
                newSize = Size.SMALL;
                break;
            case SMALL:
                newSize = Size.TINY;
                break;
            default:
                return;
        }

        for (int i = 0; i < size.quantity; i++) {
            new Meteor(
                    getPosition(),
                    getRoom(),
                    new Vector2D(0, 1).setDirection(Math.random() * Math.PI * 2),
                    Constants.METEOR_INIT_VEL * Math.random() + 1,
                    textures[(int) (Math.random() * textures.length)],
                    gameState,
                    newSize
            );
        }
    }

}
