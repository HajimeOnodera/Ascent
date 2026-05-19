package fun.ascent.skyblock.entity.mob;

import fun.ascent.skyblock.entity.mob.impl.ZoneSpawner;
import fun.ascent.skyblock.world.WorldHandler;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.timer.TaskSchedule;

import java.util.List;

public class ZonePopulationTicker {

    public static void start() {
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            InstanceContainer world = WorldHandler.getLobby();
            if (world == null || world.getPlayers().isEmpty()) {
                return TaskSchedule.seconds(10);
            }

            for (EntityRegistry entry : EntityRegistry.getZoneSpawners()) {
                ZoneSpawner spawner = (ZoneSpawner) entry.getPrototype();

                for (ZoneSpawner.SpawnZone zone : spawner.spawnZones()) {
                    int current = countInZone(zone.zoneId(), entry);
                    int needed = zone.targetCount() - current;

                    for (int i = 0; i < needed; i++) {
                        Pos pos = pickSpawnPosition(world, zone.zoneId());
                        if (pos == null) continue;

                        SkyblockMobEntity mob = entry.spawn();
                        mob.setZoneId(zone.zoneId());

                        if (!world.isChunkLoaded(pos)) {
                            world.loadChunk(pos).join();
                        }

                        mob.setInstance(world, pos);
                    }
                }
            }

            return TaskSchedule.seconds(5);
        });
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
                // Attempt to find a solid ground surface block inside bounds
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
                
                // Fallback midpoint coordinate
                return new Pos((minX + maxX) / 2.0, (minY + maxY) / 2.0, (minZ + maxZ) / 2.0);
            }
        }
        return null;
    }
}
