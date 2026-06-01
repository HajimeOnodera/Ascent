package fun.ascent.skyblock.entity.mob;

import com.mongodb.client.MongoCollection;
import fun.ascent.database.MongoProvider;
import fun.ascent.skyblock.entity.mob.impl.ZoneSpawner;
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
import java.util.concurrent.CopyOnWriteArrayList;

public class ZonePopulationTicker {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZonePopulationTicker.class);
    
    private static final List<MobSpawnSpot> cryptSpawnSpots = new CopyOnWriteArrayList<>();
    private static boolean spotsLoaded = false;

    public static void start() {
        LOGGER.info("Starting Zone Population Ticker...");
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            InstanceContainer world = WorldHandler.getLobby();
            if (world == null) {
                return TaskSchedule.seconds(10);
            }
            if (world.getPlayers().isEmpty()) {
                return TaskSchedule.seconds(10);
            }

            for (EntityRegistry entry : EntityRegistry.getZoneSpawners()) {
                ZoneSpawner spawner = (ZoneSpawner) entry.getPrototype();

                for (ZoneSpawner.SpawnZone zone : spawner.spawnZones()) {
                    if (zone.useSpots()) {
                        if (!spotsLoaded) {
                            loadCryptSpawnSpots();
                        }

                        long now = System.currentTimeMillis();
                        for (MobSpawnSpot spot : cryptSpawnSpots) {
                            List<SkyblockMobEntity> active = spot.getActiveEntities();
                            int max = zone.maxPerSpot();

                            if (active.size() < max) {
                                if (spot.getNextSpawnTime() == 0) {
                                    long delayMs = zone.spawnDelaySeconds() * 1000L;
                                    spot.setNextSpawnTime(now + delayMs);
                                    LOGGER.info("Spot at Pos({}, {}, {}) for {} is underpopulated ({}/{}). Queued spawn in {}s.",
                                            spot.getPosition().blockX(), spot.getPosition().blockY(), spot.getPosition().blockZ(),
                                            entry.getPrototype().displayName(), active.size(), max, zone.spawnDelaySeconds());
                                } else if (now >= spot.getNextSpawnTime()) {
                                    // Spawn the mob!
                                    SkyblockMobEntity mob = entry.spawn();
                                    mob.setZoneId(zone.zoneId());

                                    Pos pos = spot.getPosition();
                                    if (!world.isChunkLoaded(pos)) {
                                        world.loadChunk(pos).join();
                                    }

                                    mob.setInstance(world, pos);
                                    active.add(mob);
                                    
                                    // Reset nextSpawnTime
                                    spot.setNextSpawnTime(0);
                                    
                                    LOGGER.info("Spot Spawner: Spawned {} at Pos({}, {}, {})", 
                                            mob.displayName(), pos.blockX(), pos.blockY(), pos.blockZ());
                                }
                            } else {
                                if (spot.getNextSpawnTime() != 0) {
                                    spot.setNextSpawnTime(0);
                                }
                            }
                        }
                    } else {
                        // Fallback/Legacy global zone population logic
                        int current = countInZone(zone.zoneId(), entry);
                        int needed = zone.targetCount() - current;

                        if (needed > 0) {
                            LOGGER.info("Zone '{}': Current count of {} is {}/{} (Needed: {}). Spawning...",
                                    zone.zoneId(), entry.getPrototype().displayName(), current, zone.targetCount(), needed);

                            for (int i = 0; i < needed; i++) {
                                Pos pos = pickSpawnPosition(world, zone.zoneId());
                                if (pos == null) {
                                    LOGGER.warn("Could not find a valid spawn position in zone '{}'!", zone.zoneId());
                                    continue;
                                }

                                SkyblockMobEntity mob = entry.spawn();
                                mob.setZoneId(zone.zoneId());

                                if (!world.isChunkLoaded(pos)) {
                                    world.loadChunk(pos).join();
                                }

                                mob.setInstance(world, pos);
                                LOGGER.info("Spawned {} in zone '{}' at Pos({}, {}, {})", 
                                        mob.displayName(), zone.zoneId(), pos.blockX(), pos.blockY(), pos.blockZ());
                            }
                        }
                    }
                }
            }

            // Run every 1 second to handle accurate spawn delays
            return TaskSchedule.seconds(1);
        });
    }

    private static synchronized void loadCryptSpawnSpots() {
        if (spotsLoaded) return;
        cryptSpawnSpots.clear();
        try {
            MongoCollection<Document> collection = MongoProvider.getCollection("crypt_spawn_spots");
            for (Document doc : collection.find()) {
                double x = doc.getDouble("x");
                double y = doc.getDouble("y");
                double z = doc.getDouble("z");
                cryptSpawnSpots.add(new MobSpawnSpot(new Pos(x, y, z)));
            }
            LOGGER.info("Loaded {} Crypt Ghoul spawn spots from MongoDB.", cryptSpawnSpots.size());
        } catch (Exception e) {
            LOGGER.error("Failed to load Crypt Ghoul spawn spots from MongoDB!", e);
        } finally {
            spotsLoaded = true;
        }
    }

    public static List<MobSpawnSpot> getCryptSpawnSpots() {
        if (!spotsLoaded) {
            loadCryptSpawnSpots();
        }
        return cryptSpawnSpots;
    }

    public static void addCryptSpawnSpot(Pos pos) {
        try {
            MongoCollection<Document> collection = MongoProvider.getCollection("crypt_spawn_spots");
            Document doc = new Document()
                    .append("x", pos.x())
                    .append("y", pos.y())
                    .append("z", pos.z());
            collection.insertOne(doc);

            cryptSpawnSpots.add(new MobSpawnSpot(pos));
            LOGGER.info("Successfully added and persisted spawn spot at Pos({}, {}, {})", pos.blockX(), pos.blockY(), pos.blockZ());
        } catch (Exception e) {
            LOGGER.error("Failed to add spawn spot to MongoDB!", e);
        }
    }

    public static void clearCryptSpawnSpots() {
        try {
            MongoCollection<Document> collection = MongoProvider.getCollection("crypt_spawn_spots");
            collection.deleteMany(new Document());

            cryptSpawnSpots.clear();
            LOGGER.info("Successfully cleared all spawn spots from MongoDB and memory.");
        } catch (Exception e) {
            LOGGER.error("Failed to clear spawn spots from MongoDB!", e);
        }
    }

    private static int countInZone(String zoneId, EntityRegistry entry) {
        int count = 0;
        for (SkyblockMobEntity mob : SkyblockMobEntity.getActiveMobs()) {
            if (EntityRegistry.getByMob(mob) == entry && belongsToZone(mob, zoneId)) {
                count++;
            }
        }
        return count;
    }

    private static boolean belongsToZone(SkyblockMobEntity mob, String zoneId) {
        return zoneId.equalsIgnoreCase(mob.getZoneId());
    }

    private static Pos pickSpawnPosition(InstanceContainer world, String zoneId) {
        for (fun.ascent.skyblock.world.region.Region region : fun.ascent.skyblock.world.region.RegionManager.getAllRegions()) {
            if (region.getId().equalsIgnoreCase(zoneId)) {
                List<Integer> bounds = region.getBounds();
                int minX = bounds.get(0);
                int maxX = bounds.get(1);
                int minY = bounds.get(2);
                int maxY = bounds.get(3);
                int minZ = bounds.get(4);
                int maxZ = bounds.get(5);

                java.util.Random rand = new java.util.Random();
                for (int attempt = 0; attempt < 15; attempt++) {
                    int rx = minX + rand.nextInt(maxX - minX + 1);
                    int rz = minZ + rand.nextInt(maxZ - minZ + 1);

                    for (int ry = maxY; ry >= minY; ry--) {
                        var block = world.getBlock(rx, ry, rz);
                        var below = world.getBlock(rx, ry - 1, rz);

                        if (!below.isAir() && block.isAir()) {
                            return new Pos(rx + 0.5, ry, rz + 0.5);
                        }
                    }
                }
                return new Pos((minX + maxX) / 2.0, (minY + maxY) / 2.0, (minZ + maxZ) / 2.0);
            }
        }
        return null;
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
