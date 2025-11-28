package Core;

import TileEngine.TERenderer;

import java.io.*;

public class WorldIO {
    private static final String WORLDS_DIR = "worlds";

    /**
     * 保存World对象到worlds文件夹内部指定文件名
     */
    public static void saveWorld(World world, String fileName) {
        File dir = new File(WORLDS_DIR);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new RuntimeException("无法创建 worlds 文件夹");
            }
        }

        File file = new File(dir, fileName);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(world);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("保存World失败: " + e.getMessage());
        }
    }

    /**
     * 读取worlds文件夹内指定文件的World对象
     */
    public static World loadWorld(String fileName) {
        File file = new File(WORLDS_DIR, fileName);
        if (!file.exists()) {
            throw new RuntimeException("文件不存在: " + file.getPath());
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (World) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("加载World失败: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        World world = loadWorld("Position(1, 3)");
        TERenderer ter = new TERenderer();
        ter.initialize(61, 41);
        ter.renderFrame(world.getTiles());
    }
}
