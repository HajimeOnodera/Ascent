package fun.ascent.skyblock.item.items.modifiers;

import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.items.ItemDefinition;

public class HotPotatoBook implements ItemDefinition {

    @Override
    public String getItemId() {
        return "HOT_POTATO_BOOK";
    }

    @Override
    public SkyblockItem.Builder apply(SkyblockItem.Builder builder) {
        return builder
                .description("When applied to armor, grants <green>+2❈",
                        "<green>Defense <gray>and <red>+4❤ Health<gray>.",
                        " ",
                        "When applied to weapons, grants",
                        "<red>+2❁ Strength <gray>and <red>+2❁ Damage<gray>.",
                        " ",
                        "This can be applied to an item up to",
                        "<green>10 <gray>times!");
    }
}
