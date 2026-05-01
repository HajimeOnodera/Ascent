package fun.ascent.skyblock.utility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.HashMap;
import java.util.Map;

public class AlignUtils {

    private static final int CHAT_WIDTH = 320;
    private static final int SPACE_WIDTH = 4;
    private static final Map<Character, Integer> FONT_WIDTHS = new HashMap<>();

    static {
        // Default Minecraft font widths (roughly)
        " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~"
                .chars().forEach(c -> FONT_WIDTHS.put((char) c, 6));

        // Exceptions
        FONT_WIDTHS.put('i', 2);
        FONT_WIDTHS.put('l', 3);
        FONT_WIDTHS.put('t', 4);
        FONT_WIDTHS.put('f', 5);
        FONT_WIDTHS.put('k', 5);
        FONT_WIDTHS.put('I', 4);
        FONT_WIDTHS.put('[', 4);
        FONT_WIDTHS.put(']', 4);
        FONT_WIDTHS.put('{', 5);
        FONT_WIDTHS.put('}', 5);
        FONT_WIDTHS.put('(', 5);
        FONT_WIDTHS.put(')', 5);
        FONT_WIDTHS.put('|', 2);
        FONT_WIDTHS.put('\'', 2);
        FONT_WIDTHS.put('"', 4);
        FONT_WIDTHS.put(';', 2);
        FONT_WIDTHS.put(':', 2);
        FONT_WIDTHS.put(',', 2);
        FONT_WIDTHS.put('.', 2);
        FONT_WIDTHS.put(' ', 4);
    }

    public static Component alignCenter(Component component) {
        int componentWidth = getComponentWidth(component);
        if (componentWidth >= CHAT_WIDTH) return component;

        int paddingNeeded = (CHAT_WIDTH - componentWidth) / 2;
        return getPadding(paddingNeeded).append(component);
    }

    public static Component alignRight(Component component) {
        int componentWidth = getComponentWidth(component);
        if (componentWidth >= CHAT_WIDTH) return component;

        int paddingNeeded = CHAT_WIDTH - componentWidth;
        return getPadding(paddingNeeded).append(component);
    }

    public static Component alignLeft(Component component) {
        return component;
    }

    private static Component getPadding(int pixels) {
        int spaceCount = pixels / SPACE_WIDTH;
        return Component.text(" ".repeat(Math.max(0, spaceCount)));
    }

    private static int getComponentWidth(Component component) {
        int width = 0;
        String plainText = PlainTextComponentSerializer.plainText().serialize(component);
        boolean isBold = component.hasDecoration(TextDecoration.BOLD);

        for (char c : plainText.toCharArray()) {
            int charWidth = FONT_WIDTHS.getOrDefault(c, 6);
            if (isBold) charWidth += 1;
            width += charWidth;
        }

        for (Component child : component.children()) {
            width += getComponentWidth(child);
        }

        return width;
    }
}
