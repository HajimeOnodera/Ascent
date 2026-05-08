package fun.ascent.common;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtility {
    public static final char[] ALPHABET = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'W', 'X', 'Y', 'Z'
    };
    private static final DecimalFormat INTEGER_FORMAT = new DecimalFormat("#,###");
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final Map<Character, String> LEGACY_COLOR_TAGS = Map.ofEntries(
            Map.entry('0', "black"),
            Map.entry('1', "dark_blue"),
            Map.entry('2', "dark_green"),
            Map.entry('3', "dark_aqua"),
            Map.entry('4', "dark_red"),
            Map.entry('5', "dark_purple"),
            Map.entry('6', "gold"),
            Map.entry('7', "gray"),
            Map.entry('8', "dark_gray"),
            Map.entry('9', "blue"),
            Map.entry('a', "green"),
            Map.entry('b', "aqua"),
            Map.entry('c', "red"),
            Map.entry('d', "light_purple"),
            Map.entry('e', "yellow"),
            Map.entry('f', "white"),
            Map.entry('k', "obfuscated"),
            Map.entry('l', "bold"),
            Map.entry('m', "strikethrough"),
            Map.entry('n', "underlined"),
            Map.entry('o', "italic"),
            Map.entry('r', "reset")
    );
    private static final Map<String, NamedTextColor> TEXT_COLORS = Map.ofEntries(
            Map.entry("black", NamedTextColor.BLACK),
            Map.entry("dark_blue", NamedTextColor.DARK_BLUE),
            Map.entry("dark_green", NamedTextColor.DARK_GREEN),
            Map.entry("dark_aqua", NamedTextColor.DARK_AQUA),
            Map.entry("dark_red", NamedTextColor.DARK_RED),
            Map.entry("dark_purple", NamedTextColor.DARK_PURPLE),
            Map.entry("gold", NamedTextColor.GOLD),
            Map.entry("gray", NamedTextColor.GRAY),
            Map.entry("dark_gray", NamedTextColor.DARK_GRAY),
            Map.entry("blue", NamedTextColor.BLUE),
            Map.entry("green", NamedTextColor.GREEN),
            Map.entry("aqua", NamedTextColor.AQUA),
            Map.entry("red", NamedTextColor.RED),
            Map.entry("light_purple", NamedTextColor.LIGHT_PURPLE),
            Map.entry("yellow", NamedTextColor.YELLOW),
            Map.entry("white", NamedTextColor.WHITE)
    );

    public static String formatTimeAsAgo(long millis) {
        long timeDifference = System.currentTimeMillis() - millis;
        // Simplified the calculation logic by abstracting repetitive calculations
        long[] timeUnits = {TimeUnit.DAYS.toMillis(1), TimeUnit.HOURS.toMillis(1), TimeUnit.MINUTES.toMillis(1)};
        String[] timeLabels = {"d ago", "h ago", "m ago"};
        for (int i = 0; i < timeUnits.length; i++) {
            if (timeDifference >= timeUnits[i]) {
                return (timeDifference / timeUnits[i]) + timeLabels[i];
            }
        }
        return "Just now";
    }

    public static String commaifyAndTh(double d) {
        // "th" suffix for numbers ending in 11, 12, 13
        return switch ((int) d % 100) {
            case 11, 12, 13 -> INTEGER_FORMAT.format(d) + "th";
            default -> switch ((int) d % 10) {
                case 1 -> INTEGER_FORMAT.format(d) + "st";
                case 2 -> INTEGER_FORMAT.format(d) + "nd";
                case 3 -> INTEGER_FORMAT.format(d) + "rd";
                default -> INTEGER_FORMAT.format(d) + "th";
            };
        };
    }

    public static String formatTimeWentBy(long millis) {
        long timeDifference = System.currentTimeMillis() - millis;
        // Simplified the calculation logic by abstracting repetitive calculations
        long[] timeUnits = {TimeUnit.DAYS.toMillis(1), TimeUnit.HOURS.toMillis(1), TimeUnit.MINUTES.toMillis(1)};
        String[] timeLabels = {"d", "h", "m"};
        for (int i = 0; i < timeUnits.length; i++) {
            if (timeDifference >= timeUnits[i]) {
                return (timeDifference / timeUnits[i]) + timeLabels[i];
            }
        }
        return "Just now";
    }

    public static String createLineProgressBar(int length, String progressColor, double current, double max) {
        double percent = Math.min(current, max) / max;
        long completed = Math.round((double) length * percent);
        StringBuilder builder = new StringBuilder().append(progressColor);
        for (int i = 0; i < completed; i++)
            builder.append("-");
        builder.append("<white>");
        for (int i = 0; i < length - completed; i++)
            builder.append("-");
        builder.append(" ").append("<yellow>").append(commaify(current)).append("<gold>/")
                .append("<yellow>").append(commaify(max));
        return builder.toString();
    }

    public static String createProgressText(String text, double current, double max) {
        double percent;
        if (max != 0)
            percent = (current / max) * 100.0;
        else
            percent = 0.0;
        percent = roundTo(percent, 1);
        return "<gray>" + text + ": " + (percent < 100.0 ? "<yellow>" + commaify(percent)
                                                                 + "<gold>%" : "<green>100.0%");
    }

    public static double roundTo(double d, int decimalPlaces) {
        return Math.round(d * Math.pow(10, decimalPlaces)) / Math.pow(10, decimalPlaces);
    }

    public static String stripColor(String s) {
        if (s == null) return "";
        return s.replaceAll("<[^>]+>", "");
    }

    public static Component text(String miniMessage) {
        if (miniMessage == null) return Component.empty();
        return MINI_MESSAGE.deserialize(toMiniMessage(miniMessage)).decoration(TextDecoration.ITALIC, false);
    }

    public static Component text(Component component) {
        return component == null ? Component.empty() : component.decoration(TextDecoration.ITALIC, false);
    }

    public static List<Component> text(List<String> messages) {
        return messages.stream().map(StringUtility::text).collect(Collectors.toList());
    }

    public static String escapeMiniMessage(String value) {
        return MINI_MESSAGE.escapeTags(value == null ? "" : value);
    }

    public static String toMiniMessage(String input) {
        if (input == null || input.isEmpty()) return "";

        StringBuilder output = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            char current = input.charAt(i);
            if (current == '&' && i + 1 < input.length()) {
                char code = Character.toLowerCase(input.charAt(i + 1));
                String tag = LEGACY_COLOR_TAGS.get(code);
                if (tag != null) {
                    output.append('<').append(tag).append('>');
                    i++;
                    continue;
                }
            }
            output.append(current);
        }
        return output.toString();
    }

    public static NamedTextColor getNamedTextColor(String color) {
        if (color == null || color.isBlank()) return NamedTextColor.WHITE;
        String normalized = toMiniMessage(color).replace("<", "").replace(">", "").toLowerCase(Locale.ROOT);
        return TEXT_COLORS.getOrDefault(normalized, NamedTextColor.WHITE);
    }

    public static String shortenNumber(double number) {
        if (number < 1000) return String.valueOf((int) number);
        String[] units = new String[]{"K", "M", "B"};
        for (int i = units.length - 1; i >= 0; i--) {
            double unitValue = Math.pow(1000, i + 1);
            if (number >= unitValue) {
                return String.format("%.1f%s", number / unitValue, units[i]);
            }
        }
        return String.valueOf(number); // Fallback, should not be reached
    }

    public static String formatTimeLeft(long millis) {
        StringBuilder sb = new StringBuilder();
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        long hours = TimeUnit.MILLISECONDS.toHours(millis % TimeUnit.DAYS.toMillis(1));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis % TimeUnit.HOURS.toMillis(1));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis % TimeUnit.MINUTES.toMillis(1));

        // Eliminated redundant checks by concatenating non-zero values directly
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");
        if (seconds > 0 || sb.isEmpty()) sb.append(seconds).append("s");
        return sb.toString().trim();
    }

    public static String formatTimeLeftWrittenOut(long millis) {
        StringBuilder sb = new StringBuilder();
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        long hours = TimeUnit.MILLISECONDS.toHours(millis % TimeUnit.DAYS.toMillis(1));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis % TimeUnit.HOURS.toMillis(1));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis % TimeUnit.MINUTES.toMillis(1));

        // Eliminated redundant checks by concatenating non-zero values directly
        if (days > 0) sb.append(days).append((days == 1) ? " day " : " days ");
        if (hours > 0) sb.append(hours).append((hours == 1) ? " hour " : " hours ");
        if (minutes > 0) sb.append(minutes).append((minutes == 1) ? " minute " : " minutes ");
        if (seconds > 0 || sb.isEmpty()) sb.append(seconds).append((seconds == 1) ? " second " : " seconds ");
        return sb.toString().trim();
    }

    public static String commaify(int i) {
        return INTEGER_FORMAT.format(i);
    }

    public static Material getMaterialFromBlock(Block block) {
        return Material.fromKey(block.key());
    }

    public static String profileAge(long tbf) {
        return formatTimeWentBy(System.currentTimeMillis() - tbf);
    }

    /**
     * Unescapes a string that contains standard Java escape sequences.
     * This is untested and may not work as expected.
     *
     * @param input the string to unescape
     * @return the unescaped string
     */
    public static String unescapeJava(String input) {
        if (input == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '\\' && i < input.length() - 1) {
                char next = input.charAt(i + 1);
                switch (next) {
                    case 'b':
                        sb.append('\b');
                        i++;
                        break;
                    case 't':
                        sb.append('\t');
                        i++;
                        break;
                    case 'n':
                        sb.append('\n');
                        i++;
                        break;
                    case 'f':
                        sb.append('\f');
                        i++;
                        break;
                    case 'r':
                        sb.append('\r');
                        i++;
                        break;
                    case '\"':
                        sb.append('\"');
                        i++;
                        break;
                    case '\'':
                        sb.append('\'');
                        i++;
                        break;
                    case '\\':
                        sb.append('\\');
                        i++;
                        break;
                    case 'u':
                        // Unicode escape sequence:
                        if (i + 5 < input.length()) {
                            String hex = input.substring(i + 2, i + 6);
                            try {
                                int code = Integer.parseInt(hex, 16);
                                sb.append((char) code);
                                i += 5;
                            } catch (NumberFormatException e) {
                                // Not a valid Unicode escape, so fall back
                                sb.append('\\').append('u');
                                i++;
                            }
                        } else {
                            sb.append('\\').append('u');
                            i++;
                        }
                        break;
                    default:
                        // Unrecognized escape, skip the backslash
                        sb.append(next);
                        i++;
                        break;
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String getAsRomanNumeral(int num) {
        if (num == 0) return "";
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder roman = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                num -= values[i];
                roman.append(symbols[i]);
            }
        }
        return roman.toString();
    }

    /**
     * Capitalizes the first letter of the input string and lowercases the rest.
     * @param input The string to capitalize.
     * @return The input string with the first letter capitalized and the rest lowercased. If the input is null or empty, it returns the input as is.
     */
    public static String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public static String getTextFromComponent(Component component) {
        if (component == null)
            throw new IllegalArgumentException("Component cannot be null");
        if (!(component instanceof TextComponent))
            throw new IllegalArgumentException("Component must be a TextComponent, but got: " + component.getClass().getSimpleName());
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    public static String getAuctionSetupFormattedTime(long millis) {
        return formatTimeLeft(millis).replace(" ", "")
                .replaceAll("s$", "");
    }

    public static String formatAsDate(long millis) {
        // Month as display, Day, Year
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
        return sdf.format(millis);
    }

    public static String toNormalCase(String string) {
        string = string.replace("_", " ");
        String[] spl = string.split(" ");
        StringBuilder sb = new StringBuilder();

        for (String value : spl) {
            String s = value;
            if (s.isEmpty()) {
                continue;
            }
            if (s.length() == 1) {
                s = s.toUpperCase();
            } else {
                s = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
            }
            // Append the processed string to the StringBuilder
            // Only add a space if it's not the first word
            if (!sb.isEmpty()) {
                sb.append(" ");
            }
            sb.append(s);
        }
        return sb.toString();
    }

    public static String commaify(double d) {
        return d < 1 ? "0" : INTEGER_FORMAT.format(d);
    }

    public static String decimalify(double d, int decimalPlaces) {
        if (decimalPlaces < 1)
            throw new IllegalArgumentException();
        String builder = "#." + "#".repeat(decimalPlaces);
        DecimalFormat df = new DecimalFormat(builder);
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(d);
    }

    public static List<String> splitByWordAndLength(String string, int splitLength) {
        List<String> result = new ArrayList<>();
        String[] lines = string.split("\n", -1);
        for (String line : lines) {
            if (line.isEmpty()) {
                result.add("");
                continue;
            }

            String[] words = line.split(" ");
            StringBuilder currentString = new StringBuilder();

            for (String word : words) {
                if (word.isEmpty()) {
                    continue; // skip extra spaces
                }
                // Check if adding the next word exceeds the split length (considering the space)
                int extraSpace = currentString.isEmpty() ? 0 : 1;
                if (currentString.length() + extraSpace + word.length() > splitLength) {
                    // Add the currentString to the result and reset it
                    if (!currentString.isEmpty()) {
                        result.add(currentString.toString());
                        currentString = new StringBuilder();
                    }
                }
                // Add a space before the word if it's not the first word in the currentString
                if (!currentString.isEmpty()) {
                    currentString.append(" ");
                }
                currentString.append(word);
            }

            // Add any remaining text to the result
            result.add(currentString.toString());
        }

        return result;
    }

    public static List<String> splitByNewLine(String string) {
        return new ArrayList<>(Arrays.asList(string.split("\n", -1)));
    }

    public static List<String> splitByNewLine(String string, String lineStarter) {
        List<String> lines = new ArrayList<>(Arrays.asList(string.split("\n", -1)));
        lines.replaceAll(s -> lineStarter + s);
        return lines;
    }

    public static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    public static String zeroed(long l) {
        return String.format("%02d", l);
    }

    public static String commaify(long l) {
        return INTEGER_FORMAT.format(l);
    }

    public static String limitStringLength(String s, int charLimit) {
        return s.length() <= charLimit ? s : s.substring(0, charLimit);
    }

    public static String ntify(int i) {
        return switch (i % 100) {
            case 11, 12, 13 -> i + "th";
            default -> switch (i % 10) {
                case 1 -> i + "st";
                case 2 -> i + "nd";
                case 3 -> i + "rd";
                default -> i + "th";
            };
        };
    }

    public static long parseDuration(String duration) {
        long totalMillis = 0;
        Pattern pattern = Pattern.compile("(\\d+)([dhms])");
        Matcher matcher = pattern.matcher(duration);
        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1));
            char unit = matcher.group(2).charAt(0);
            switch (unit) {
                case 'd' -> totalMillis += TimeUnit.DAYS.toMillis(value);
                case 'h' -> totalMillis += TimeUnit.HOURS.toMillis(value);
                case 'm' -> totalMillis += TimeUnit.MINUTES.toMillis(value);
                case 's' -> totalMillis += TimeUnit.SECONDS.toMillis(value);
            }
        }
        return totalMillis;
    }
}


