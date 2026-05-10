package fun.ascent.skyblock.item.items.consumables;

import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.items.ItemDefinition;

public class RawSoulflow implements ItemDefinition {

    @Override
    public String getItemId() {
        return "RAW_SOULFLOW";
    }

    @Override
    public SkyblockItem.Builder apply(SkyblockItem.Builder builder) {
        return builder
                .description("Hold an right-click to consume,",
                        "gaining <dark_aqua>+1⸎ Soulflow<gray>!");
    }
}
