package fun.ascent.skyblock.minion.base;

import fun.ascent.skyblock.minion.model.MinionTask;
import fun.ascent.skyblock.minion.model.MinionType;
import fun.ascent.skyblock.minion.visual.MinionAnimation;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;

import java.util.List;
import java.util.UUID;

public abstract class MiningMinion extends SkyblockMinion {
    protected MiningMinion(UUID ownerUuid, MinionType type, int tier, Instance instance, Pos position) {
        super(ownerUuid, type, tier, instance, position);
    }

    @Override
    protected void performAction() {
        Pos placePos = findAir(getMiningPositions());
        if (placePos != null) {
            setCurrentTask(MinionTask.FILL);
            MinionAnimation.animatePlace(this, placePos,
                    () -> getInstance().setBlock(placePos, getProfile().generatedBlock()));
            return;
        }
        Pos breakPos = findBreakBlock(getMiningPositions(), getProfile().generatedBlock());
        if (breakPos == null) {
            return;
        }
        setCurrentTask(MinionTask.HARVEST);
        List<ItemStack> drops = getProfile().createHarvestDrops();
        if (!canStoreDrops(drops)) {
            return;
        }
        Block block = getInstance().getBlock(breakPos);
        MinionAnimation.animateBreak(this, breakPos, block, () -> {
            getInstance().setBlock(breakPos, Block.AIR);
            storeDrops(drops);
        });
    }
}
