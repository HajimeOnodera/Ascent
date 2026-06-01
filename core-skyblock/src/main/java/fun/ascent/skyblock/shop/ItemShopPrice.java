package fun.ascent.skyblock.shop;

import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.item.ItemNBT;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;

import java.util.List;

public class ItemShopPrice implements ShopPrice {

    private final SkyblockItem item;
    private final int amount;

    public ItemShopPrice(SkyblockItem item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    @Override
    public List<Component> getGUIDisplay() {
        return List.of(StringUtility.text(item.getRarity().getColor() + item.getDisplayName() + " <dark_gray>x" + amount));
    }

    @Override
    public String getNamePlural() {
        return item.getDisplayName();
    }

    @Override
    public boolean canAfford(SkyblockPlayer player) {
        return count(player, item) >= amount;
    }

    @Override
    public void processPurchase(SkyblockPlayer player) {
        remove(player, item, amount);
    }

    @Override
    public ShopPrice multiply(int amount) {
        return new ItemShopPrice(item, this.amount * amount);
    }

    @Override
    public ShopPrice divide(double amount) {
        return new ItemShopPrice(item, Math.max((int) (this.amount / amount), 1));
    }

    private static int count(SkyblockPlayer player, SkyblockItem target) {
        int count = 0;
        for (int slot = 0; slot < player.getInventory().getSize(); slot++) {
            ItemStack stack = player.getInventory().getItemStack(slot);
            if (matches(stack, target)) {
                count += stack.amount();
            }
        }
        return count;
    }

    private static void remove(SkyblockPlayer player, SkyblockItem target, int amount) {
        int remaining = amount;
        for (int slot = 0; slot < player.getInventory().getSize() && remaining > 0; slot++) {
            ItemStack stack = player.getInventory().getItemStack(slot);
            if (!matches(stack, target)) continue;

            int taken = Math.min(stack.amount(), remaining);
            int newAmount = stack.amount() - taken;
            player.getInventory().setItemStack(slot, newAmount <= 0 ? ItemStack.AIR : stack.withAmount(newAmount));
            remaining -= taken;
        }
    }

    private static boolean matches(ItemStack stack, SkyblockItem target) {
        if (stack == null || stack.isAir() || target == null) return false;
        String stackId = ItemNBT.getItemId(stack);
        if (stackId != null) {
            return stackId.equalsIgnoreCase(target.getItemId());
        }
        return stack.material() == target.getMaterial();
    }
}
