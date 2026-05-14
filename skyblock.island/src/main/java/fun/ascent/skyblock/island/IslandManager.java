package fun.ascent.skyblock.island;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;

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
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            loadedIslands.values().forEach(Island::runVacantCheck);
            return TaskSchedule.seconds(10);
        });
        
        // Also run a periodic auto-save for all loaded islands (e.g. every 30 seconds)
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            saveAll();
            return TaskSchedule.seconds(30);
        });
    }
}
