package gameObjects;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import constants.Constants;
import gameObjects.shootMode.*;
import graphics.Animation;
import graphics.Assets;
import graphics.Sound;
import input.KeyBoard;
import map.*;
import math.Vector2D;
import states.GameState;

public class Player extends MovingObject {

    private Vector2D heading;
    private Vector2D acceleration;
    private boolean accelerating = false;
    private boolean spawning, visible;
    private long spawnTime, flickerTime, shieldTime, doubleScoreTime, fastFireTime, doubleGunTime;
    private Sound loose = null;
    private boolean shieldOn, doubleScoreOn, fastFireOn, doubleGunOn;
    private ShootMode shootMode;                // modo de disparo usando patron STRATEGY
    private Animation shieldEffect = null;
    private final String name;

    public Player(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, String name, GameState gameState) {
        super(position, Map.getMap().getRoom(Map.getSize().rows -1 , 0), velocity, maxVel, texture, gameState);
        this.name = name;
        heading = new Vector2D(0, 1);
        acceleration = new Vector2D();
        spawnTime = 0;
        flickerTime = 0;
        shieldTime = 0;
        fastFireTime = 0;
        doubleGunTime = 0;
        visible = true;

        shootMode = new NormalShot(); // inicia con disparo normal

        try {
            loose = new Sound(Assets.playerLoose);
            shieldEffect = new Animation(Assets.shieldEffect, 80, null);    
        } catch (Exception ex){
            visible = false;
        }

    }


    @Override
    public void update(float dt) {

        // POWER UPS ----------------------------------------------------
        updatePowerUps(dt);

        // RESPAWNING ----------------------------------------------------

        if (spawning) {

            flickerTime += dt;
            spawnTime += dt;

            if (flickerTime > Constants.FLICKER_TIME) {

                visible = !visible;
                flickerTime = 0;
            }

            if (spawnTime > Constants.SPAWNING_TIME) {
                spawning = false;
                visible = true;
            }

        }

        // SHOOTING ----------------------------------------------------

        shootMode.shoot(
                getCenter().add(heading.scale(width)),
                room,
                heading,
                Constants.LASER_VEL,
                angle,
                Assets.blueLaser,
                gameState,
                this,
                dt
        );

        // MOVEMENT ----------------------------------------------------

        if (KeyBoard.RIGHT)
            angle += Constants.DELTAANGLE;
        if (KeyBoard.LEFT)
            angle -= Constants.DELTAANGLE;

        if (KeyBoard.UP) {
            acceleration = heading.scale(Constants.ACC);
            accelerating = true;
        } else {
            if (velocity.getMagnitude() != 0)
                acceleration = (velocity.scale(-1).normalize()).scale(Constants.ACC / 2);
            accelerating = false;
        }

        velocity = velocity.add(acceleration);

        velocity = velocity.limit(maxVel);

        heading = heading.setDirection(angle - Math.PI / 2);

        position = position.add(velocity);


        // LIMITES DE MAPA ----------------------------------------------------
        limitPosition();

        collidesWith();
    }

    private void updatePowerUps(float dt) {
        if (shieldOn) {
            shieldTime += dt;
            shieldEffect.update(dt);
        }
        if (doubleScoreOn)
            doubleScoreTime += dt;

        if (fastFireOn)
            fastFireTime += dt;

        if (doubleGunOn)
            doubleGunTime += dt;

        if (shieldTime > Constants.SHIELD_TIME) {
            shieldTime = 0;
            shieldOn = false;
        }

        if (doubleScoreTime > Constants.DOUBLE_SCORE_TIME) {
            doubleScoreOn = false;
            doubleScoreTime = 0;
        }

        if (fastFireTime > Constants.FAST_FIRE_TIME) {
            shootMode = new NormalShot();
            fastFireOn = false;
            fastFireTime = 0;
        }

        if (doubleGunTime > Constants.DOUBLE_GUN_TIME) {
            shootMode = new NormalShot();
            doubleGunOn = false;
            doubleGunTime = 0;
        }
    }

    public void setShield() {
        if (shieldOn)
            shieldTime = 0;
        shieldOn = true;
    }

    public void setDoubleScore() {
        if (doubleScoreOn)
            doubleScoreTime = 0;
        doubleScoreOn = true;
    }

    public void setFastFire() {
        if (fastFireOn)
            fastFireTime = 0;
        fastFireOn = true;
        shootMode = new FastShot();                 // cambia el modo de disparo ---------------------
    }

    public void setDoubleGun() {
        if (doubleGunOn) {
            doubleGunTime = 0;
            shootMode = new TripleShot();
            return;
        }
        doubleGunOn = true;
        shootMode = new DoubleShot();                   // cambia el modo de disparo ---------------------
    }

    @Override
    public void Destroy(boolean points) {
        spawning = true;

        room.addAnimation(new Animation(
                Assets.exp,
                50,
                position.subtract(new Vector2D((double) Assets.exp[0].getWidth() / 2, (double) Assets.exp[0].getHeight() / 2))
        ));

        spawnTime = 0;

        loose.play();

        if (!gameState.subtractLife(position)) {
            gameState.gameOver();
            super.Destroy(points);
        }

        resetValues();
    }

    private void resetValues() {
        angle = 0;
        velocity = new Vector2D();
        position = GameState.PLAYER_START_POSITION;
    }

    @Override
    public void draw(Graphics g) {
        if (!visible)
            return;

        Graphics2D g2d = (Graphics2D) g;

        AffineTransform at1 = AffineTransform.getTranslateInstance(position.getX() + (double) width / 2 + 5,
                position.getY() + (double) height / 2 + 10);

        AffineTransform at2 = AffineTransform.getTranslateInstance(position.getX() + 5, position.getY() + (double) height / 2 + 10);

        at1.rotate(angle, -5, -10);
        at2.rotate(angle, (double) width / 2 - 5, -10);

        if (accelerating) {
            g2d.drawImage(Assets.speed, at1, null);
            g2d.drawImage(Assets.speed, at2, null);
        }

        if (shieldOn) {
            BufferedImage currentFrame = shieldEffect.getCurrentFrame();
            AffineTransform at3 = AffineTransform.getTranslateInstance(
                    position.getX() - (double) currentFrame.getWidth() / 2 + (double) width / 2,
                    position.getY() - (double) currentFrame.getHeight() / 2 + (double) height / 2);

            at3.rotate(angle, (double) currentFrame.getWidth() / 2, (double) currentFrame.getHeight() / 2);

            g2d.drawImage(shieldEffect.getCurrentFrame(), at3, null);
        }

        at = AffineTransform.getTranslateInstance(position.getX(), position.getY());

        at.rotate(angle, (double) width / 2, (double) height / 2);

        if (doubleGunOn)
            g2d.drawImage(Assets.doubleGunPlayer, at, null);
        else
            g2d.drawImage(texture, at, null);

    }

    public boolean isSpawning() {
        return spawning;
    }

    public boolean isShieldOn() {
        return shieldOn;
    }

    public boolean isDoubleScoreOn() {
        return doubleScoreOn;
    }

    public String getName() {
        return name;
    }

    public void drawRoom(Graphics g) {
        room.draw(g);
    }
}
