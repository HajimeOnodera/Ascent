package fun.ascent.skyblock.item.registries;

import java.util.Set;

public class ArrowPoisonRegistry {
    private static final Set<String> ARROW_POISON_IDS = Set.of(
            "TOXIC_ARROW_POISON",
            "TWILIGHT_ARROW_POISON"

    );

    public static boolean isArrowPoison(String itemId) {
        return ARROW_POISON_IDS.contains(itemId);
    }
}
