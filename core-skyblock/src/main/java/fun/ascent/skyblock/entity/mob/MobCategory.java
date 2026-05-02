package fun.ascent.skyblock.entity.mob;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MobCategory {
    UNDEAD("༕", "§2", "Undead"),
    SKELETAL("🦴", "§f", "Skeletal"),
    ANIMAL("☮", "§a", "Animal"),
    ARTHROPOD("Ж", "§4", "Arthropod"),
    AQUATIC("⚓", "§1", "Aquatic"),
    CONSTRUCT("⚙", "§7", "Construct"),
    ENDER("⊙", "§5", "Ender"),
    GLACIAL("❄", "§b", "Glacial"),
    HUMANOID("✰", "§e", "Humanoid"),
    SUBTERRANEAN("⛏", "§6", "Subterranean"),
    WITHER("☠", "§8", "Wither"),
    ARCANE("♃", "§5", "Arcane"),
    AERIAL("✈", "§7", "Aerial"),
    WOODLAND("⸙", "§2", "Woodland");

    private final String symbol;
    private final String color;
    private final String displayName;

    public String prefix() {
        return color + symbol;
    }
}