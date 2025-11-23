package Core;

import java.util.Objects;
import java.util.Random;

/**
 * config of DungeonConfig, must be created by Builder
 * eg: DungeonConfig config = new DungeonConfig.Builder().build();
 */
public class DungeonConfig {
    // 1. 字段声明为 final，但不在此处初始化
    private final int windowWidth;
    private final int windowHeight;
    private final int maxRooms;
    private final int maxRoomWidth;
    private final int maxRoomHeight;
    private final long seed;
    private final Random random;

    // 2. 私有构造函数，只能通过 Builder 构建
    private DungeonConfig(Builder builder) {
        this.windowWidth = builder.windowWidth;
        this.windowHeight = builder.windowHeight;
        this.maxRooms = builder.maxRooms;
        this.maxRoomWidth = builder.maxRoomWidth;
        this.maxRoomHeight = builder.maxRoomHeight;
        this.seed = builder.seed;
        this.random = new Random(builder.seed); // 重要：每次 new 保证实例隔离
    }

    // 3. 所有字段必须有 Getter（不可变对象需要暴露访问）
    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public int getMaxRooms() {
        return maxRooms;
    }

    public int getMaxRoomWidth() {
        return maxRoomWidth;
    }

    public int getMaxRoomHeight() {
        return maxRoomHeight;
    }

    public long getSeed() {
        return seed;
    }

    public Random getRandom() {
        // 返回新实例防止外部篡改（防御性拷贝）
        // return new Random(seed);
        return  random;
    }

    // 4. 重写 toString, equals, hashCode（工业级标配）
    @Override
    public String toString() {
        return "DungeonConfig{" +
                "windowWidth=" + windowWidth +
                ", windowHeight=" + windowHeight +
                ", maxRooms=" + maxRooms +
                ", maxRoomWidth=" + maxRoomWidth +
                ", maxRoomHeight=" + maxRoomHeight +
                ", seed=" + seed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DungeonConfig that = (DungeonConfig) o;
        return windowWidth == that.windowWidth &&
                windowHeight == that.windowHeight &&
                maxRooms == that.maxRooms &&
                maxRoomWidth == that.maxRoomWidth &&
                maxRoomHeight == that.maxRoomHeight &&
                seed == that.seed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(windowWidth, windowHeight, maxRooms,
                maxRoomWidth, maxRoomHeight, seed);
    }

    // 5. Builder 静态内部类
    public static class Builder {
        // 5.1 与外部类相同的字段，用默认值
        private int windowWidth = 41;
        private int windowHeight = 41;
        private int maxRooms = 500;
        private int maxRoomWidth = 11;
        private int maxRoomHeight = 11;
        private long seed = System.currentTimeMillis(); // 默认用当前时间

        // 5.2 链式 Setter（返回 Builder 自身）
        public Builder windowWidth(int width) {
            if (width <= 0 || width % 2 == 0) {
                throw new IllegalArgumentException("Width must be positive odd number");
            }
            this.windowWidth = width;
            return this;
        }

        public Builder windowHeight(int height) {
            if (height <= 0 || height % 2 == 0) {
                throw new IllegalArgumentException("Height must be positive odd number");
            }
            this.windowHeight = height;
            return this;
        }

        public Builder windowSize(int width, int height) {
            return windowWidth(width).windowHeight(height);
        }

        public Builder maxRooms(int maxRooms) {
            if (maxRooms < 1) {
                throw new IllegalArgumentException("Max rooms must be at least 1");
            }
            this.maxRooms = maxRooms;
            return this;
        }

        public Builder maxRoomWidth(int maxWidth) {
            if (maxWidth < 3 || maxWidth % 2 == 0) {
                throw new IllegalArgumentException("Max room width must be >= 3 and odd");
            }
            this.maxRoomWidth = maxWidth;
            return this;
        }

        public Builder maxRoomHeight(int maxHeight) {
            if (maxHeight < 3 || maxHeight % 2 == 0) {
                throw new IllegalArgumentException("Max room height must be >= 3 and odd");
            }
            this.maxRoomHeight = maxHeight;
            return this;
        }

        public Builder maxRoomSize(int width, int height) {
            if (width < 3 || width % 2 == 0 ||
                height < 3 || height % 2 == 0) {
                throw new IllegalArgumentException("room width and height must be >= 3 and odd");
            }
            return maxRoomWidth(width).maxRoomHeight(height);
        }

        public Builder seed(long seed) {
            this.seed = seed;
            return this;
        }

        // 5.3 验证逻辑（在 build() 时调用）
        private void validate() {
            if (maxRoomWidth > windowWidth || maxRoomHeight > windowHeight) {
                throw new IllegalStateException(
                        "Room size cannot exceed window size");
            }
            if (maxRooms > (windowWidth * windowHeight) / 2) {
                throw new IllegalStateException(
                        "Max rooms too high for the given window size");
            }
        }

        // 5.4 构建方法
        public DungeonConfig build() {
            validate();
            return new DungeonConfig(this);
        }
    }

    public static void main(String[] args) {
        // once create the object, can't be modified anymore
        DungeonConfig config1 = new DungeonConfig.Builder().build();
    }
}