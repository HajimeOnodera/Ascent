package fun.ascent.skyblock.item;

public enum Rarity {
    COMMON("§f", "COMMON"),
    UNCOMMON("§a", "UNCOMMON"),
    RARE("§9", "RARE"),
    EPIC("§5", "EPIC"),
    LEGENDARY("§6", "LEGENDARY"),
    MYTHIC("§d", "MYTHIC"),
    DIVINE("§b", "DIVINE"),
    SPECIAL("§c", "SPECIAL"),
    VERY_SPECIAL("§c", "VERY SPECIAL"),
    ULTIMATE("§4", "ULTIMATE"),
    ADMIN("§4", "ADMIN");

    private final String color;
    private final String display;

    Rarity(String color, String display) {
        this.color = color;
        this.display = display;


    }

    public String getColor() {
        return color;
    }

    public String getDisplay() {
        return color + "§l" + display;
    }
}

