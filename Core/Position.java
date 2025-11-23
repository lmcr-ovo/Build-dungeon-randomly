package Core;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * 不可变的二维坐标值对象
 * 只存储数据，不包含任何业务逻辑
 */
public final class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // 值对象的核心：基于字段的访问器，没有setter
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // 不可变对象的修改：返回新实例
    public Position add(int dx, int dy) {
        return new Position(x + dx, y + dy);
    }

    public Position copy() {
        return new Position(x, y);
    }

    public Position move(Direction dir, int steps) {
        return new Position(
                x + dir.getDx() * steps,
                y + dir.getDy() * steps
        );
    }

    public List<Position> getQuaPos() {
        List<Position> quaPos = new LinkedList<>();
        for (Direction dir : Direction.getCardinalDirections()) {
            quaPos.add(move(dir, 1));
        }
        return quaPos;
    }

    // 值对象必须重写equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("Position(%d, %d)", x, y);
    }
}