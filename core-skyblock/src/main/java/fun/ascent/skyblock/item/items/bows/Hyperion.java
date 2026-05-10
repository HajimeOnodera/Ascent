package fun.ascent.skyblock.item.items.bows;

import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.items.ItemDefinition;

public class Hyperion implements ItemDefinition {

    @Override
    public String getItemId() {
        return "HYPERION";
    }

    @Override
    public SkyblockItem.Builder apply(SkyblockItem.Builder builder) {
        return builder
                .description("Deals +<red>50% <gray>damage to",
                        "Withers. Grants <red>+1❁ Damage",
                        "and <green>+2<aqua>✎ Intelligence",
                        "per <red>Catacombs <gray>level.");
    }
}
