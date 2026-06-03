package fun.ascent.skyblock.island;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class IslandManager {
    private static final Map<UUID, Island> loadedIslands = new ConcurrentHashMap<>();

    private static final int MAX_SAVES_PER_CYCLE = 25;
    private static int saveCursor = 0;

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

    private static void saveRoundRobin() {
        if (loadedIslands.isEmpty()) return;

        List<UUID> keys = new ArrayList<>(loadedIslands.keySet());
        if (keys.isEmpty()) return;

        if (saveCursor >= keys.size()) {
            saveCursor = 0;
        }

        int saved = 0;
        int idx = saveCursor;

        while (saved < MAX_SAVES_PER_CYCLE && saved < keys.size()) {
            UUID islandId = keys.get(idx);
            Island island = loadedIslands.get(islandId);
            if (island != null) {
                island.save();
            }

            saved++;
            idx++;
            if (idx >= keys.size()) {
                idx = 0;
            }

            if (idx == saveCursor) break;
        }

        saveCursor = keys.isEmpty() ? 0 : (idx % keys.size());
    }

    public static void runVacantLoop() {
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            loadedIslands.values().forEach(Island::runVacantCheck);
            return TaskSchedule.seconds(10);
        });

        MinecraftServer.getSchedulerManager().submitTask(() -> {
            saveRoundRobin();
            return TaskSchedule.seconds(30);
        });
    }
}
