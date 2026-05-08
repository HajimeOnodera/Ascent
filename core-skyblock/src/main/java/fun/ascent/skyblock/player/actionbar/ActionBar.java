package fun.ascent.skyblock.player.actionbar;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ActionBar {

    private static final Map<UUID, ActionBar> bars = new ConcurrentHashMap<>();

    private final EnumMap<Section, PriorityQueue<Replacement>> replacements = new EnumMap<>(Section.class);
    private final EnumMap<Section, String> defaults = new EnumMap<>(Section.class);

    private ActionBar() {
        for (Section s : Section.VALUES) {
            replacements.put(s, new PriorityQueue<>(Comparator.comparingInt(Replacement::priority).reversed()));
            defaults.put(s, "");
        }
    }

    public static ActionBar of(UUID uuid) {
        return bars.computeIfAbsent(uuid, k -> new ActionBar());
    }

    public static void remove(UUID uuid) {
        bars.remove(uuid);
    }

    public void setDefault(Section section, String display) {
        defaults.put(section, display);
    }

    public void addReplacement(Section section, String display, int durationTicks, int priority) {
        Replacement r = new Replacement(display, priority);
        replacements.get(section).offer(r);
        if (durationTicks > 0) {
            MinecraftServer.getSchedulerManager().scheduleTask(
                    () -> replacements.get(section).remove(r),
                    TaskSchedule.tick(durationTicks),
                    TaskSchedule.stop()
            );
        }
    }

    public String build() {
        StringBuilder sb = new StringBuilder();
        for (Section section : Section.VALUES) {
            String display = resolve(section);
            if (display.isEmpty()) continue;
            if (!sb.isEmpty()) sb.append("     ");
            sb.append(display);
        }
        return sb.toString();
    }

    private String resolve(Section section) {
        PriorityQueue<Replacement> queue = replacements.get(section);
        if (!queue.isEmpty()) return queue.peek().display();
        return defaults.get(section);
    }

    public enum Section {
        HEALTH, DEFENSE, MANA;
        static final Section[] VALUES = values();
    }

    private record Replacement(String display, int priority) {}
}