package fun.ascent.skyblock.item.items.cloak;

import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.items.ItemDefinition;

public class ShadowAssassinCloak implements ItemDefinition {

    @Override
    public String getItemId() {
        return "SHADOW_ASSASSIN_CLOAK";
    }

    @Override
    public SkyblockItem.Builder apply(SkyblockItem.Builder builder) {
        return builder
                .description("On teleport: Your next melee hit",
                        "within <green>5s<gray> deals <red>10% <gray>more damage.");
    }
}
