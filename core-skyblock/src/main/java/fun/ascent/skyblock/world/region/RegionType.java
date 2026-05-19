package fun.ascent.skyblock.world.region;

import lombok.Getter;

@Getter
public enum RegionType {
    HUB("SkyBlock Hub", "<aqua>"),
    VILLAGE("Village", "<aqua>"),
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
    WILDERNESS("Wilderness", "<dark_green>"),
    COMBAT_SETTLEMENT("Combat Settlement", "<red>"),
    COMMUNITY_CENTER("Community Center", "<aqua>"),
    CRYPTS("Crypts", "<dark_gray>"),
    FISHING_OUTPOST("Fishing Outpost", "<blue>"),
    FLOWER_HOUSE("Flower House", "<light_purple>"),
    FORAGING_CAMP("Foraging Camp", "<dark_green>"),
    LIBRARY("Library", "<aqua>"),
    MINING_DISTRICT("Mining District", "<gold>"),
    MUSEUM("Museum", "<aqua>"),
    RUINS("Ruins", "<gray>"),
    SHENS_AUCTION("Shen's Auction", "<gold>"),
    THAUMATURGIST("Thaumaturgist", "<dark_purple>"),
    TRADE_CENTER("Trade Center", "<gold>"),
    ARCHERY_RANGE("Archery Range", "<red>"),
    ELECTION_ROOM("Election Room", "<aqua>"),
    GUNPOWDER_MINES("Gunpowder Mines", "<dark_gray>"),
    LAPIS_QUARRY("Lapis Quarry", "<blue>"),
    PIGMENS_DEN("Pigmen's Den", "<red>"),
    SLIMEHILL("Slimehill", "<green>"),
    DIAMOND_RESERVE("Diamond Reserve", "<aqua>"),
    OBSIDIAN_SANCTUARY("Obsidian Sanctuary", "<dark_purple>"),
    DARK_THICKET("Dark Thicket", "<dark_green>"),
    SPRUCE_WOODS("Spruce Woods", "<dark_green>"),
    SAVANNA_WOODLAND("Savanna Woodland", "<yellow>"),
    DWARVEN_MINES("Dwarven Mines", "<aqua>"),
    TRIALS_OF_FIRE("Trials of Fire", "<gold>"),
    DUNGEON_HUB("Dungeon Hub", "<red>");

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
