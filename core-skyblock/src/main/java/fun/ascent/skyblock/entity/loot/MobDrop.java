package fun.ascent.skyblock.entity.loot;

import net.minestom.server.item.ItemStack;

public record MobDrop(ItemStack item, double chance, int minAmount, int maxAmount) {

    public int rolledAmount() {
        if (minAmount == maxAmount) return minAmount;
        return minAmount + (int) (Math.random() * (maxAmount - minAmount + 1));
    }

    public boolean rolls() {
        return Math.random() * 100 < chance;
    }
}