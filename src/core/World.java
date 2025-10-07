package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World implements Serializable {
    private final int width;
    private final int height;
    private final Random random;
    private final TETile[][] world;
    private final long seed;

    public World(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        this.random = new Random(seed);
        this.world = new TETile[width][height];
        fillWorldWithNothingTiles();
    }

    public TETile[][] generateWorld() {
        List<int[]> roomCenters = generateRandomRooms();
        connectRooms(roomCenters);
        return world;
    }

    private void fillWorldWithNothingTiles() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    private List<int[]> generateRandomRooms() {
        int twelve = 12;
        int numRooms = twelve + random.nextInt(6);
        List<int[]> roomCenters = new ArrayList<>();

        for (int i = 0; i < numRooms; i++) {
            int roomWidth = 5 + random.nextInt(6);
            int roomHeight = 5 + random.nextInt(6);
            int x = random.nextInt(width - roomWidth - 2) + 1;
            int y = random.nextInt(height - roomHeight - 2) + 1;

            if (addRoomWithWalls(x, y, roomWidth, roomHeight)) {
                int centerX = x + roomWidth / 2;
                int centerY = y + roomHeight / 2;
                roomCenters.add(new int[]{centerX, centerY});
            }
        }
        return roomCenters;
    }

    private boolean addRoomWithWalls(int x, int y, int roomWidth, int roomHeight) {
        for (int i = x - 1; i <= x + roomWidth; i++) {
            for (int j = y - 1; j <= y + roomHeight; j++) {
                if (i >= 0 && i < width && j >= 0 && j < height && world[i][j] != Tileset.NOTHING) {
                    return false;
                }
            }
        }

        for (int i = x; i < x + roomWidth; i++) {
            for (int j = y; j < y + roomHeight; j++) {
                world[i][j] = Tileset.FLOOR;
            }
        }

        for (int i = x - 1; i <= x + roomWidth; i++) {
            if (i >= 0 && i < width) {
                if (y - 1 >= 0) {
                    world[i][y - 1] = Tileset.WALL;
                }
                if (y + roomHeight < height) {
                    world[i][y + roomHeight] = Tileset.WALL;
                }
            }
        }

        for (int j = y - 1; j <= y + roomHeight; j++) {
            if (j >= 0 && j < height) {
                if (x - 1 >= 0) {
                    world[x - 1][j] = Tileset.WALL;
                }
                if (x + roomWidth < width) {
                    world[x + roomWidth][j] = Tileset.WALL;
                }
            }
        }

        return true;
    }

    private void connectRooms(List<int[]> roomCenters) {
        for (int i = 0; i < roomCenters.size() - 1; i++) {
            int[] start = roomCenters.get(i);
            int[] end = roomCenters.get(i + 1);
            createHallwayWithWalls(start[0], start[1], end[0], end[1]);
        }
    }

    private void createHallwayWithWalls(int x1, int y1, int x2, int y2) {
        if (random.nextBoolean()) {
            drawHorizontalHallwayWithWalls(x1, x2, y1);
            drawVerticalHallwayWithWalls(y1, y2, x2);
        } else {
            drawVerticalHallwayWithWalls(y1, y2, x1);
            drawHorizontalHallwayWithWalls(x1, x2, y2);
        }
    }

    private void drawHorizontalHallwayWithWalls(int x1, int x2, int y) {
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            world[x][y] = Tileset.FLOOR;
            if (y > 0 && world[x][y - 1] == Tileset.NOTHING) {
                world[x][y - 1] = Tileset.WALL;
            }
            if (y < height - 1 && world[x][y + 1] == Tileset.NOTHING) {
                world[x][y + 1] = Tileset.WALL;
            }
        }
    }

    private void drawVerticalHallwayWithWalls(int y1, int y2, int x) {
        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            world[x][y] = Tileset.FLOOR;
            if (x > 0 && world[x - 1][y] == Tileset.NOTHING) {
                world[x - 1][y] = Tileset.WALL;
            }
            if (x < width - 1 && world[x + 1][y] == Tileset.NOTHING) {
                world[x + 1][y] = Tileset.WALL;
            }
        }
    }

    public long getSeed() {
        return seed;
    }
}
