package fun.ascent.skyblock.dungeon.generation;

import java.util.*;

public class DungeonGenerator {

    private final DungeonConfig config;
    private final int gridSize;
    private final Room[][] grid;
    private final LinkedHashMap<Room, RoomShapeHandler> shapeHandlers = new LinkedHashMap<>();
    private final Map<RoomType, Integer> specialRequirements;

    private List<Room> mainPath;
    private Random random;

    public DungeonGenerator(DungeonConfig config) {
        this.config = config;
        this.gridSize = config.gridSize();
        this.grid = new Room[gridSize][gridSize];
        this.specialRequirements = config.specialRoomRequirements();
    }

    public void generate(long seed) {
        long start = System.nanoTime();
        random = new Random(seed);
        mainPath = new ArrayList<>();
        shapeHandlers.clear();

        try {
            initializeGrid();
            placeStartAndEnd();
            generateMainPath();
            placeFairyRoom();
            generateShapes();
            cleanInternalConnections();
            connectAllRooms();
            placeSpecialRooms();
        } catch (Exception e) {
            generate(seed + 1);
            return;
        }

        long elapsed = System.nanoTime() - start;
        System.out.printf("[Dungeon] Generated %dx%d layout in %.2fms%n", gridSize, gridSize, elapsed / 1_000_000.0);
    }


