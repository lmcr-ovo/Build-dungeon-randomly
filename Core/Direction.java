package Core;

/**
 * 方向枚举：类型安全，单例，可遍历
 */
public enum Direction {
    NORTH(0, 1),
    SOUTH(0, -1),
    EAST(1, 0),
    WEST(-1, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    // 获取所有方向
    public static Direction[] getCardinalDirections() {
        return new Direction[]{NORTH, SOUTH, EAST, WEST};
    }
}