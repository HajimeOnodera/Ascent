package fun.ascent.skyblock.minion;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

import java.util.UUID;

public final class CaveSpiderMinion extends MobMinion {
    public CaveSpiderMinion(UUID ownerUuid, int tier, Instance instance, Pos position) {
        super(ownerUuid, MinionType.CAVE_SPIDER, tier, instance, position);
    }
}
