package fun.ascent.skyblock.calendar.events;

import fun.ascent.skyblock.calendar.*;
import net.minestom.server.event.Event;

public final class SkyBlockEventActiveEvent implements Event {

    private final Calendar.SkyBlockEvent skyBlockEvent;
    private final Calendar.SkyBlockTime time;

    public SkyBlockEventActiveEvent(Calendar.SkyBlockEvent skyBlockEvent, Calendar.SkyBlockTime time) {
        this.skyBlockEvent = skyBlockEvent;
        this.time = time;
    }

    public Calendar.SkyBlockEvent getSkyBlockEvent() { return skyBlockEvent; }
    public Calendar.SkyBlockTime getTime() { return time; }

    public boolean isSpecialEvent() { return skyBlockEvent.isSpecialEvent(); }
    public boolean isDwarvenKingEvent() { return skyBlockEvent.name().startsWith("KING_"); }

    public DwarvenKing getDwarvenKing() { return DwarvenKing.fromEvent(skyBlockEvent); }
    public Month getMonth() { return time.getMonth(); }
    public int getDay() { return time.day; }
    public int getYear() { return time.year; }
}
