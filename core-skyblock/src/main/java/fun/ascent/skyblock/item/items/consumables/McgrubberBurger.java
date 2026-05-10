package fun.ascent.skyblock.item.items.consumables;

import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.items.ItemDefinition;

public class McgrubberBurger implements ItemDefinition {

    @Override
    public String getItemId() {
        return "MCGRUBBER_BURGER";
    }

    @Override
    public SkyblockItem.Builder apply(SkyblockItem.Builder builder) {
        return builder
                .description("Consume this tasty burger to",
                        "permanently gain a <light_purple>Grubber",
                        "<light_purple>Stack<gray>.",
                        " ",
                        "Each <light_purple>stack <gray>grants:",
                        "<green> +12ф Rift Time ",
                        "<light_purple> +5% Motes <gray>from <light_purple>Motes Grubber",
                        "<light_purple> +40 Motes<gray> on <dark_purple>Orb <gray>pickup",
                        "<dark_gray>Max 5 stacks!");
    }
}
