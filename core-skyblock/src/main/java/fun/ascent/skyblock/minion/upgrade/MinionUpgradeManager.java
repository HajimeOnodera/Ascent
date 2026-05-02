package fun.ascent.skyblock.minion;

import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public final class MinionUpgradeManager {
    private MinionUpgradeManager() {
    }

    public static MinionUpgradeCost getUpgradeCost(SkyblockMinion minion) {
        return new MinionUpgradeCost(minion.getProfile().upgradeMaterial(), minion.getType().getUpgradeCost(minion.getTier()));
    }

    public static boolean canAfford(SkyblockPlayer player, SkyblockMinion minion) {
        if (player == null) {
            return false;
        }
        MinionUpgradeCost cost = getUpgradeCost(minion);
        if (cost == null) {
            return false;
        }
        return count(player.getInventory(), cost.material()) >= cost.amount();
    }

    public static boolean tryUpgrade(SkyblockPlayer player, SkyblockMinion minion) {
        if (!minion.canUpgrade()) {
            return false;
        }
        MinionUpgradeCost cost = getUpgradeCost(minion);
        if (cost == null || !remove(player.getInventory(), cost.material(), cost.amount())) {
            return false;
        }
        minion.upgrade();
        return true;
    }

    private static int count(PlayerInventory inventory, Material material) {
        int total = 0;
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack stack = inventory.getItemStack(slot);
            if (stack.material() == material) {
                total += stack.amount();
            }
        }
        return total;
    }

    private static boolean remove(PlayerInventory inventory, Material material, int amount) {
        int remaining = amount;
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack stack = inventory.getItemStack(slot);
            if (stack.material() != material) {
                continue;
            }
            int taken = Math.min(stack.amount(), remaining);
            int nextAmount = stack.amount() - taken;
            inventory.setItemStack(slot, nextAmount <= 0 ? ItemStack.AIR : stack.withAmount(nextAmount));
            remaining -= taken;
            if (remaining <= 0) {
                return true;
            }
        }
        return false;
    }
}
