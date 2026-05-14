package fun.ascent.skyblock.crafting;

import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShapedRecipe extends SkyblockRecipe {
    private final List<String> pattern;
    private final Map<Character, RecipeIngredient> ingredients;
    private final int width;
    private final int height;

    public ShapedRecipe(String id, String resultItemId, int resultAmount, RecipeType recipeType, String[] pattern, Map<Character, RecipeIngredient> ingredients) {
        super(id, resultItemId, resultAmount, recipeType);
        this.pattern = normalizePattern(pattern);
        this.ingredients = ingredients;
        this.height = this.pattern.size();
        this.width = this.pattern.isEmpty() ? 0 : this.pattern.getFirst().length();
    }

    private List<String> normalizePattern(String[] original) {
        int minRow = Integer.MAX_VALUE, maxRow = Integer.MIN_VALUE;
        int minCol = Integer.MAX_VALUE, maxCol = Integer.MIN_VALUE;

        for (int r = 0; r < original.length; r++) {
            for (int c = 0; c < original[r].length(); c++) {
                if (original[r].charAt(c) != ' ') {
                    minRow = Math.min(minRow, r);
                    maxRow = Math.max(maxRow, r);
                    minCol = Math.min(minCol, c);
                    maxCol = Math.max(maxCol, c);
                }
            }
        }

        if (minRow == Integer.MAX_VALUE) return List.of();

        List<String> normalized = new ArrayList<>();
        for (int r = minRow; r <= maxRow; r++) {
            normalized.add(original[r].substring(minCol, maxCol + 1));
        }
        return normalized;
    }

    @Override
    public ItemStack[] getDisplayGrid(SkyblockPlayer player) {
        ItemStack[] grid = new ItemStack[9];
        for (int i = 0; i < 9; i++) grid[i] = ItemStack.AIR;
        
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                char symbol = pattern.get(r).charAt(c);
                if (symbol == ' ') continue;
                
                RecipeIngredient ingredient = ingredients.get(symbol);
                if (ingredient != null) {
                    SkyblockItem item = ItemRegistry.getItem(ingredient.getItemId());
                    if (item != null) {
                        grid[r * 3 + c] = item.buildItemStack(player).withAmount(ingredient.getAmount());
                    } else {
                        grid[r * 3 + c] = ItemStack.of(Material.fromKey("minecraft:" + ingredient.getItemId().toLowerCase())).withAmount(ingredient.getAmount());
                    }
                }
            }
        }
        return grid;
    }

    @Override
    public boolean matches(ItemStack[] grid) {
        if (pattern.isEmpty()) return false;

        for (int startRow = 0; startRow <= 3 - height; startRow++) {
            for (int startCol = 0; startCol <= 3 - width; startCol++) {
                if (checkAt(grid, startRow, startCol)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkAt(ItemStack[] grid, int startRow, int startCol) {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                int gridIdx = r * 3 + c;
                int patternRow = r - startRow;
                int patternCol = c - startCol;

                boolean inPattern = patternRow >= 0 && patternRow < height && patternCol >= 0 && patternCol < width;
                ItemStack stack = grid[gridIdx];

                if (inPattern) {
                    char symbol = pattern.get(patternRow).charAt(patternCol);
                    if (symbol == ' ') {
                        if (stack != null && !stack.isAir()) return false;
                    } else {
                        RecipeIngredient ingredient = ingredients.get(symbol);
                        if (ingredient == null) return false;
                        if (stack == null || stack.isAir()) return false;
                        
                        SkyblockItem si = SkyblockItem.fromStack(stack);
                        if (si == null) {
                            if (!stack.material().name().equals(ingredient.getItemId())) return false;
                        } else {
                            if (!ingredient.getItemId().equals(si.getItemId())) return false;
                        }
                        
                        if (stack.amount() < ingredient.getAmount()) return false;
                    }
                } else {
                    if (stack != null && !stack.isAir()) return false;
                }
            }
        }
        return true;
    }

    @Override
    public void consume(Inventory inv, int[] gridSlots) {
        for (int startRow = 0; startRow <= 3 - height; startRow++) {
            for (int startCol = 0; startCol <= 3 - width; startCol++) {
                ItemStack[] currentGrid = new ItemStack[9];
                for(int i=0; i<9; i++) currentGrid[i] = inv.getItemStack(gridSlots[i]);
                
                if (checkAt(currentGrid, startRow, startCol)) {
                    for (int r = 0; r < height; r++) {
                        for (int c = 0; c < width; c++) {
                            char symbol = pattern.get(r).charAt(c);
                            if (symbol == ' ') continue;
                            
                            RecipeIngredient ingredient = ingredients.get(symbol);
                            int slot = gridSlots[(startRow + r) * 3 + (startCol + c)];
                            ItemStack stack = inv.getItemStack(slot);
                            inv.setItemStack(slot, stack.withAmount(stack.amount() - ingredient.getAmount()));
                        }
                    }
                    return;
                }
            }
        }
    }
}
