package fun.ascent.common.util;

import net.kyori.adventure.text.Component;

public final class CC {

    private CC() {}

    public static Component c(String legacy) {
        return ColorUtils.colorize(legacy);
    }

    public static String mm(String legacy) {
        return ColorUtils.toMiniMessage(legacy);
    }

    public static String strip(String legacy) {
        return ColorUtils.stripColor(legacy);
    }
}
