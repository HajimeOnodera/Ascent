package fun.ascent.skyblock.dungeon.template;

import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.InstanceContainer;
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

    private final String name;
    private final int width;
    private final int height;
    private final int length;
    private final int[] stateIds;
    private final int[] doorMarkers;

    private RoomTemplate(String name, int width, int height, int length, int[] stateIds) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.length = length;
        this.stateIds = stateIds;
        this.doorMarkers = detectDoorMarkers();
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

    public void paste(InstanceContainer instance, int baseX, int baseY, int baseZ, int rotation) {
        rotation = rotation & 3;
        int pasteW = (rotation % 2 == 0) ? width : length;
        int pasteL = (rotation % 2 == 0) ? length : width;

        preloadChunks(instance, baseX, baseZ, pasteW, pasteL);

        for (int y = 0; y < height; y++) {
            for (int dz = 0; dz < pasteL; dz++) {
                for (int dx = 0; dx < pasteW; dx++) {
                    int ox, oz;
                    switch (rotation) {
                        case 1 -> { ox = dz; oz = length - 1 - dx; }
                        case 2 -> { ox = width - 1 - dx; oz = length - 1 - dz; }
                        case 3 -> { ox = width - 1 - dz; oz = dx; }
                        default -> { ox = dx; oz = dz; }
                    }

                    int stateId = stateIds[(y * length + oz) * width + ox];
                    if (stateId == 0) continue;

                    Block block = Block.fromStateId((short) stateId);
                    if (block == null) continue;

                    if (rotation != 0) block = rotateBlockState(block, rotation);

                    String waterlogged = block.getProperty("waterlogged");
                    if ("true".equals(waterlogged)) {
                        block = block.withProperty("waterlogged", "false");
                    }

                    int wx = baseX + dx;
                    int wy = baseY + y;
                    int wz = baseZ + dz;

                    Chunk chunk = instance.getChunk(wx >> 4, wz >> 4);
                    if (chunk != null) {
                        chunk.setBlock(wx, wy, wz, block);
                    }
                }
            }
        }
    }

    private static Block rotateBlockState(Block block, int rotation) {
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
