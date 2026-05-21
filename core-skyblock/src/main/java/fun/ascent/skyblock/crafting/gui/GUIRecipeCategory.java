package fun.ascent.skyblock.crafting.gui;
import fun.ascent.skyblock.crafting.RecipeRegistry;
import fun.ascent.skyblock.crafting.SkyblockRecipe;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.collections.CollectionRegistry;
import fun.ascent.skyblock.player.skill.SkillType;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
            boolean unlocked = recipe.getCanCraft().apply(player).allowed();
            ItemStack icon;
            if (unlocked) {
                icon = result.buildItemStack(player).withAmount(recipe.getResultAmount());
                List<Component> lore = new ArrayList<>(icon.get(DataComponents.LORE, List.of()));
                lore.add(Component.empty());
                lore.add(text("<yellow>Click to view recipe!"));
                icon = icon.withLore(lore);
            } else {
                List<Component> lore = new ArrayList<>();
                lore.add(text("<gray>Unlock this recipe by meeting the"));
                lore.add(text("<gray>following requirements:"));
                lore.add(Component.empty());
                boolean hasReqs = false;
                for (Map.Entry<String, Integer> entry : recipe.getRequiredCollections().entrySet()) {
                    String colId = entry.getKey();
                    int reqTier = entry.getValue();
                    var col = CollectionRegistry.get(colId);
                    if (col != null) {
                        hasReqs = true;
                        int currentProgress = player.getActiveProfile().unlockedCollections.getOrDefault(colId, 0);
                        int currentTier = col.getTierFromProgress(currentProgress);
                        boolean met = currentTier >= reqTier;
                        String statusColor = met ? "<green>" : "<red>";
                        String checkSymbol = met ? "✔" : "✖";
                        lore.add(text(statusColor + checkSymbol + " Requires " + col.name() + " Collection " + getRomanNumeral(reqTier)));
                    }
                }
                for (Map.Entry<String, Integer> entry : recipe.getRequiredSkills().entrySet()) {
                    String skillName = entry.getKey();
                    int reqLevel = entry.getValue();
                    try {
                        SkillType skillType = SkillType.valueOf(skillName.toUpperCase());
                        hasReqs = true;
                        int currentLevel = player.getSkillData().getLevel(skillType);
                        boolean met = currentLevel >= reqLevel;
                        String statusColor = met ? "<green>" : "<red>";
                        String checkSymbol = met ? "✔" : "✖";
                        lore.add(text(statusColor + checkSymbol + " Requires " + skillType.getDisplayName() + " Level " + reqLevel));
                    } catch (Exception ignored) {}
                }
                if (!hasReqs) {
                    lore.add(text("<red>✖ Locked"));
                }
                icon = ItemStack.builder(Material.BARRIER)
                        .customName(text("<red>Recipe Locked!"))
                        .lore(lore)
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
                    if (recipe.getCanCraft().apply(player).allowed()) {
                        GUIRecipeView.open(player, recipe);
                    } else {
                        var res = recipe.getCanCraft().apply(player);
                        player.sendMessage("§c" + res.errorMessage());
                    }
                    return;
                }
            }
        });
        player.openInventory(inv);
    }
    private static String getRomanNumeral(int number) {
        if (number <= 0) return "";
        return switch(number) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            case 8 -> "VIII";
            case 9 -> "IX";
            case 10 -> "X";
            default -> String.valueOf(number);
        };
    }
}
