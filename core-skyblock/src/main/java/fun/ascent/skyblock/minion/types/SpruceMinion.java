package fun.ascent.skyblock.minion.types;

import fun.ascent.skyblock.minion.base.TreeMinion;
import fun.ascent.skyblock.minion.model.MinionType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

import java.util.UUID;

public final class SpruceMinion extends TreeMinion {
    public SpruceMinion(UUID ownerUuid, int tier, Instance instance, Pos position) {
        super(ownerUuid, MinionType.SPRUCE, tier, instance, position);
    }
}
