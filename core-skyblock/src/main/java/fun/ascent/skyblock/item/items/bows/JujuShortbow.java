package fun.ascent.skyblock.item.items.bows;

import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.items.ItemDefinition;

public class JujuShortbow implements ItemDefinition {

    @Override
    public String getItemId() {
        return "JUJU_SHORTBOW";
    }

    @Override
    public SkyblockItem.Builder apply(SkyblockItem.Builder builder) {
        return builder
                .description("Hits <red>3<gray> mobs on impact.",
                        "Can damage endermen.");
    }
}
