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

public abstract class TreeMinion extends SkyblockMinion {
    protected TreeMinion(UUID ownerUuid, MinionType type, int tier, Instance instance, Pos position) {
        super(ownerUuid, type, tier, instance, position);
    }

    @Override
    protected void performAction() {
        Pos placePos = findTreePlacement();
        if (placePos != null) {
            setCurrentTask(MinionTask.FILL);
            MinionAnimation.animatePlace(this, placePos.add(0, 1, 0), () -> createTreeAt(placePos.blockX(), placePos.blockY(), placePos.blockZ()));
            return;
        }
        Pos breakPos = findTreeBreak();
        if (breakPos == null) {
            return;
        }
        setCurrentTask(MinionTask.HARVEST);
        List<ItemStack> drops = List.of(ItemStack.of(getProfile().outputMaterial(), 3));
        if (!canStoreDrops(drops)) {
            return;
        }
        Block block = getInstance().getBlock(breakPos);
        MinionAnimation.animateBreak(this, breakPos, block, () -> {
            removeTreeAt(breakPos.blockX(), breakPos.blockY() - 1, breakPos.blockZ());
            storeDrops(drops);
        });
    }
}
