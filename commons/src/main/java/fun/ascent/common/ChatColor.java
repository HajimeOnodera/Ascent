package fun.ascent.common;

import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;

@Getter
public enum ChatColor {
    BLACK('0', 0, NamedTextColor.BLACK),
    DARK_BLUE('1', 1, NamedTextColor.DARK_BLUE),
    DARK_GREEN('2', 2, NamedTextColor.DARK_GREEN),
    DARK_AQUA('3', 3, NamedTextColor.DARK_AQUA),
    DARK_RED('4', 4, NamedTextColor.DARK_RED),
    DARK_PURPLE('5', 5, NamedTextColor.DARK_PURPLE),
    GOLD('6', 6, NamedTextColor.GOLD),
    GRAY('7', 7, NamedTextColor.GRAY),
    DARK_GRAY('8', 8, NamedTextColor.DARK_GRAY),
    BLUE('9', 9, NamedTextColor.BLUE),
    GREEN('a', 10, NamedTextColor.GREEN),
    AQUA('b', 11, NamedTextColor.AQUA),
    RED('c', 12, NamedTextColor.RED),
    LIGHT_PURPLE('d', 13, NamedTextColor.LIGHT_PURPLE),
    YELLOW('e', 14, NamedTextColor.YELLOW),
    WHITE('f', 15, NamedTextColor.WHITE),
    MAGIC('k', 16, true),
    BOLD('l', 17, true),
    STRIKETHROUGH('m', 18, true),
    UNDERLINE('n', 19, true),
    ITALIC('o', 20, true),
    RESET('r', 21);

    public static final char COLOR_CHAR = '§';
    private final int intCode;
    private final char code;
    private final boolean isFormat;
    private final NamedTextColor namedTextColor;
    private final String toString;

    ChatColor(char code, int intCode) {
        this(code, intCode, false);
    }

    ChatColor(char code, int intCode, NamedTextColor namedTextColor) {
        this(code, intCode, false, namedTextColor);
    }

    ChatColor(char code, int intCode, boolean isFormat) {
        this(code, intCode, isFormat, null);
    }

    ChatColor(char code, int intCode, boolean isFormat, NamedTextColor namedTextColor) {
        this.code = code;
        this.intCode = intCode;
        this.isFormat = isFormat;
        this.namedTextColor = namedTextColor;
        this.toString = new String(new char[]{'§', code});
    }

    public static ChatColor getByCode(char code) {
        for (ChatColor color : values()) {
            if (Character.toLowerCase(color.code) == Character.toLowerCase(code)) {
                return color;
            }
        }
        return null;
    }

    public static ChatColor getLastColor(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        for (int i = text.length() - 2; i >= 0; i--) {

            char currentChar = text.charAt(i);
            if (currentChar == ChatColor.COLOR_CHAR) {

                char colorCode = text.charAt(i + 1);
                for (ChatColor chatColor : ChatColor.values()) {

                    if (chatColor.getCode() == colorCode) {
                        return chatColor;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.toString;
    }
}