package gameObjects.shootMode;

import constants.Constants;
import gameObjects.Laser;
import gameObjects.MovingObject;
import input.KeyBoard;
import map.Room;
import math.Vector2D;
import states.GameState;

import java.awt.image.BufferedImage;

public class NormalShot implements ShootMode {
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
        new Laser(position, room, velocity, maxVel, angle, texture, gameState, owner);
        shoot.play();
    }
}
