package fun.ascent.skyblock.world;

public enum IslandTypes {
    PRIVATE_ISLAND("Private Island"),
    GARDEN("Garden"),
    HUB("Hub"),
    DUNGEON_HUB("Dungeon Hub"),
    THE_BARN("The Barn"),
    MUSHROOM_DESERT("Mushroom Desert"),
    THE_PARK("The Park"),
    SPIDERS_DEN("Spider's Den"),
    CRIMSON_ISLE("Crimson Isle"),
    THE_END("The End"),
    GOLD_MINE("Gold Mine"),
    DEEP_CAVERNS("Deep Caverns"),
    DWARVEN_MINES("Dwarven Mines"),
    CRYSTAL_HOLLOWS("Crystal Hollows"),
    JERRYS_WORKSHOP("Jerry's Workshop"),
    RIFT("Rift"),
    BACKWATER_BAYOU("Backwater Bayou"),
    GALATEA("Galatea"),;

    private final String display;

    IslandTypes(String display) {
        this.display = display;
    }
    public String getLocationDisplay() {
        return display;
    }
}
