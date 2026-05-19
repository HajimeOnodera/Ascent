package fun.ascent.skyblock.dungeon.commands;

import fun.ascent.skyblock.dungeon.template.RoomTemplate;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.world.WorldHandler;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class DroomCommand extends Command {

    private static final Path ROOMS_DIR = Path.of("core-skyblock", "akyblockResources", "dungeonRooms");
    private static final int EDIT_BASE_Y = 0;

    private static final Map<UUID, EditSession> sessions = new ConcurrentHashMap<>();

    public DroomCommand() {
        super("droom");

        var actionArg = ArgumentType.String("action");
        var nameArg = ArgumentType.String("name");

        setDefaultExecutor((sender, _) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;
            sendHelp(player);
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;
            String action = context.get(actionArg).toLowerCase();

            switch (action) {
                case "list" -> listSchematics(player);
                case "save" -> saveSession(player, null);
                case "leave" -> leaveSession(player);
                default -> sendHelp(player);
            }
        }, actionArg);

        addSyntax((sender, context) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;
            String action = context.get(actionArg).toLowerCase();
            String name = context.get(nameArg);

            switch (action) {
                case "edit" -> editSchematic(player, name);
                case "save" -> saveSession(player, name);
                case "new" -> newSchematic(player, name);
                default -> sendHelp(player);
            }
        }, actionArg, nameArg);
    }

    private void sendHelp(SkyblockPlayer player) {
        player.sendMessage("§e/droom edit <name> §7- Edit an existing .droom file");
        player.sendMessage("§e/droom new <WxHxL> §7- Create a new schematic (e.g. 31x130x31)");
        player.sendMessage("§e/droom save [name] §7- Save the current edit session");
        player.sendMessage("§e/droom leave §7- Leave without saving");
        player.sendMessage("§e/droom list §7- List available .droom files");
    }

    private void listSchematics(SkyblockPlayer player) {
        if (!Files.isDirectory(ROOMS_DIR)) {
            player.sendMessage("§cRooms directory not found: " + ROOMS_DIR);
            return;
        }

        List<String> names = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(ROOMS_DIR, "*.droom")) {
            for (Path file : stream) {
                names.add(file.getFileName().toString().replace(".droom", ""));
            }
        } catch (IOException e) {
            player.sendMessage("§cFailed to list schematics: " + e.getMessage());
            return;
        }

        Collections.sort(names);
        player.sendMessage("§eAvailable .droom files §7(" + names.size() + ")§e:");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < names.size(); i++) {
            if (i > 0) sb.append("§7, ");
            sb.append("§a").append(names.get(i));
        }
        player.sendMessage(sb.toString());
    }

    private void editSchematic(SkyblockPlayer player, String name) {
        if (sessions.containsKey(player.getUuid())) {
            player.sendMessage("§cYou already have an edit session. Use /droom save or /droom leave first.");
            return;
        }

        Path file = ROOMS_DIR.resolve(name + ".droom");
        if (!Files.exists(file)) {
            player.sendMessage("§cSchematic not found: §e" + name);
            return;
        }

        RoomTemplate template;
        try {
            template = RoomTemplate.load(file);
        } catch (IOException e) {
            player.sendMessage("§cFailed to load: " + e.getMessage());
            return;
        }

        InstanceManager manager = MinecraftServer.getInstanceManager();
        InstanceContainer editInstance = manager.createInstanceContainer();
        editInstance.setChunkSupplier(LightingChunk::new);
        editInstance.setTag(WorldHandler.worldID, "droom_edit_" + player.getUsername());

        int pasteX = 0;
        int pasteZ = 0;

        template.pasteWithPreload(editInstance, pasteX, EDIT_BASE_Y, pasteZ, 0);

        EditSession session = new EditSession(name, editInstance, file,
                pasteX, EDIT_BASE_Y, pasteZ,
                template.width(), template.height(), template.length());
        sessions.put(player.getUuid(), session);

        double cx = pasteX + template.width() / 2.0;
        double cz = pasteZ + template.length() / 2.0;
        player.setInstance(editInstance, new Pos(cx, EDIT_BASE_Y + 70, cz));

        player.sendMessage("§aEdit session started for §e" + name);
        player.sendMessage("§7Dimensions: §e" + template.width() + "x" + template.height() + "x" + template.length());
        player.sendMessage("§7Pasted at §e(" + pasteX + ", " + EDIT_BASE_Y + ", " + pasteZ + ")");
    }

    private void newSchematic(SkyblockPlayer player, String dimensions) {
        if (sessions.containsKey(player.getUuid())) {
            player.sendMessage("§cYou already have an edit session. Use /droom save or /droom leave first.");
            return;
        }

        String[] parts = dimensions.toLowerCase().split("x");
        if (parts.length != 3) {
            player.sendMessage("§cFormat: /droom new <width>x<height>x<length> (e.g. 31x130x31)");
            return;
        }

        int width, height, length;
        try {
            width = Integer.parseInt(parts[0]);
            height = Integer.parseInt(parts[1]);
            length = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            player.sendMessage("§cInvalid dimensions. Use numbers like 31x130x31");
            return;
        }

        InstanceManager manager = MinecraftServer.getInstanceManager();
        InstanceContainer editInstance = manager.createInstanceContainer();
        editInstance.setChunkSupplier(LightingChunk::new);
        editInstance.setTag(WorldHandler.worldID, "droom_new_" + player.getUsername());

        int pasteX = 0;
        int pasteZ = 0;
        preloadChunks(editInstance, pasteX, pasteZ, width, length);

        EditSession session = new EditSession(null, editInstance, null,
                pasteX, EDIT_BASE_Y, pasteZ, width, height, length);
        sessions.put(player.getUuid(), session);

        double cx = pasteX + width / 2.0;
        double cz = pasteZ + length / 2.0;
        player.setInstance(editInstance, new Pos(cx, EDIT_BASE_Y + 10, cz));

        player.sendMessage("§aNew schematic session started §7(" + width + "x" + height + "x" + length + ")");
        player.sendMessage("§7Use §e/droom save <name> §7to save.");
    }

    private void saveSession(SkyblockPlayer player, String nameOverride) {
        EditSession session = sessions.get(player.getUuid());
        if (session == null) {
            player.sendMessage("§cNo active edit session. Use /droom edit <name> first.");
            return;
        }

        String saveName = (nameOverride != null) ? nameOverride : session.name;
        if (saveName == null) {
            player.sendMessage("§cYou must specify a name: /droom save <name>");
            return;
        }

        Path saveFile = ROOMS_DIR.resolve(saveName + ".droom");

        try {
            writeDroom(saveFile, session);
        } catch (IOException e) {
            player.sendMessage("§cFailed to save: " + e.getMessage());
            return;
        }

        player.sendMessage("§aSaved §e" + saveName + ".droom §a(" + session.width + "x" + session.height + "x" + session.length + ")");
        cleanupSession(player);
    }

    private void leaveSession(SkyblockPlayer player) {
        EditSession session = sessions.get(player.getUuid());
        if (session == null) {
            player.sendMessage("§cNo active edit session.");
            return;
        }

        player.sendMessage("§aLeft edit session without saving.");
        cleanupSession(player);
    }

    private void cleanupSession(SkyblockPlayer player) {
        EditSession session = sessions.remove(player.getUuid());
        if (session == null) return;

        InstanceContainer lobby = WorldHandler.getLobby();
        if (lobby != null) {
            player.setInstance(lobby, WorldHandler.getLobbySpawn());
        }

        MinecraftServer.getInstanceManager().unregisterInstance(session.instance);
    }

    private void writeDroom(Path file, EditSession session) throws IOException {
        int blockCount = session.width * session.height * session.length;
        int fileSize = 5 + 1 + 12 + blockCount * 4;

        ByteBuffer buf = ByteBuffer.allocate(fileSize).order(ByteOrder.BIG_ENDIAN);
        buf.put((byte) 'D');
        buf.put((byte) 'R');
        buf.put((byte) 'O');
        buf.put((byte) 'O');
        buf.put((byte) 'M');
        buf.put((byte) 1);

        buf.putInt(session.width);
        buf.putInt(session.height);
        buf.putInt(session.length);

        InstanceContainer instance = session.instance;
        for (int y = 0; y < session.height; y++) {
            for (int z = 0; z < session.length; z++) {
                for (int x = 0; x < session.width; x++) {
                    int wx = session.baseX + x;
                    int wy = session.baseY + y;
                    int wz = session.baseZ + z;

                    Block block = instance.getBlock(wx, wy, wz);
                    buf.putInt(block.stateId());
                }
            }
        }

        buf.flip();
        Files.createDirectories(file.getParent());
        Files.write(file, buf.array());
    }

    private void preloadChunks(InstanceContainer instance, int baseX, int baseZ, int width, int length) {
        int minCX = baseX >> 4;
        int maxCX = (baseX + width - 1) >> 4;
        int minCZ = baseZ >> 4;
        int maxCZ = (baseZ + length - 1) >> 4;

        List<CompletableFuture<Chunk>> futures = new ArrayList<>();
        for (int cx = minCX; cx <= maxCX; cx++) {
            for (int cz = minCZ; cz <= maxCZ; cz++) {
                futures.add(instance.loadChunk(cx, cz));
            }
        }
        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
    }

    public static EditSession getSession(UUID uuid) {
        return sessions.get(uuid);
    }

    public static class EditSession {
        public final String name;
        public final InstanceContainer instance;
        public final Path originalFile;
        public final int baseX, baseY, baseZ;
        public final int width, height, length;

        EditSession(String name, InstanceContainer instance, Path originalFile,
                    int x, int y, int z, int w, int h, int l) {
            this.name = name;
            this.instance = instance;
            this.originalFile = originalFile;
            this.baseX = x;
            this.baseY = y;
            this.baseZ = z;
            this.width = w;
            this.height = h;
            this.length = l;
        }
    }
}
