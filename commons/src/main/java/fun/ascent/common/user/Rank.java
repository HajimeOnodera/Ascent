package fun.ascent.common.user;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

@Getter
public enum Rank {
    OWNER("OWNER", "[OWNER] ", NamedTextColor.RED, NamedTextColor.RED),
    ADMIN("ADMIN", "[ADMIN] ", NamedTextColor.RED, NamedTextColor.RED),
    GAME_MASTER("GM", "[GM] ", NamedTextColor.DARK_GREEN, NamedTextColor.DARK_GREEN),
    YOUTUBE("YOUTUBE", "[YOUTUBE] ", NamedTextColor.WHITE, NamedTextColor.RED),
    MVP_PLUS_PLUS("MVP++", "[MVP++] ", NamedTextColor.GOLD, NamedTextColor.AQUA),
    MVP_PLUS("MVP+", "[MVP+] ", NamedTextColor.RED, NamedTextColor.AQUA),
    MVP("MVP", "[MVP] ", NamedTextColor.AQUA, NamedTextColor.AQUA),
    VIP_PLUS("VIP+", "[VIP+] ", NamedTextColor.GOLD, NamedTextColor.GREEN),
    VIP("VIP", "[VIP] ", NamedTextColor.GREEN, NamedTextColor.GREEN),
    DEFAULT("NONE", "", NamedTextColor.GRAY, NamedTextColor.GRAY);

    private final String name;
    private final String prefix;
    private final TextColor color;
    private final TextColor bracketColor;

    Rank(String name, String prefix, TextColor color, TextColor bracketColor) {
        this.name = name;
        this.prefix = prefix;
        this.color = color;
        this.bracketColor = bracketColor;
    }

    public Component getFormattedPrefix() {
        if (this == DEFAULT) return Component.empty();
        
        // Simplified for now, can be expanded for specific char coloring (like MVP+)
        return Component.text(prefix).color(color);
    }
}
