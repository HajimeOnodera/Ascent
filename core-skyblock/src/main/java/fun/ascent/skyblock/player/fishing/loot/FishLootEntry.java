package fun.ascent.skyblock.player.fishing.loot;

import net.minestom.server.item.Material;

import java.util.concurrent.ThreadLocalRandom;

public record FishLootEntry(
        Material material,
        String displayName,
        int weight,
        double xpReward,
        int minimumLevel,
        int minAmount,
        int maxAmount
) {

    public int rollAmount() {
        if (minAmount >= maxAmount) return minAmount;
        return ThreadLocalRandom.current().nextInt(minAmount, maxAmount + 1);
    }
}
