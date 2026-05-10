package fun.ascent.skyblock.item.items.helmet;

import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.items.ItemDefinition;

public class DiamondNecronHead implements ItemDefinition {

    @Override
    public String getItemId() {
        return "DIAMOND_NECRON_HEAD";
    }

    @Override
    public SkyblockItem.Builder apply(SkyblockItem.Builder builder) {
        return builder
                .description("Obtained from killing <red>Maxor, Storm,",
                        "<red>Goldor, and Necron <gray>in <red>The Catacombs",
                        "lots of times. Grants <green>2x <gray>stats on <red>The",
                        "<red>Catacombs Floor VII<gray>.",
                        " ",
                        "<dark_gray>Disciples of the Wither King. Inherited",
                        "<dark_gray>the Catacombs eons ago. Never",
                        "<dark_gray>defeated, feared by anything living",
                        "<dark_gray>AND dead.");
    }
}
