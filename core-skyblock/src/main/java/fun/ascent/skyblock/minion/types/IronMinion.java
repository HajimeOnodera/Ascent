package fun.ascent.skyblock.minion.types;

import fun.ascent.skyblock.minion.base.MiningMinion;
import fun.ascent.skyblock.minion.model.MinionType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

import java.util.UUID;

public final class IronMinion extends MiningMinion {
    public IronMinion(UUID ownerUuid, int tier, Instance instance, Pos position) {
        super(ownerUuid, MinionType.IRON, tier, instance, position);
    }
}
