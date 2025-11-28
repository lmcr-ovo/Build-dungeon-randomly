package Core;

import TileEngine.TERenderer;
import TileEngine.Tileset;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 地牢控制器 - 协调所有生成逻辑
 * DungeonController → RoomFactory → Room
 *          ↓                ↓
 *      MazeGenerator → World
 */
public class DungeonController {
    private final World world;
    private final DungeonConfig config;
    private final RoomFac roomFac;
    private final MazeGenerator mazeGenerator;
    private final FloodGenerator floodGenerator;
    private final List<Room> rooms = new ArrayList<>();
    private TreasureRoom treasureRoom;
    private DeadEndKiller endKiller;


    public DungeonController(DungeonConfig config) {
        this.config = config;
        this.world = new World(config.getWindowWidth(), config.getWindowHeight());
        this.mazeGenerator = new MazeGenerator(world, config.getRandom());
        this.floodGenerator = new FloodGenerator(world, config.getRandom());
        this.roomFac = new RoomFac(world, config);
    }


    private void reset() {
        world.reset(Tileset.WALL);
        rooms.clear();
    }

    private void generateRooms() {
        // 先生成宝藏房
        treasureRoom = roomFac.createTreasureRoom();
        rooms.add(treasureRoom);

        // 生成普通房间
        int attempts = 0;
        while (rooms.size() < config.getMaxRooms() && attempts < config.getMaxRooms()) {
            Room candidate = roomFac.createRoom();
            // 使用实例方法检查重叠
            boolean overlap = rooms.stream()
                    .anyMatch(existing -> existing.overlapsWith(candidate));

            if (!overlap) {
                rooms.add(candidate);
            }
            attempts++;
        }
        // 加载到世界
        rooms.forEach(room -> room.load(world));
    }

    private void generateMaze() {
        for (int x = 1; x < world.getWidth(); x += 2) {
            for (int y = 1; y < world.getHeight(); y += 2) {
                Position start = new Position(x, y);
                mazeGenerator.carveMaze(start);

                // debug
                String fileName = start.toString();
                WorldIO.saveWorld(world, fileName);
            }
        }
    }

    private void render() {
        TERenderer ter = new TERenderer();
        ter.initialize(config.getWindowWidth(), config.getWindowHeight());
        ter.renderFrame(world.getTiles());
    }

    public void generate() {
        reset();
        generateRooms();
        endKiller = new DeadEndKiller(world, config.getRandom(), treasureRoom.getTreasurePos());
        generateMaze();

        floodGenerator.floodConnect(new Position(1, 1));
        endKiller.fillDeadEnds();
        endKiller.killDeadEnds();
        render();

        // 打印统计
        System.out.printf("生成完成：%d 个房间，地图大小 %dx%d，种子 %d%n",
                rooms.size(), config.getWindowWidth(), config.getWindowHeight(), config.getSeed());
    }

    // 测试方法
    public String printWorld() {
        return world.toString();
    }

    public static void main(String[] args) {
        DungeonConfig config = new DungeonConfig.Builder()
                .seed(13256)
                .maxRoomSize(21, 21)
                .windowWidth(61)
                .build();

        DungeonController controller = new DungeonController(config);
        controller.generate();
    }
}
