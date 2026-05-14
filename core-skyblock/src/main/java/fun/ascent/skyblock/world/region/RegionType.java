package fun.ascent.skyblock.world.region;

import lombok.Getter;

@Getter
public enum RegionType {
    HUB("SkyBlock Hub", "<aqua>"),
    VILLAGE("Village", "<green>"),
    BANK("Bank", "<gold>"),
    AUCTION_HOUSE("Auction House", "<gold>"),
    BAZAAR("Bazaar Alley", "<yellow>"),
    FARM("Farm", "<yellow>"),
    BARN("The Barn", "<aqua>"),
    MUSHROOM_DESERT("Mushroom Desert", "<yellow>"),
    COAL_MINE("Coal Mine", "<dark_gray>"),
    GOLD_MINE("Gold Mine", "<gold>"),
    DEEP_CAVERNS("Deep Caverns", "<aqua>"),
    GRAVEYARD("Graveyard", "<red>"),
    SPIDER_DEN("Spider's Den", "<dark_red>"),
    THE_END("The End", "<light_purple>"),
    FOREST("Forest", "<dark_green>"),
    BIRCH_PARK("Birch Park", "<green>"),
    MOUNTAIN("Mountain", "<aqua>"),
    PRIVATE_ISLAND("Private Island", "<green>"),
    WILDERNESS("Wilderness", "<dark_green>");

    private final String name;
    private final String color;

    RegionType(String name, String color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public String toString() {
        return color + name;
    }
}
