package Core;

import TileEngine.Tileset;


public class Room {
    private Position anchor;
    private int width;
    private int height;

    public Room(Position anchor, int width, int length) {
        this.anchor = anchor;
        this.width = width;
        this.height = length;
    }

    public int getLeft() {
        return anchor.getX();
    }

    public int getRight() {
        return anchor.getX() + width - 1;
    }

    public int getUp() {
        return anchor.getY();
    }

    public int getDown() {
        return anchor.getY() - height + 1;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Position getAnchor() {
        return anchor;
    }


    public boolean overlapsWith(Room other) {
        return !(this.getRight() <= other.getLeft() ||
                this.getLeft() >= other.getRight() ||
                this.getUp() <= other.getDown() ||
                this.getDown() >= other.getUp());
    }


    // Room.java 中改为接受 World
    public void load(World world) {
        int x1 = getLeft();
        int x2 = getRight();
        int yTop = getUp();
        int yBottom = getDown();

        for (int x = x1; x <= x2; x++) {
            for (int y = yBottom; y <= yTop; y++) {
                boolean isWall = (x == x1 || x == x2 || y == yBottom || y == yTop);
                world.setTile(new Position(x, y), isWall ? Tileset.WALL : Tileset.FLOOR);
            }
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Room[anchor=(%d,%d), width=%d, height=%d, xRange=%d-%d, yRange=%d-%d]",
                anchor.getX(), anchor.getY(), width, height,
                getLeft(), getRight(), getDown(), getUp()
        );
    }
}
