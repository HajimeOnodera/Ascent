package fun.ascent.skyblock.utility;

import net.kyori.adventure.text.format.NamedTextColor;

public class ColorUtils {

    public static NamedTextColor getNamedTextColor(String legacyColor) {
        return switch (legacyColor.toLowerCase()) {
            case "§0" -> NamedTextColor.BLACK;
            case "§1" -> NamedTextColor.DARK_BLUE;
            case "§2" -> NamedTextColor.DARK_GREEN;
            case "§3" -> NamedTextColor.DARK_AQUA;
            case "§4" -> NamedTextColor.DARK_RED;
            case "§5" -> NamedTextColor.DARK_PURPLE;
            case "§6" -> NamedTextColor.GOLD;
            case "§7" -> NamedTextColor.GRAY;
            case "§8" -> NamedTextColor.DARK_GRAY;
            case "§9" -> NamedTextColor.BLUE;
            case "§a" -> NamedTextColor.GREEN;
            case "§b" -> NamedTextColor.AQUA;
            case "§c" -> NamedTextColor.RED;
            case "§d" -> NamedTextColor.LIGHT_PURPLE;
            case "§e" -> NamedTextColor.YELLOW;
            case "§f" -> NamedTextColor.WHITE;
            default -> NamedTextColor.WHITE;
        };
    }

}
