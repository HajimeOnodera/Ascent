package fun.ascent.skyblock.player.fishing.bait;

import java.util.HashMap;
import java.util.Map;

public final class BaitDefinitions {

    private static final Map<String, BaitEffect> DEFINITIONS = new HashMap<>();

    static {
        DEFINITIONS.put("MINNOW_BAIT", new BaitEffect(0.15, 0.0, 0.0, 30));
        DEFINITIONS.put("FISH_BAIT", new BaitEffect(0.30, 0.0, 0.0, 60));
        DEFINITIONS.put("LIGHT_BAIT", new BaitEffect(0.0, 0.0, 0.0, 40));
        DEFINITIONS.put("DARK_BAIT", new BaitEffect(0.0, 0.0, 0.0, 35));
        DEFINITIONS.put("SPOOKY_BAIT", new BaitEffect(0.0, 0.15, 0.0, 20));
        DEFINITIONS.put("SHARK_BAIT", new BaitEffect(0.0, 0.10, 0.0, 0));
        DEFINITIONS.put("BLESSED_BAIT", new BaitEffect(0.0, 0.0, 0.15, 0));
        DEFINITIONS.put("ICE_BAIT", new BaitEffect(0.0, 0.05, 0.0, 10));
        DEFINITIONS.put("WHALE_BAIT", new BaitEffect(0.0, 0.20, 0.20, 0));
    }

    public static BaitEffect get(String itemId) {
        if (itemId == null) {
            return new BaitEffect(0.0, 0.0, 0.0, 0);
        }
        return DEFINITIONS.getOrDefault(itemId.toUpperCase(), new BaitEffect(0.0, 0.0, 0.0, 0));
    }

    private BaitDefinitions() {}
}
