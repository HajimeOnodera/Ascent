package fun.ascent.skyblock.minion;

public final class MinionData {
    private final MinionType type;
    private final MinionTier tier;

    public MinionData(MinionType type, int tier) {
        this.type = type;
        this.tier = type.getTierData(tier);
    }

    public MinionType getType() {
        return type;
    }

    public int getTier() {
        return tier.tier();
    }

    public int getActionDelaySeconds() {
        return tier.actionDelaySeconds();
    }

    public int getStorageSlots() {
        return tier.storageSlots();
    }

    public int getMaxStorage() {
        return tier.maxStorage();
    }
}
