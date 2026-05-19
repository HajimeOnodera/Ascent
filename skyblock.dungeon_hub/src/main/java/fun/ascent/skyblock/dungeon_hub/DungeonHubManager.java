package fun.ascent.skyblock.dungeon_hub;

import fun.ascent.common.world.PolarWorlds;
import fun.ascent.common.world.WorldRegistry;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.InstanceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class DungeonHubManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DungeonHubManager.class);
    private static final Pos SPAWN = new Pos(-27, 121, 0, 0, 0);
    private static boolean initialized = false;
    @Getter
    private static InstanceContainer dungeonHubInstance;

    public static void init() {
        if (initialized) {
            LOGGER.warn("DungeonHubManager.init() called but already initialized!");
            return;
        }
        initialized = true;
        LOGGER.info("Initializing Dungeon Hub...");
        createDungeonHubWorld();
    }

    private static void createDungeonHubWorld() {
        try {
            InstanceContainer container = MinecraftServer.getInstanceManager().createInstanceContainer();
            container.setTag(WorldRegistry.WORLD_ID_TAG, "dungeon_hub");
            container.setChunkLoader(PolarWorlds.setupMemoryPolarWorld(Path.of("maps/dungeon_hub.polar"), -1));
            dungeonHubInstance = container;
            LOGGER.info("Dungeon Hub world loaded successfully.");
        } catch (Exception e) {
            LOGGER.error("Failed to load Dungeon Hub world!", e);
        }
    }

    public static Pos getSpawnPos() {
        return SPAWN;
    }
}
