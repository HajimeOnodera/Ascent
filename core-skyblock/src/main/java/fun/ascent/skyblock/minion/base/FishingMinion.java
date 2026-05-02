package fun.ascent.skyblock.minion;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;

import java.util.List;
import java.util.UUID;

public abstract class FishingMinion extends SkyblockMinion {
    protected FishingMinion(UUID ownerUuid, MinionType type, int tier, Instance instance, Pos position) {
        super(ownerUuid, type, tier, instance, position);
    }

    @Override
    protected void performAction() {
        setCurrentTask(MinionTask.FISH);
        Pos waterPos = findWater();
        if (waterPos == null) {
            setWarning("This location is not perfect!", "/!\\");
            return;
        }
        List<ItemStack> drops = getProfile().createHarvestDrops();
        if (!canStoreDrops(drops)) {
            return;
        }
        MinionAnimation.animateFishing(this, waterPos, () -> storeDrops(drops));
    }
}
