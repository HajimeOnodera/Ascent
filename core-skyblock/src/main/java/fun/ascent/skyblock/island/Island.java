package fun.ascent.skyblock.island;

import fun.ascent.common.world.WorldRegistry;
import fun.ascent.skyblock.island.data.IslandDatabase;
import lombok.Getter;
import lombok.Setter;
import net.hollowcube.polar.PolarLoader;
import net.hollowcube.polar.PolarReader;
import net.hollowcube.polar.PolarWriter;
import net.hollowcube.polar.PolarWorld;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import org.bson.Document;
import org.bson.types.Binary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class Island {

    private final UUID islandId;
    @Getter
    private InstanceContainer instance;
    private PolarWorld polarWorld;
    @Getter
    private boolean loaded = false;
    private int version = 1;

    // Additional data to be saved in MongoDB alongside the world
    @Getter
    @Setter
    private List<Document> minionData = new ArrayList<>();
    @Getter
    @Setter
    private List<Document> npcData = new ArrayList<>();

    public Island(UUID islandId) {
        this.islandId = islandId;
    }

    public CompletableFuture<InstanceContainer> load() {
        if (loaded && instance != null) {
            return CompletableFuture.completedFuture(instance);
        }

        System.out.println("[Island] Loading island " + islandId);
        return CompletableFuture.supplyAsync(() -> {
            try {
                Document doc = IslandDatabase.getIsland(islandId);
                if (doc == null) {
                    System.out.println("[Island] No saved data found, loading template for " + islandId);
                    // New island, load from template
                    byte[] templateBytes = Files.readAllBytes(Path.of("maps/privisland.polar"));
                    this.polarWorld = PolarReader.read(templateBytes);
                    this.version = 1;
                } else {
                    System.out.println("[Island] Found saved data for " + islandId);
                    this.version = doc.getInteger("version", 1);
                    Binary data = doc.get("data", Binary.class);
                    this.polarWorld = PolarReader.read(data.getData());

                    // Load additional data
                    this.minionData = doc.getList("minions", Document.class, new ArrayList<>());
                    this.npcData = doc.getList("npcs", Document.class, new ArrayList<>());
                    System.out.println("[Island] Restored " + minionData.size() + " minions and " + npcData.size() + " NPCs from DB");
                }

                // Create instance on the main thread (Minestom requirement)
                CompletableFuture<Void> syncFuture = new CompletableFuture<>();
                MinecraftServer.getSchedulerManager().submitTask(() -> {
                    System.out.println("[Island] Creating instance container for " + islandId);
                    this.instance = MinecraftServer.getInstanceManager().createInstanceContainer();
                    this.instance.setChunkLoader(new PolarLoader(polarWorld));
                    this.instance.setTag(WorldRegistry.WORLD_ID_TAG, islandId.toString());
                    this.loaded = true;
                    syncFuture.complete(null);
                    return TaskSchedule.stop();
                }, ExecutionType.TICK_END);
                
                syncFuture.join();
                System.out.println("[Island] Island " + islandId + " load complete");
                return instance;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public String getName() {
        return islandId.toString();
    }

    public void runVacantCheck() {
        if (instance == null || !loaded) return;

        if (instance.getPlayers().isEmpty()) {
            System.out.println("[Island] Island " + islandId + " is vacant, unloading...");
            save();
            unload();
        }
    }

    public void unload() {
        if (instance != null) {
            MinecraftServer.getInstanceManager().unregisterInstance(instance);
            instance = null;
        }
        this.polarWorld = null;
        this.loaded = false;
        IslandManager.removeIsland(islandId);
    }

    public void save() {
        if (!loaded || instance == null || polarWorld == null) return;
        
        System.out.println("[Island] Saving island " + islandId + " to database...");
        // Update polarWorld with current instance state
        new PolarLoader(polarWorld).saveInstance(instance);
        byte[] bytes = PolarWriter.write(polarWorld);
        IslandDatabase.saveIsland(islandId, bytes, version, minionData, npcData);
    }
}
