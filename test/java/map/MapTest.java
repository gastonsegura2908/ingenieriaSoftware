package map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {
// testeos
    @org.junit.jupiter.api.Test
    void getRoom() {


        Map.setSize(Map.Size.SMALL);
        Map map = Map.getMap();

        Room room = map.getRoom(0,0);
        assertEquals(0, room.getCol());
        assertEquals(0, room.getRow());

        room = map.getRoom(1,1);
        assertNotEquals(1, room.getCol());
        assertNotEquals(1, room.getRow());


        Map.setSize(Map.Size.MEDIUM);
        map = Map.getMap();

        room = map.getRoom(0,1);
        assertEquals(1, room.getCol());
        assertEquals(0, room.getRow());

        room = map.getRoom(1,0);
        assertEquals(0, room.getCol());
        assertEquals(1, room.getRow());

        room = map.getRoom(1,1);
        assertEquals(1, room.getCol());
        assertEquals(1, room.getRow());

        room = map.getRoom(2,2);
        assertNotEquals(2, room.getCol());
        assertNotEquals(2, room.getRow());


        Map.setSize(Map.Size.BIG);
        map = Map.getMap();

        room = map.getRoom(1,2);
        assertEquals(2, room.getCol());
        assertEquals(1, room.getRow());

        room = map.getRoom(2,2);
        assertEquals(2, room.getCol());
        assertEquals(2, room.getRow());

        room = map.getRoom(2,1);
        assertEquals(1, room.getCol());
        assertEquals(2, room.getRow());

        room = map.getRoom(3,3);
        assertEquals(0, room.getCol());
        assertEquals(0, room.getRow());


    }

    @Test
    void getRoomTop() {
        Map.setSize(Map.Size.SMALL);
        Map map = Map.getMap();
        Room room = map.getRoom(0,0);
        Room roomProx = map.getRoomTop(room);
        assertEquals(0, roomProx.getCol());
        assertEquals(0, roomProx.getRow());

        room = map.getRoom(1,0);
        roomProx = map.getRoomTop(room);
        assertEquals(0, roomProx.getCol());
        assertEquals(0, roomProx.getRow());


        Map.setSize(Map.Size.MEDIUM);
        map = Map.getMap();

        room = map.getRoom(0,1);
        roomProx = map.getRoomTop(room);
        assertEquals(1, roomProx.getCol());
        assertEquals(1, roomProx.getRow());

        room = map.getRoom(0,0);
        roomProx = map.getRoomTop(room);
        assertEquals(0, roomProx.getCol());
        assertEquals(1, roomProx.getRow());

        room = map.getRoom(0,1);
        roomProx = map.getRoomTop(room);
        assertEquals(1, roomProx.getCol());
        assertNotEquals(2, roomProx.getRow());


        Map.setSize(Map.Size.BIG);
        map = Map.getMap();

        room = map.getRoom(0,2);
        roomProx = map.getRoomTop(room);
        assertEquals(2, roomProx.getCol());
        assertEquals(2, roomProx.getRow());

        room = map.getRoom(2,0);
        roomProx = map.getRoomTop(room);
        assertEquals(0, roomProx.getCol());
        assertEquals(1, roomProx.getRow());

        room = map.getRoom(0,0);
        roomProx = map.getRoomTop(room);
        assertEquals(0, roomProx.getCol());
        assertEquals(2, roomProx.getRow());


    }

    @Test
    void getRoomLeft() {
        Map.setSize(Map.Size.SMALL);
        Map map = Map.getMap();
        Room room = map.getRoom(0,0);
        Room roomProx = map.getRoomLeft(room);
        assertEquals(0, roomProx.getCol());
        assertEquals(0, roomProx.getRow());

        room = map.getRoom(1,0);
        roomProx = map.getRoomLeft(room);
        assertEquals(0, roomProx.getCol());
        assertEquals(0, roomProx.getRow());


        Map.setSize(Map.Size.MEDIUM);
        map = Map.getMap();

        room = map.getRoom(0,1);
        roomProx = map.getRoomLeft(room);
        assertEquals(0, roomProx.getCol());
        assertEquals(0, roomProx.getRow());

        room = map.getRoom(1,1);
        roomProx = map.getRoomLeft(room);
        assertEquals(0, roomProx.getCol());
        assertEquals(1, roomProx.getRow());

        room = map.getRoom(1,0);
        roomProx = map.getRoomLeft(room);
        assertNotEquals(2, roomProx.getCol());
        assertEquals(1, roomProx.getRow());


        Map.setSize(Map.Size.BIG);
        map = Map.getMap();

        room = map.getRoom(0,2);
        roomProx = map.getRoomLeft(room);
        assertEquals(1, roomProx.getCol());
        assertEquals(0, roomProx.getRow());

        room = map.getRoom(2,0);
        roomProx = map.getRoomLeft(room);
        assertEquals(2, roomProx.getCol());
        assertEquals(2, roomProx.getRow());

        room = map.getRoom(1,0);
        roomProx = map.getRoomLeft(room);
        assertEquals(2, roomProx.getCol());
        assertEquals(1, roomProx.getRow());

    }

    @Test
    void getRoomRight() {

        Map.setSize(Map.Size.SMALL);
        Map map = Map.getMap();
        Room room = map.getRoom(0,0);
        Room roomProx = map.getRoomRight(room);
        assertEquals(0, roomProx.getCol());
        assertEquals(0, roomProx.getRow());

        room = map.getRoom(1,0);
        roomProx = map.getRoomRight(room);
        assertEquals(0, roomProx.getCol());
        assertEquals(0, roomProx.getRow());


        Map.setSize(Map.Size.MEDIUM);
        map = Map.getMap();

        room = map.getRoom(0,1);
        roomProx = map.getRoomRight(room);
        assertEquals(0, roomProx.getCol());
        assertEquals(0, roomProx.getRow());

        room = map.getRoom(1,1);
        roomProx = map.getRoomRight(room);
        assertEquals(0, roomProx.getCol());
        assertEquals(1, roomProx.getRow());

        room = map.getRoom(1,0);
        roomProx = map.getRoomRight(room);
        assertNotEquals(2, roomProx.getCol());
        assertEquals(1, roomProx.getRow());


        Map.setSize(Map.Size.BIG);
        map = Map.getMap();

        room = map.getRoom(0,2);
        roomProx = map.getRoomRight(room);
        assertEquals(0, roomProx.getCol());
        assertEquals(0, roomProx.getRow());

        room = map.getRoom(2,0);
        roomProx = map.getRoomRight(room);
        assertEquals(1, roomProx.getCol());
        assertEquals(2, roomProx.getRow());

        room = map.getRoom(1,0);
        roomProx = map.getRoomRight(room);
        assertEquals(1, roomProx.getCol());
        assertEquals(1, roomProx.getRow());

    }

    @Test
    void getRoomBottom() {
        Map.setSize(Map.Size.SMALL);
        Map map = Map.getMap();
        Room room = map.getRoom(0,0);
        Room roomProx = map.getRoomBottom(room);
        assertEquals(0, roomProx.getCol());
        assertEquals(0, roomProx.getRow());

        room = map.getRoom(1,0);
        roomProx = map.getRoomBottom(room);
        assertEquals(0, roomProx.getCol());
        assertEquals(0, roomProx.getRow());


        Map.setSize(Map.Size.MEDIUM);
        map = Map.getMap();

        room = map.getRoom(1,1);
        roomProx =map.getRoomBottom(room);
        assertEquals(1, roomProx.getCol());
        assertEquals(0, roomProx.getRow());

        room = map.getRoom(1,0);
        roomProx = map.getRoomBottom(room);
        assertEquals(0, roomProx.getCol());
        assertEquals(0, roomProx.getRow());

        room = map.getRoom(1,1);
        roomProx = map.getRoomBottom(room);
        assertEquals(1, roomProx.getCol());
        assertNotEquals(2, roomProx.getRow());


        Map.setSize(Map.Size.BIG);
        map = Map.getMap();

        room = map.getRoom(0,2);
        roomProx = map.getRoomBottom(room);
        assertEquals(2, roomProx.getCol());
        assertEquals(1, roomProx.getRow());

        room = map.getRoom(2,0);
        roomProx = map.getRoomBottom(room);
        assertEquals(0, roomProx.getCol());
        assertEquals(0, roomProx.getRow());

        room = map.getRoom(2,2);
        roomProx = map.getRoomBottom(room);
        assertEquals(2, roomProx.getCol());
        assertEquals(0, roomProx.getRow());
    }
}