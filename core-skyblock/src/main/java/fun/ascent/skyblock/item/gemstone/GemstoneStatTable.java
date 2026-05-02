package fun.ascent.skyblock.item.gemstone;

import fun.ascent.skyblock.item.Rarity;

import java.util.EnumMap;
import java.util.Map;

/**
 * Static lookup table for gemstone stat values based on type, quality, and item rarity.
 * Values are indexed as [quality][rarity] where rarity index is 0=COMMON through 5=MYTHIC.
 */
public class GemstoneStatTable {

    // Map<GemstoneType, double[quality_ordinal][rarity_index]>
    private static final Map<GemstoneType, double[][]> TABLES = new EnumMap<>(GemstoneType.class);

    static {
        // Ruby - Health
        register(GemstoneType.RUBY, new double[][]{
                {1, 2, 3, 4, 5, 6},       // ROUGH
                {2, 3, 5, 7, 9, 12},      // FLAWED
                {4, 6, 8, 10, 14, 18},    // FINE
                {6, 8, 12, 16, 20, 24},   // FLAWLESS
                {8, 10, 16, 22, 26, 30}   // PERFECT
        });

        // Amber - Mining Speed
        register(GemstoneType.AMBER, new double[][]{
                {5, 6, 8, 10, 14, 18},
                {7, 8, 12, 16, 20, 28},
                {10, 12, 16, 22, 28, 40},
                {14, 18, 24, 32, 40, 56},
                {20, 24, 32, 44, 56, 72}
        });

        // Sapphire - Intelligence
        register(GemstoneType.SAPPHIRE, new double[][]{
                {1, 2, 3, 4, 5, 6},
                {2, 3, 5, 7, 9, 12},
                {4, 6, 8, 10, 14, 18},
                {6, 8, 12, 16, 20, 24},
                {8, 10, 16, 22, 26, 30}
        });

        // Jade - Mining Fortune
        register(GemstoneType.JADE, new double[][]{
                {2, 4, 6, 8, 10, 14},
                {4, 6, 8, 12, 16, 20},
                {6, 8, 12, 16, 22, 28},
                {8, 12, 16, 22, 28, 36},
                {12, 16, 22, 28, 36, 44}
        });

        // Amethyst - Defense
        register(GemstoneType.AMETHYST, new double[][]{
                {1, 2, 3, 4, 5, 6},
                {2, 3, 5, 7, 9, 12},
                {4, 6, 8, 10, 14, 18},
                {6, 8, 12, 16, 20, 24},
                {8, 10, 16, 22, 26, 30}
        });

        // Topaz - Pristine
        register(GemstoneType.TOPAZ, new double[][]{
                {0.4, 0.4, 0.4, 0.4, 0.4, 0.4},
                {0.8, 0.8, 0.8, 0.8, 0.8, 0.8},
                {1.2, 1.2, 1.2, 1.2, 1.2, 1.2},
                {1.6, 1.6, 1.6, 1.6, 1.6, 1.6},
                {2.0, 2.0, 2.0, 2.0, 2.0, 2.0}
        });

        // Jasper - Strength
        register(GemstoneType.JASPER, new double[][]{
                {1, 2, 3, 4, 5, 6},
                {2, 3, 5, 7, 9, 12},
                {4, 6, 8, 10, 14, 18},
                {6, 8, 12, 16, 20, 24},
                {8, 10, 16, 22, 26, 30}
        });

        // Opal - True Defense
        register(GemstoneType.OPAL, new double[][]{
                {1, 2, 3, 4, 5, 6},
                {2, 3, 5, 7, 9, 12},
                {4, 6, 8, 10, 14, 18},
                {6, 8, 12, 16, 20, 24},
                {8, 10, 16, 22, 26, 30}
        });

        // Aquamarine - Sea Creature Chance
        register(GemstoneType.AQUAMARINE, new double[][]{
                {0.2, 0.3, 0.4, 0.5, 0.6, 0.8},
                {0.4, 0.5, 0.6, 0.8, 1.0, 1.2},
                {0.6, 0.8, 1.0, 1.2, 1.4, 1.8},
                {0.8, 1.0, 1.2, 1.6, 2.0, 2.4},
                {1.0, 1.2, 1.6, 2.0, 2.4, 3.0}
        });

        // Citrine - Foraging Fortune
        register(GemstoneType.CITRINE, new double[][]{
                {1, 2, 3, 4, 5, 6},
                {2, 3, 5, 7, 9, 12},
                {4, 6, 8, 10, 14, 18},
                {6, 8, 12, 16, 20, 24},
                {8, 10, 16, 22, 26, 30}
        });

        // Onyx - Crit Damage
        register(GemstoneType.ONYX, new double[][]{
                {1, 2, 3, 4, 5, 6},
                {2, 3, 5, 7, 9, 12},
                {4, 6, 8, 10, 14, 18},
                {6, 8, 12, 16, 20, 24},
                {8, 10, 16, 22, 26, 30}
        });

        // Peridot - Farming Fortune
        register(GemstoneType.PERIDOT, new double[][]{
                {2, 4, 6, 8, 10, 14},
                {4, 6, 8, 12, 16, 20},
                {6, 8, 12, 16, 22, 28},
                {8, 12, 16, 22, 28, 36},
                {12, 16, 22, 28, 36, 44}
        });
    }

    private static void register(GemstoneType type, double[][] values) {
        TABLES.put(type, values);
    }

    /**
     * Get the stat bonus for a gemstone of the given type, quality, and item rarity.
     *
     * @return the stat bonus value, or 0 if no data
     */
    public static double getStatValue(GemstoneType type, GemstoneQuality quality, Rarity itemRarity) {
        double[][] table = TABLES.get(type);
        if (table == null) return 0;

        int qualityIndex = quality.ordinal();
        int rarityIndex = rarityToIndex(itemRarity);

        if (qualityIndex >= table.length) return 0;
        if (rarityIndex >= table[qualityIndex].length) return 0;

        return table[qualityIndex][rarityIndex];
    }

    private static int rarityToIndex(Rarity rarity) {
        return switch (rarity) {
            case COMMON -> 0;
            case UNCOMMON -> 1;
            case RARE -> 2;
            case EPIC -> 3;
            case LEGENDARY -> 4;
            case MYTHIC, DIVINE, SPECIAL, VERY_SPECIAL -> 5;
            case ULTIMATE -> 0;
            case ADMIN -> 0;
        };
    }
}
