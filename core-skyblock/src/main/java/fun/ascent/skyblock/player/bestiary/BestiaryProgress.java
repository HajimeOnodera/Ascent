package fun.ascent.skyblock.player.bestiary;

import java.util.HashMap;
import java.util.Map;

public class BestiaryProgress {
    private Map<String, Integer> kills = new HashMap<>();

    public Map<String, Integer> getKills() {
        if (kills == null) {
            kills = new HashMap<>();
        }
        return kills;
    }

    public int getKills(String mobId) {
        return getKills().getOrDefault(mobId, 0);
    }

    public int getKills(BestiaryMobType mob) {
        return getKills(mob.id());
    }

    public int getKills(BestiaryFamily family) {
        return family.mobs().stream()
                .mapToInt(this::getKills)
                .sum();
    }

    public int increment(String mobId, int amount) {
        int updated = Math.max(0, getKills(mobId) + amount);
        getKills().put(mobId, updated);
        return updated;
    }
}
