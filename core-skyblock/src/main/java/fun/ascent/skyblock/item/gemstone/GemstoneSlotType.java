package fun.ascent.skyblock.item.gemstone;

import net.minestom.server.item.Material;

public enum GemstoneSlotType {
    MINING("✦", "§5", Material.PURPLE_STAINED_GLASS_PANE,
            GemstoneType.JADE, GemstoneType.AMBER, GemstoneType.TOPAZ),
    OFFENSIVE("☠", "§9", Material.BLUE_STAINED_GLASS_PANE,
            GemstoneType.SAPPHIRE, GemstoneType.JASPER),
    COMBAT("⚔", "§4", Material.RED_STAINED_GLASS_PANE,
            GemstoneType.RUBY, GemstoneType.AMETHYST, GemstoneType.SAPPHIRE,
            GemstoneType.JASPER, GemstoneType.ONYX, GemstoneType.OPAL),
    DEFENSIVE("☤", "§a", Material.LIME_STAINED_GLASS_PANE,
            GemstoneType.AMETHYST, GemstoneType.RUBY, GemstoneType.OPAL),
    UNIVERSAL("❂", "§f", Material.WHITE_STAINED_GLASS_PANE,
            GemstoneType.values()),
    RUBY("❤", "§c", Material.RED_STAINED_GLASS_PANE, GemstoneType.RUBY),
    AMBER("⸕", "§6", Material.YELLOW_STAINED_GLASS_PANE, GemstoneType.AMBER),
    TOPAZ("✧", "§e", Material.YELLOW_STAINED_GLASS_PANE, GemstoneType.TOPAZ),
    JADE("☘", "§a", Material.LIME_STAINED_GLASS_PANE, GemstoneType.JADE),
    SAPPHIRE("✎", "§b", Material.LIGHT_BLUE_STAINED_GLASS_PANE, GemstoneType.SAPPHIRE),
    AMETHYST("❈", "§5", Material.PURPLE_STAINED_GLASS_PANE, GemstoneType.AMETHYST),
    JASPER("❁", "§d", Material.PINK_STAINED_GLASS_PANE, GemstoneType.JASPER),
    OPAL("❂", "§f", Material.WHITE_STAINED_GLASS_PANE, GemstoneType.OPAL),
    CITRINE("☘", "§4", Material.RED_STAINED_GLASS_PANE, GemstoneType.CITRINE),
    AQUAMARINE("α", "§3", Material.BLUE_STAINED_GLASS_PANE, GemstoneType.AQUAMARINE),
    PERIDOT("☘", "§2", Material.GREEN_STAINED_GLASS_PANE, GemstoneType.PERIDOT),
    ONYX("☣", "§9", Material.BLUE_STAINED_GLASS_PANE, GemstoneType.ONYX),
    CHISEL("", "§f", Material.WHITE_STAINED_GLASS_PANE,
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

    public String getSymbol() { return symbol; }
    public String getColorCode() { return colorCode; }
    public Material getGlassPaneMaterial() { return glassPaneMaterial; }
    public GemstoneType[] getAllowedTypes() { return allowedTypes; }

    public boolean accepts(GemstoneType type) {
        for (GemstoneType t : allowedTypes) {
            if (t == type) return true;
        }
        return false;
    }
}
