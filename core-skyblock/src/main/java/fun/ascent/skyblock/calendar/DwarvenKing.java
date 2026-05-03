package fun.ascent.skyblock.calendar;

import lombok.Getter;

@Getter
public enum DwarvenKing {
    BRAMMOR(0, "Brammor", Calendar.SkyBlockEvent.KING_BRAMMOR),
    EMKAM(1, "Emkam", Calendar.SkyBlockEvent.KING_EMKAM),
    REDROS(2, "Redros", Calendar.SkyBlockEvent.KING_REDROS),
    ERREN(3, "Erren", Calendar.SkyBlockEvent.KING_ERREN),
    THORMYR(4, "Thormyr", Calendar.SkyBlockEvent.KING_THORMYR),
    EMMOR(5, "Emmor", Calendar.SkyBlockEvent.KING_EMMOR),
    GRANDAN(6, "Grandan", Calendar.SkyBlockEvent.KING_GRANDAN);

    private final int index;
    private final String displayName;
    private final Calendar.SkyBlockEvent linkedEvent;

    DwarvenKing(int index, String displayName, Calendar.SkyBlockEvent linkedEvent) {
        this.index = index;
        this.displayName = displayName;
        this.linkedEvent = linkedEvent;
    }

    public static DwarvenKing fromIndex(int index) {
        return values()[index % values().length];
    }

    public static DwarvenKing fromEvent(Calendar.SkyBlockEvent event) {
        for (DwarvenKing k : values()) if (k.linkedEvent == event) return k;
        return null;
    }

    @Override
    public String toString() { return displayName; }
}
