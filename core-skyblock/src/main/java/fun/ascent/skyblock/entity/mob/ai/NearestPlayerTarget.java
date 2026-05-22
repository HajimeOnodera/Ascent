package fun.ascent.skyblock.entity.mob.ai;

import fun.ascent.skyblock.entity.mob.SkyblockMobEntity;
import fun.ascent.skyblock.world.region.Region;
import fun.ascent.skyblock.world.region.RegionManager;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class NearestPlayerTarget extends TargetSelector {

    private final double range;

    public NearestPlayerTarget(@NotNull EntityCreature creature, double range) {
        super(creature);
        this.range = range;
    }

    @Override
    public Entity findTarget() {
        Instance world = entityCreature.getInstance();
        if (world == null) return null;

        String zoneId = null;
        if (entityCreature instanceof SkyblockMobEntity mob) {
            zoneId = mob.getZoneId();
        }

        final String activeZone = zoneId;
        final double rangeSq = range * range;

        // OPTIMIZATION: Query the players list directly. This avoids expensive spatial index spatial tree queries
        // across all other entities (like damage indicators, item drops, custom displays, etc.)
        return world.getPlayers().stream()
                .filter(p -> !p.isRemoved())
                .filter(p -> p.getDistanceSquared(entityCreature) <= rangeSq)
                .filter(p -> {
                    if (activeZone == null) return true;
                    Region playerRegion = RegionManager.getRegion(world, p.getPosition());
                    return playerRegion != null && playerRegion.getId().equalsIgnoreCase(activeZone);
                })
                .min(Comparator.comparingDouble(e -> e.getDistanceSquared(entityCreature)))
                .orElse(null);
    }
}
