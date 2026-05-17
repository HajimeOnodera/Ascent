package fun.ascent.skyblock.item.items.tools;

import fun.ascent.skyblock.item.ItemType;
import fun.ascent.skyblock.item.Rarity;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.items.ItemDefinition;
import fun.ascent.skyblock.player.stats.Stats;
import net.minestom.server.item.Material;

public class VanillaFishingRod implements ItemDefinition {

    @Override
    public String getItemId() {
        return "FISHING_ROD";
    }

    @Override
    public SkyblockItem.Builder apply(SkyblockItem.Builder builder) {
        return builder
                .displayName("Fishing Rod")
                .itemType(ItemType.FISHING_ROD)
                .stat(Stats.FISHING_SPEED, 20.0)
                .stat(Stats.SEA_CREATURE_CHANCE, 5.0)
                .description(
                        "A simple wooden rod used for catching fish,",
                        "treasures, and sea creatures."
                );
    }
}
