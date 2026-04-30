package fun.ascent.skyblock.item;

public enum ItemType {
    SWORD,
    BOW,
    LONGSWORD,
    FISHING_ROD,
    FISHING_WEAPON,
    WAND,
    AXE,
    PICKAXE,
    DRILL,
    SHOVEL,
    HOE,
    GAUNTLET,
    SHEARS,
    HELMET,
    CHESTPLATE,
    LEGGINGS,
    BOOTS,
    BELT,
    CLOAK,
    GLOVES,
    NECKLACE,
    BRACELET,
    DEPLOYABLE,
    ACCESSORY,
    HATCESSORY,
    DUNGEON_PASS,
    PET_ITEM,
    REFORGE_STONE,
    COSMETIC,
    TRAVEL_SCROLL,
    ARROW,
    ARROW_POISON,
    BAIT,
    NONE;

    public boolean isWeapon() {
        return switch (this) {
            case SWORD, BOW, LONGSWORD, FISHING_WEAPON, WAND, AXE, GAUNTLET -> true;
            default -> false;
        };
    }
    public boolean isArmor() {
        return switch (this) {
            case HELMET, CHESTPLATE, LEGGINGS, BOOTS -> true;
            default -> false;
        };
    }
    public boolean isTool() {
        return switch (this) {
            case PICKAXE, DRILL, SHOVEL, HOE, SHEARS, FISHING_ROD -> true;
            default -> false;
        };
    }
    public boolean isEquipment() {
        return switch (this) {
            case BELT, NECKLACE, BRACELET, GLOVES, CLOAK -> true;
            default -> false;
        };
    }
    public boolean isAccessory() {
        return switch (this) {
            case ACCESSORY, HATCESSORY -> true;
            default -> false;
        };
    }
}
