package fun.ascent.skyblock.item.gui;

import fun.ascent.common.gui.InventoryGUI;
import fun.ascent.common.item.GUIClickableItem;
import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.skyblock.item.ItemType;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class ItemCategoryGUI extends InventoryGUI {

    public ItemCategoryGUI() {
        super("Item Browser - Categories", InventoryType.CHEST_3_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        // Weapons
        addCategory(10, "Weapons", Material.DIAMOND_SWORD, "View all weapons", ItemType.SWORD);
        
        // Armor
        addCategory(11, "Armor", Material.DIAMOND_CHESTPLATE, "View all armor pieces", ItemType.CHESTPLATE);
        
        // Tools
        addCategory(12, "Tools", Material.DIAMOND_PICKAXE, "View all tools and drills", ItemType.PICKAXE);
        
        // Accessories
        addCategory(13, "Accessories", Material.GOLD_NUGGET, "View all accessories and talismans", ItemType.ACCESSORY);
        
        // Equipment
        addCategory(14, "Equipment", Material.LEATHER, "View all equipment pieces", ItemType.BELT);
        
        // Consumables
        addCategory(15, "Consumables", Material.APPLE, "View all consumables and baits", ItemType.BAIT);
        
        // Others
        addCategory(16, "Others", Material.CHEST, "View all other items", ItemType.NONE);

        updateItemStacks(getInventory(), e.player());
    }

    private void addCategory(int slot, String name, Material icon, String lore, ItemType sampleType) {
        set(new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, Player player) {
                new ItemListGUI(name, sampleType).open(player);
            }

            @Override
            public ItemStack.Builder getItem(Player p) {
                return ItemStackCreator.getStack("<green>" + name, icon, 1, "<gray>" + lore);
            }
        });
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
