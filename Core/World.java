package Core;

import TileEngine.TETile;
import TileEngine.Tileset;

import java.io.Serializable;
import java.io.*;
/**
 * 世界/地图的统一访问入口
 * 所有对tiles的读写必须经由此类
 */
public class World implements Serializable {
    private final TETile[][] tiles;
    private final int width;
    private final int height;

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new TETile[width][height];
        reset(Tileset.WALL);
    }

    public void reset(TETile fillTile) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = fillTile;
            }
        }
    }

    // 安全访问：先检查边界
    public TETile getTile(Position pos) {
        if (!isInBounds(pos)) {
            return Tileset.NOTHING; // 或抛出异常
        }
        return tiles[pos.getX()][pos.getY()];
    }

    public void setTile(Position pos, TETile tile) {
        if (!isInBounds(pos)) {
            throw new RuntimeException("位置越界: " + pos);
        }
        tiles[pos.getX()][pos.getY()] = tile;
    }

    // 核心逻辑：雕刻地板（从Position移到World）
    public void carve(Position pos) {
        setTile(pos, Tileset.FLOOR);
    }

    // 核心逻辑：检查能否雕刻（从Position移到World）
    public boolean canCarve(Position pos, Direction dir, int distance) {
        Position target = pos.move(dir, distance);
        if (!isInBounds(target)) {
            return false;
        }
        return getTile(target) == Tileset.WALL;
    }

    public boolean isInBounds(Position pos) {
        return pos.getX() >= 0 && pos.getX() < width &&
                pos.getY() >= 0 && pos.getY() < height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public TETile[][] getTiles() {
        // 返回防御性拷贝或不可变视图
        return tiles.clone();
    }

    // 添加到 World.java
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("World:\n");
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                sb.append(tiles[x][y].character());
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}