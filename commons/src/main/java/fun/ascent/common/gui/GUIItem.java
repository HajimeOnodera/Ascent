package fun.ascent.common.gui;

import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;

public abstract class GUIItem {
    public final int itemSlot;
    public abstract ItemStack.Builder getItem(Player player);

    public GUIItem(int slot) {
        this.itemSlot = slot;
    }

    public boolean canPickup() {
        return false;
    }
}
