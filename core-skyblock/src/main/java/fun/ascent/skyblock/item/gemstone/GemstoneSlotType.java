package fun.ascent.skyblock.item.gemstone;

import lombok.Getter;
import net.minestom.server.item.Material;

@Getter
public enum GemstoneSlotType {
    MINING("✦", "<dark_purple>", Material.PURPLE_STAINED_GLASS_PANE,
            GemstoneType.JADE, GemstoneType.AMBER, GemstoneType.TOPAZ),
    OFFENSIVE("☠", "<blue>", Material.BLUE_STAINED_GLASS_PANE,
            GemstoneType.SAPPHIRE, GemstoneType.JASPER),
    COMBAT("⚔", "<dark_red>", Material.RED_STAINED_GLASS_PANE,
            GemstoneType.RUBY, GemstoneType.AMETHYST, GemstoneType.SAPPHIRE,
            GemstoneType.JASPER, GemstoneType.ONYX, GemstoneType.OPAL),
    DEFENSIVE("☤", "<green>", Material.LIME_STAINED_GLASS_PANE,
            GemstoneType.AMETHYST, GemstoneType.RUBY, GemstoneType.OPAL),
    UNIVERSAL("❂", "<white>", Material.WHITE_STAINED_GLASS_PANE,
            GemstoneType.values()),
    RUBY("❤", "<red>", Material.RED_STAINED_GLASS_PANE, GemstoneType.RUBY),
    AMBER("⸕", "<gold>", Material.YELLOW_STAINED_GLASS_PANE, GemstoneType.AMBER),
    TOPAZ("✧", "<yellow>", Material.YELLOW_STAINED_GLASS_PANE, GemstoneType.TOPAZ),
    JADE("☘", "<green>", Material.LIME_STAINED_GLASS_PANE, GemstoneType.JADE),
    SAPPHIRE("✎", "<aqua>", Material.LIGHT_BLUE_STAINED_GLASS_PANE, GemstoneType.SAPPHIRE),
    AMETHYST("❈", "<dark_purple>", Material.PURPLE_STAINED_GLASS_PANE, GemstoneType.AMETHYST),
    JASPER("❁", "<light_purple>", Material.PINK_STAINED_GLASS_PANE, GemstoneType.JASPER),
    OPAL("❂", "<white>", Material.WHITE_STAINED_GLASS_PANE, GemstoneType.OPAL),
    CITRINE("☘", "<dark_red>", Material.RED_STAINED_GLASS_PANE, GemstoneType.CITRINE),
    AQUAMARINE("α", "<dark_aqua>", Material.BLUE_STAINED_GLASS_PANE, GemstoneType.AQUAMARINE),
    PERIDOT("☘", "<dark_green>", Material.GREEN_STAINED_GLASS_PANE, GemstoneType.PERIDOT),
    ONYX("☣", "<blue>", Material.BLUE_STAINED_GLASS_PANE, GemstoneType.ONYX),
    CHISEL("", "<white>", Material.WHITE_STAINED_GLASS_PANE,
            GemstoneType.CITRINE, GemstoneType.AQUAMARINE, GemstoneType.PERIDOT, GemstoneType.ONYX);

    private final String symbol;
    private final String colorCode;
    private final Material glassPaneMaterial;
    private final GemstoneType[] allowedTypes;

    GemstoneSlotType(String symbol, String colorCode, Material glassPaneMaterial, GemstoneType... allowedTypes) {
        this.symbol = symbol;
        this.colorCode = colorCode;
        this.glassPaneMaterial = glassPaneMaterial;
        this.allowedTypes = allowedTypes;
    }

    public boolean accepts(GemstoneType type) {
        for (GemstoneType t : allowedTypes) {
            if (t == type) return true;
        }
        return false;
    }
}

