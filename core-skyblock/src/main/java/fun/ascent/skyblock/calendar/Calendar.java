package fun.ascent.skyblock.calendar;

import fun.ascent.skyblock.calendar.events.*;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Calendar {

    private static final long SKYBLOCK_EPOCH = 1560275700000L;
    private static final int DAYS_IN_YEAR = 372;
    private static final int DAYS_IN_MONTH = 31;
    private static final int MONTHS_IN_YEAR = 12;
    private static final int SECONDS_PER_DAY = 1200;
    private static final int SECONDS_PER_MONTH = SECONDS_PER_DAY * DAYS_IN_MONTH;
    private static final int SECONDS_PER_HOUR = 50;
    private static final int SECONDS_PER_MINUTE = 50;

    private static final Set<SkyBlockEvent> activeEvents = ConcurrentHashMap.newKeySet();
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static boolean monitoring = false;

    private Calendar() {
    }

    public static void init() {
        // Initialization if needed
    }

    public enum SkyBlockEvent {
        DARK_AUCTION(false),
        JACOBS_CONTEST(false),
        KING_BRAMMOR(false),
        KING_EMKAM(false),
        KING_REDROS(false),
        KING_ERREN(false),
        KING_THORMYR(false),
        KING_EMMOR(false),
        KING_GRANDAN(false),
        SPOOKY_FESTIVAL(true),
        TRAVELLING_ZOO(true),
        JERRYS_WORKSHOP(true),
        SEASON_OF_JERRY(true),
        NEW_YEAR_CELEBRATION(true),
        BANK_INTEREST(false),
        ELECTION_RESULTS(false),
        SPECIAL_MAYOR(true),
        CULT_FALLEN_STAR(false),
        BINGO_EVENT(true);

        private final boolean specialEvent;

        SkyBlockEvent(boolean specialEvent) {
            this.specialEvent = specialEvent;
        }

        public boolean isSpecialEvent() {
            return specialEvent;
        }

        public boolean isDwarvenKingEvent() {
            return name().startsWith("KING_");
        }

        public DwarvenKing getDwarvenKing() {
            return DwarvenKing.fromEvent(this);
        }
    }

    public static class SkyBlockTime {
        public final int year, month, day, hour, minute, second;
        public final long timestamp;

        public SkyBlockTime(int year, int month, int day, int hour, int minute, int second) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.hour = hour;
            this.minute = minute;
            this.second = second;
            this.timestamp = System.currentTimeMillis();
        }

        public Month getMonth() {
            return Month.fromNumber(month);
        }

        public String getMonthName() {
            Month m = getMonth();
            return m != null ? m.getDisplayName() : "Unknown";
        }

        public boolean isAM() {
            return hour < 12;
        }

        public boolean isPM() {
            return hour >= 12;
        }

        public boolean isNight() {
            return hour >= 19 || hour < 6;
        }

        public boolean isDay() {
            return !isNight();
        }

        public int getDisplayHour() {
            return hour == 0 ? 12 : (hour > 12 ? hour - 12 : hour);
        }

        public String getPeriod() {
            return hour >= 12 ? "pm" : "am";
        }

        public String getHourFormatted() {
            String e = "";
            if (isNight()) {
                e = " <aqua>☽</aqua>";
            } else if (isDay()) {
                e = " <yellow>☀</yellow>";
            }

            return getTimeFormatted() + e;
        }

        public String getOrdinalDay() {
            String suffix;
            if (day >= 11 && day <= 13)
                suffix = "th";
            else
                switch (day % 10) {
                    case 1:
                        suffix = "st";
                        break;
                    case 2:
                        suffix = "nd";
                        break;
                    case 3:
                        suffix = "rd";
                        break;
                    default:
                        suffix = "th";
                }
            return day + suffix;
        }

        public String getTimeFormatted() {
            int displayMinute = (minute / 20) * 20;
            return String.format("%d:%02d%s", getDisplayHour(), displayMinute, getPeriod());
        }

        public String getDateFormatted() {
            return getMonthName() + " " + getOrdinalDay();
        }

        public Day getDay() {
            // SkyBlock days cycle through the week based on total days elapsed
            int totalDays = (year - 1) * DAYS_IN_YEAR + (month - 1) * DAYS_IN_MONTH + day;
            int dayIndex = ((totalDays - 1) % 7) + 1;
            return Day.fromNumber(dayIndex);
        }

        public String getDayName() {
            Day d = getDay();
            return d != null ? d.getDisplayName() : "Unknown";
        }

        public String getFullFormatted() {
            return getTimeFormatted() + " & " + getDateFormatted();
        }

        public long getTotalSeconds() {
            return (long) (year - 1) * DAYS_IN_YEAR * SECONDS_PER_DAY
                    + (long) (month - 1) * SECONDS_PER_MONTH
                    + (long) (day - 1) * SECONDS_PER_DAY
                    + (long) hour * SECONDS_PER_HOUR
                    + (long) minute * SECONDS_PER_MINUTE
                    + second;
        }

        public long toMinecraftTicks() {
            double ticksFromHour = hour * 1000.0;
            double ticksFromMinute = minute * (1000.0 / 24.0);
            double ticksFromSecond = second * (1000.0 / 24.0 / 60.0);
            return (long) (ticksFromHour + ticksFromMinute + ticksFromSecond) % 24000;
        }

        @Override
        public String toString() {
            return String.format("%02d:%02d %s - %d/%d/%d - %s", getDisplayHour(), minute, getPeriod().toUpperCase(),
                    day, month, year, getMonthName());
        }
    }

    public static class EventInfo {
        public final SkyBlockEvent event;
        public final SkyBlockTime startsAt;
        public final SkyBlockTime endsAt;
        public final String startsAtString;
        public final String endsAtString;
        public final boolean isActive;

        public EventInfo(SkyBlockEvent event, SkyBlockTime startsAt, SkyBlockTime endsAt, SkyBlockTime currentTime,
                boolean isActive) {
            this.event = event;
            this.startsAt = startsAt;
            this.endsAt = endsAt;
            this.isActive = isActive;

            long startsInSeconds = startsAt.getTotalSeconds() - currentTime.getTotalSeconds();
            long endsInSeconds = endsAt.getTotalSeconds() - currentTime.getTotalSeconds();
            if (startsInSeconds < 0)
                startsInSeconds = 0;

            this.startsAtString = formatDuration(startsInSeconds);
            this.endsAtString = formatDuration(Math.abs(endsInSeconds));
        }

        public boolean isDwarvenKingEvent() {
            return event.isDwarvenKingEvent();
        }

        public DwarvenKing getDwarvenKing() {
            return event.getDwarvenKing();
        }

        public boolean isSpecialEvent() {
            return event.isSpecialEvent();
        }

        public long secondsUntilStart(SkyBlockTime current) {
            return Math.max(0, startsAt.getTotalSeconds() - current.getTotalSeconds());
        }

        public long secondsUntilEnd(SkyBlockTime current) {
            return Math.max(0, endsAt.getTotalSeconds() - current.getTotalSeconds());
        }

        @Override
        public String toString() {
            return String.format("%s: %s to %s (starts in %s, ends in %s) - %s",
                    event, startsAt, endsAt, startsAtString, endsAtString, isActive ? "ACTIVE" : "INACTIVE");
        }
    }

    public static SkyBlockTime getCurrentTime() {
        long elapsed = System.currentTimeMillis() - SKYBLOCK_EPOCH;

        long yearMs = (long) DAYS_IN_YEAR * SECONDS_PER_DAY * 1000;
        int year = (int) (elapsed / yearMs) + 1;
        elapsed %= yearMs;

        long monthMs = (long) SECONDS_PER_MONTH * 1000;
        int month = (int) (elapsed / monthMs) + 1;
        elapsed %= monthMs;

        long dayMs = (long) SECONDS_PER_DAY * 1000;
        int day = (int) (elapsed / dayMs) + 1;
        elapsed %= dayMs;

        long hourMs = (long) SECONDS_PER_HOUR * 1000;
        int hour = (int) (elapsed / hourMs);
        elapsed %= hourMs;

        long minuteMs = (long) SECONDS_PER_MINUTE * 1000;
        int minute = (int) (elapsed / minuteMs);
        elapsed %= minuteMs;

        int second = (int) (elapsed / 1000);

        return new SkyBlockTime(year, month, day, hour, minute, second);
    }

    public static Month getCurrentMonth() {
        return getCurrentTime().getMonth();
    }

    public static DwarvenKing getCurrentDwarvenKing() {
        return getActiveEvents().stream()
                .filter(SkyBlockEvent::isDwarvenKingEvent)
                .map(SkyBlockEvent::getDwarvenKing)
                .findFirst()
                .orElse(null);
    }

    public static Set<SkyBlockEvent> getActiveEvents() {
        SkyBlockTime current = getCurrentTime();
        return getEventsForDay(current.day, current.month, current.year);
    }

    public static boolean isEventActive(SkyBlockEvent event) {
        return getActiveEvents().contains(event);
    }

    public static void syncWorldTime(net.minestom.server.instance.Instance world) {
        world.setTime(getCurrentTime().toMinecraftTicks());
    }

    public static void startWorldTimeSync(net.minestom.server.instance.Instance world, long intervalTicks) {
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            world.setTimeRate(0);
            world.setTime(getCurrentTime().toMinecraftTicks());
        }).repeat(TaskSchedule.tick((int) intervalTicks)).schedule();
    }

    public static Set<SkyBlockEvent> getEventsForDay(int day, int month, int year) {
        Set<SkyBlockEvent> events = new HashSet<>();
        int totalDays = (year - 1) * DAYS_IN_YEAR + (month - 1) * DAYS_IN_MONTH + day;

        if (totalDays % 3 == 0)
            events.add(SkyBlockEvent.DARK_AUCTION);
        if (totalDays % 3 == 1)
            events.add(SkyBlockEvent.JACOBS_CONTEST);

        events.add(DwarvenKing.fromIndex((5 + totalDays) % DwarvenKing.values().length).getLinkedEvent());

        if (month == 8 && day >= 29)
            events.add(SkyBlockEvent.SPOOKY_FESTIVAL);
        if ((month == 4 || month == 10) && day >= 1 && day <= 3)
            events.add(SkyBlockEvent.TRAVELLING_ZOO);

        if (month == 12) {
            events.add(SkyBlockEvent.JERRYS_WORKSHOP);
            if (day >= 24 && day <= 26)
                events.add(SkyBlockEvent.SEASON_OF_JERRY);
            if (day >= 29)
                events.add(SkyBlockEvent.NEW_YEAR_CELEBRATION);
        }

        if (day == 1)
            events.add(SkyBlockEvent.BANK_INTEREST);
        if (month == 3 && day == 27)
            events.add(SkyBlockEvent.ELECTION_RESULTS);
        if ((year % 8 == 0 && month >= 6) || (year % 8 == 1 && month <= 3))
            events.add(SkyBlockEvent.SPECIAL_MAYOR);
        if (day == 7 || day == 14 || day == 21 || day == 28)
            events.add(SkyBlockEvent.CULT_FALLEN_STAR);
        if (day <= 7)
            events.add(SkyBlockEvent.BINGO_EVENT);

        return events;
    }

    public static List<EventInfo> getUpcomingEvents() {
        return getUpcomingEvents(14);
    }

    public static List<EventInfo> getUpcomingEvents(int eventCount) {
        List<EventInfo> result = new ArrayList<>();
        SkyBlockTime currentTime = getCurrentTime();
        Set<String> seen = new HashSet<>();

        for (int i = 0; i < 100 && result.size() < eventCount; i++) {
            int checkDay = currentTime.day + i;
            int checkMonth = currentTime.month;
            int checkYear = currentTime.year;

            while (checkDay > DAYS_IN_MONTH) {
                checkDay -= DAYS_IN_MONTH;
                checkMonth++;
                if (checkMonth > MONTHS_IN_YEAR) {
                    checkMonth = 1;
                    checkYear++;
                }
            }

            for (SkyBlockEvent event : getEventsForDay(checkDay, checkMonth, checkYear)) {
                String key = event + "-" + checkYear + "-" + checkMonth + "-" + checkDay;
                if (seen.add(key)) {
                    SkyBlockTime start = new SkyBlockTime(checkYear, checkMonth, checkDay, 0, 0, 0);
                    SkyBlockTime end = getEventEndTime(event, start);
                    boolean active = i == 0 && getActiveEvents().contains(event);
                    result.add(new EventInfo(event, start, end, currentTime, active));
                    if (result.size() >= eventCount)
                        break;
                }
            }
        }

        return result.stream()
                .sorted(Comparator.comparingLong(e -> e.startsAt.getTotalSeconds()))
                .limit(eventCount)
                .collect(Collectors.toList());
    }

    public static EventInfo getNextEvent() {
        SkyBlockTime current = getCurrentTime();
        return getUpcomingEvents(30).stream()
                .filter(e -> e.startsAt.getTotalSeconds() > current.getTotalSeconds())
                .min(Comparator.comparingLong(e -> e.startsAt.getTotalSeconds()))
                .orElse(null);
    }

    public static Optional<EventInfo> getNextOccurrenceOf(SkyBlockEvent event) {
        SkyBlockTime current = getCurrentTime();
        return getUpcomingEvents(60).stream()
                .filter(e -> e.event == event && e.startsAt.getTotalSeconds() > current.getTotalSeconds())
                .min(Comparator.comparingLong(e -> e.startsAt.getTotalSeconds()));
    }

    private static SkyBlockTime getEventEndTime(SkyBlockEvent event, SkyBlockTime start) {
        switch (event) {
            case SPOOKY_FESTIVAL:
                return new SkyBlockTime(start.year, start.month, 31, 23, 23, 59);
            case TRAVELLING_ZOO:
                return new SkyBlockTime(start.year, start.month, 3, 23, 23, 59);
            case JERRYS_WORKSHOP:
                return new SkyBlockTime(start.year, start.month, 31, 23, 23, 59);
            case SEASON_OF_JERRY:
                return new SkyBlockTime(start.year, start.month, 26, 23, 23, 59);
            case NEW_YEAR_CELEBRATION:
                return new SkyBlockTime(start.year, start.month, 31, 23, 23, 59);
            case BINGO_EVENT:
                return new SkyBlockTime(start.year, start.month, 7, 23, 23, 59);
            case SPECIAL_MAYOR:
                return new SkyBlockTime(start.year, start.year % 8 == 0 ? 12 : 3, 31, 23, 23, 59);
            default:
                return new SkyBlockTime(start.year, start.month, start.day, 23, 23, 59);
        }
    }

    private static boolean isMultiDayEvent(SkyBlockEvent event) {
        switch (event) {
            case SPOOKY_FESTIVAL:
            case TRAVELLING_ZOO:
            case JERRYS_WORKSHOP:
            case SEASON_OF_JERRY:
            case NEW_YEAR_CELEBRATION:
            case SPECIAL_MAYOR:
            case BINGO_EVENT:
                return true;
            default:
                return false;
        }
    }

    private static boolean isFirstDayOfMultiDayEvent(SkyBlockEvent event, SkyBlockTime time) {
        switch (event) {
            case SPOOKY_FESTIVAL:
                return time.month == 8 && time.day == 29;
            case TRAVELLING_ZOO:
                return (time.month == 4 || time.month == 10) && time.day == 1;
            case JERRYS_WORKSHOP:
                return time.month == 12 && time.day == 1;
            case SEASON_OF_JERRY:
                return time.month == 12 && time.day == 24;
            case NEW_YEAR_CELEBRATION:
                return time.month == 12 && time.day == 29;
            case BINGO_EVENT:
                return time.day == 1;
            case SPECIAL_MAYOR:
                return (time.year % 8 == 0 && time.month == 6 && time.day == 1) ||
                        (time.year % 8 == 1 && time.month == 1 && time.day == 1);
            default:
                return true;
        }
    }

    private static boolean isLastDayOfMultiDayEvent(SkyBlockEvent event, SkyBlockTime time) {
        int nextDay = time.day + 1, nextMonth = time.month, nextYear = time.year;
        if (nextDay > DAYS_IN_MONTH) {
            nextDay = 1;
            nextMonth++;
            if (nextMonth > MONTHS_IN_YEAR) {
                nextMonth = 1;
                nextYear++;
            }
        }
        return !getEventsForDay(nextDay, nextMonth, nextYear).contains(event);
    }

    // Time update loop
    private static final long SKYBLOCK_MINUTE_REAL_MS = 8333; // ~8.333 seconds
    private static volatile SkyBlockTime cachedTime;

    public static SkyBlockTime getCachedTime() {
        return cachedTime != null ? cachedTime : getCurrentTime();
    }

    public static void startTimeUpdates() {
        if (scheduler.isShutdown())
            return;
        monitoring = true;

        scheduler.execute(() -> {
            syncSleep();

            while (monitoring) {
                long start = System.currentTimeMillis();

                updateTime();
                checkEventChanges();

                long diff = System.currentTimeMillis() - start;
                long sleep = SKYBLOCK_MINUTE_REAL_MS - diff;

                if (sleep < 1)
                    sleep = 1;

                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException ignored) {
                }
            }
        });
    }

    private static void syncSleep() {
        long ms = System.currentTimeMillis();

        long ceil = (ms / 10000) * 10000 + 5000;

        if (ceil <= ms) {
            ceil += 10000;
        }

        long sleep = ceil - ms;

        MinecraftServer.LOGGER.info("[SkyBlock Calendar] Sync Sleep " + sleep);

        try {
            Thread.sleep(sleep);
        } catch (InterruptedException ignored) {
        }
    }

    private static synchronized void updateTime() {
        cachedTime = getCurrentTime();

        // Update scoreboards on main thread
        MinecraftServer.getSchedulerManager().scheduleNextTick(() -> {
            // TODO: Update player scoreboards if needed
        });
    }

    public static void stopMonitoring() {
        monitoring = false;
        scheduler.shutdown();
    }

    private static void checkEventChanges() {
        if (!monitoring)
            return;

        Set<SkyBlockEvent> currentEvents = getActiveEvents();
        SkyBlockTime currentTime = getCurrentTime();

        for (SkyBlockEvent event : currentEvents) {
            if (activeEvents.add(event)) {
                if (!event.isSpecialEvent() || !isMultiDayEvent(event)
                        || isFirstDayOfMultiDayEvent(event, currentTime)) {
                    MinecraftServer.getSchedulerManager().scheduleNextTick(() -> MinecraftServer.getGlobalEventHandler()
                            .call(new SkyBlockEventActiveEvent(event, currentTime)));
                }
            }
        }

        Iterator<SkyBlockEvent> it = activeEvents.iterator();
        while (it.hasNext()) {
            SkyBlockEvent event = it.next();
            if (!currentEvents.contains(event)) {
                it.remove();
                if (!event.isSpecialEvent() || !isMultiDayEvent(event)
                        || isLastDayOfMultiDayEvent(event, currentTime)) {
                    MinecraftServer.getSchedulerManager().scheduleNextTick(() -> MinecraftServer.getGlobalEventHandler()
                            .call(new SkyBlockEventInactiveEvent(event, currentTime)));
                }
            }
        }
    }

    public static String formatDuration(long totalSeconds) {
        totalSeconds = Math.abs(totalSeconds);
        long days = totalSeconds / 86400;
        totalSeconds %= 86400;
        long hours = totalSeconds / 3600;
        totalSeconds %= 3600;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0)
            sb.append(days).append("d ");
        if (hours > 0)
            sb.append(hours).append("h ");
        if (minutes > 0)
            sb.append(minutes).append("m ");
        if (seconds > 0)
            sb.append(seconds).append("s");
        String result = sb.toString().trim();
        return result.isEmpty() ? "0s" : result;
    }
}
