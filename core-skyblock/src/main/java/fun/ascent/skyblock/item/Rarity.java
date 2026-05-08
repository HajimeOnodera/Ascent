package fun.ascent.skyblock.item;

import lombok.Getter;

public enum Rarity {
    COMMON("<white>", "COMMON"),
    UNCOMMON("<green>", "UNCOMMON"),
    RARE("<blue>", "RARE"),
    EPIC("<dark_purple>", "EPIC"),
    LEGENDARY("<gold>", "LEGENDARY"),
    MYTHIC("<light_purple>", "MYTHIC"),
    DIVINE("<aqua>", "DIVINE"),
    SPECIAL("<red>", "SPECIAL"),
    VERY_SPECIAL("<red>", "VERY SPECIAL"),
    ULTIMATE("<dark_red>", "ULTIMATE"),
    ADMIN("<dark_red>", "ADMIN");

    @Getter
    private final String color;
    private final String display;

    Rarity(String color, String display) {
        this.color = color;
        this.display = display;


    }

    public String getRarityColor() {
        return color;
    }

    public String getDisplay() {
        return color + "<bold>" + display;
    }

    public Rarity getNextRarity() {
        return switch (this) {
            case COMMON -> UNCOMMON;
            case UNCOMMON -> RARE;
            case RARE -> EPIC;
            case EPIC -> LEGENDARY;
            case LEGENDARY -> MYTHIC;
            case MYTHIC -> DIVINE;
            case SPECIAL -> VERY_SPECIAL;
            default -> null;
        };
    }

}


