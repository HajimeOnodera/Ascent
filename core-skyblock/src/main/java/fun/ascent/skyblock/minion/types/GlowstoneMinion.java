package fun.ascent.skyblock.minion;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

import java.util.UUID;

public final class GlowstoneMinion extends MiningMinion {
    public GlowstoneMinion(UUID ownerUuid, int tier, Instance instance, Pos position) {
        super(ownerUuid, MinionType.GLOWSTONE, tier, instance, position);
    }
}
