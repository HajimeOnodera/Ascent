package fun.ascent.skyblock.calendar;

import lombok.Getter;

@Getter
public enum Day {
    SUNDAY(1, "Sunday"),
    MONDAY(2, "Monday"),
    TUESDAY(3, "Tuesday"),
    WEDNESDAY(4, "Wednesday"),
    THURSDAY(5, "Thursday"),
    FRIDAY(6, "Friday"),
    SATURDAY(7, "Saturday");

    private final int number;
    private final String displayName;

    Day(int number, String displayName) {
        this.number = number;
        this.displayName = displayName;
    }

    public static Day fromNumber(int number) {
        for (Day d : values()) {
            if (d.number == number) return d;
        }
        return null;
    }
}
