package fun.ascent.skyblock.minion.types;

import fun.ascent.skyblock.minion.base.TreeMinion;
import fun.ascent.skyblock.minion.model.MinionType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

import java.util.UUID;

public final class OakMinion extends TreeMinion {
    public OakMinion(UUID ownerUuid, int tier, Instance instance, Pos position) {
        super(ownerUuid, MinionType.OAK, tier, instance, position);
    }
}
