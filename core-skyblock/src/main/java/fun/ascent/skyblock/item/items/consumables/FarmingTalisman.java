package fun.ascent.skyblock.item.items.consumables;

import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.items.ItemDefinition;

public class FarmingTalisman implements ItemDefinition {

    @Override
    public String getItemId() {
        return "FARMING_TALISMAN";
    }

    @Override
    public SkyblockItem.Builder apply(SkyblockItem.Builder builder) {
        return builder
                .description("Increases your <white>✦ Speed <gray>by",
                        "<green>+10 <gray>whilst held in the",
                        "<aqua>Farm<gray>, <aqua>The Barn<gray>,",
                        "<yellow>Mushroom Desert<gray>, and",
                        "<aqua>Garden<gray>.");
    }
}
