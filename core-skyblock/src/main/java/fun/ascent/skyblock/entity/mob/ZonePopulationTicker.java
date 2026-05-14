package fun.ascent.skyblock.entity.mob;

import fun.ascent.skyblock.entity.mob.impl.ZoneSpawner;
import fun.ascent.skyblock.world.WorldHandler;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.timer.TaskSchedule;

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
        // Stub — wire into your zone/region system here
        return true;
    }

    private static Pos pickSpawnPosition(InstanceContainer world, String zoneId) {
        // Stub — wire into your zone/region system to pick a valid ground position
        return null;
    }
}
