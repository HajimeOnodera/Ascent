package fun.ascent.skyblock.crafting.gui;

import fun.ascent.skyblock.crafting.RecipeRegistry;
import fun.ascent.skyblock.crafting.SkyblockRecipe;
import fun.ascent.skyblock.menus.SkyblockMenu;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;

import static fun.ascent.common.StringUtility.text;

public class GUIRecipeBook {

    private static final int[] CATEGORY_SLOTS = {20, 21, 22, 23, 24, 29, 30, 31, 32};

    public static void open(SkyblockPlayer player) {
        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, text("<grey>Recipe Book"));

        // Fill background
        ItemStack filler = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE).customName(Component.empty()).build();
        for (int i = 0; i < 54; i++) {
            inv.setItemStack(i, filler);
        }

        SkyblockRecipe.RecipeType[] types = SkyblockRecipe.RecipeType.values();
        for (int i = 0; i < CATEGORY_SLOTS.length && i < types.length; i++) {
            SkyblockRecipe.RecipeType type = types[i];
            if (type == SkyblockRecipe.RecipeType.NONE) continue;

            int slot = CATEGORY_SLOTS[i];
            List<SkyblockRecipe> typeRecipes = RecipeRegistry.getByType(type);
            
            int unlocked = 0;
            for (SkyblockRecipe recipe : typeRecipes) {
                if (player.getActiveProfile().unlockedRecipes.contains(recipe.getId())) {
                    unlocked++;
                }
            }

            double percent = typeRecipes.isEmpty() ? 0 : (unlocked / (double) typeRecipes.size()) * 100;
            String progressBar = getProgressBar(unlocked, typeRecipes.size());

            ItemStack icon = ItemStack.builder(type.getIcon())
                    .customName(text("<green>" + type.name().substring(0, 1).toUpperCase() + type.name().substring(1).toLowerCase() + " Recipes"))
                    .lore(
                            text("<gray>Recipes unlocked: <yellow>" + unlocked + "<gold>/<yellow>" + typeRecipes.size() + " <gold>(<yellow>" + String.format("%.1f", percent) + "%<gold>)"),
                            text(progressBar),
                            Component.empty(),
                            text("<yellow>Click to view!")
                    )
                    .build();

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
            
            // Go back to Skyblock Menu
            if (slot == 48) {
                SkyblockMenu.open(player);
                return;
            }

            for (int i = 0; i < CATEGORY_SLOTS.length; i++) {
                if (slot == CATEGORY_SLOTS[i]) {
                    SkyblockRecipe.RecipeType type = types[i];
                    GUIRecipeCategory.open(player, type);
                    return;
                }
            }
        });

        player.openInventory(inv);
    }

    private static String getProgressBar(int unlocked, int total) {
        if (total == 0) return "<gray><st>─────────────────";
        double ratio = (double) unlocked / total;
        String bar = "─────────────────";
        int completed = (int) (ratio * bar.length());
        return "<dark_green><st>" + bar.substring(0, completed) + "<gray><st>" + bar.substring(completed) + " <yellow>" + unlocked + "<gold>/<yellow>" + total;
    }
}
