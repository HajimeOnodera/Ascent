package fun.ascent.skyblock.crafting;

import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public class ShapelessRecipe extends SkyblockRecipe {
    private final List<RecipeIngredient> ingredients;

    public ShapelessRecipe(String id, String resultItemId, int resultAmount, RecipeType recipeType, List<RecipeIngredient> ingredients) {
        super(id, resultItemId, resultAmount, recipeType);
        this.ingredients = ingredients;
    }

    @Override
    public ItemStack[] getDisplayGrid(SkyblockPlayer player) {
        ItemStack[] grid = new ItemStack[9];
        for (int i = 0; i < 9; i++) grid[i] = ItemStack.AIR;
        for (int i = 0; i < ingredients.size() && i < 9; i++) {
            RecipeIngredient ingredient = ingredients.get(i);
            SkyblockItem item = ItemRegistry.getItem(ingredient.getItemId());
            if (item != null) {
                grid[i] = item.buildItemStack(player).withAmount(ingredient.getAmount());
            } else {
                grid[i] = ItemStack.of(Material.fromKey("minecraft:" + ingredient.getItemId().toLowerCase())).withAmount(ingredient.getAmount());
            }
        }
        return grid;
    }

    @Override
    public boolean matches(ItemStack[] grid) {
        List<ItemStack> gridItems = new ArrayList<>();
        for (ItemStack stack : grid) {
            if (stack != null && !stack.isAir()) gridItems.add(stack);
        }

        if (gridItems.size() != ingredients.size()) return false;

        List<RecipeIngredient> needed = new ArrayList<>(ingredients);
        for (ItemStack stack : gridItems) {
            SkyblockItem si = SkyblockItem.fromStack(stack);
            boolean matched = false;
            for (int i = 0; i < needed.size(); i++) {
                RecipeIngredient req = needed.get(i);
                boolean idMatches;
                if (si == null) {
                    idMatches = stack.material().name().equals(req.getItemId());
                } else {
                    idMatches = si.getItemId().equals(req.getItemId());
                }

                if (idMatches && stack.amount() >= req.getAmount()) {
                    needed.remove(i);
                    matched = true;
                    break;
                }
            }
            if (!matched) return false;
        }

        return needed.isEmpty();
    }

    @Override
    public void consume(Inventory inv, int[] gridSlots) {
        List<RecipeIngredient> needed = new ArrayList<>(ingredients);
        for (int slot : gridSlots) {
            ItemStack stack = inv.getItemStack(slot);
            if (stack.isAir()) continue;

            SkyblockItem si = SkyblockItem.fromStack(stack);
            for (int i = 0; i < needed.size(); i++) {
                RecipeIngredient req = needed.get(i);
                boolean idMatches;
                if (si == null) {
                    idMatches = stack.material().name().equals(req.getItemId());
                } else {
                    idMatches = si.getItemId().equals(req.getItemId());
                }

                if (idMatches && stack.amount() >= req.getAmount()) {
                    inv.setItemStack(slot, stack.withAmount(stack.amount() - req.getAmount()));
                    needed.remove(i);
                    break;
                }
            }
        }
    }
}
