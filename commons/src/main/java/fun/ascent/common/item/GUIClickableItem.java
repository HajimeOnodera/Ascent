package fun.ascent.common.item;

import fun.ascent.common.gui.GUIItem;
import fun.ascent.common.gui.InventoryGUI;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public abstract class GUIClickableItem extends GUIItem {
    public GUIClickableItem(int slot) {
        super(slot);
    }

    public abstract void run(InventoryPreClickEvent e, Player player);

    public void runPost(InventoryClickEvent e, Player player) {}

    public static GUIClickableItem getCloseItem(int slot) {
        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                player.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                return ItemStackCreator.createNamedItemStack(Material.BARRIER, "<red>Close");
            }
        };
    }

    public static GUIClickableItem getGoBackItem(int slot, InventoryGUI gui) {
        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                gui.open(player);
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                return ItemStackCreator.getStack("<green>Go Back",
                        Material.ARROW, 1, "<gray>To " + gui.getTitle());
            }
        };
    }

    static GUIClickableItem createGUIOpenerItem(InventoryGUI gui, String name, int slot, Material type, String... lore) {
        return new GUIClickableItem(slot) {
            @Override
            public ItemStack.Builder getItem(Player player) {
                return ItemStackCreator.getStack(name, type, 1, lore);
            }

            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                if (gui == null) return;
                gui.open(player);
            }
        };
    }
}

