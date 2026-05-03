package fun.ascent.skyblock.calendar.events;

import fun.ascent.skyblock.calendar.*;
import net.minestom.server.event.Event;

public record SkyBlockEventInactiveEvent(Calendar.SkyBlockEvent skyBlockEvent,
                                         Calendar.SkyBlockTime time) implements Event {

    public boolean isSpecialEvent() {
        return skyBlockEvent.isSpecialEvent();
    }

    public boolean isDwarvenKingEvent() {
        return skyBlockEvent.name().startsWith("KING_");
    }

    public DwarvenKing getDwarvenKing() {
        return DwarvenKing.fromEvent(skyBlockEvent);
    }

    public Month getMonth() {
        return time.getMonth();
    }

    public int getDay() {
        return time.day;
    }

    public int getYear() {
        return time.year;
    }
}
