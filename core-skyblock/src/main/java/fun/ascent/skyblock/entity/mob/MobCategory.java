package fun.ascent.skyblock.entity.mob;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MobCategory {
    UNDEAD("༕", "<dark_green>", "Undead"),
    SKELETAL("🦴", "<white>", "Skeletal"),
    ANIMAL("☮", "<green>", "Animal"),
    ARTHROPOD("Ж", "<dark_red>", "Arthropod"),
    AQUATIC("⚓", "<dark_blue>", "Aquatic"),
    CONSTRUCT("⚙", "<gray>", "Construct"),
    ENDER("⊙", "<dark_purple>", "Ender"),
    GLACIAL("❄", "<aqua>", "Glacial"),
    HUMANOID("✰", "<yellow>", "Humanoid"),
    SUBTERRANEAN("⛏", "<gold>", "Subterranean"),
    WITHER("☠", "<dark_gray>", "Wither"),
    ARCANE("♃", "<dark_purple>", "Arcane"),
    AERIAL("✈", "<gray>", "Aerial"),
    WOODLAND("⸙", "<dark_green>", "Woodland");

    private final String symbol;
    private final String color;
    private final String displayName;

    public String prefix() {
        return color + symbol;
    }
}
