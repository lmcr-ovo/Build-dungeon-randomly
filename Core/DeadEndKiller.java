package Core;

import TileEngine.Tileset;

import java.util.*;

public class DeadEndKiller {
    private final World world;
    private final FloodGenerator floodGenerator;
    private final Position treasurePos;

    public DeadEndKiller(World world, Random random, Position treasurePos) {
        this.world = world;
        this.floodGenerator = new FloodGenerator(world, random);
        this.treasurePos = treasurePos;
    }

    private boolean isDeadEnd(Position pos) {
        int way = 0;
        for (Direction dir : Direction.getCardinalDirections()) {
            Position dirPos = pos.move(dir, 1);
            if (world.getTile(dirPos) != Tileset.FLOOR) {
                way += 1;
            }
        }
        return way == 3;
    }

    private List<Position> findDeadEnds() {
        List<Position> ends = new LinkedList<>();
        for (int x = 1; x < world.getWidth(); x += 2) {
            for (int y = 1; y < world.getHeight(); y += 2) {
                Position pos = new Position(x, y);
                if (isDeadEnd(pos)) {
                    ends.add(pos);
                }
            }
        }
        return ends;
    }

    private void killEnd(Position endPos) {
        if (!isDeadEnd(endPos)) {
            return;
        }
        world.setTile(endPos, Tileset.WALL);
        for (Direction dir : Direction.getCardinalDirections()) {
            Position dirPos = endPos.move(dir, 1);
            if (world.getTile(dirPos) == Tileset.FLOOR) {
                killEnd(dirPos);
            }
        }
    }

    public void fillDeadEnds() {
        for (Position endPos : findDeadEnds()) {
            killEnd(endPos);
        }
    }

    private Set<Position> getAllWalls() {
        Set<Position> walls = new HashSet<>();
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                Position pos = new Position(x, y);
                if (world.getTile(pos) == Tileset.WALL) {
                    walls.add(new Position(x, y));
                }
            }
        }
        return walls;
    }

    public void killDeadEnds() {
        Set<Position> allWalls = getAllWalls();
        List<Position> reachableWalls = floodGenerator.flood(treasurePos);
        for (Position wallPos : allWalls) {
            if (!reachableWalls.contains(wallPos)) {
                world.setTile(wallPos, Tileset.NOTHING);
            }
        }
    }
}
