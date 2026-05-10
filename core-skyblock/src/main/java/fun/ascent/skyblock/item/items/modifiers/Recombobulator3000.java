package fun.ascent.skyblock.item.items.modifiers;

import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.items.ItemDefinition;

public class Recombobulator3000 implements ItemDefinition {

    @Override
    public String getItemId() {
        return "RECOMBOBULATOR_3000";
    }

    @Override
    public SkyblockItem.Builder apply(SkyblockItem.Builder builder) {
        return builder
                .description("Permanently increases the rarity of",
                        "an item. Can only be applied to an",
                        "item once.");
    }
}
