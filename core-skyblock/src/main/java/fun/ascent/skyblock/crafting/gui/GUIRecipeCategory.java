package fun.ascent.skyblock.crafting.gui;

import fun.ascent.skyblock.crafting.RecipeRegistry;
import fun.ascent.skyblock.crafting.SkyblockRecipe;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

import static fun.ascent.common.StringUtility.text;

public class GUIRecipeCategory {

    public static void open(SkyblockPlayer player, SkyblockRecipe.RecipeType type) {
        String title = type.name().substring(0, 1).toUpperCase() + type.name().substring(1).toLowerCase() + " Recipes";
        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, text("<grey>" + title));

        // Fill background
        ItemStack filler = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE).customName(Component.empty()).build();
        for (int i = 0; i < 54; i++) {
            inv.setItemStack(i, filler);
        }

        List<SkyblockRecipe> recipes = RecipeRegistry.getByType(type);
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

        for (int i = 0; i < recipes.size() && i < slots.length; i++) {
            SkyblockRecipe recipe = recipes.get(i);
            int slot = slots[i];

            SkyblockItem result = ItemRegistry.getItem(recipe.getResultItemId());
            if (result == null) continue;

            boolean unlocked = player.getActiveProfile().unlockedRecipes.contains(recipe.getId());
            ItemStack icon;

            if (unlocked) {
                icon = result.buildItemStack(player).withAmount(recipe.getResultAmount());
                List<Component> lore = new ArrayList<>(icon.get(DataComponents.LORE, List.of()));
                lore.add(Component.empty());
                lore.add(text("<yellow>Click to view recipe!"));
                icon = icon.withLore(lore);
            } else {
                icon = ItemStack.builder(Material.BARRIER)
                        .customName(text("<red>Recipe Locked!"))
                        .lore(text("<gray>Unlock this recipe in collections"), text("<gray>to view the crafting pattern."))
                        .build();
            }

            inv.setItemStack(slot, icon);
        }

        inv.setItemStack(49, ItemStack.builder(Material.BARRIER).customName(text("<red>Close")).build());
        inv.setItemStack(48, ItemStack.builder(Material.ARROW).customName(text("<green>Go Back")).build());

        inv.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            int slot = event.getSlot();

            if (slot == 49) {
                player.closeInventory();
                return;
            }
            
            if (slot == 48) {
                GUIRecipeBook.open(player);
                return;
            }

            for (int i = 0; i < recipes.size() && i < slots.length; i++) {
                if (slot == slots[i]) {
                    SkyblockRecipe recipe = recipes.get(i);
                    if (player.getActiveProfile().unlockedRecipes.contains(recipe.getId())) {
                        GUIRecipeView.open(player, recipe);
                    } else {
                        player.sendMessage("§cYou haven't unlocked this recipe yet!");
                    }
                    return;
                }
            }
        });

        player.openInventory(inv);
    }
}
