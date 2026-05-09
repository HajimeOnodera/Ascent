package fun.ascent.proxy.manager;

import fun.ascent.proxy.config.*;
import fun.ascent.proxy.service.*;
import fun.ascent.database.PlayerRepository;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks player session times and persists total playtime to MongoDB.
 * Playtime is stored in the player document under "playtime.totalMs" (milliseconds).
 */
public final class PlaytimeTracker {

    private static final String FIELD_TOTAL = "playtime.totalMs";
    private static final Map<UUID, Long> sessionStart = new ConcurrentHashMap<>();

    private PlaytimeTracker() {}

    /**
     * Called when a player logs in — records the session start time.
     */
    public static void onLogin(UUID uuid) {
        sessionStart.put(uuid, System.currentTimeMillis());
    }

    /**
     * Called when a player disconnects — calculates session duration
     * and adds it to the stored total in MongoDB.
     */
    public static void onDisconnect(UUID uuid) {
        Long start = sessionStart.remove(uuid);
        if (start == null) return;

        long sessionMs = System.currentTimeMillis() - start;
        if (sessionMs <= 0) return;

        try {
            PlayerRepository.incrementField(uuid, FIELD_TOTAL, sessionMs);
        } catch (Exception e) {
            System.err.println("[PlaytimeTracker] Failed to save playtime for " + uuid + ": " + e.getMessage());
        }
    }

    /**
     * Returns the total playtime in milliseconds for a player,
     * including the current active session if they are online.
     */
    public static long getTotalPlaytimeMs(UUID uuid) {
        long stored = 0;
        try {
            Object val = PlayerRepository.getField(uuid, FIELD_TOTAL, null);
            if (val instanceof Number n) {
                stored = n.longValue();
            }
        } catch (Exception e) {
            System.err.println("[PlaytimeTracker] Failed to load playtime for " + uuid + ": " + e.getMessage());
        }

        // Add current session if player is online
        Long start = sessionStart.get(uuid);
        if (start != null) {
            stored += System.currentTimeMillis() - start;
        }

        return stored;
    }

    /**
     * Flushes all active sessions to the database (e.g. on proxy shutdown).
     */
    public static void flushAll() {
        for (UUID uuid : sessionStart.keySet()) {
            onDisconnect(uuid);
        }
    }
}
