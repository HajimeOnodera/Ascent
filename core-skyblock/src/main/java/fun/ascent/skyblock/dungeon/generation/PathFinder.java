package fun.ascent.skyblock.dungeon.generation;

import java.util.*;

public final class PathFinder {

    private static final int MAX_ATTEMPTS = 1000;

    private PathFinder() {}

    public static List<Room> findPath(Random random, Room[][] grid, Room start, Room end, int minLength, int maxLength) {
        int gridW = grid.length;
        int gridH = grid[0].length;

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            List<Room> path = new ArrayList<>();
            boolean[][] visited = new boolean[gridW][gridH];
            if (dfs(random, grid, start, end, minLength, maxLength, path, visited)) {
                return path;
            }
        }

        return List.of();
    }

    private static boolean dfs(Random random, Room[][] grid, Room current, Room end,
                               int minLength, int maxLength, List<Room> path, boolean[][] visited) {
        if (current == end && path.size() >= minLength && path.size() <= maxLength) {
            path.add(current);
            return true;
        }
        if (path.size() > maxLength) return false;

        GridPoint pos = current.position();
        visited[pos.x()][pos.y()] = true;
        path.add(current);

        List<Room> neighbors = getNeighbors(grid, current);
        Collections.shuffle(neighbors, random);

        for (Room neighbor : neighbors) {
            GridPoint np = neighbor.position();
            if (!visited[np.x()][np.y()]) {
                if (dfs(random, grid, neighbor, end, minLength, maxLength, path, visited)) {
                    return true;
                }
            }
        }

        path.removeLast();
        visited[pos.x()][pos.y()] = false;
        return false;
    }

    static List<Room> getNeighbors(Room[][] grid, Room room) {
        int x = room.position().x();
        int y = room.position().y();
        int w = grid.length;
        int h = grid[0].length;

        List<Room> neighbors = new ArrayList<>(4);
        if (x > 0)     neighbors.add(grid[x - 1][y]);
        if (x < w - 1) neighbors.add(grid[x + 1][y]);
        if (y > 0)     neighbors.add(grid[x][y - 1]);
        if (y < h - 1) neighbors.add(grid[x][y + 1]);
        return neighbors;
    }
}
