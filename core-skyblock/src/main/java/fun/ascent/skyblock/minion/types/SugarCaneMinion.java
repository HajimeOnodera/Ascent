package fun.ascent.skyblock.minion.types;

import fun.ascent.skyblock.minion.base.CropMinion;
import fun.ascent.skyblock.minion.model.MinionType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

import java.util.UUID;

public final class SugarCaneMinion extends CropMinion {
    public SugarCaneMinion(UUID ownerUuid, int tier, Instance instance, Pos position) {
        super(ownerUuid, MinionType.SUGARCANE, tier, instance, position);
    }
}
