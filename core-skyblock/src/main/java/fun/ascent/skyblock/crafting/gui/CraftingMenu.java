package fun.ascent.skyblock.crafting.gui;

import fun.ascent.skyblock.crafting.RecipeRegistry;
import fun.ascent.skyblock.crafting.SkyblockRecipe;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.events.PlayerCraftItemEvent;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.sound.SoundEvent;

import java.util.ArrayList;
import java.util.List;

import static fun.ascent.common.StringUtility.text;
import static net.kyori.adventure.sound.Sound.*;

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
        Click click = event.getClick();

        // 1. Block drag clicks completely while GUI is open to prevent drag-painting exploits
        if (click instanceof Click.Drag) {
            event.setCancelled(true);
            return;
        }

        // 2. Block double-clicking completely to prevent pulling items/duplication
        if (click instanceof Click.Double) {
            event.setCancelled(true);
            return;
        }

        int slot = event.getSlot();

        if (slot == CLOSE_SLOT) {
            event.setCancelled(true);
            player.closeInventory();
            return;
        }

        boolean isTopInventory = slot < inv.getSize() && event.getInventory() == inv;

        // 3. Block hotbar swapping (number-key swaps) and offhand swapping in the top inventory
        if (isTopInventory && (click instanceof Click.HotbarSwap || click instanceof Click.OffhandSwap)) {
            event.setCancelled(true);
            return;
        }

        // 4. Block drop clicks (Q/Ctrl+Q) in the top inventory
        if (isTopInventory && (click instanceof Click.DropSlot || click instanceof Click.DropCursor)) {
            event.setCancelled(true);
            return;
        }

        // If clicking top inventory background slots (slots < 54), cancel
        if (isTopInventory && !isGridSlot(slot) && slot != RESULT_SLOT) {
            event.setCancelled(true);
            return;
        }

        // Handle clicks in bottom inventory (player inventory)
        boolean isBottomClick = (event.getInventory() instanceof net.minestom.server.inventory.PlayerInventory) || (slot >= inv.getSize());
        if (isBottomClick) {
            boolean isShift = event.getClick() instanceof Click.LeftShift || event.getClick() instanceof Click.RightShift;
            if (isShift) {
                event.setCancelled(true);
                ItemStack toDistribute = event.getClickedItem();
                if (!toDistribute.isAir()) {
                    ItemStack remaining = distributeToGrid(inv, toDistribute);
                    int playerSlot = (event.getInventory() instanceof net.minestom.server.inventory.PlayerInventory) ? slot : (slot - inv.getSize());
                    player.getInventory().setItemStack(playerSlot, remaining);
                    updateResult(inv, player);
                }
            } else {
                // Let normal click pass so they can interact with their inventory, but update result next tick
                MinecraftServer.getSchedulerManager().scheduleNextTick(() -> updateResult(inv, player));
            }
            return;
        }

        // Handle shift click on grid slots (to return items back to player inventory)
        boolean isGridShift = event.getClick() instanceof Click.LeftShift || event.getClick() instanceof Click.RightShift;
        if (isGridSlot(slot) && isGridShift) {
            event.setCancelled(true);
            ItemStack toReturn = inv.getItemStack(slot);
            if (!toReturn.isAir()) {
                if (player.getInventory().addItemStack(toReturn)) {
                    inv.setItemStack(slot, ItemStack.AIR);
                }
                updateResult(inv, player);
            }
            return;
        }

        // Handle click on result slot
        if (slot == RESULT_SLOT) {
            event.setCancelled(true);
            ItemStack result = inv.getItemStack(RESULT_SLOT);
            if (result.isAir()) {
                return;
            }

            SkyblockRecipe recipe = findCurrentRecipe(inv);
            if (recipe == null) {
                return;
            }

            SkyblockRecipe.CraftingResult craftResult = recipe.getCanCraft().apply(player);
            if (!craftResult.allowed()) {
                player.sendMessage("§c" + craftResult.errorMessage());
                player.playSound(sound(SoundEvent.ENTITY_VILLAGER_NO, Source.MASTER, 1f, 1f));
                return;
            }

            ItemStack craftedItem;
            SkyblockItem resultItem = ItemRegistry.getItem(recipe.getResultItemId());
            if (resultItem != null) {
                craftedItem = resultItem.buildItemStack(player).withAmount(recipe.getResultAmount());
            } else {
                Material material = Material.fromKey("minecraft:" + recipe.getResultItemId().toLowerCase());
                if (material == null) {
                    material = Material.fromKey(recipe.getResultItemId().toLowerCase());
                }
                if (material == null || material == Material.AIR) {
                    return;
                }
                craftedItem = ItemStack.of(material, recipe.getResultAmount());
            }

            boolean isShift = event.getClick() instanceof Click.LeftShift || event.getClick() instanceof Click.RightShift;
            if (isShift) {
                int maxCraftsByInventory = getMaxCraftsByInventory(player, craftedItem, recipe.getResultAmount());
                if (maxCraftsByInventory <= 0) {
                    player.sendMessage("§cYour inventory is full!");
                    return;
                }

                int craftedCount = 0;
                while (craftedCount < maxCraftsByInventory) {
                    ItemStack[] currentGrid = new ItemStack[9];
                    for (int i = 0; i < 9; i++) {
                        currentGrid[i] = inv.getItemStack(GRID_SLOTS[i]);
                    }
                    if (!recipe.matches(currentGrid)) {
                        break;
                    }

                    SkyblockRecipe.CraftingResult tickCraftResult = recipe.getCanCraft().apply(player);
                    if (!tickCraftResult.allowed()) {
                        break;
                    }

                    recipe.consume(inv, GRID_SLOTS);
                    craftedCount++;
                }

                if (craftedCount > 0) {
                    int totalAmount = craftedCount * recipe.getResultAmount();
                    ItemStack finalOutput = craftedItem.withAmount(totalAmount);
                    player.getInventory().addItemStack(finalOutput);
                    MinecraftServer.getGlobalEventHandler().call(new PlayerCraftItemEvent(player, finalOutput));
                    player.playSound(sound(SoundEvent.BLOCK_NOTE_BLOCK_PLING, Source.MASTER, 1f, 1.2f));
                }
            } else {
                ItemStack cursorItem = player.getInventory().getCursorItem();
                if (!cursorItem.isAir()) {
                    if (!cursorItem.isSimilar(craftedItem)) {
                        return;
                    }
                    int newAmount = cursorItem.amount() + craftedItem.amount();
                    if (newAmount > cursorItem.material().maxStackSize()) {
                        return;
                    }
                    recipe.consume(inv, GRID_SLOTS);
                    player.getInventory().setCursorItem(cursorItem.withAmount(newAmount));
                } else {
                    recipe.consume(inv, GRID_SLOTS);
                    player.getInventory().setCursorItem(craftedItem);
                }
                MinecraftServer.getGlobalEventHandler().call(new PlayerCraftItemEvent(player, craftedItem));
                player.playSound(sound(SoundEvent.BLOCK_NOTE_BLOCK_PLING, Source.MASTER, 1f, 1.2f));
            }

            updateResult(inv, player);
            return;
        }

        // For grid slot clicks, update results after the click is processed
        MinecraftServer.getSchedulerManager().scheduleNextTick(() -> updateResult(inv, player));
    }

    private static void updateResult(Inventory inv, SkyblockPlayer player) {
        SkyblockRecipe recipe = findCurrentRecipe(inv);
        if (recipe == null) {
            inv.setItemStack(RESULT_SLOT, ItemStack.AIR);
            return;
        }

        ItemStack resultStack;
        SkyblockItem resultItem = ItemRegistry.getItem(recipe.getResultItemId());
        if (resultItem != null) {
            resultStack = resultItem.buildItemStack(player).withAmount(recipe.getResultAmount());
        } else {
            Material material = Material.fromKey("minecraft:" + recipe.getResultItemId().toLowerCase());
            if (material == null) {
                material = Material.fromKey(recipe.getResultItemId().toLowerCase());
            }
            if (material == null || material == Material.AIR) {
                inv.setItemStack(RESULT_SLOT, ItemStack.AIR);
                return;
            }
            resultStack = ItemStack.of(material, recipe.getResultAmount());
        }

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

    private static boolean isGridSlot(int slot) {
        for (int gridSlot : GRID_SLOTS) {
            if (gridSlot == slot) {
                return true;
            }
        }
        return false;
    }

    private static ItemStack distributeToGrid(Inventory inv, ItemStack toDistribute) {
        if (toDistribute.isAir()) return toDistribute;
        int remainingAmount = toDistribute.amount();

        // Phase 1: Try to merge with existing items of the same type in the grid slots
        for (int slot : GRID_SLOTS) {
            if (remainingAmount <= 0) break;
            ItemStack stack = inv.getItemStack(slot);
            if (!stack.isAir() && stack.isSimilar(toDistribute)) {
                int maxStack = stack.material().maxStackSize();
                int currentAmount = stack.amount();
                if (currentAmount < maxStack) {
                    int add = Math.min(remainingAmount, maxStack - currentAmount);
                    inv.setItemStack(slot, stack.withAmount(currentAmount + add));
                    remainingAmount -= add;
                }
            }
        }

        // Phase 2: Place in first empty grid slot
        for (int slot : GRID_SLOTS) {
            if (remainingAmount <= 0) break;
            ItemStack stack = inv.getItemStack(slot);
            if (stack.isAir()) {
                int maxStack = toDistribute.material().maxStackSize();
                int add = Math.min(remainingAmount, maxStack);
                inv.setItemStack(slot, toDistribute.withAmount(add));
                remainingAmount -= add;
            }
        }

        if (remainingAmount <= 0) {
            return ItemStack.AIR;
        } else {
            return toDistribute.withAmount(remainingAmount);
        }
    }

    private static int getMaxCraftsByInventory(SkyblockPlayer player, ItemStack craftedItem, int amountPerCraft) {
        if (amountPerCraft <= 0) return 0;
        int maxStack = craftedItem.material().maxStackSize();
        int availableSpace = 0;

        for (int slot = 0; slot < 36; slot++) {
            ItemStack stack = player.getInventory().getItemStack(slot);
            if (stack.isAir()) {
                availableSpace += maxStack;
            } else if (stack.isSimilar(craftedItem)) {
                availableSpace += Math.max(0, maxStack - stack.amount());
            }
        }
        return availableSpace / amountPerCraft;
    }
}
