package fun.ascent.skyblock.item.gemstone;

import fun.ascent.skyblock.item.Rarity;

public enum GemstoneQuality {
    ROUGH(Rarity.COMMON, 100),
    FLAWED(Rarity.UNCOMMON, 1000),
    FINE(Rarity.RARE, 10_000),
    FLAWLESS(Rarity.EPIC, 100_000),
    PERFECT(Rarity.LEGENDARY, 500_000);

    private final Rarity rarity;
    private final int removalCost;

    GemstoneQuality(Rarity rarity, int removalCost) {
        this.rarity = rarity;
        this.removalCost = removalCost;
    }

    public Rarity getRarity() { return rarity; }
    public int getRemovalCost() { return removalCost; }
}
