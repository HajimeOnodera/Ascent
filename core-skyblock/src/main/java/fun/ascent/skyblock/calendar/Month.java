package fun.ascent.skyblock.calendar;

import lombok.Getter;

@Getter
public enum Month {
    EARLY_SPRING(1, "Early Spring"),
    SPRING(2, "Spring"),
    LATE_SPRING(3, "Late Spring"),
    EARLY_SUMMER(4, "Early Summer"),
    SUMMER(5, "Summer"),
    LATE_SUMMER(6, "Late Summer"),
    EARLY_AUTUMN(7, "Early Autumn"),
    AUTUMN(8, "Autumn"),
    LATE_AUTUMN(9, "Late Autumn"),
    EARLY_WINTER(10, "Early Winter"),
    WINTER(11, "Winter"),
    LATE_WINTER(12, "Late Winter");

    private final int number;
    private final String displayName;

    Month(int number, String displayName) {
        this.number = number;
        this.displayName = displayName;
    }

    public static Month fromNumber(int number) {
        for (Month m : values()) if (m.number == number) return m;
        return null;
    }

    @Override
    public String toString() { return displayName; }
}
