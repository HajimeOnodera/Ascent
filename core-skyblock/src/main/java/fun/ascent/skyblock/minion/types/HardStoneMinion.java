package fun.ascent.skyblock.minion.types;

import fun.ascent.skyblock.minion.base.MiningMinion;
import fun.ascent.skyblock.minion.model.MinionType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

import java.util.UUID;

public final class HardStoneMinion extends MiningMinion {
    public HardStoneMinion(UUID ownerUuid, int tier, Instance instance, Pos position) {
        super(ownerUuid, MinionType.HARDSTONE, tier, instance, position);
    }
}
