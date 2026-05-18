package fun.ascent.skyblock.item.items.bait;

import fun.ascent.skyblock.item.ItemType;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.items.ItemDefinition;

public class MinnowBait implements ItemDefinition {

    @Override
    public String getItemId() {
        return "MINNOW_BAIT";
    }

    @Override
    public SkyblockItem.Builder apply(SkyblockItem.Builder builder) {
        return builder
                .displayName("Minnow Bait")
                .itemType(ItemType.BAIT)
                .description(
                        "Increases fishing speed by 15%.",
                        "Consumed on cast."
                );
    }
}
