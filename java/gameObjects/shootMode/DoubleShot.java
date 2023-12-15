package gameObjects.shootMode;

import constants.Constants;
import gameObjects.Laser;
import gameObjects.MovingObject;
import input.KeyBoard;
import map.Room;
import math.Vector2D;
import states.GameState;

import java.awt.image.BufferedImage;

public class DoubleShot implements ShootMode{
    private long fireRate = 0;

    @Override
    public void shoot(
            Vector2D position,
            Room room,
            Vector2D velocity,
            double maxVel,
            double angle,
            BufferedImage texture,
            GameState gameState,
            MovingObject owner,
            float dt
    ) {
        if (shoot.getFramePosition() > 8500) {
            shoot.stop();
        }
        if (!KeyBoard.SHOOT || fireRate < Constants.FIRERATE) {
            fireRate += dt;
            return;
        }
        fireRate = 0;

        Vector2D leftGun = position;
        Vector2D rightGun = position;

        Vector2D temp = new Vector2D(velocity);
        temp.normalize();
        temp = temp.setDirection(angle - 1.3f);
        temp = temp.scale(owner.getWidth());
        rightGun = rightGun.add(temp);

        temp = temp.setDirection(angle - 1.9f);
        leftGun = leftGun.add(temp);

        new Laser(leftGun, room, velocity, maxVel, angle, texture, gameState,owner);
        new Laser(rightGun, room, velocity, maxVel, angle, texture, gameState,owner);

        shoot.play();
    }
}
