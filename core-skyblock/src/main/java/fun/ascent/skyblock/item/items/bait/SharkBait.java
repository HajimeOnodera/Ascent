package fun.ascent.skyblock.item.items.bait;

import fun.ascent.skyblock.item.ItemType;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.items.ItemDefinition;
import net.minestom.server.item.Material;

public class SharkBait implements ItemDefinition {

    @Override
    public String getItemId() {
        return "SHARK_BAIT";
    }

    @Override
    public SkyblockItem.Builder apply(SkyblockItem.Builder builder) {
        return builder
                .displayName("Shark Bait")
                .itemType(ItemType.BAIT)
                .description(
                        "Increases sea creature chance by 10%.",
                        "Consumed on cast."
                );
    }
}
