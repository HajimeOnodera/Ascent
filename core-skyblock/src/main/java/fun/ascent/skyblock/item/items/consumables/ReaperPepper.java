package fun.ascent.skyblock.item.items.consumables;

import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.items.ItemDefinition;

public class ReaperPepper implements ItemDefinition {

    @Override
    public String getItemId() {
        return "REAPER_PEPPER";
    }

    @Override
    public SkyblockItem.Builder apply(SkyblockItem.Builder builder) {
        return builder
                .description("Consume this <italic>dangerous <reset><gray>pepper to",
                        "permanently gain <red>+1❤ Health <gray>and",
                        "<aqua>+1❄ Cold Resistance<gray>.",
                        " ",
                        "You may eat up to <green>5 <gray>peppers before",
                        "it's considered truly dangerous.");
    }
}
