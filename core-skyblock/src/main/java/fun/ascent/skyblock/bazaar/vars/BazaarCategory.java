    package fun.ascent.skyblock.bazaar.vars;

import net.minestom.server.item.Material;

public enum BazaarCategory {

    FARMING("<gold>Bazaar ➔ Farming", "FARMING", Material.GOLDEN_HOE, Material.YELLOW_STAINED_GLASS_PANE),
    MINING("<gold>Bazaar ➔ Mining", "MINING", Material.DIAMOND_PICKAXE, Material.LIGHT_BLUE_STAINED_GLASS_PANE),
    COMBAT("<gold>Bazaar ➔ Combat", "COMBAT", Material.IRON_SWORD, Material.RED_STAINED_GLASS_PANE),
    FISHING_WOOD("<gold>Bazaar ➔ Woods & Fishes", "WOODS_FISHES", Material.FISHING_ROD, Material.ORANGE_STAINED_GLASS_PANE),
    ODDITIES("<gold>Bazaar ➔ Oddities", "ODDITIES", Material.ENCHANTING_TABLE, Material.MAGENTA_STAINED_GLASS_PANE);

    public final String titleMiniMessage;
    public final String name;
    public final Material icon;
    public final Material pane;

    BazaarCategory(String titleMiniMessage, String name, Material icon, Material pane) {
        this.titleMiniMessage = titleMiniMessage;
        this.name = name;
        this.icon = icon;
        this.pane = pane;
    }

    public String getTitle() {
        return titleMiniMessage;
    }
}
