package fun.ascent.skyblock.crafting.gui;

import fun.ascent.skyblock.crafting.SkyblockRecipe;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import static fun.ascent.common.StringUtility.text;

public class GUIRecipeView {

    private static final int[] GRID_SLOTS = {10, 11, 12, 19, 20, 21, 28, 29, 30};
    private static final int RESULT_SLOT = 23;

    public static void open(SkyblockPlayer player, SkyblockRecipe recipe) {
        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, text("<grey>View Recipe"));

        // Fill background
        ItemStack filler = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE).customName(Component.empty()).build();
        for (int i = 0; i < 54; i++) {
            inv.setItemStack(i, filler);
        }

        // Show crafting table background in slots
        ItemStack[] displayGrid = recipe.getDisplayGrid(player);
        for (int i = 0; i < 9; i++) {
            ItemStack stack = displayGrid[i];
            if (stack == null || stack.isAir()) {
                inv.setItemStack(GRID_SLOTS[i], ItemStack.builder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).customName(text("<gray>Grid Slot")).build());
            } else {
                inv.setItemStack(GRID_SLOTS[i], stack);
            }
        }

        // Add result item
        SkyblockItem resultItem = ItemRegistry.getItem(recipe.getResultItemId());
        if (resultItem != null) {
            inv.setItemStack(RESULT_SLOT, resultItem.buildItemStack(player).withAmount(recipe.getResultAmount()));
        }

        // Navigation
        inv.setItemStack(49, ItemStack.builder(Material.BARRIER).customName(text("<red>Close")).build());
        inv.setItemStack(48, ItemStack.builder(Material.ARROW).customName(text("<green>Go Back")).build());

        inv.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            int slot = event.getSlot();
            if (slot == 49) player.closeInventory();
            if (slot == 48) GUIRecipeCategory.open(player, recipe.getRecipeType());
        });

        player.openInventory(inv);
    }
}
