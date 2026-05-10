package fun.ascent.skyblock.item.items.consumables;

import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.items.ItemDefinition;

public class WrigglingLarva implements ItemDefinition {

    @Override
    public String getItemId() {
        return "WRIGGLING_LARVA";
    }

    @Override
    public SkyblockItem.Builder apply(SkyblockItem.Builder builder) {
        return builder
                .description("Consume this wriggling, disgusting",
                        "larva to permanently gain <dark_green>+2ൠ",
                        "<dark_green>Bonus Pest Chance<gray>.",
                        " ",
                        "You can consume up to <green>5 <gray>larvae",
                        "before the sight of them makes you",
                        "too sick to consume more.");
    }
}
