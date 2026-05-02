package fun.ascent.skyblock.minion.types;

import fun.ascent.skyblock.minion.base.CropMinion;
import fun.ascent.skyblock.minion.model.MinionType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

import java.util.UUID;

public final class PotatoMinion extends CropMinion {
    public PotatoMinion(UUID ownerUuid, int tier, Instance instance, Pos position) {
        super(ownerUuid, MinionType.POTATO, tier, instance, position);
    }
}
