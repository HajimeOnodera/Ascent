package fun.ascent.skyblock.item.registries;

import java.util.Set;

public class AnvilCombinableRegistry {
    private static final Set<String> ANVIL_COMBINABLE_ID = Set.of(
            "RECOMBOBULATOR_3000",
            "WOOD_SINGULARITY",
            "SILEX",
            "TRANSMISSION_TUNER",
            "DIVAN_POWDER_COATING",
            "GOLDEN_BOUNTY",
            "MANA_DISINTEGRATOR",
            "PESTHUNTING_GUIDE",
            "GOLD_BOTTLE_CAP",
            "TROUBLED_BUBBLE",
            "SEVERED_PINCER",
            "OCTOPUS_TENDRIL",
            "CHAIN_END_TIMES",
            "SEVERED_HAND",
            "ENDSTONE_IDOL",
            "ENSNARED_SNAIL",
            "FATEFUL_STINGER",
            "TURBO_GOURD",
            "ENCHANTED_TURBO_GOURD",
            "OVERCLOCKER_3000",
            "PRICKLY_CREEPER",
            "VIBRANT_CORAL",

            "IMPLOSION_SCROLL",
            "WITHER_SHIELD_SCROLL",
            "SHADOW_WARP_SCROLL",

            "FIRST_MASTER_STAR",
            "SECOND_MASTER_STAR",
            "THIRD_MASTER_STAR",
            "FOURTH_MASTER_STAR",
            "FIFTH_MASTER_STAR",

            "HOT_POTATO_BOOK",
            "FUMING_POTATO_BOOK",
            "BOOK_OF_STATS",
            "THE_ART_OF_WAR",
            "FARMING_FOR_DUMMIES",
            "THE_ART_OF_PEACE",
            "JALAPENO_BOOK",
            "BOOKWORM_BOOK",
            "POLARVOID_BOOK",
            "WET_BOOK",

            "FIGHTING_BOOSTER",
            "FORAGING_FORTUNE_BOOSTER",
            "FORAGING_WISDOM_BOOSTER",
            "SWEEP_BOOSTER"

    );

    public static boolean isAnvilCombinable(String itemId) {
        return ANVIL_COMBINABLE_ID.contains(itemId);
    }
}
