package map;

/**
 * Clase Mapa implementada con patron Singleton
 * por juego solo puede haber un mapa
 */
public class Map {
    public enum Size {
        SMALL("small 1x1", 1, 1),
        MEDIUM("medium 2x2", 2, 2),
        BIG("big 3x3", 3, 3);
        public final String text;
        public final int rows;
        public final int cols;

        Size(String text, int rows, int cols) {
            this.text = text;
            this.rows = rows;
            this.cols = cols;
        }
    }

    private static Map map = null;
    private final Room[][] rooms;
    private static Size size = Size.SMALL;

    private Map(Size size) {
        Map.size = size;

        rooms = new Room[size.rows][size.cols];

        for (int i = 0; i < size.rows; i++) {
            for (int j = 0; j < size.cols; j++) {
                rooms[i][j] = new Room(i, j);
            }
        }
    }

    public static Map getMap() {
        if (map != null) return map;
        map = new Map(size);
        return map;
    }

    /**
     * Devuelve la sala a la derecha
     */
    public Room getRoomRight(Room room) {
        int row = room.getRow();
        int col = room.getCol() + 1;
        if (col >= size.cols)
            col = 0;
        return rooms[row][col];
    }

    /**
     * Devuelve la sala a la izquierda
     */
    public Room getRoomLeft(Room room) {
        int row = room.getRow();
        int col = room.getCol() - 1;
        if (col < 0)
            col = size.cols - 1;
        return rooms[row][col];
    }

    /**
     * Devuelve la sala hacia arriba
     */
    public Room getRoomTop(Room room) {
        int row = room.getRow() - 1;
        int col = room.getCol();
        if (row < 0)
            row = size.rows - 1;
        return rooms[row][col];
    }

    /**
     * Devuelve la sala hacia abajo
     */
    public Room getRoomBottom(Room room) {
        int row = room.getRow() + 1;
        int col = room.getCol();
        if (row >= size.rows)
            row = 0;
        return rooms[row][col];
    }

    public void update(float dt) {
        for (Room[] row : rooms)
            for (Room room : row)
                room.update(dt);
    }

    public Room getRoom(int row, int col){
        try {
            return rooms[row][col];
        } catch (IndexOutOfBoundsException ex) {
            return rooms[0][0]; //por defecto devuelvo la 0,0
        }

    }

    public static void setSize(Size size) {
        map = null;
        Map.size = size;
    }
    public static Size getSize() {
        return Map.size;
    }
    public void clear() {
        for (Room[] row : rooms)
            for (Room room : row)
                room.clear();
        map = null;
    }

    public int countMeteors() {
        int count = 0;
        for (Room[] row : rooms)
            for (Room room : row)
                count += room.countMeteors();

        return count;
    }
}