    private void initializeGrid() {
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                grid[x][y] = new Room(new GridPoint(x, y), RoomType.NORMAL);
            }
        }
    }

    private void placeStartAndEnd() {
        GridPoint startPos = randomWallPosition();
        GridPoint endPos;
        do {
            endPos = randomWallPosition();
        } while (onSameWall(startPos, endPos) || startPos.distance(endPos) < 2);

        roomAt(startPos).setType(RoomType.SPAWN);
        roomAt(endPos).setType(RoomType.BLOOD);
    }

    private GridPoint randomWallPosition() {
        return switch (random.nextInt(4)) {
            case 0 -> new GridPoint(random.nextInt(gridSize), 0);
            case 1 -> new GridPoint(random.nextInt(gridSize), gridSize - 1);
            case 2 -> new GridPoint(0, random.nextInt(gridSize));
            default -> new GridPoint(gridSize - 1, random.nextInt(gridSize));
        };
    }

    private boolean onSameWall(GridPoint a, GridPoint b) {
        return a.x() == b.x() || a.y() == b.y();
    }

    private void generateMainPath() {
        Room start = findRoom(RoomType.SPAWN);
        Room end = findRoom(RoomType.BLOOD);
        int minLen = Math.max(2, gridSize / 2);
        int maxLen = gridSize * gridSize / 3;

        mainPath = PathFinder.findPath(random, grid, start, end, minLen, maxLen);
        if (mainPath.isEmpty()) throw new RuntimeException("Failed to find main path");

        for (int i = 0; i < mainPath.size() - 1; i++) {
            Room current = mainPath.get(i);
            Room next = mainPath.get(i + 1);
            current.connect(next);
            next.connect(current);
        }
    }


    private void placeFairyRoom() {
        Room start = findRoom(RoomType.SPAWN);
        Room end = findRoom(RoomType.BLOOD);
        int margin = Math.max(1, Math.min(3, mainPath.size() / 3));

        List<Room> candidates = new ArrayList<>();
        for (int i = margin; i < mainPath.size() - margin; i++) {
            Room room = mainPath.get(i);
            if (!isAdjacent(room, start) && !isAdjacent(room, end)) {
                candidates.add(room);
            }
        }

        if (candidates.isEmpty()) {
            for (int i = 1; i < mainPath.size() - 1; i++) {
                Room room = mainPath.get(i);
                if (!isAdjacent(room, start) && !isAdjacent(room, end)) {
                    candidates.add(room);
                }
            }
        }

        if (candidates.isEmpty()) throw new RuntimeException("No fairy room");
        candidates.get(random.nextInt(candidates.size())).setType(RoomType.FAIRY);
    }

    private boolean isAdjacent(Room a, Room b) {
        return a.position().manhattan(b.position()) == 1;
    }


    private void generateShapes() {
        RoomShape[] shapes = RoomShape.fittingShapes(gridSize);
        int specialCount = specialRequirements.size();

        for (int i = 0; i < 100; i++) {
            Set<Room> occupied = shapeHandlers.keySet();
            int freeNormalRooms = 0;
            for (Room[] row : grid) {
                for (Room room : row) {
                    if (room.type() == RoomType.NORMAL && !occupied.contains(room)) freeNormalRooms++;
                }
            }
            if (freeNormalRooms <= specialCount) break;

            RoomShape shape = shapes[random.nextInt(shapes.length)];
            int rot = (shape == RoomShape.L_SHAPE) ? random.nextInt(3) : random.nextInt(4);
            tryPlaceShape(shape, rot);
        }

        Set<Room> occupied = shapeHandlers.keySet();
        for (Room[] row : grid) {
            for (Room room : row) {
                if (room.type() == RoomType.NORMAL && !occupied.contains(room)) {
                    RoomShapeHandler handler = new RoomShapeHandler(RoomShape.SINGLE, 0);
                    handler.addRoom(room);
                    shapeHandlers.put(room, handler);
                }
            }
        }
    }

    private void tryPlaceShape(RoomShape shape, int rotation) {
        int x = random.nextInt(gridSize);
        int y = random.nextInt(gridSize);
        GridPoint origin = new GridPoint(x, y);

        GridPoint[] points = shape.rotated(rotation);
        List<Room> rooms = new ArrayList<>();

        for (GridPoint offset : points) {
            int dx = origin.x() + offset.x();
            int dy = origin.y() + offset.y();
            if (dx < 0 || dx >= gridSize || dy < 0 || dy >= gridSize) return;
            Room room = grid[dx][dy];
            if (room.type() != RoomType.NORMAL) return;
            if (shapeHandlers.containsKey(room)) return;
            rooms.add(room);
        }

        int externalConnections = 0;
        for (Room room : rooms) {
            for (Room connected : room.connections()) {
                if (!rooms.contains(connected)) externalConnections++;
            }
        }
        if (externalConnections > 2) return;

        RoomShapeHandler handler = new RoomShapeHandler(shape, rotation);
        for (GridPoint offset : points) {
            Room room = grid[origin.x() + offset.x()][origin.y() + offset.y()];
            room.setShape(shape);
            handler.addRoom(room);
        }
        for (Room room : handler.rooms()) {
            shapeHandlers.put(room, handler);
        }
    }

    private void cleanInternalConnections() {
        Set<RoomShapeHandler> handlers = new HashSet<>(shapeHandlers.values());
        for (RoomShapeHandler handler : handlers) {
            List<Room> rooms = handler.rooms();
            for (Room room : rooms) {
                room.connections().removeIf(connected -> {
                    if (rooms.contains(connected)) {
                        connected.disconnect(room);
                        return true;
                    }
                    return false;
                });
            }
        }
    }

    private void connectAllRooms() {
        List<Room> stems = new ArrayList<>(mainPath);
        if (stems.size() > 2) {
            stems.removeFirst();
            stems.removeLast();
        }

        Set<Room> visited = new HashSet<>();
        for (Room room : mainPath) {
            RoomShapeHandler handler = shapeHandlers.get(room);
            if (handler != null) visited.addAll(handler.rooms());
        }

        int index = 0;
        while (hasUnconnectedRooms() && index < 100) {
            Room current = stems.get(index % stems.size());
            RoomShapeHandler handler = shapeHandlers.get(current);

            if (handler != null) {
                for (Room room : handler.rooms()) {
                    connectNeighbors(room, stems, visited, handler);
                }
            } else {
                connectNeighbors(current, stems, visited, null);
            }
            visited.add(current);
            index++;
        }
    }

    private boolean hasUnconnectedRooms() {
        for (Room[] row : grid) {
            for (Room room : row) {
                RoomShapeHandler handler = shapeHandlers.get(room);
                if (handler != null) {
                    if (handler.rooms().stream().allMatch(r -> r.connections().isEmpty())) return true;
                } else {
                    if (room.connections().isEmpty()) return true;
                }
            }
        }
        return false;
    }

    private void connectNeighbors(Room room, List<Room> stems, Set<Room> visited, RoomShapeHandler ownHandler) {
        for (Room neighbor : PathFinder.getNeighbors(grid, room)) {
            RoomShapeHandler neighborHandler = shapeHandlers.get(neighbor);

            boolean isVisited = visited.contains(neighbor) ||
                    (neighborHandler != null && neighborHandler.rooms().stream().anyMatch(visited::contains));
            boolean isStem = stems.contains(neighbor) ||
                    (neighborHandler != null && neighborHandler.rooms().stream().anyMatch(stems::contains));
            boolean isMainPath = mainPath.contains(neighbor) ||
                    (neighborHandler != null && neighborHandler.rooms().stream().anyMatch(mainPath::contains));
            boolean isSameShape = ownHandler != null && ownHandler.rooms().contains(neighbor);

            if (!isVisited && !isStem && !isMainPath && !isSameShape) {
                room.connect(neighbor);
                neighbor.connect(room);
                stems.add(neighbor);
            }
        }
    }

    // --- Special Rooms ---

    private void placeSpecialRooms() {
        List<Room> deadEnds = new ArrayList<>();
        for (Room[] row : grid) {
            for (Room room : row) {
                RoomShapeHandler handler = shapeHandlers.get(room);
                if (handler == null || handler.shape() != RoomShape.SINGLE) continue;
                if (room.connections().size() > 1) continue;
                if (room.type() != RoomType.NORMAL) continue;
                deadEnds.add(room);
            }
        }

        int required = specialRequirements.values().stream().mapToInt(Integer::intValue).sum();

        if (deadEnds.size() < required) {
            splitShapesForSpecialRooms(required - deadEnds.size(), deadEnds);
        }

        if (deadEnds.size() < required) {
            throw new RuntimeException("Not enough special rooms. please fix me");
        }

        Collections.shuffle(deadEnds, random);
        int idx = 0;
        for (var entry : specialRequirements.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                deadEnds.get(idx++).setType(entry.getKey());
            }
        }
    }

    private void splitShapesForSpecialRooms(int needed, List<Room> deadEnds) {
        List<RoomShapeHandler> candidates = new ArrayList<>(
                shapeHandlers.values().stream()
                        .filter(h -> h.shape() != RoomShape.SINGLE)
                        .distinct()
                        .toList()
        );
        Collections.shuffle(candidates, random);

        int created = 0;
        for (RoomShapeHandler handler : candidates) {
            if (created >= needed) break;

            List<Room> edgeCandidates = handler.rooms().stream()
                    .filter(r -> r.connections().isEmpty() && r.type() == RoomType.NORMAL)
                    .toList();
            if (edgeCandidates.isEmpty()) continue;

            Room edge = edgeCandidates.get(random.nextInt(edgeCandidates.size()));
            decouple(handler, edge);
            deadEnds.add(edge);
            created++;
        }
    }

    private void decouple(RoomShapeHandler handler, Room roomToRemove) {
        for (Room r : handler.rooms()) shapeHandlers.remove(r);

        List<Room> remaining = new ArrayList<>(handler.rooms());
        remaining.remove(roomToRemove);

        if (remaining.size() == 1) {
            registerSingle(remaining.getFirst());
        } else {
            GridPoint[] points = remaining.stream().map(Room::position).toArray(GridPoint[]::new);
            boolean contiguous = isContiguous(points);

            if (contiguous) {
                var id = RoomShape.identify(points);
                RoomShapeHandler newHandler = new RoomShapeHandler(id.shape(), id.rotation());
                for (Room room : remaining) {
                    room.setShape(id.shape());
                    newHandler.addRoom(room);
                    shapeHandlers.put(room, newHandler);
                }
            } else {
                for (Room room : remaining) registerSingle(room);
            }
        }

        registerSingle(roomToRemove);

        // Connect decoupled room to its nearest former sibling
        Room closest = remaining.stream()
                .filter(r -> r.position().x() == roomToRemove.position().x() || r.position().y() == roomToRemove.position().y())
                .min(Comparator.comparingDouble(r -> r.position().distance(roomToRemove.position())))
                .orElse(remaining.getFirst());
        roomToRemove.connect(closest);
        closest.connect(roomToRemove);
    }

    private void registerSingle(Room room) {
        room.setShape(RoomShape.SINGLE);
        RoomShapeHandler handler = new RoomShapeHandler(RoomShape.SINGLE, 0);
        handler.addRoom(room);
        shapeHandlers.put(room, handler);
    }

    private boolean isContiguous(GridPoint[] points) {
        if (points.length <= 1) return true;
        Set<GridPoint> set = new HashSet<>(Arrays.asList(points));
        Set<GridPoint> visited = new HashSet<>();
        Deque<GridPoint> stack = new ArrayDeque<>();
        stack.push(points[0]);
        visited.add(points[0]);
        while (!stack.isEmpty()) {
            GridPoint p = stack.pop();
            for (GridPoint neighbor : List.of(
                    new GridPoint(p.x() - 1, p.y()), new GridPoint(p.x() + 1, p.y()),
                    new GridPoint(p.x(), p.y() - 1), new GridPoint(p.x(), p.y() + 1))) {
                if (set.contains(neighbor) && visited.add(neighbor)) {
                    stack.push(neighbor);
                }
            }
        }
        return visited.size() == points.length;
    }

    // --- Accessors ---

    public Room[][] grid() { return grid; }
    public int gridSize() { return gridSize; }
    public List<Room> mainPath() { return mainPath; }
    public LinkedHashMap<Room, RoomShapeHandler> shapeHandlers() { return shapeHandlers; }
    public DungeonConfig config() { return config; }

    public Room spawnRoom() { return findRoom(RoomType.SPAWN); }

    private Room roomAt(GridPoint p) { return grid[p.x()][p.y()]; }

    private Room findRoom(RoomType type) {
        for (Room[] row : grid) {
            for (Room room : row) {
                if (room.type() == type) return room;
            }
        }
        return null;
    }
}
