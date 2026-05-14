package fun.ascent.skyblock.entity.mob.ai;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.ai.TargetSelector;
import org.jetbrains.annotations.NotNull;

public class LastAttackerTarget extends TargetSelector {

    private final double range;

    public LastAttackerTarget(@NotNull EntityCreature creature, double range) {
        super(creature);
        this.range = range;
    }

    @Override
    public Entity findTarget() {
        var source = entityCreature.getLastDamageSource();
        if (source == null) return null;

        Entity attacker = source.getAttacker();
        if (attacker == null || attacker.isRemoved()) return null;

        if (entityCreature.getDistanceSquared(attacker) > range * range) return null;

        return attacker;
    }
}
