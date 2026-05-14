package fun.ascent.skyblock.minion.model;

import lombok.Getter;

public final class MinionData {
    @Getter
    private final MinionType type;
    private final MinionTier tier;

    public MinionData(MinionType type, int tier) {
        this.type = type;
        this.tier = type.getTierData(tier);
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
