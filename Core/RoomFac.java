package Core;

import TileEngine.Tileset;
import java.util.Random;

/**
 * 统一房间工厂：负责创建所有房间类型
 */
public class RoomFac {
    private final World world;
    private final Random random;
    private final int maxRoomWidth;
    private final int maxRoomHeight;

    public RoomFac(World world, DungeonConfig config) {
        this.world = world;
        this.random = config.getRandom();
        this.maxRoomWidth = config.getMaxRoomWidth();
        this.maxRoomHeight = config.getMaxRoomHeight();
    }

    /** 创建普通房间 */
    public Room createRoom() {
        return createBaseRoom();
    }

    /** 创建宝藏房间 */
    public TreasureRoom createTreasureRoom() {
        Room base = createBaseRoom();
        return new TreasureRoom(
                base.getAnchor(),
                base.getWidth(),
                base.getHeight(),
                Tileset.LOCKED_DOOR
        );
    }

    // ===== 私有辅助方法：生成基础房间数据 =====
    private Room createBaseRoom() {
        int width = random.nextInt(maxRoomWidth / 2) * 2 + 5;
        int height = random.nextInt(maxRoomHeight / 2) * 2 + 5;

        int worldWidth = world.getWidth();
        int worldHeight = world.getHeight();

        int minX = 0;
        int maxX = worldWidth - width + 1;
        int minY = height - 1;
        int maxY = worldHeight - 1;

        int anchorX = random.nextInt((maxX - minX + 1) / 2) * 2 + minX;

        int anchorY = random.nextInt((maxY - minY + 2) / 2) * 2 + minY;

        return new Room(new Position(anchorX, anchorY), width, height);
    }
}