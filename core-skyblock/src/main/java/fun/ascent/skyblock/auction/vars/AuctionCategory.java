package fun.ascent.skyblock.auction.vars;

public enum AuctionCategory {

    WEAPONS("<gold>Weapons", "GOLDEN_SWORD"),
    ARMOR("<aqua>Armor", "DIAMOND_CHESTPLATE"),
    ACCESSORIES("<dark_green>Accessories", "PLAYER_HEAD"),
    CONSUMABLES("<red>Consumables", "APPLE"),
    BLOCKS("<gray>Blocks", "STONE"),
    TOOLS_MISC("<light_purple>Tools & Misc", "FISHING_ROD");

    public final String displayName;
    public final String materialName;

    AuctionCategory(String displayName, String materialName) {
        this.displayName = displayName;
        this.materialName = materialName;
    }}
