package fun.ascent.skyblock.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minestom.server.item.ItemStack;

public class RecipeRegistry {
    private static final List<SkyblockRecipe> RECIPES = new ArrayList<>();
    private static final Map<String, SkyblockRecipe> RECIPES_BY_ID = new HashMap<>();

    public static void init() {
        RECIPES.clear();
        RECIPES_BY_ID.clear();

        // ENCHANTED MATERIALS
        registerEnchanted(SkyblockRecipe.RecipeType.MINING, "ENCHANTED_COAL", "COAL");
        registerEnchanted(SkyblockRecipe.RecipeType.MINING, "ENCHANTED_IRON_INGOT", "IRON_INGOT");
        registerEnchanted(SkyblockRecipe.RecipeType.MINING, "ENCHANTED_GOLD_INGOT", "GOLD_INGOT");
        registerEnchanted(SkyblockRecipe.RecipeType.MINING, "ENCHANTED_DIAMOND", "DIAMOND");
        registerEnchanted(SkyblockRecipe.RecipeType.MINING, "ENCHANTED_EMERALD", "EMERALD");
        registerEnchanted(SkyblockRecipe.RecipeType.MINING, "ENCHANTED_LAPIS_LAZULI", "LAPIS_LAZULI");
        registerEnchanted(SkyblockRecipe.RecipeType.MINING, "ENCHANTED_REDSTONE", "REDSTONE");
        
        registerEnchanted(SkyblockRecipe.RecipeType.FORAGING, "ENCHANTED_OAK_LOG", "OAK_LOG");
        registerEnchanted(SkyblockRecipe.RecipeType.FORAGING, "ENCHANTED_BIRCH_LOG", "BIRCH_LOG");
        registerEnchanted(SkyblockRecipe.RecipeType.FORAGING, "ENCHANTED_SPRUCE_LOG", "SPRUCE_LOG");
        registerEnchanted(SkyblockRecipe.RecipeType.FORAGING, "ENCHANTED_DARK_OAK_LOG", "DARK_OAK_LOG");

        // GENERATORS (MINIONS) - Level 1
        registerMinion(SkyblockRecipe.RecipeType.MINING, "COAL_GENERATOR_1", "COAL", "WOODEN_PICKAXE");
        registerMinion(SkyblockRecipe.RecipeType.MINING, "IRON_GENERATOR_1", "IRON_INGOT", "WOODEN_PICKAXE");
        registerMinion(SkyblockRecipe.RecipeType.MINING, "GOLD_GENERATOR_1", "GOLD_INGOT", "WOODEN_PICKAXE");
        registerMinion(SkyblockRecipe.RecipeType.MINING, "DIAMOND_GENERATOR_1", "DIAMOND", "WOODEN_PICKAXE");
        registerMinion(SkyblockRecipe.RecipeType.MINING, "EMERALD_GENERATOR_1", "EMERALD", "WOODEN_PICKAXE");
        registerMinion(SkyblockRecipe.RecipeType.MINING, "LAPIS_GENERATOR_1", "LAPIS_LAZULI", "WOODEN_PICKAXE");
        registerMinion(SkyblockRecipe.RecipeType.MINING, "REDSTONE_GENERATOR_1", "REDSTONE", "WOODEN_PICKAXE");
        registerMinion(SkyblockRecipe.RecipeType.MINING, "OBSIDIAN_GENERATOR_1", "OBSIDIAN", "WOODEN_PICKAXE");
        
        registerMinion(SkyblockRecipe.RecipeType.FARMING, "WHEAT_GENERATOR_1", "WHEAT", "WOODEN_HOE");
        registerMinion(SkyblockRecipe.RecipeType.FARMING, "CARROT_GENERATOR_1", "CARROT_ITEM", "WOODEN_HOE");
        registerMinion(SkyblockRecipe.RecipeType.FARMING, "POTATO_GENERATOR_1", "POTATO_ITEM", "WOODEN_HOE");
        registerMinion(SkyblockRecipe.RecipeType.FARMING, "PUMPKIN_GENERATOR_1", "PUMPKIN", "WOODEN_HOE");
        registerMinion(SkyblockRecipe.RecipeType.FARMING, "MELON_GENERATOR_1", "MELON", "WOODEN_HOE");
        
        registerMinion(SkyblockRecipe.RecipeType.FORAGING, "OAK_GENERATOR_1", "OAK_LOG", "WOODEN_AXE");
        registerMinion(SkyblockRecipe.RecipeType.FORAGING, "BIRCH_GENERATOR_1", "BIRCH_LOG", "WOODEN_AXE");
        registerMinion(SkyblockRecipe.RecipeType.FORAGING, "SPRUCE_GENERATOR_1", "SPRUCE_LOG", "WOODEN_AXE");
        registerMinion(SkyblockRecipe.RecipeType.FORAGING, "DARK_OAK_GENERATOR_1", "DARK_OAK_LOG", "WOODEN_AXE");
        
        registerMinion(SkyblockRecipe.RecipeType.MINING, "COBBLESTONE_GENERATOR_1", "COBBLESTONE", "WOODEN_PICKAXE");
    }

    private static void registerEnchanted(SkyblockRecipe.RecipeType type, String id, String ingredientId) {
        register(new ShapedRecipe(
            id, id, 1, type,
            new String[]{"XXX", "XXX", "XXX"},
            Map.of('X', new RecipeIngredient(ingredientId, 32))
        ));
    }

    private static void registerMinion(SkyblockRecipe.RecipeType type, String id, String resId, String toolId) {
        register(new ShapedRecipe(
            id, id, 1, type,
            new String[]{"XXX", "XWX", "XXX"},
            Map.of('X', new RecipeIngredient(resId, 8), 'W', new RecipeIngredient(toolId, 1))
        ));
    }

    public static void register(SkyblockRecipe recipe) {
        RECIPES.add(recipe);
        RECIPES_BY_ID.put(recipe.getId(), recipe);
    }

    public static SkyblockRecipe findMatch(ItemStack[] grid) {
        for (SkyblockRecipe recipe : RECIPES) {
            if (recipe.matches(grid)) {
                return recipe;
            }
        }
        return null;
    }

    public static List<SkyblockRecipe> getByType(SkyblockRecipe.RecipeType type) {
        List<SkyblockRecipe> typeRecipes = new ArrayList<>();
        for (SkyblockRecipe recipe : RECIPES) {
            if (recipe.getRecipeType() == type) {
                typeRecipes.add(recipe);
            }
        }
        return typeRecipes;
    }

    public static SkyblockRecipe getById(String id) {
        return RECIPES_BY_ID.get(id);
    }

    public static int getTotalRecipesCount() {
        return RECIPES.size();
    }
}
