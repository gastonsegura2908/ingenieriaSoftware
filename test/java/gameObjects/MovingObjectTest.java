package gameObjects;

import constants.Constants;
import graphics.Assets;
import map.Map;
import math.Vector2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovingObjectTest {
    /**
     *  Se verifica que limitPosition cambie al objeto de sala adecuadamente
     */
    @Test
    void limitPosition() {
        Map.setSize(Map.Size.MEDIUM);
        Map.getMap();

        Vector2D startPosition = new Vector2D(Constants.HALF_WIDTH, Constants.HALF_HEIGHT);
        Vector2D velocidad = new Vector2D(Constants.WIDTH + 100, 0);

        Player player = new Player(startPosition, velocidad,
                Constants.PLAYER_MAX_VEL, Assets.player, "a", null);

        player.limitPosition();

        //Mapa 2x2

        // no se movio el player
        assertEquals(Map.getMap().getRoom(1,0), player.getRoom());

        // se mueve hacia la derecha
        player.position = player.position.add(velocidad);
        player.limitPosition();
        assertNotSame(Map.getMap().getRoom(1,0), player.getRoom());
        assertSame(Map.getMap().getRoom(1,1), player.getRoom());

        // se mueve hacia la derecha
        player.position = player.position.add(velocidad);
        player.limitPosition();
        assertSame(Map.getMap().getRoom(1,0), player.getRoom());

        // se mueve hacia la derecha
        player.position = player.position.add(velocidad);
        player.limitPosition();
        assertSame(Map.getMap().getRoom(1,1), player.getRoom());

        // se mueve hacia la izquierda
        player.position = player.position.add(velocidad.scale(-1));
        player.limitPosition();
        assertSame(Map.getMap().getRoom(1,0), player.getRoom());

        // se mueve hacia la izquierda
        player.position = player.position.add(velocidad.scale(-1));
        player.limitPosition();
        assertSame(Map.getMap().getRoom(1,1), player.getRoom());

        // se mueve hacia abajo
        velocidad = new Vector2D(0, Constants.HEIGHT + 100);
        player.position = player.position.add(velocidad);
        player.limitPosition();
        assertSame(Map.getMap().getRoom(0,1), player.getRoom());

        // se mueve hacia abajo
        player.position = player.position.add(velocidad);
        player.limitPosition();
        assertSame(Map.getMap().getRoom(1,1), player.getRoom());

        // se mueve hacia arriba
        player.position = player.position.add(velocidad.scale(-1));
        player.limitPosition();
        assertSame(Map.getMap().getRoom(0,1), player.getRoom());


    }
}