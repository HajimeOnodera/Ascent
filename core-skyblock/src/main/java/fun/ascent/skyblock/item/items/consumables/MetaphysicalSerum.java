package fun.ascent.skyblock.item.items.consumables;

import fun.ascent.skyblock.item.ItemAbility;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.items.ItemDefinition;

import java.util.List;

public class MetaphysicalSerum implements ItemDefinition {
    public String getItemId() {
        return "METAPHYSICAL_SERUM";
    }

    @Override
    public SkyblockItem.Builder apply(SkyblockItem.Builder builder) {
        return builder
                .description("Drinking this serum shortens",
                        "the chain required for &dSuperpairs",
                        "add-on experiments by <green>1<gray>.",
                        " ",
                        "You may drink up to <green>3<gray> doses",
                        "of the serum before the taste",
                        "becomes to abstract.");
    }
}

