package gameObjects.shootMode;

import gameObjects.MovingObject;
import graphics.Assets;
import graphics.Sound;
import map.Room;
import math.Vector2D;
import states.GameState;

import java.awt.image.BufferedImage;

public interface ShootMode {
    Sound shoot = new Sound(Assets.playerShoot);

    void shoot(
            Vector2D position,
            Room room,
            Vector2D velocity,
            double maxVel,
            double angle,
            BufferedImage texture,
            GameState gameState,
            MovingObject owner,
            float dt
    );
}
