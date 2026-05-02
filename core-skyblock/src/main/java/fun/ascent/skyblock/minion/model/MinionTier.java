package fun.ascent.skyblock.minion;

public record MinionTier(int tier, int actionDelaySeconds, int storageSlots) {
    public int maxStorage() {
        return storageSlots * 64;
    }
}
