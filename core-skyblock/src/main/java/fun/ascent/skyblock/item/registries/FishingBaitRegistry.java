package fun.ascent.skyblock.item.registries;

import java.util.Set;

public class FishingBaitRegistry {
    private static final Set<String> FISHING_BAIT_IDS = Set.of(
            "MINNOW_BAIT",
            "DARK_BAIT",
            "SPOOKY_BAIT",
            "LIGHT_BAIT",
            "SPIKED_BAIT",
            "FISH_BAIT",
            "CARROT_BAIT",
            "CORRUPTED_BAIT",
            "OBFUSCATED_FISH_1_DIAMOND",
            "OBFUSCATED_FISH_1_GOLD",
            "OBFUSCATED_FISH_1_SILVER",
            "OBFUSCATED_FISH_1_BRONZE",
            "OBFUSCATED_FISH_2_DIAMOND",
            "OBFUSCATED_FISH_2_GOLD",
            "OBFUSCATED_FISH_2_SILVER",
            "OBFUSCATED_FISH_2_BRONZE",
            "ICE_BAIT",
            "BLESSED_BAIT",
            "SHARK_BAIT",
            "GLOWY_CHUM_BAIT",
            "HOT_BAIT",
            "WORM_BAIT",
            "GOLDEN_BAIT",
            "WHALE_BAIT",
            "FROZEN_BAIT",
            "HOTSPOT_BAIT",
            "TREASURE_BAIT"
    );

    public static boolean isFishingBait(String itemId) {
        return FISHING_BAIT_IDS.contains(itemId);
    }
}
