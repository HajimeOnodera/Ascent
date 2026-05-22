package fun.ascent.skyblock.dungeon;

import fun.ascent.skyblock.dungeon.generation.*;
import fun.ascent.skyblock.dungeon.template.RoomTemplate;
import fun.ascent.skyblock.dungeon.template.RoomTemplateLoader;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class DungeonInstance {

    public static final int ROOM_SIZE = 31;
    public static final int GAP = 1;
    public static final int CELL_STRIDE = ROOM_SIZE + GAP;
    public static final int GRID_ORIGIN_X = -200;
    public static final int GRID_ORIGIN_Z = -200;

    private static final int PASTE_Y = -64;
    private static final int FLOOR_Y = 66;
    private static final int MARKER_DETECT_Y = PASTE_Y + 131;
    private static final int DOOR_CARVE_Y = FLOOR_Y + 3;
    private static final int DOOR_HEIGHT = 4;
    private static final int WALL_CENTER = ROOM_SIZE / 2;
    private static final int L_ROT_OFFSET = 1;

    private final UUID id;
    private final DungeonFloor floor;
    private final DungeonGenerator generator;
    private final InstanceContainer instance;
    private final Map<Room, String> roomNameMap = new HashMap<>();

    public DungeonInstance(UUID id, DungeonFloor floor, DungeonGenerator generator, InstanceContainer instance) {
        this.id = id;
        this.floor = floor;
        this.generator = generator;
        this.instance = instance;
    }

    public void buildWorld(RoomTemplateLoader templates) {
        long start = System.nanoTime();
        Random random = new Random();
        Room[][] grid = generator.grid();
        int gridSize = generator.gridSize();

        // Batch preload ALL chunks needed for the entire dungeon grid
        preloadAllChunks(gridSize);

        pasteRooms(random, grid, gridSize, templates);
        processDoors(grid, gridSize, templates, random);

        // Invalidate all chunks and recalculate lighting
        invalidateAllChunks(gridSize);
        LightingChunk.relight(instance, instance.getChunks());

        long elapsed = System.nanoTime() - start;
        System.out.printf("[Dungeon] Built world for %s in %.2fms%n", floor.shortName(), elapsed / 1_000_000.0);
    }

    private void preloadAllChunks(int gridSize) {
        int minX = GRID_ORIGIN_X - GAP;
        int maxX = GRID_ORIGIN_X + gridSize * CELL_STRIDE + GAP;
        int minZ = GRID_ORIGIN_Z - GAP;
        int maxZ = GRID_ORIGIN_Z + gridSize * CELL_STRIDE + GAP;

        int minCX = minX >> 4;
        int maxCX = maxX >> 4;
        int minCZ = minZ >> 4;
        int maxCZ = maxZ >> 4;

        List<CompletableFuture<Chunk>> futures = new ArrayList<>();
        for (int cx = minCX; cx <= maxCX; cx++) {
            for (int cz = minCZ; cz <= maxCZ; cz++) {
                futures.add(instance.loadChunk(cx, cz));
            }
        }
        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
    }

    private void invalidateAllChunks(int gridSize) {
        int minX = GRID_ORIGIN_X - GAP;
        int maxX = GRID_ORIGIN_X + gridSize * CELL_STRIDE + GAP;
        int minZ = GRID_ORIGIN_Z - GAP;
        int maxZ = GRID_ORIGIN_Z + gridSize * CELL_STRIDE + GAP;

        int minCX = minX >> 4;
        int maxCX = maxX >> 4;
        int minCZ = minZ >> 4;
        int maxCZ = maxZ >> 4;

        for (int cx = minCX; cx <= maxCX; cx++) {
            for (int cz = minCZ; cz <= maxCZ; cz++) {
                Chunk chunk = instance.getChunk(cx, cz);
                if (chunk != null) chunk.invalidate();
            }
        }
    }

    private void pasteRooms(Random random, Room[][] grid, int gridSize, RoomTemplateLoader templates) {
        Set<Room> pasted = new HashSet<>();
        Set<String> usedSchems = new HashSet<>();
        LinkedHashMap<Room, RoomShapeHandler> handlers = generator.shapeHandlers();

        for (int gx = 0; gx < gridSize; gx++) {
            for (int gy = 0; gy < gridSize; gy++) {
                Room room = grid[gx][gy];
                if (pasted.contains(room)) continue;

                RoomShapeHandler handler = handlers.get(room);
                RoomTemplate template = null;
                int pasteRotation = 0;

                boolean isSingle = handler == null || handler.shape() == RoomShape.SINGLE;
                int connectionCount = isSingle ? countConnections(room) : 0;

                Set<String> triedNames = new HashSet<>();
                for (int attempt = 0; attempt < 30; attempt++) {
                    RoomTemplate candidate = pickTemplate(random, room, handler, templates, usedSchems);
                    if (candidate == null) break;
                    if (!triedNames.add(candidate.name())) continue;

                    if (isSingle && countTemplateMarkers(candidate) < connectionCount) continue;

                    int rot = computePasteRotation(handler, room, candidate);
                    if (validateRotation(room, candidate, rot)) {
                        template = candidate;
                        pasteRotation = rot;
                        break;
                    }
                }

                if (template == null) {
                    List<RoomTemplate> allCandidates = getAllTemplates(room, handler, templates);
                    if (allCandidates.isEmpty()) {
                        allCandidates = templates.getByShape(RoomShape.SINGLE);
                    }
                    int bestScore = Integer.MIN_VALUE;
                    for (RoomTemplate candidate : allCandidates) {
                        for (int rot = 0; rot < (isSingle ? 4 : 1); rot++) {
                            int effectiveRot = isSingle ? rot : computePasteRotation(handler, room, candidate);
                            if (!validateRotation(room, candidate, effectiveRot)) continue;
                            int coverage = countMarkerCoverage(room, candidate, effectiveRot);
                            int markerCount = countTemplateMarkers(candidate);
                            int score = coverage * 100 + markerCount;
                            if (score > bestScore) {
                                bestScore = score;
                                template = candidate;
                                pasteRotation = effectiveRot;
                            }
                        }
                    }
                }

                if (template == null) {
                    List<RoomTemplate> allCandidates = getAllTemplates(room, handler, templates);
                    if (allCandidates.isEmpty()) {
                        allCandidates = templates.getByShape(RoomShape.SINGLE);
                    }
                    int bestCoverage = -1;
                    for (RoomTemplate candidate : allCandidates) {
                        for (int rot = 0; rot < (isSingle ? 4 : 1); rot++) {
                            int effectiveRot = isSingle ? rot : computePasteRotation(handler, room, candidate);
                            int coverage = countMarkerCoverage(room, candidate, effectiveRot);
                            if (coverage > bestCoverage) {
                                bestCoverage = coverage;
                                template = candidate;
                                pasteRotation = effectiveRot;
                            }
                        }
                    }
                }

                if (template == null) {
                    if (handler != null) pasted.addAll(handler.rooms());
                    else pasted.add(room);
                    continue;
                }

                int minGx = gx, minGy = gy;
                if (handler != null) {
                    for (Room r : handler.rooms()) {
                        minGx = Math.min(minGx, r.position().x());
                        minGy = Math.min(minGy, r.position().y());
                    }
                }

                int worldX = GRID_ORIGIN_X + minGx * CELL_STRIDE;
                int worldZ = GRID_ORIGIN_Z + minGy * CELL_STRIDE;
                template.paste(instance, worldX, PASTE_Y, worldZ, pasteRotation);

                if (handler != null) {
                    for (Room r : handler.rooms()) roomNameMap.put(r, template.name());
                    pasted.addAll(handler.rooms());
                } else {
                    roomNameMap.put(room, template.name());
                    pasted.add(room);
                }
            }
        }
    }

    private boolean validateRotation(Room room, RoomTemplate template, int rotation) {
        int[] markers = template.doorMarkers();
        if (markers == null) return true;

        boolean[] connections = getEffectiveConnections(room);

        int[] rotatedMarkers = new int[4];
        for (int dir = 0; dir < 4; dir++) {
            rotatedMarkers[(dir + rotation) % 4] = markers[dir];
        }

        for (int dir = 0; dir < 4; dir++) {
            if (rotatedMarkers[dir] == RoomTemplate.DOOR_REQUIRED && !connections[dir]) return false;
        }
        return true;
    }

    private int countConnections(Room room) {
        int count = 0;
        for (boolean c : getConnectionDirections(room)) {
            if (c) count++;
        }
        return count;
    }

    private int countTemplateMarkers(RoomTemplate template) {
        int[] markers = template.doorMarkers();
        if (markers == null) return 0;
        int count = 0;
        for (int m : markers) {
            if (m != RoomTemplate.DOOR_NONE) count++;
        }
        return count;
    }

    private int countMarkerCoverage(Room room, RoomTemplate template, int rotation) {
        int[] markers = template.doorMarkers();
        if (markers == null) return 0;

        boolean[] connections = getEffectiveConnections(room);
        int covered = 0;
        for (int dir = 0; dir < 4; dir++) {
            int rotatedDir = (dir + rotation) % 4;
            if (connections[rotatedDir] && markers[dir] != RoomTemplate.DOOR_NONE) {
                covered++;
            }
        }
        return covered;
    }

    private boolean[] getEffectiveConnections(Room room) {
        LinkedHashMap<Room, RoomShapeHandler> handlers = generator.shapeHandlers();
        RoomShapeHandler handler = handlers.get(room);
        if (handler != null && handler.rooms().size() > 1) {
            boolean[] connections = new boolean[4];
            for (Room cell : handler.rooms()) {
                boolean[] cellDirs = getConnectionDirections(cell);
                for (int i = 0; i < 4; i++) connections[i] |= cellDirs[i];
            }
            return connections;
        }
        return getConnectionDirections(room);
    }

    private int computePasteRotation(RoomShapeHandler handler, Room room, RoomTemplate template) {
        if (handler == null || handler.shape() == RoomShape.SINGLE) {
            return computeSingleRotation(room, template);
        }
        if (handler.shape() == RoomShape.SQUARE) return 0;

        GridPoint[] points = handler.rooms().stream().map(Room::position).toArray(GridPoint[]::new);
        RoomShape.ShapeIdentification id = RoomShape.identify(points);
        int generatorRot = id.rotation();

        if (handler.shape() == RoomShape.L_SHAPE) {
            return (generatorRot + L_ROT_OFFSET) % 4;
        }
        return generatorRot;
    }

    private int computeSingleRotation(Room room, RoomTemplate template) {
        int[] markers = template.doorMarkers();
        if (markers == null) return 0;

        boolean[] connections = getConnectionDirections(room);
        int gridSize = generator.gridSize();
        boolean[] onEdge = {
                room.position().y() == 0,
                room.position().x() == gridSize - 1,
                room.position().y() == gridSize - 1,
                room.position().x() == 0
        };

        int bestRot = 0;
        int bestScore = Integer.MIN_VALUE;

        for (int rot = 0; rot < 4; rot++) {
            int score = 0;
            boolean valid = true;

            int[] rotatedMarkers = new int[4];
            for (int dir = 0; dir < 4; dir++) {
                rotatedMarkers[(dir + rot) % 4] = markers[dir];
            }

            for (int dir = 0; dir < 4; dir++) {
                int marker = rotatedMarkers[dir];

                if (marker == RoomTemplate.DOOR_REQUIRED) {
                    if (connections[dir]) score += 10;
                    else { valid = false; break; }
                } else if (marker == RoomTemplate.DOOR_OPTIONAL) {
                    if (connections[dir]) score += 5;
                    else if (onEdge[dir]) score -= 3;
                } else if (marker == RoomTemplate.DOOR_NONE) {
                    if (connections[dir]) score -= 50;
                }
            }

            if (valid && score > bestScore) {
                bestScore = score;
                bestRot = rot;
            }
        }
        return bestRot;
    }

    private boolean[] getConnectionDirections(Room room) {
        boolean[] dirs = new boolean[4];
        GridPoint pos = room.position();
        for (Room connected : room.connections()) {
            GridPoint cPos = connected.position();
            int dx = cPos.x() - pos.x();
            int dy = cPos.y() - pos.y();
            if (dy == -1) dirs[0] = true;
            else if (dx == 1) dirs[1] = true;
            else if (dy == 1) dirs[2] = true;
            else if (dx == -1) dirs[3] = true;
        }
        return dirs;
    }

    private void processDoors(Room[][] grid, int gridSize, RoomTemplateLoader templates, Random random) {
        Set<Long> processed = new HashSet<>();

        for (int gx = 0; gx < gridSize; gx++) {
            for (int gy = 0; gy < gridSize; gy++) {
                Room room = grid[gx][gy];
                int cellX = GRID_ORIGIN_X + gx * CELL_STRIDE;
                int cellZ = GRID_ORIGIN_Z + gy * CELL_STRIDE;

                int[][] neighbors = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
                int[][] markerPos = {
                        {cellX + WALL_CENTER, cellZ},
                        {cellX + ROOM_SIZE - 1, cellZ + WALL_CENTER},
                        {cellX + WALL_CENTER, cellZ + ROOM_SIZE - 1},
                        {cellX, cellZ + WALL_CENTER}
                };

                for (int dir = 0; dir < 4; dir++) {
                    int ngx = gx + neighbors[dir][0];
                    int ngy = gy + neighbors[dir][1];

                    long key = pairKey(gx, gy, ngx, ngy, gridSize);
                    if (!processed.add(key)) continue;

                    boolean hasNeighbor = ngx >= 0 && ngx < gridSize && ngy >= 0 && ngy < gridSize;
                    if (!hasNeighbor || !isConnected(room, grid[ngx][ngy])) {
                        clearMarker(markerPos[dir][0], markerPos[dir][1]);
                        continue;
                    }

                    int nCellX = GRID_ORIGIN_X + ngx * CELL_STRIDE;
                    int nCellZ = GRID_ORIGIN_Z + ngy * CELL_STRIDE;
                    int oppositeDir = (dir + 2) % 4;
                    int[][] nMarkerPos = {
                            {nCellX + WALL_CENTER, nCellZ},
                            {nCellX + ROOM_SIZE - 1, nCellZ + WALL_CENTER},
                            {nCellX + WALL_CENTER, nCellZ + ROOM_SIZE - 1},
                            {nCellX, nCellZ + WALL_CENTER}
                    };

                    boolean thisLoaded = instance.isChunkLoaded(markerPos[dir][0] >> 4, markerPos[dir][1] >> 4);
                    boolean otherLoaded = instance.isChunkLoaded(nMarkerPos[oppositeDir][0] >> 4, nMarkerPos[oppositeDir][1] >> 4);

                    Block thisMarker = thisLoaded ? instance.getBlock(markerPos[dir][0], MARKER_DETECT_Y, markerPos[dir][1]) : Block.AIR;
                    Block otherMarker = otherLoaded ? instance.getBlock(nMarkerPos[oppositeDir][0], MARKER_DETECT_Y, nMarkerPos[oppositeDir][1]) : Block.AIR;
                    boolean thisHasMarker = isMarkerRequired(thisMarker) || isMarkerOptional(thisMarker);
                    boolean otherHasMarker = isMarkerRequired(otherMarker) || isMarkerOptional(otherMarker);

                    if (thisHasMarker && otherHasMarker) {
                        carveDoor(gx, gy, dir);

                        Room neighbor = grid[ngx][ngy];
                        DoorType doorType = classifyDoor(room, neighbor);
                        pasteDoorSchematic(templates, random, gx, gy, dir, doorType);

                        if (doorType == DoorType.WITHER && shouldPlaceWitherBlock(room, neighbor)) {
                            placeDoorBlock(gx, gy, dir, Block.COAL_BLOCK);
                        } else if (doorType == DoorType.BLOOD) {
                            placeDoorBlock(gx, gy, dir, Block.RED_CONCRETE);
                        } else if (doorType == DoorType.STARTER) {
                            placeDoorBlock(gx, gy, dir, Block.CHISELED_STONE_BRICKS);
                        }
                    }

                    clearMarker(markerPos[dir][0], markerPos[dir][1]);
                    clearMarker(nMarkerPos[oppositeDir][0], nMarkerPos[oppositeDir][1]);
                }
            }
        }
    }

    private void clearMarker(int mx, int mz) {
        if (!instance.isChunkLoaded(mx >> 4, mz >> 4)) return;
        Block block = instance.getBlock(mx, MARKER_DETECT_Y, mz);
        if (isMarkerRequired(block) || isMarkerOptional(block)) {
            instance.setBlock(mx, MARKER_DETECT_Y, mz, Block.STONE_BRICKS);
        }
    }

    private boolean shouldPlaceWitherBlock(Room roomA, Room roomB) {
        if (roomA.type() == RoomType.FAIRY || roomB.type() == RoomType.FAIRY) {
            Room fairy = roomA.type() == RoomType.FAIRY ? roomA : roomB;
            Room other = roomA.type() == RoomType.FAIRY ? roomB : roomA;
            int fairyIdx = mainPathIndex(fairy);
            int otherIdx = mainPathIndex(other);
            return otherIdx > fairyIdx;
        }
        return true;
    }

    private int mainPathIndex(Room room) {
        List<Room> mainPath = generator.mainPath();
        int idx = mainPath.indexOf(room);
        if (idx >= 0) return idx;

        LinkedHashMap<Room, RoomShapeHandler> handlers = generator.shapeHandlers();
        RoomShapeHandler handler = handlers.get(room);
        if (handler != null) {
            for (Room cell : handler.rooms()) {
                idx = mainPath.indexOf(cell);
                if (idx >= 0) return idx;
            }
        }
        return -1;
    }

    private DoorType classifyDoor(Room roomA, Room roomB) {
        if (roomA == null || roomB == null) return DoorType.NORMAL;

        if (roomA.type() == RoomType.SPAWN || roomB.type() == RoomType.SPAWN) return DoorType.STARTER;
        if (roomA.type() == RoomType.BLOOD || roomB.type() == RoomType.BLOOD) return DoorType.BLOOD;

        if (areConsecutiveOnMainPath(roomA, roomB)
                && isWitherEligible(roomA.type()) && isWitherEligible(roomB.type())) {
            return DoorType.WITHER;
        }

        return DoorType.NORMAL;
    }

    private boolean areConsecutiveOnMainPath(Room roomA, Room roomB) {
        int idxA = mainPathIndex(roomA);
        int idxB = mainPathIndex(roomB);
        if (idxA < 0 || idxB < 0) return false;
        return Math.abs(idxA - idxB) == 1;
    }

    private boolean isWitherEligible(RoomType type) {
        return type == RoomType.NORMAL || type == RoomType.FAIRY;
    }

    private void pasteDoorSchematic(RoomTemplateLoader templates, Random random,
                                    int gx, int gy, int dir, DoorType doorType) {
        RoomTemplate doorTemplate = pickDoorTemplate(templates, random, doorType);
        if (doorTemplate == null) return;

        int cellX = GRID_ORIGIN_X + gx * CELL_STRIDE;
        int cellZ = GRID_ORIGIN_Z + gy * CELL_STRIDE;
        int halfW = doorTemplate.width() / 2;
        int halfL = doorTemplate.length() / 2;

        int doorX, doorZ, rotation;
        switch (dir) {
            case 0 -> { doorX = cellX + WALL_CENTER - halfW; doorZ = cellZ - GAP - halfL; rotation = 3; }
            case 1 -> { doorX = cellX + ROOM_SIZE - halfL; doorZ = cellZ + WALL_CENTER - halfW; rotation = 0; }
            case 2 -> { doorX = cellX + WALL_CENTER - halfW; doorZ = cellZ + ROOM_SIZE - halfL; rotation = 1; }
            default -> { doorX = cellX - GAP - halfL; doorZ = cellZ + WALL_CENTER - halfW; rotation = 2; }
        }

        doorTemplate.paste(instance, doorX, FLOOR_Y + 2, doorZ, rotation);
    }

    private RoomTemplate pickDoorTemplate(RoomTemplateLoader templates, Random random, DoorType doorType) {
        return switch (doorType) {
            case BLOOD -> templates.get("BloodDoor");
            case STARTER -> templates.get("StarterDoor");
            case WITHER -> {
                RoomTemplate t1 = templates.get("WitherDoor-1");
                RoomTemplate t2 = templates.get("WitherDoor-2");
                if (t1 == null) yield t2;
                if (t2 == null) yield t1;
                yield random.nextBoolean() ? t1 : t2;
            }
            case NORMAL -> {
                List<RoomTemplate> options = new ArrayList<>();
                for (int i = 1; i <= 3; i++) {
                    RoomTemplate t = templates.get("NormalDoor-" + i);
                    if (t != null) options.add(t);
                }
                yield options.isEmpty() ? null : options.get(random.nextInt(options.size()));
            }
        };
    }

    private boolean isMarkerRequired(Block block) {
        return block.compare(Block.CARVED_PUMPKIN) || block.compare(Block.PUMPKIN);
    }

    private boolean isMarkerOptional(Block block) {
        return block.compare(Block.END_STONE);
    }

    private boolean isConnected(Room a, Room b) {
        return a.connections().contains(b);
    }

    private void carveDoor(int gx, int gy, int dir) {
        int cellX = GRID_ORIGIN_X + gx * CELL_STRIDE;
        int cellZ = GRID_ORIGIN_Z + gy * CELL_STRIDE;

        switch (dir) {
            case 0 -> {
                int gapZ = cellZ - 1;
                int centerX = cellX + WALL_CENTER;
                for (int w = -2; w <= 2; w++)
                    for (int y = DOOR_CARVE_Y; y < DOOR_CARVE_Y + DOOR_HEIGHT; y++)
                        for (int d = -3; d <= 3; d++)
                            instance.setBlock(centerX + w, y, gapZ + d, Block.AIR);
            }
            case 1 -> {
                int gapX = cellX + ROOM_SIZE;
                int centerZ = cellZ + WALL_CENTER;
                for (int w = -2; w <= 2; w++)
                    for (int y = DOOR_CARVE_Y; y < DOOR_CARVE_Y + DOOR_HEIGHT; y++)
                        for (int d = -3; d <= 3; d++)
                            instance.setBlock(gapX + d, y, centerZ + w, Block.AIR);
            }
            case 2 -> {
                int gapZ = cellZ + ROOM_SIZE;
                int centerX = cellX + WALL_CENTER;
                for (int w = -2; w <= 2; w++)
                    for (int y = DOOR_CARVE_Y; y < DOOR_CARVE_Y + DOOR_HEIGHT; y++)
                        for (int d = -3; d <= 3; d++)
                            instance.setBlock(centerX + w, y, gapZ + d, Block.AIR);
            }
            case 3 -> {
                int gapX = cellX - 1;
                int centerZ = cellZ + WALL_CENTER;
                for (int w = -2; w <= 2; w++)
                    for (int y = DOOR_CARVE_Y; y < DOOR_CARVE_Y + DOOR_HEIGHT; y++)
                        for (int d = -3; d <= 3; d++)
                            instance.setBlock(gapX + d, y, centerZ + w, Block.AIR);
            }
        }
    }

    private void placeDoorBlock(int gx, int gy, int dir, Block block) {
        int cellX = GRID_ORIGIN_X + gx * CELL_STRIDE;
        int cellZ = GRID_ORIGIN_Z + gy * CELL_STRIDE;

        int centerX, centerZ;
        switch (dir) {
            case 0 -> { centerX = cellX + WALL_CENTER; centerZ = cellZ - 1; }
            case 1 -> { centerX = cellX + ROOM_SIZE; centerZ = cellZ + WALL_CENTER; }
            case 2 -> { centerX = cellX + WALL_CENTER; centerZ = cellZ + ROOM_SIZE; }
            default -> { centerX = cellX - 1; centerZ = cellZ + WALL_CENTER; }
        }

        for (int dx = -1; dx <= 1; dx++)
            for (int y = DOOR_CARVE_Y; y < DOOR_CARVE_Y + DOOR_HEIGHT; y++)
                for (int dz = -1; dz <= 1; dz++)
                    instance.setBlock(centerX + dx, y, centerZ + dz, block);
    }

    private long pairKey(int x1, int y1, int x2, int y2, int gridSize) {
        long ka = (long) x1 * gridSize + y1;
        long kb = (long) x2 * gridSize + y2;
        return Math.min(ka, kb) * 10000L + Math.max(ka, kb);
    }

    private List<RoomTemplate> getAllTemplates(Room room, RoomShapeHandler handler,
                                              RoomTemplateLoader templates) {
        return switch (room.type()) {
            case SPAWN -> {
                RoomTemplate t = templates.get("Special-Spawn");
                yield t != null ? List.of(t) : List.of();
            }
            case BLOOD -> {
                List<RoomTemplate> list = new ArrayList<>();
                for (String name : new String[]{"Special-Blood-Pillarless", "Special-Blood-Plants", "Special-Blood-Red"}) {
                    RoomTemplate t = templates.get(name);
                    if (t != null) list.add(t);
                }
                yield list;
            }
            case FAIRY -> {
                RoomTemplate t = templates.get("Special-Fairy");
                yield t != null ? List.of(t) : List.of();
            }
            case MINIBOSS -> templates.getByType(RoomType.MINIBOSS);
            case TRAP -> templates.getByType(RoomType.TRAP);
            case PUZZLE -> templates.getByType(RoomType.PUZZLE);
            case NORMAL -> {
                RoomShape shape = handler != null ? handler.shape() : RoomShape.SINGLE;
                yield templates.getByShape(shape);
            }
        };
    }

    private RoomTemplate pickTemplate(Random random, Room room, RoomShapeHandler handler,
                                      RoomTemplateLoader templates, Set<String> used) {
        return switch (room.type()) {
            case SPAWN -> templates.get("Special-Spawn");
            case BLOOD -> pickFromNames(random, templates, used,
                    "Special-Blood-Pillarless", "Special-Blood-Plants", "Special-Blood-Red");
            case FAIRY -> templates.get("Special-Fairy");
            case MINIBOSS -> templates.pickForType(random, RoomType.MINIBOSS, used);
            case TRAP -> {
                RoomTemplate t = templates.pickForType(random, RoomType.TRAP, used);
                yield t != null ? t : templates.get("Special-Trap-1");
            }
            case PUZZLE -> templates.pickForType(random, RoomType.PUZZLE, used);
            case NORMAL -> {
                if (handler == null) yield templates.pickForShape(random, RoomShape.SINGLE, used);
                yield templates.pickForShape(random, handler.shape(), used);
            }
        };
    }

    private RoomTemplate pickFromNames(Random random, RoomTemplateLoader templates,
                                       Set<String> used, String... names) {
        List<String> available = new ArrayList<>();
        for (String name : names) {
            if (!used.contains(name) && templates.get(name) != null) available.add(name);
        }
        if (available.isEmpty()) available.addAll(Arrays.asList(names));
        String picked = available.get(random.nextInt(available.size()));
        used.add(picked);
        return templates.get(picked);
    }

    public Pos spawnPosition() {
        Room spawn = generator.spawnRoom();
        if (spawn == null) return new Pos(GRID_ORIGIN_X + 0.5, FLOOR_Y + 6, GRID_ORIGIN_Z + 0.5);

        GridPoint pos = spawn.position();
        double centerX = GRID_ORIGIN_X + pos.x() * CELL_STRIDE + ROOM_SIZE / 2.0;
        double centerZ = GRID_ORIGIN_Z + pos.y() * CELL_STRIDE + ROOM_SIZE / 2.0;
        return new Pos(centerX, FLOOR_Y + 6, centerZ);
    }

    public UUID id() { return id; }
    public DungeonFloor floor() { return floor; }
    public DungeonGenerator generator() { return generator; }
    public InstanceContainer instance() { return instance; }
    public Map<Room, String> roomNameMap() { return roomNameMap; }

    private enum DoorType {
        NORMAL, STARTER, BLOOD, WITHER
    }
}
