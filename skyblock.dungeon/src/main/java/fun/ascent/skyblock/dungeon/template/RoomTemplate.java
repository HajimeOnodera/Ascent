package fun.ascent.skyblock.dungeon.template;

import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.Section;
import net.minestom.server.instance.block.Block;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RoomTemplate {

    private static final byte[] MAGIC = {'D', 'R', 'O', 'O', 'M'};
    private static final String[] FACING_CW = {"north", "east", "south", "west"};

    private static final int MARKER_SCHEM_Y = 131;

    public static final int DOOR_NONE = 0;
    public static final int DOOR_OPTIONAL = 1;
    public static final int DOOR_REQUIRED = 2;

    // Primitive rotation cache: index = stateId * 4 + rotation, value = rotated stateId
    // stateId fits in a short (max ~26000 in 1.21), 32768 * 4 = 131072 entries (~1MB total)
    private static final int MAX_STATE_ID = 32768;
    private static final int[] ROTATION_CACHE = new int[MAX_STATE_ID * 4];
    private static final boolean[] ROTATION_CACHE_SET = new boolean[MAX_STATE_ID * 4];

    private final String name;
    private final int width;
    private final int height;
    private final int length;
    private final int[] stateIds; // cleaned (waterlogged stripped)
    private final int[] doorMarkers;

    private RoomTemplate(String name, int width, int height, int length, int[] stateIds) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.length = length;
        this.stateIds = cleanStateIds(stateIds);
        this.doorMarkers = detectDoorMarkers();
    }

    /**
     * Strip waterlogged=true from all stateIds at load time.
     */
    private static int[] cleanStateIds(int[] raw) {
        int[] cleaned = new int[raw.length];
        for (int i = 0; i < raw.length; i++) {
            int sid = raw[i];
            if (sid == 0) { cleaned[i] = 0; continue; }
            Block b = Block.fromStateId((short) sid);
            if (b != null && "true".equals(b.getProperty("waterlogged"))) {
                cleaned[i] = b.withProperty("waterlogged", "false").stateId();
            } else {
                cleaned[i] = sid;
            }
        }
        return cleaned;
    }

    private static int getCachedRotatedStateId(int stateId, int rotation) {
        if (rotation == 0) return stateId;
        if (stateId < 0 || stateId >= MAX_STATE_ID) return stateId;
        int idx = stateId * 4 + rotation;
        if (ROTATION_CACHE_SET[idx]) return ROTATION_CACHE[idx];

        Block block = Block.fromStateId((short) stateId);
        if (block == null) {
            ROTATION_CACHE[idx] = stateId;
            ROTATION_CACHE_SET[idx] = true;
            return stateId;
        }

        block = rotateBlockState(block, rotation);

        // Strip waterlogged
        String waterlogged = block.getProperty("waterlogged");
        if ("true".equals(waterlogged)) {
            block = block.withProperty("waterlogged", "false");
        }

        int rotatedId = block.stateId();
        ROTATION_CACHE[idx] = rotatedId;
        ROTATION_CACHE_SET[idx] = true;
        return rotatedId;
    }

    private int[] detectDoorMarkers() {
        if (MARKER_SCHEM_Y >= height) return null;

        int[] markers = new int[4];
        int centerX = width / 2;
        int centerZ = length / 2;

        markers[0] = classifyMarker(centerX, MARKER_SCHEM_Y, 0);
        markers[1] = classifyMarker(width - 1, MARKER_SCHEM_Y, centerZ);
        markers[2] = classifyMarker(centerX, MARKER_SCHEM_Y, length - 1);
        markers[3] = classifyMarker(0, MARKER_SCHEM_Y, centerZ);

        for (int m : markers) {
            if (m != DOOR_NONE) return markers;
        }
        return null;
    }

    private int classifyMarker(int x, int y, int z) {
        int index = (y * length + z) * width + x;
        if (index < 0 || index >= stateIds.length) return DOOR_NONE;

        int stateId = stateIds[index];
        if (stateId == 0) return DOOR_NONE;

        Block block = Block.fromStateId((short) stateId);
        if (block == null) return DOOR_NONE;

        if (block.compare(Block.CARVED_PUMPKIN) || block.compare(Block.PUMPKIN)) {
            return DOOR_REQUIRED;
        }
        if (block.compare(Block.END_STONE)) {
            return DOOR_OPTIONAL;
        }
        return DOOR_NONE;
    }

    public int[] doorMarkers() { return doorMarkers; }

    public static RoomTemplate load(Path file) throws IOException {
        byte[] data = Files.readAllBytes(file);
        ByteBuffer buf = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN);

        for (byte m : MAGIC) {
            if (buf.get() != m) throw new IOException("Invalid .droom magic: " + file.getFileName());
        }
        byte version = buf.get();
        if (version != 1) throw new IOException("Unsupported .droom version: " + version);

        int width = buf.getInt();
        int height = buf.getInt();
        int length = buf.getInt();
        int blockCount = width * height * length;

        int[] stateIds = new int[blockCount];
        for (int i = 0; i < blockCount; i++) {
            stateIds[i] = buf.getInt();
        }

        String name = file.getFileName().toString().replace(".droom", "");
        return new RoomTemplate(name, width, height, length, stateIds);
    }

    public void paste(InstanceContainer instance, int baseX, int baseY, int baseZ) {
        paste(instance, baseX, baseY, baseZ, 0);
    }

    /**
     * Fast paste using direct section palette writes with on-the-fly rotation.
     * Chunks MUST be pre-loaded before calling this method.
     * Rotation is computed inline — no pre-allocated rotation arrays needed.
     */
    public void paste(InstanceContainer instance, int baseX, int baseY, int baseZ, int rotation) {
        rotation = rotation & 3;
        int pasteW = (rotation % 2 == 0) ? width : length;
        int pasteL = (rotation % 2 == 0) ? length : width;

        for (int y = 0; y < height; y++) {
            int wy = baseY + y;
            int sectionIndex = wy >> 4;
            int localY = wy & 0xF;

            for (int dz = 0; dz < pasteL; dz++) {
                int wz = baseZ + dz;
                int cz = wz >> 4;
                int localZ = wz & 0xF;

                // Cache the current chunk to avoid repeated lookups
                int lastCx = Integer.MIN_VALUE;
                Chunk chunk = null;
                Section section = null;

                for (int dx = 0; dx < pasteW; dx++) {
                    // Map destination (dx,dz) back to source (ox,oz) in the original template
                    int ox, oz;
                    switch (rotation) {
                        case 1  -> { ox = dz; oz = length - 1 - dx; }
                        case 2  -> { ox = width - 1 - dx; oz = length - 1 - dz; }
                        case 3  -> { ox = width - 1 - dz; oz = dx; }
                        default -> { ox = dx; oz = dz; }
                    }

                    int stateId = stateIds[(y * length + oz) * width + ox];
                    if (stateId == 0) continue;

                    // Rotate the block state (cached, O(1) for repeated lookups)
                    if (rotation != 0) {
                        stateId = getCachedRotatedStateId(stateId, rotation);
                    }

                    int wx = baseX + dx;
                    int cx = wx >> 4;

                    if (cx != lastCx || chunk == null) {
                        chunk = instance.getChunk(cx, cz);
                        if (chunk == null) continue;
                        section = chunk.getSection(sectionIndex);
                        lastCx = cx;
                    }

                    section.blockPalette().set(wx & 0xF, localY, localZ, stateId);
                }
            }
        }
    }

    /**
     * Legacy paste that preloads its own chunks. Used by DroomCommand editor.
     */
    public void pasteWithPreload(InstanceContainer instance, int baseX, int baseY, int baseZ, int rotation) {
        rotation = rotation & 3;
        int pasteW = (rotation % 2 == 0) ? width : length;
        int pasteL = (rotation % 2 == 0) ? length : width;
        preloadChunks(instance, baseX, baseZ, pasteW, pasteL);
        paste(instance, baseX, baseY, baseZ, rotation);
    }

    static Block rotateBlockState(Block block, int rotation) {
        String facing = block.getProperty("facing");
        if (facing != null) {
            String rotated = rotateHorizontalFacing(facing, rotation);
            if (rotated != null) block = block.withProperty("facing", rotated);
        }

        String axis = block.getProperty("axis");
        if (axis != null && rotation % 2 == 1) {
            if ("x".equals(axis)) block = block.withProperty("axis", "z");
            else if ("z".equals(axis)) block = block.withProperty("axis", "x");
        }

        String signRotation = block.getProperty("rotation");
        if (signRotation != null) {
            try {
                int r = Integer.parseInt(signRotation);
                r = (r + rotation * 4) % 16;
                block = block.withProperty("rotation", String.valueOf(r));
            } catch (NumberFormatException ignored) {}
        }

        String n = block.getProperty("north");
        String e = block.getProperty("east");
        String s = block.getProperty("south");
        String w = block.getProperty("west");
        if (n != null && e != null && s != null && w != null) {
            String[] vals = {n, e, s, w};
            String[] rotated = new String[4];
            for (int i = 0; i < 4; i++) rotated[(i + rotation) % 4] = vals[i];
            block = block.withProperty("north", rotated[0])
                         .withProperty("east", rotated[1])
                         .withProperty("south", rotated[2])
                         .withProperty("west", rotated[3]);
        }

        return block;
    }

    private static String rotateHorizontalFacing(String facing, int rotation) {
        for (int i = 0; i < 4; i++) {
            if (FACING_CW[i].equals(facing)) {
                return FACING_CW[(i + rotation) % 4];
            }
        }
        return null;
    }

    private void preloadChunks(InstanceContainer instance, int baseX, int baseZ, int pasteW, int pasteL) {
        int minCX = baseX >> 4;
        int maxCX = (baseX + pasteW - 1) >> 4;
        int minCZ = baseZ >> 4;
        int maxCZ = (baseZ + pasteL - 1) >> 4;

        List<CompletableFuture<Chunk>> futures = new ArrayList<>();
        for (int cx = minCX; cx <= maxCX; cx++) {
            for (int cz = minCZ; cz <= maxCZ; cz++) {
                futures.add(instance.loadChunk(cx, cz));
            }
        }
        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
    }

    public String name() { return name; }
    public int width() { return width; }
    public int height() { return height; }
    public int length() { return length; }
}
