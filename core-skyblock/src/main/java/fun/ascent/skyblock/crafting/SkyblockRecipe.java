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
