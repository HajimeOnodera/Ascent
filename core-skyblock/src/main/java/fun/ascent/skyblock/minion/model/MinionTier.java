package fun.ascent.skyblock.minion.model;

public record MinionTier(int tier, int actionDelaySeconds, int storageSlots) {
    public int maxStorage() {
        return storageSlots * 64;
    }
}
