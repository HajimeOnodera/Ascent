package fun.ascent.skyblock.player.bestiary;

public final class BestiaryMilestones {

    private static final int[][] BRACKETS = {
            {20, 40, 60, 100, 200, 400, 800, 1400, 2000, 3000, 6000, 12000, 20000, 30000, 40000, 50000, 60000, 72000, 86000, 100000, 200000, 400000, 600000, 800000, 1000000},
            {5, 10, 15, 25, 50, 100, 200, 350, 500, 750, 1500, 3000, 5000, 7500, 10000, 12500, 15000, 18000, 21500, 25000, 50000, 100000, 150000, 200000, 250000},
            {4, 8, 12, 16, 20, 40, 80, 140, 200, 300, 600, 1200, 2000, 3000, 4000, 5000, 6000, 7200, 8600, 10000, 20000, 40000, 60000, 80000, 100000},
            {2, 4, 6, 10, 15, 20, 25, 35, 50, 75, 150, 300, 500, 750, 1000, 1350, 1650, 2000, 2500, 3000, 5000, 10000, 15000, 20000, 25000},
            {1, 2, 3, 5, 7, 10, 15, 20, 25, 30, 60, 120, 200, 300, 400, 500, 600, 720, 860, 1000, 2000, 4000, 6000, 8000, 10000},
            {1, 2, 3, 5, 7, 9, 14, 17, 21, 25, 50, 80, 125, 175, 250, 325, 425, 525, 625, 750, 1500, 3000, 4500, 6000, 7500},
            {1, 2, 3, 5, 7, 9, 11, 14, 17, 20, 30, 40, 55, 75, 100, 150, 200, 275, 375, 500, 1000, 1500, 2000, 2500, 3000}
    };

    private enum RewardTier {
        TIER_1(2, 2, 2, 50),
        TIER_2(4, 4, 4, 100),
        TIER_3(6, 6, 6, 150),
        TIER_4(8, 8, 8, 200),
        TIER_5(10, 10, 10, 250),
        TIER_6(13, 13, 12, 300),
        TIER_7(16, 16, 14, 350),
        TIER_8(19, 19, 16, 400),
        TIER_9(22, 22, 18, 450),
        TIER_10(25, 25, 20, 500),
        TIER_11(29, 29, 22, 550),
        TIER_12(33, 33, 24, 600),
        TIER_13(37, 37, 26, 650),
        TIER_14(41, 41, 28, 700),
        TIER_15(45, 45, 30, 750),
        TIER_16(50, 50, 32, 800),
        TIER_17(55, 55, 34, 850),
        TIER_18(60, 60, 36, 900),
        TIER_19(65, 65, 38, 950),
        TIER_20(70, 70, 40, 1000),
        TIER_21(76, 76, 42, 1050),
        TIER_22(82, 82, 44, 1100),
        TIER_23(88, 88, 46, 1150),
        TIER_24(94, 94, 48, 1200),
        TIER_25(100, 100, 50, 1250);

        private final int magicFind;
        private final int strength;
        private final int coinGain;
        private final int extraXpOrbChance;

        RewardTier(int magicFind, int strength, int coinGain, int extraXpOrbChance) {
            this.magicFind = magicFind;
            this.strength = strength;
            this.coinGain = coinGain;
            this.extraXpOrbChance = extraXpOrbChance;
        }
    }

    private BestiaryMilestones() {
    }

    public static int currentTier(BestiaryFamily family, int kills) {
        int[] thresholds = thresholds(family);
        for (int tier = family.maxTier() - 1; tier >= 0; tier--) {
            if (kills >= thresholds[tier]) {
                return tier + 1;
            }
        }
        return 0;
    }

    public static int maxKills(BestiaryFamily family) {
        return thresholds(family)[family.maxTier() - 1];
    }

    public static int killsIntoCurrentTier(BestiaryFamily family, int kills) {
        int tier = currentTier(family, kills);
        if (tier >= family.maxTier()) {
            return 0;
        }
        int[] thresholds = thresholds(family);
        int previousThreshold = tier == 0 ? 0 : thresholds[tier - 1];
        return Math.max(0, kills - previousThreshold);
    }

    public static int killsForNextTier(BestiaryFamily family, int kills) {
        int tier = currentTier(family, kills);
        if (tier >= family.maxTier()) {
            return 0;
        }
        int[] thresholds = thresholds(family);
        int previousThreshold = tier == 0 ? 0 : thresholds[tier - 1];
        return thresholds[tier] - previousThreshold;
    }

    public static int magicFindGain(int tier) {
        if (tier < 1 || tier > RewardTier.values().length) {
            return 0;
        }
        if (tier == 1) {
            return RewardTier.TIER_1.magicFind;
        }
        return RewardTier.values()[tier - 1].magicFind - RewardTier.values()[tier - 2].magicFind;
    }

    public static int strengthGain(int tier) {
        if (tier < 1 || tier > RewardTier.values().length) {
            return 0;
        }
        if (tier == 1) {
            return RewardTier.TIER_1.strength;
        }
        return RewardTier.values()[tier - 1].strength - RewardTier.values()[tier - 2].strength;
    }

    public static int coinGainBonus(int tier) {
        if (tier < 1 || tier > RewardTier.values().length) {
            return 0;
        }
        if (tier == 1) {
            return RewardTier.TIER_1.coinGain;
        }
        return RewardTier.values()[tier - 1].coinGain - RewardTier.values()[tier - 2].coinGain;
    }

    public static int extraXpOrbChanceBonus(int tier) {
        if (tier < 1 || tier > RewardTier.values().length) {
            return 0;
        }
        if (tier == 1) {
            return RewardTier.TIER_1.extraXpOrbChance;
        }
        return RewardTier.values()[tier - 1].extraXpOrbChance - RewardTier.values()[tier - 2].extraXpOrbChance;
    }

    public static int totalMagicFind(int tier) {
        if (tier < 1 || tier > RewardTier.values().length) {
            return 0;
        }
        return RewardTier.values()[tier - 1].magicFind;
    }

    public static int totalStrength(int tier) {
        if (tier < 1 || tier > RewardTier.values().length) {
            return 0;
        }
        return RewardTier.values()[tier - 1].strength;
    }

    public static int totalCoinGain(int tier) {
        if (tier < 1 || tier > RewardTier.values().length) {
            return 0;
        }
        return RewardTier.values()[tier - 1].coinGain;
    }

    public static int totalExtraXpOrbChance(int tier) {
        if (tier < 1 || tier > RewardTier.values().length) {
            return 0;
        }
        return RewardTier.values()[tier - 1].extraXpOrbChance;
    }

    private static int[] thresholds(BestiaryFamily family) {
        return BRACKETS[family.bracket() - 1];
    }
}
