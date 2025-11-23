package Core;

import TileEngine.TETile;
import TileEngine.Tileset;
import java.util.Random;

/**
 * 递归回溯迷宫生成器
 */
public class MazeGenerator {
    private final World world;
    private final Random random;

    public MazeGenerator(World world, Random random) {
        this.world = world;
        this.random = random;
    }

    public void carveMaze(Position start) {
        TETile startTile = world.getTile(start);
        if (startTile == Tileset.FLOOR || startTile == Tileset.LOCKED_DOOR) {
            return;
        }
        world.carve(start);
        Direction[] dirs = Direction.getCardinalDirections();
        shuffleArray(dirs);
        for (Direction dir : dirs) {
            if (world.canCarve(start, dir, 2)) {
                world.carve(start.move(dir, 1));
                System.out.println("start changed to "+ start.move(dir, 2).toString() +"");
                System.out.print(world);
                carveMaze(start.move(dir, 2));
            }
        }
    }

    // Fisher-Yates 打乱数组
    private void shuffleArray(Direction[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            Direction temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
}