package fun.ascent.skyblock.entity.mob;

import com.mongodb.client.MongoCollection;
import fun.ascent.database.MongoProvider;
import fun.ascent.skyblock.entity.mob.impl.SpotSpawner;
import fun.ascent.skyblock.world.WorldHandler;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.timer.TaskSchedule;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class SpotSpawnerTicker {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpotSpawnerTicker.class);

    private static final Map<Class<?>, MobSpotData> mobSpotMap = new ConcurrentHashMap<>();
    private static boolean initialized = false;

    public static void start() {
        LOGGER.info("Starting Spot Spawner Ticker...");
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            try {
                InstanceContainer world = WorldHandler.getLobby();
                if (world == null) {
                    return TaskSchedule.seconds(10);
                }
                if (world.getPlayers().isEmpty()) {
                    return TaskSchedule.seconds(10);
                }

                if (!initialized) {
                    initialize();
                }

                long now = System.currentTimeMillis();

                for (MobSpotData data : mobSpotMap.values()) {
                    try {
                        tickMobType(data, world, now);
                    } catch (Throwable t) {
                        LOGGER.error("Error ticking spot spawner for {}",
                                data.entry.getPrototype().displayName(), t);
                    }
                }
            } catch (Throwable t) {
                LOGGER.error("Fatal error in SpotSpawnerTicker background task!", t);
            }

            return TaskSchedule.seconds(1);
        });
    }

    private static synchronized void initialize() {
        if (initialized) return;

        for (EntityRegistry entry : EntityRegistry.getSpotSpawners()) {
            SpotSpawner spawner = (SpotSpawner) entry.getPrototype();
            SpotSpawner.SpotConfig config = spawner.spotConfig();

            List<MobSpawnSpot> spots = loadSpotsFromMongo(config.collectionName());

            MobSpotData data = new MobSpotData(entry, config, spots);
            mobSpotMap.put(entry.getPrototype().getClass(), data);

            LOGGER.info("Registered spot spawner for '{}' with {} spots from collection '{}'.",
                    entry.getPrototype().displayName(), spots.size(), config.collectionName());
        }

        initialized = true;
    }

    private static void tickMobType(MobSpotData data, InstanceContainer world, long now) {
        for (MobSpawnSpot spot : data.spots) {
            try {
                List<SkyblockMobEntity> active = spot.getActiveEntities();
                int max = data.config.maxPerSpot();

                if (active.size() < max) {
                    if (spot.getNextSpawnTime() == 0) {
                        long delayMs = data.config.spawnDelaySeconds() * 1000L;
                        spot.setNextSpawnTime(now + delayMs);
                        LOGGER.info("Spot at Pos({}, {}, {}) for {} is underpopulated ({}/{}). Queued spawn in {}s.",
                                spot.getPosition().blockX(), spot.getPosition().blockY(), spot.getPosition().blockZ(),
                                data.entry.getPrototype().displayName(), active.size(), max, data.config.spawnDelaySeconds());
                    } else if (now >= spot.getNextSpawnTime()) {
                        SkyblockMobEntity mob = data.entry.spawn();

                        Pos pos = spot.getPosition();
                        if (!world.isChunkLoaded(pos)) {
                            world.loadChunk(pos).join();
                        }

                        mob.setInstance(world, pos);
                        active.add(mob);

                        spot.setNextSpawnTime(0);

                        LOGGER.info("Spot Spawner: Spawned {} at Pos({}, {}, {})",
                                mob.displayName(), pos.blockX(), pos.blockY(), pos.blockZ());
                    }
                } else {
                    if (spot.getNextSpawnTime() != 0) {
                        spot.setNextSpawnTime(0);
                    }
                }
            } catch (Throwable t) {
                LOGGER.error("Error ticking spot at {} for {}",
                        spot.getPosition(), data.entry.getPrototype().displayName(), t);
            }
        }
    }

    private static List<MobSpawnSpot> loadSpotsFromMongo(String collectionName) {
        List<MobSpawnSpot> spots = new ArrayList<>();
        try {
            MongoCollection<Document> collection = MongoProvider.getCollection(collectionName);
            for (Document doc : collection.find()) {
                double x = doc.getDouble("x");
                double y = doc.getDouble("y");
                double z = doc.getDouble("z");
                spots.add(new MobSpawnSpot(new Pos(x, y, z)));
            }
        } catch (Throwable t) {
            LOGGER.error("Failed to load spawn spots from MongoDB collection '{}'!", collectionName, t);
        }
        return spots;
    }

    // --- Public API for commands ---

    public static void addSpot(Class<? extends SkyblockMobEntity> mobClass, Pos pos) {
        MobSpotData data = mobSpotMap.get(mobClass);
        if (data == null) {
            LOGGER.warn("Cannot add spot — no SpotSpawner registered for class {}", mobClass.getSimpleName());
            return;
        }
        try {
            MongoCollection<Document> collection = MongoProvider.getCollection(data.config.collectionName());
            Document doc = new Document()
                    .append("x", pos.x())
                    .append("y", pos.y())
                    .append("z", pos.z());
            collection.insertOne(doc);

            data.spots.add(new MobSpawnSpot(pos));
            LOGGER.info("Added spawn spot for '{}' at Pos({}, {}, {})",
                    data.entry.getPrototype().displayName(), pos.blockX(), pos.blockY(), pos.blockZ());
        } catch (Throwable t) {
            LOGGER.error("Failed to add spawn spot to MongoDB!", t);
        }
    }

    public static List<MobSpawnSpot> getSpots(Class<? extends SkyblockMobEntity> mobClass) {
        MobSpotData data = mobSpotMap.get(mobClass);
        if (data == null) return List.of();
        return data.spots;
    }

    public static void clearSpots(Class<? extends SkyblockMobEntity> mobClass) {
        MobSpotData data = mobSpotMap.get(mobClass);
        if (data == null) return;
        try {
            MongoCollection<Document> collection = MongoProvider.getCollection(data.config.collectionName());
            collection.deleteMany(new Document());

            // Remove all active mobs from spots before clearing
            for (MobSpawnSpot spot : data.spots) {
                for (SkyblockMobEntity mob : spot.getActiveEntities()) {
                    mob.remove();
                }
            }
            data.spots.clear();
            LOGGER.info("Cleared all spawn spots for '{}'.", data.entry.getPrototype().displayName());
        } catch (Throwable t) {
            LOGGER.error("Failed to clear spawn spots from MongoDB!", t);
        }
    }

    // --- Inner classes ---

    private static class MobSpotData {
        final EntityRegistry entry;
        final SpotSpawner.SpotConfig config;
        final List<MobSpawnSpot> spots;

        MobSpotData(EntityRegistry entry, SpotSpawner.SpotConfig config, List<MobSpawnSpot> spots) {
            this.entry = entry;
            this.config = config;
            this.spots = new CopyOnWriteArrayList<>(spots);
        }
    }

    public static class MobSpawnSpot {
        private final Pos position;
        private final List<SkyblockMobEntity> activeEntities = new CopyOnWriteArrayList<>();
        private long nextSpawnTime = 0;

        public MobSpawnSpot(Pos position) {
            this.position = position;
            this.nextSpawnTime = System.currentTimeMillis();
        }

        public Pos getPosition() {
            return position;
        }

        public List<SkyblockMobEntity> getActiveEntities() {
            activeEntities.removeIf(mob -> mob.isRemoved() || mob.getInstance() == null);
            return activeEntities;
        }

        public long getNextSpawnTime() {
            return nextSpawnTime;
        }

        public void setNextSpawnTime(long nextSpawnTime) {
            this.nextSpawnTime = nextSpawnTime;
        }
    }
}
