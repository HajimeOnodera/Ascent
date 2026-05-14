package fun.ascent.skyblock.minion.types;

import fun.ascent.skyblock.minion.base.TreeMinion;
import fun.ascent.skyblock.minion.model.MinionType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

import java.util.UUID;

public final class DarkOakMinion extends TreeMinion {
    public DarkOakMinion(UUID ownerUuid, int tier, Instance instance, Pos position) {
        super(ownerUuid, MinionType.DARK_OAK, tier, instance, position);
    }
}
