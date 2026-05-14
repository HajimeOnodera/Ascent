package fun.ascent.skyblock.crafting.gui;

import fun.ascent.skyblock.crafting.RecipeRegistry;
import fun.ascent.skyblock.crafting.SkyblockRecipe;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;

import java.util.ArrayList;
import java.util.List;

import static fun.ascent.common.StringUtility.text;

public class CraftingMenu {

    private static final int[] GRID_SLOTS = {10, 11, 12, 19, 20, 21, 28, 29, 30};
    private static final int RESULT_SLOT = 24;
    private static final int CLOSE_SLOT = 49;

    public static void open(SkyblockPlayer player) {
        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, text("<grey>Crafting Table"));

        // Fill background
        ItemStack filler = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE).customName(Component.empty()).build();
        for (int i = 0; i < 54; i++) {
            inv.setItemStack(i, filler);
        }

        // Clear grid slots and result slot
        for (int slot : GRID_SLOTS) inv.setItemStack(slot, ItemStack.AIR);
        inv.setItemStack(RESULT_SLOT, ItemStack.AIR);

        inv.setItemStack(CLOSE_SLOT, ItemStack.builder(Material.BARRIER).customName(text("<red>Close")).build());

        inv.eventNode().addListener(InventoryPreClickEvent.class, event -> handleClick(event, player, inv));
        inv.eventNode().addListener(InventoryCloseEvent.class, event -> {
            for (int slot : GRID_SLOTS) {
                ItemStack stack = inv.getItemStack(slot);
                if (!stack.isAir()) {
                    player.getInventory().addItemStack(stack);
                    inv.setItemStack(slot, ItemStack.AIR);
                }
            }
        });

        player.openInventory(inv);
    }

    private static void handleClick(InventoryPreClickEvent event, SkyblockPlayer player, Inventory inv) {
        int slot = event.getSlot();

        if (slot == CLOSE_SLOT) {
            event.setCancelled(true);
            player.closeInventory();
            return;
        }

        // If clicking background, cancel
        if (event.getClickedItem().material() == Material.GRAY_STAINED_GLASS_PANE) {
            event.setCancelled(true);
            return;
        }

        // If clicking result slot
        if (slot == RESULT_SLOT) {
            ItemStack result = inv.getItemStack(RESULT_SLOT);
            if (result.isAir()) {
                event.setCancelled(true);
                return;
            }

            SkyblockRecipe recipe = findCurrentRecipe(inv);
            if (recipe != null) {
                SkyblockRecipe.CraftingResult craftResult = recipe.getCanCraft().apply(player);
                if (!craftResult.allowed()) {
                    player.sendMessage("§c" + craftResult.errorMessage());
                    event.setCancelled(true);
                    return;
                }
                
                // Give result and consume grid
                player.getInventory().addItemStack(result);
                recipe.consume(inv, GRID_SLOTS);
            }

            event.setCancelled(true);
            updateResult(inv, player);
            return;
        }

        MinecraftServer.getSchedulerManager().buildTask(() -> updateResult(inv, player)).delay(TaskSchedule.nextTick()).schedule();
    }

    private static void updateResult(Inventory inv, SkyblockPlayer player) {
        SkyblockRecipe recipe = findCurrentRecipe(inv);
        if (recipe == null) {
            inv.setItemStack(RESULT_SLOT, ItemStack.AIR);
            return;
        }

        SkyblockItem resultItem = ItemRegistry.getItem(recipe.getResultItemId());
        if (resultItem == null) return;

        ItemStack resultStack = resultItem.buildItemStack(player).withAmount(recipe.getResultAmount());
        
        // Add "Click to craft" lore or lock warning
        List<Component> lore = new ArrayList<>(resultStack.get(DataComponents.LORE, List.of()));
        lore.add(Component.empty());
        
        SkyblockRecipe.CraftingResult craftResult = recipe.getCanCraft().apply(player);
        if (craftResult.allowed()) {
            lore.add(text("<yellow>Click to craft!"));
        } else {
            lore.add(text("<red>Recipe Locked!"));
            lore.add(text("<grey>" + craftResult.errorMessage()));
        }
        
        inv.setItemStack(RESULT_SLOT, resultStack.withLore(lore));
    }

    private static SkyblockRecipe findCurrentRecipe(Inventory inv) {
        ItemStack[] grid = new ItemStack[9];
        for (int i = 0; i < 9; i++) {
            grid[i] = inv.getItemStack(GRID_SLOTS[i]);
        }
        return RecipeRegistry.findMatch(grid);
    }
}
