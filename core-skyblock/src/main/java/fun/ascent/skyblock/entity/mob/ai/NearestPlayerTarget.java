package fun.ascent.skyblock.entity.mob.ai;

import fun.ascent.skyblock.player.SkyblockPlayer;
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

        return world.getNearbyEntities(entityCreature.getPosition(), range).stream()
                .filter(e -> e instanceof SkyblockPlayer)
                .filter(e -> !e.isRemoved())
                .min(Comparator.comparingDouble(e -> e.getDistanceSquared(entityCreature)))
                .orElse(null);
    }
}