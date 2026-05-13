package fun.ascent.skyblock.island;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class IslandManager {
    private static final Map<UUID, Island> loadedIslands = new ConcurrentHashMap<>();

    public static Island getIsland(UUID islandId) {
        return loadedIslands.computeIfAbsent(islandId, Island::new);
    }

    public static void unloadIsland(UUID islandId) {
        Island island = loadedIslands.get(islandId);
        if (island != null) {
            island.unload();
        }
    }

    protected static void removeIsland(UUID islandId) {
        loadedIslands.remove(islandId);
    }

    public static void saveAll() {
        loadedIslands.values().forEach(Island::save);
    }

    public static void runVacantLoop() {
        net.minestom.server.MinecraftServer.getSchedulerManager().submitTask(() -> {
            loadedIslands.values().forEach(Island::runVacantCheck);
            return net.minestom.server.timer.TaskSchedule.seconds(10);
        });
    }
}
