package fun.ascent.skyblock.crafting;

import fun.ascent.skyblock.player.SkyblockPlayer;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.function.Function;

@Getter
public abstract class SkyblockRecipe {
    protected final String id;
    protected final String resultItemId;
    @Setter
    protected int resultAmount;
    protected final RecipeType recipeType;
    @Setter
    protected Function<SkyblockPlayer, CraftingResult> canCraft;

    @Setter @Getter
    protected java.util.Map<String, Integer> requiredCollections = new java.util.HashMap<>();
    @Setter @Getter
    protected java.util.Map<String, Integer> requiredSkills = new java.util.HashMap<>();

    protected SkyblockRecipe(String id, String resultItemId, int resultAmount, RecipeType recipeType) {
        this.id = id;
        this.resultItemId = resultItemId;
        this.resultAmount = resultAmount;
        this.recipeType = recipeType;
        this.canCraft = (player) -> {
            if (player.getActiveProfile().unlockedRecipes.contains(id)) {
                return new CraftingResult(true, null);
            }
            return new CraftingResult(false, "You haven't unlocked this recipe yet!");
        };
    }

    public abstract boolean matches(ItemStack[] grid);

    public abstract ItemStack[] getDisplayGrid(SkyblockPlayer player);

    public abstract void consume(Inventory inv, int[] gridSlots);

    protected boolean matchesIngredient(ItemStack stack, String ingredientId) {
        if (stack == null || stack.isAir()) return false;
        if (ingredientId == null) return false;

        String reqId = ingredientId.toUpperCase().replace("MINECRAFT:", "");
        
        // 1. Get raw material name from stack
        String matName = stack.material().name().toUpperCase().replace("MINECRAFT:", "");

        // 2. Get SkyblockItem ID if it exists
        fun.ascent.skyblock.item.SkyblockItem si = fun.ascent.skyblock.item.SkyblockItem.fromStack(stack);
        String siId = si != null ? si.getItemId().toUpperCase().replace("MINECRAFT:", "") : null;

        // Perform matching checks
        // a. Direct match with SkyblockItem ID
        if (siId != null && siId.equalsIgnoreCase(reqId)) {
            return true;
        }

        // b. Direct match with material name
        if (matName.equalsIgnoreCase(reqId)) {
            return true;
        }

        // c. Normalize comparison (e.g. CARROT_ITEM vs CARROT, POTATO_ITEM vs POTATO, MELON vs MELON_SLICE)
        if (normalizeId(reqId).equalsIgnoreCase(normalizeId(matName))) {
            return true;
        }
        
        if (siId != null && normalizeId(reqId).equalsIgnoreCase(normalizeId(siId))) {
            return true;
        }

        return false;
    }

    private String normalizeId(String id) {
        String normalized = id.toUpperCase();
        if (normalized.endsWith("_ITEM")) {
            normalized = normalized.substring(0, normalized.length() - 5);
        }
        if (normalized.equals("MELON_SLICE") || normalized.equals("MELON")) {
            return "MELON";
        }
        return normalized;
    }

    public record CraftingResult(boolean allowed, String errorMessage) {}

    @Getter
    public enum RecipeType {
        FARMING(Material.GOLDEN_HOE),
        MINING(Material.STONE_PICKAXE),
        COMBAT(Material.STONE_SWORD),
        FORAGING(Material.JUNGLE_SAPLING),
        FISHING(Material.FISHING_ROD),
        CARPENTRY(Material.CRAFTING_TABLE),
        NONE(Material.AIR);

        private final Material icon;
        RecipeType(Material icon) { this.icon = icon; }
    }
}
