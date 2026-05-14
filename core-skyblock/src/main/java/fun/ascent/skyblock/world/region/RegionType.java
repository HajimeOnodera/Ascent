package fun.ascent.skyblock.world.region;

import lombok.Getter;

@Getter
public enum RegionType {
    HUB("SkyBlock Hub", "§b"),
    VILLAGE("Village", "§a"),
    BANK("Bank", "§6"),
    AUCTION_HOUSE("Auction House", "§6"),
    BAZAAR("Bazaar Alley", "§e"),
    FARM("Farm", "§e"),
    BARN("The Barn", "§b"),
    MUSHROOM_DESERT("Mushroom Desert", "§e"),
    COAL_MINE("Coal Mine", "§8"),
    GOLD_MINE("Gold Mine", "§6"),
    DEEP_CAVERNS("Deep Caverns", "§b"),
    GRAVEYARD("Graveyard", "§c"),
    SPIDER_DEN("Spider's Den", "§4"),
    THE_END("The End", "§d"),
    FOREST("Forest", "§2"),
    BIRCH_PARK("Birch Park", "§a"),
    MOUNTAIN("Mountain", "§b"),
    PRIVATE_ISLAND("Private Island", "§a"),
    WILDERNESS("Wilderness", "§2");

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
