package fun.ascent.skyblock.dungeon.generation;

import java.util.*;

public enum RoomShape {
    SINGLE("1x1", true, p(0, 0)),
    DOUBLE("1x2", false, p(0, 0), p(1, 0)),
    TRIPLE("1x3", false, p(0, 0), p(1, 0), p(2, 0)),
    QUAD("1x4", false, p(0, 0), p(1, 0), p(2, 0), p(3, 0)),
    L_SHAPE("L", false, p(0, 0), p(1, 0), p(0, 1)),
    SQUARE("2x2", false, p(0, 0), p(1, 0), p(0, 1), p(1, 1));

    private final String prefix;
    private final boolean excludeFromPlacement;
    private final GridPoint[] points;

    RoomShape(String prefix, boolean excludeFromPlacement, GridPoint... points) {
        this.prefix = prefix;
        this.excludeFromPlacement = excludeFromPlacement;
        this.points = points;
    }

    public String prefix() {
        return prefix;
    }

    public boolean excludeFromPlacement() {
        return excludeFromPlacement;
    }

    public GridPoint[] points() {
        return points;
    }

    public GridPoint[] rotated(int rotations) {
        GridPoint[] result = points.clone();
        for (int i = 0; i < rotations; i++) {
            result = rotate90(result);
        }
        return normalize(result);
    }

    public static RoomShape[] fittingShapes(int gridSize) {
        List<RoomShape> fitting = new ArrayList<>();
        for (RoomShape shape : values()) {
            if (shape.excludeFromPlacement) continue;
            boolean fits = true;
            for (GridPoint p : shape.points) {
                if (p.x() >= gridSize - 1 || p.y() >= gridSize - 1) {
                    fits = false;
                    break;
                }
            }
            if (fits) fitting.add(shape);
        }
        return fitting.toArray(RoomShape[]::new);
    }

    public static ShapeIdentification identify(GridPoint[] inputPoints) {
        GridPoint[] normalized = normalize(inputPoints);
        for (RoomShape shape : values()) {
            for (int r = 0; r < 4; r++) {
                if (pointsEqual(normalized, normalize(shape.rotated(r)))) {
                    return new ShapeIdentification(shape, r);
                }
            }
        }
        throw new IllegalArgumentException("Unrecognizable shape from points: " + Arrays.toString(inputPoints));
    }

    private static GridPoint[] rotate90(GridPoint[] points) {
        GridPoint[] rotated = new GridPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            rotated[i] = new GridPoint(-points[i].y(), points[i].x());
        }
        return rotated;
    }

    private static GridPoint[] normalize(GridPoint[] points) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        for (GridPoint p : points) {
            if (p.x() < minX) minX = p.x();
            if (p.y() < minY) minY = p.y();
        }
        GridPoint[] shifted = new GridPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            shifted[i] = new GridPoint(points[i].x() - minX, points[i].y() - minY);
        }
        return shifted;
    }

    private static boolean pointsEqual(GridPoint[] a, GridPoint[] b) {
        if (a.length != b.length) return false;
        return Set.of(a).equals(Set.of(b));
    }

    private static GridPoint p(int x, int y) {
        return new GridPoint(x, y);
    }

    public record ShapeIdentification(RoomShape shape, int rotation) {}
}
