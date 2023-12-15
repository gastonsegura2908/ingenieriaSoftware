package map;

import constants.Constants;
import gameObjects.*;
import graphics.Animation;
import graphics.Assets;
import math.Vector2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {
    private final Room room = new Room(0,0);
    @Test
    void addObject() {
        assertTrue(room.getMovingObjects().isEmpty());

        // agrego un objeto
        Player player = new Player(new Vector2D(), new Vector2D(), Constants.PLAYER_MAX_VEL, Assets.player, "a", null);
        room.addObject(player);
        assertEquals(1, room.getMovingObjects().size());
        assertFalse(room.getMovingObjects().isEmpty());

        // agrego otro objeto
        new Meteor(new Vector2D(), room, new Vector2D(),Constants.PLAYER_MAX_VEL, Assets.player, null, Size.BIG);
        assertEquals(2, room.getMovingObjects().size());
    }

    @Test
    void removeObject() {

        Player player = new Player(new Vector2D(), new Vector2D(),Constants.PLAYER_MAX_VEL, Assets.player, "a", null);

        room.removeObject(player); // intento sacar un elemento inexistente
        assertTrue(room.getMovingObjects().isEmpty());

        // agrego dos objetos
        room.addObject(player);

        Meteor met = new Meteor(new Vector2D(), room, new Vector2D(),Constants.PLAYER_MAX_VEL, Assets.player, null, Size.BIG);

        assertFalse(room.getMovingObjects().isEmpty());

        // saco un elemento
        room.removeObject(player);
        assertEquals(1, room.getMovingObjects().size());

        // saco un elemento
        room.removeObject(met);
        assertEquals(0, room.getMovingObjects().size());

        assertTrue(room.getMovingObjects().isEmpty());
    }

    @Test
    void addAnimation() {
        assertTrue(room.animations.isEmpty());
        room.addAnimation(new Animation(
                Assets.exp,
                50,
                new Vector2D()
        ));
        assertEquals(1,room.animations.size());
    }

    @Test
    void clear() {

        room.addAnimation(new Animation(
                Assets.exp,
                50,
                new Vector2D()
        ));

        assertEquals(1,room.animations.size());

        Player player = new Player(new Vector2D(), new Vector2D(),Constants.PLAYER_MAX_VEL, Assets.player, "a", null);

        room.addObject(player);

        assertFalse(room.getMovingObjects().isEmpty());
        assertFalse(room.animations.isEmpty());

        room.clear();

        assertTrue(room.getMovingObjects().isEmpty());
        assertTrue(room.animations.isEmpty());
    }

    @Test
    void countMeteors() {

        Player player = new Player(new Vector2D(), new Vector2D(),Constants.PLAYER_MAX_VEL, Assets.player, "a", null);

        room.removeObject(player); // intento sacar un elemento inexistente

        // agrego dos objetos
        room.addObject(player);

        assertEquals(0, room.countMeteors());

        // agrego 5 meteoros
        new Meteor(new Vector2D(), room, new Vector2D(),Constants.PLAYER_MAX_VEL, Assets.player, null, Size.BIG);
        new Meteor(new Vector2D(), room, new Vector2D(),Constants.PLAYER_MAX_VEL, Assets.player, null, Size.BIG);
        new Meteor(new Vector2D(), room, new Vector2D(),Constants.PLAYER_MAX_VEL, Assets.player, null, Size.BIG);
        new Meteor(new Vector2D(), room, new Vector2D(),Constants.PLAYER_MAX_VEL, Assets.player, null, Size.BIG);
        new Meteor(new Vector2D(), room, new Vector2D(),Constants.PLAYER_MAX_VEL, Assets.player, null, Size.BIG);

        assertEquals(5, room.countMeteors());

    }
}