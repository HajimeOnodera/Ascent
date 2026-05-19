package fun.ascent.skyblock.dungeon;

import fun.ascent.skyblock.dungeon.generation.DungeonConfig;
import fun.ascent.skyblock.dungeon.generation.DungeonGenerator;
import fun.ascent.skyblock.dungeon.template.RoomTemplateLoader;
import fun.ascent.skyblock.world.WorldHandler;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DungeonManager {

    private static final Path ROOMS_DIR = Path.of("core-skyblock", "akyblockResources", "dungeonRooms");

    private static DungeonManager instance;

    private final RoomTemplateLoader templates = new RoomTemplateLoader();
    private final Map<UUID, DungeonInstance> activeDungeons = new ConcurrentHashMap<>();
    private final Map<UUID, DungeonInstance> playerDungeonMap = new ConcurrentHashMap<>();
    private final Queue<InstanceContainer> instancePool = new ConcurrentLinkedQueue<>();

    private DungeonManager() {}

    public static DungeonManager get() {
        if (instance == null) instance = new DungeonManager();
        return instance;
    }

    public void initialize() {
        try {
            templates.loadAll(ROOMS_DIR);
        } catch (IOException e) {
            System.err.println("[Dungeon] Failed to load room templates: " + e.getMessage());
        }
    }

    public DungeonInstance createDungeon(DungeonFloor floor) {
        UUID id = UUID.randomUUID();

        DungeonConfig config = new DungeonConfig(floor);
        DungeonGenerator generator = new DungeonGenerator(config);
        long seed = System.nanoTime();
        generator.generate(seed);
        generator.printGrid(seed);

        InstanceContainer container = acquireInstance();
        container.setTag(WorldHandler.worldID, "dungeon_" + id.toString().substring(0, 8));
        DungeonInstance dungeon = new DungeonInstance(id, floor, generator, container);
        dungeon.buildWorld(templates);

        activeDungeons.put(id, dungeon);
        System.out.printf("[Dungeon] Created %s dungeon: %s%n", floor.shortName(), id);
        return dungeon;
    }

    public void addPlayer(Player player, DungeonInstance dungeon) {
        playerDungeonMap.put(player.getUuid(), dungeon);
        player.setInstance(dungeon.instance(), dungeon.spawnPosition());
    }

    public void trackPlayer(UUID playerUuid, DungeonInstance dungeon) {
        playerDungeonMap.put(playerUuid, dungeon);
    }

    public void removePlayer(Player player) {
        playerDungeonMap.remove(player.getUuid());
    }

    public DungeonInstance getDungeon(UUID playerUUID) {
        return playerDungeonMap.get(playerUUID);
    }

    public void destroyDungeon(UUID dungeonId) {
        DungeonInstance dungeon = activeDungeons.remove(dungeonId);
        if (dungeon == null) return;

        playerDungeonMap.values().removeIf(d -> d.id().equals(dungeonId));

        InstanceContainer container = dungeon.instance();
        InstanceManager manager = MinecraftServer.getInstanceManager();
        manager.unregisterInstance(container);

        System.out.printf("[Dungeon] Destroyed dungeon: %s%n", dungeonId);
    }

    private InstanceContainer acquireInstance() {
        InstanceContainer pooled = instancePool.poll();
        if (pooled != null) return pooled;

        InstanceManager manager = MinecraftServer.getInstanceManager();
        InstanceContainer container = manager.createInstanceContainer();
        container.setChunkSupplier(LightingChunk::new);
        return container;
    }

    public void returnInstance(InstanceContainer container) {
        instancePool.offer(container);
    }

    public RoomTemplateLoader templates() {
        return templates;
    }

    public Map<UUID, DungeonInstance> activeDungeons() {
        return activeDungeons;
    }
}
