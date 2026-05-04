package fun.ascent.common.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ColorUtils {

    private ColorUtils() {}

    private static final MiniMessage MINI = MiniMessage.miniMessage();

    // ── Colour code → NamedTextColor ────────────────────────────────────────

    private static final Map<Character, NamedTextColor> COLOR_MAP = Map.ofEntries(
            Map.entry('0', NamedTextColor.BLACK),
            Map.entry('1', NamedTextColor.DARK_BLUE),
            Map.entry('2', NamedTextColor.DARK_GREEN),
            Map.entry('3', NamedTextColor.DARK_AQUA),
            Map.entry('4', NamedTextColor.DARK_RED),
            Map.entry('5', NamedTextColor.DARK_PURPLE),
            Map.entry('6', NamedTextColor.GOLD),
            Map.entry('7', NamedTextColor.GRAY),
            Map.entry('8', NamedTextColor.DARK_GRAY),
            Map.entry('9', NamedTextColor.BLUE),
            Map.entry('a', NamedTextColor.GREEN),
            Map.entry('b', NamedTextColor.AQUA),
            Map.entry('c', NamedTextColor.RED),
            Map.entry('d', NamedTextColor.LIGHT_PURPLE),
            Map.entry('e', NamedTextColor.YELLOW),
            Map.entry('f', NamedTextColor.WHITE)
    );

    // ── Formatting code → MiniMessage tag ───────────────────────────────────

    private static final Map<Character, String> FORMAT_MAP = Map.of(
            'k', "obfuscated",
            'l', "bold",
            'm', "strikethrough",
            'n', "underlined",
            'o', "italic",
            'r', "reset"
    );

    // Matches &0-9a-fk-or / §0-9a-fk-or  (case-insensitive)
    private static final Pattern LEGACY_CODE = Pattern.compile("[&§]([0-9a-fk-orA-FK-OR])");
    // Matches &#RRGGBB
    private static final Pattern HEX_AMP    = Pattern.compile("&#([0-9a-fA-F]{6})");
    // Matches §x§R§R§G§G§B§B  (Bungee-style hex)
    private static final Pattern HEX_BUNGEE = Pattern.compile("§x(§[0-9a-fA-F]){6}");

    // ── Public API ──────────────────────────────────────────────────────────

    public static NamedTextColor getNamedTextColor(String legacyColor) {
        if (legacyColor == null || legacyColor.length() < 2) return NamedTextColor.WHITE;
        char code = Character.toLowerCase(legacyColor.charAt(1));
        return COLOR_MAP.getOrDefault(code, NamedTextColor.WHITE);
    }

    public static String toMiniMessage(String legacy) {
        if (legacy == null) return "";

        String result = legacy;

        // 1 ── &#RRGGBB → <#RRGGBB>
        result = HEX_AMP.matcher(result).replaceAll("<#$1>");

        // 2 ── §x§R§R§G§G§B§B → <#RRGGBB>
        Matcher bungee = HEX_BUNGEE.matcher(result);
        StringBuilder sb = new StringBuilder();
        while (bungee.find()) {
            String hex = bungee.group().replace("§x", "").replace("§", "");
            bungee.appendReplacement(sb, "<#" + hex + ">");
        }
        bungee.appendTail(sb);
        result = sb.toString();

        // 3 ── &/§ colour + format codes → MiniMessage tags
        Matcher m = LEGACY_CODE.matcher(result);
        sb = new StringBuilder();
        while (m.find()) {
            char code = Character.toLowerCase(m.group(1).charAt(0));

            String tag;
            if (FORMAT_MAP.containsKey(code)) {
                tag = "<" + FORMAT_MAP.get(code) + ">";
            } else {
                NamedTextColor color = COLOR_MAP.get(code);
                tag = color != null ? "<" + color + ">" : m.group();
            }
            m.appendReplacement(sb, Matcher.quoteReplacement(tag));
        }
        m.appendTail(sb);

        return sb.toString();
    }

    public static Component colorize(String legacy) {
        return MINI.deserialize("<!italic>" + toMiniMessage(legacy));
    }

    /**
     * Strips all legacy colour and formatting codes ({@code &} and {@code §}) from a string.
     */
    public static String stripColor(String legacy) {
        if (legacy == null) return "";
        return legacy
                .replaceAll("[&§][0-9a-fk-orA-FK-OR]", "")
                .replaceAll("&#[0-9a-fA-F]{6}", "")
                .replaceAll("§x(§[0-9a-fA-F]){6}", "");
    }
}
