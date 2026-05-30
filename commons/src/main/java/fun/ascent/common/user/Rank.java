package fun.ascent.common.user;

import fun.ascent.common.StringUtility;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@Getter
public enum Rank {
    OWNER("<red>[OWNER] ", true, NamedTextColor.RED),
    ADMIN("<red>[ADMIN] ", true, NamedTextColor.RED),
    STAFF("<red>[<gold>𝒜<red>] ", true, NamedTextColor.RED),
    YOUTUBE("<red>[<white>YOUTUBE<red>] ", false, NamedTextColor.RED),
    MVP_PLUS_PLUS("<gold>[MVP<black>++<gold>] ", false, NamedTextColor.GOLD),
    MVP_PLUS("<aqua>[MVP<red>+<aqua>] ", false, NamedTextColor.AQUA),
    MVP("<aqua>[MVP] ", false, NamedTextColor.AQUA),
    VIP_PLUS("<green>[VIP<gold>+<green>] ", false, NamedTextColor.GREEN),
    VIP("<green>[VIP] ", false, NamedTextColor.GREEN),
    DEFAULT("<gray>", false, NamedTextColor.GRAY);

    private final String prefix;
    private final boolean isStaff;
    private final NamedTextColor textColor;

    Rank(String prefix, boolean isStaff, NamedTextColor textColor) {
        this.prefix = prefix;
        this.isStaff = isStaff;
        this.textColor = textColor;
    }

    public boolean isEqualOrHigherThan(Rank rank) {
        return this.ordinal() <= rank.ordinal();
    }

    public Component getFormattedPrefix() {
        return StringUtility.text(prefix);
    }
}
