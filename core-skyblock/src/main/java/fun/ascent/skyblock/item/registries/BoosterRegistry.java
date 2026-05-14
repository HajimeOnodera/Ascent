package fun.ascent.skyblock.item.registries;

import java.util.Set;

public class BoosterRegistry {
    private static final Set<String> BOOSTER_ID = Set.of(
            "FIGHTING_BOOSTER",
            "FORAGING_FORTUNE_BOOSTER",
            "FORAGING_WISDOM_BOOSTER",
            "SWEEP_BOOSTER"

    );

    public static boolean isBooster(String itemId) {
        return BOOSTER_ID.contains(itemId);
    }
}
