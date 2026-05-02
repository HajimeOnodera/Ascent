package fun.ascent.skyblock.minion.model;

public enum MinionCategory {
    MINING(new int[]{14, 14, 12, 12, 10, 10, 9, 9, 8, 8, 7, 7}, new int[]{1, 3, 3, 6, 6, 9, 9, 12, 12, 15, 15, 15}),
    FARMING(new int[]{15, 15, 13, 13, 11, 11, 10, 10, 9, 9, 8, 8}, new int[]{1, 3, 3, 6, 6, 9, 9, 12, 12, 15, 15, 15}),
    FORAGING(new int[]{48, 48, 45, 45, 42, 42, 38, 38, 33, 33, 27, 27}, new int[]{1, 3, 3, 6, 6, 9, 9, 12, 12, 15, 15, 15}),
    COMBAT(new int[]{27, 27, 24, 24, 21, 21, 18, 18, 15, 15, 12, 12}, new int[]{2, 4, 4, 6, 6, 9, 9, 12, 12, 15, 15, 15}),
    FISHING(new int[]{78, 78, 72, 72, 66, 66, 60, 60, 54, 54, 48, 48}, new int[]{2, 4, 4, 6, 6, 9, 9, 12, 12, 15, 15, 15});

    private final int[] delaysByTier;
    private final int[] storageByTier;

    MinionCategory(int[] delaysByTier, int[] storageByTier) {
        this.delaysByTier = delaysByTier;
        this.storageByTier = storageByTier;
    }

    public int getDelaySeconds(int tier) {
        return delaysByTier[index(tier)];
    }

    public int getStorageSlots(int tier) {
        return storageByTier[index(tier)];
    }

    private int index(int tier) {
        return Math.max(0, Math.min(delaysByTier.length - 1, tier - 1));
    }
}
