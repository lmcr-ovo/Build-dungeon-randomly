package Core;

import TileEngine.TETile;

public class TreasureRoom extends Room {

    private TETile treasureTile;
    private Position treasurePos;

    public TreasureRoom(Position anchor, int width, int height, TETile treasureTile) {
        super(anchor, width, height);
        this.treasureTile = treasureTile;

        int centerX = (getLeft() + getRight()) / 2;
        int centerY = (getDown() + getUp()) / 2;
        treasurePos = new Position(centerX, centerY);
    }



    public Position getTreasurePos() {
        return treasurePos;
    }

    @Override
    public void load(World world) {
        super.load(world);

        // 确保宝藏位置在房间内部（不在墙上）
        if (treasurePos.getX() > getLeft() && treasurePos.getX() < getRight() &&
                treasurePos.getY() > getDown() && treasurePos.getY() < getUp()) {
            world.setTile(treasurePos, treasureTile);
        } else {
            System.err.println("警告：宝藏位置无效 " + treasurePos);
        }
    }
    @Override
    public String toString() {
        return "TreasureRoom{" +
                "anchor=" + getAnchor() +
                ", width=" + getWidth() +
                ", height=" + getHeight() +
                ", treasureTile=" + treasureTile.description() +
                '}';
    }
}
