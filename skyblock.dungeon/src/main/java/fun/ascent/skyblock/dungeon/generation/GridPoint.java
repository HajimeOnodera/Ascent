package fun.ascent.skyblock.dungeon.generation;

public record GridPoint(int x, int y) {

    public GridPoint add(GridPoint other) {
        return new GridPoint(x + other.x, y + other.y);
    }

    public double distance(GridPoint other) {
        int dx = x - other.x;
        int dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public int manhattan(GridPoint other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }
}
