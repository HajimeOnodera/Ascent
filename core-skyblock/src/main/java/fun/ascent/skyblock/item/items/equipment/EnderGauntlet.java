package fun.ascent.skyblock.item.items.equipment;

import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.items.ItemDefinition;

public class EnderGauntlet implements ItemDefinition {

    @Override
    public String getItemId() {
        return "ENDER_GAUNTLET";
    }

    @Override
    public SkyblockItem.Builder apply(SkyblockItem.Builder builder) {
        return builder
                .description("All stats on this piece of equipment",
                        "are multiplied by <dark_purple>2x<gray> while on the End",
                        "Island!");
    }
}
