package Core;

import TileEngine.Tileset;

import java.util.*;

public class FloodGenerator {
    private int[][] regionMark;
    private final World world;
    private final Random random;
    public FloodGenerator(World world, Random random) {
        this.world = world;
        this.regionMark = new int[world.getWidth()][world.getHeight()];
        this.random = random;
    }
    /**
     * wall is 0
     * water is 1
     * no water is -1
     */
    public void initial() {
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                boolean isWall = world.getTile(new Position(x, y)) == Tileset.WALL;
                regionMark[x][y] = isWall ? 0 : -1;
            }
        }
    }

    private void markWater(Position pos) {
        regionMark[pos.getX()][pos.getY()] = 1;
    }

    private int getMark(Position pos) {
        if (!world.isInBounds(pos)) {
            return 1;
        }
        return regionMark[pos.getX()][pos.getY()];
    }

    private boolean isWater(Position pos) {
        return  getMark(pos) == 1;
    }

    private boolean isWall(Position pos) {
        return getMark(pos) == 0;
    }

    private boolean isDrought(Position pos) {
        return getMark(pos) == -1;
    }

    /**
     * get all flooding walls include corner
     * @param source
     * @return
     */
    public List<Position> flood(Position source) {
        initial();
        return floodHelper(source).getAllWalls();
    }

    /**
     * get flooding walls
     * @param source
     * @return
     */
    private FloodWalls floodHelper(Position source) {
        Set<Position> walls = new HashSet<>();
        Deque<Position> waters = new LinkedList<>();
        markWater(source);
        waters.add(source);
        // handle corner
        Deque<Direction> deque = new LinkedList<>();
        FloodWalls floodWalls = new FloodWalls();

        while (!waters.isEmpty()) {
            Position first = waters.removeFirst();
            deque.clear();
            for (Direction dir : Direction.getCardinalDirections()) {
                Position pos = first.move(dir, 1);
                if (isDrought(pos)) {
                    markWater(pos);
                    waters.add(pos);
                }
                if (isWall(pos)) {
                    deque.add(dir);
                    floodWalls.addNotCornerWall(pos);
                }
            }
            if (deque.size() == 2) {
                Direction d1 = deque.remove();
                Direction d2 = deque.remove();
                Position corner = first.move(d1, 1).move(d2, 1);
                floodWalls.addCornerWall(corner);
            }
        }
        return floodWalls;
    }

    private List<Position> findDam(List<Position> walls) {
        List<Position> dams = new LinkedList<>();
        for (Position wallPos : walls) {
            for (Direction dir : Direction.getCardinalDirections()) {
                Position pos = wallPos.move(dir, 1);
                if (isDrought(pos)) {
                    dams.add(wallPos);
                }
            }
        }
        return dams;
    }

    public void floodConnect(Position source) {
        initial();
        floodConnectHepler(source);
    }


    private void floodConnectHepler(Position source) {
        List<Position> floodWall = floodHelper(source).getNotCornerWall();

        while (true) {
            List<Position> dams = findDam(floodWall);
            if (dams.isEmpty()) {
                break;
            }
            Position dam = dams.get(random.nextInt(dams.size()));

            world.setTile(dam, Tileset.FLOOR);
            floodConnectHepler(dam);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("World:\n");
        for (int y = world.getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x < world.getWidth(); x++) {
                sb.append(regionMark[x][y]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private class FloodWalls {
        List<Position> cornerWall;
        List<Position> notCornerWall;

        FloodWalls() {
            cornerWall = new LinkedList<>();
            notCornerWall = new LinkedList<>();
        }

        void addCornerWall(Position pos) {
            cornerWall.add(pos);
        }

        void addNotCornerWall(Position pos) {
            notCornerWall.add(pos);
        }

        public List<Position> getCornerWall() {
            return cornerWall;
        }

        public List<Position> getNotCornerWall() {
            return notCornerWall;
        }

        public List<Position> getAllWalls() {
            List<Position> allWalls = new LinkedList<>();
            allWalls.addAll(cornerWall);
            allWalls.addAll(notCornerWall);
            return allWalls;
        }
    }
}
