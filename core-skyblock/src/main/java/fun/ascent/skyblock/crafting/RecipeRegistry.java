package fun.ascent.skyblock.crafting;

import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

import com.google.gson.*;
import fun.ascent.skyblock.config.ConfigPaths;
import fun.ascent.skyblock.player.skill.SkillType;
import fun.ascent.skyblock.player.collections.CollectionRegistry;
import net.minestom.server.item.ItemStack;
import org.yaml.snakeyaml.Yaml;

public class RecipeRegistry {
    private static final List<SkyblockRecipe> RECIPES = new ArrayList<>();
    private static final Map<String, SkyblockRecipe> RECIPES_BY_ID = new HashMap<>();

    public static void init() {
        RECIPES.clear();
        RECIPES_BY_ID.clear();

        // Register core vanilla recipes first (bypassing permissions/locks)
        registerVanillaRecipes();

        // ENCHANTED MATERIALS
        registerEnchanted(SkyblockRecipe.RecipeType.MINING, "ENCHANTED_COAL", "COAL");
        registerEnchanted(SkyblockRecipe.RecipeType.MINING, "ENCHANTED_IRON_INGOT", "IRON_INGOT");
        registerEnchanted(SkyblockRecipe.RecipeType.MINING, "ENCHANTED_GOLD_INGOT", "GOLD_INGOT");
        registerEnchanted(SkyblockRecipe.RecipeType.MINING, "ENCHANTED_DIAMOND", "DIAMOND");
        registerEnchanted(SkyblockRecipe.RecipeType.MINING, "ENCHANTED_EMERALD", "EMERALD");
        registerEnchanted(SkyblockRecipe.RecipeType.MINING, "ENCHANTED_LAPIS_LAZULI", "LAPIS_LAZULI");
        registerEnchanted(SkyblockRecipe.RecipeType.MINING, "ENCHANTED_REDSTONE", "REDSTONE");
        registerEnchanted(SkyblockRecipe.RecipeType.MINING, "ENCHANTED_COBBLESTONE", "COBBLESTONE");
        
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

        // Dynamically load custom and vanilla recipe JSONs from config
        loadCustomRecipes();
        loadYamlRecipes();
        loadItemYamlRecipes();
    }

    private static void registerVanillaRecipes() {
        // Wood Logs to Planks
        registerVanillaShaped("OAK_PLANKS", 4, new String[]{"X"}, Map.of('X', "OAK_LOG"));
        registerVanillaShaped("BIRCH_PLANKS", 4, new String[]{"X"}, Map.of('X', "BIRCH_LOG"));
        registerVanillaShaped("SPRUCE_PLANKS", 4, new String[]{"X"}, Map.of('X', "SPRUCE_LOG"));
        registerVanillaShaped("DARK_OAK_PLANKS", 4, new String[]{"X"}, Map.of('X', "DARK_OAK_LOG"));

        // Planks to Stick
        registerVanillaShaped("STICK", 4, new String[]{"P", "P"}, Map.of('P', "OAK_PLANKS"));
        registerVanillaShaped("STICK_ALT1", "STICK", 4, new String[]{"P", "P"}, Map.of('P', "BIRCH_PLANKS"));
        registerVanillaShaped("STICK_ALT2", "STICK", 4, new String[]{"P", "P"}, Map.of('P', "SPRUCE_PLANKS"));
        registerVanillaShaped("STICK_ALT3", "STICK", 4, new String[]{"P", "P"}, Map.of('P', "DARK_OAK_PLANKS"));

        // Crafting Table
        registerVanillaShaped("CRAFTING_TABLE", 1, new String[]{"PP", "PP"}, Map.of('P', "OAK_PLANKS"));
        registerVanillaShaped("CRAFTING_TABLE_ALT1", "CRAFTING_TABLE", 1, new String[]{"PP", "PP"}, Map.of('P', "BIRCH_PLANKS"));
        registerVanillaShaped("CRAFTING_TABLE_ALT2", "CRAFTING_TABLE", 1, new String[]{"PP", "PP"}, Map.of('P', "SPRUCE_PLANKS"));
        registerVanillaShaped("CRAFTING_TABLE_ALT3", "CRAFTING_TABLE", 1, new String[]{"PP", "PP"}, Map.of('P', "DARK_OAK_PLANKS"));

        // Chest
        registerVanillaShaped("CHEST", 1, new String[]{"PPP", "P P", "PPP"}, Map.of('P', "OAK_PLANKS"));
        registerVanillaShaped("CHEST_ALT1", "CHEST", 1, new String[]{"PPP", "P P", "PPP"}, Map.of('P', "BIRCH_PLANKS"));
        registerVanillaShaped("CHEST_ALT2", "CHEST", 1, new String[]{"PPP", "P P", "PPP"}, Map.of('P', "SPRUCE_PLANKS"));
        registerVanillaShaped("CHEST_ALT3", "CHEST", 1, new String[]{"PPP", "P P", "PPP"}, Map.of('P', "DARK_OAK_PLANKS"));

        // Furnace
        registerVanillaShaped("FURNACE", 1, new String[]{"CCC", "C C", "CCC"}, Map.of('C', "COBBLESTONE"));

        // Torch
        registerVanillaShaped("TORCH", 4, new String[]{"C", "S"}, Map.of('C', "COAL", 'S', "STICK"));
        registerVanillaShaped("TORCH_ALT", "TORCH", 4, new String[]{"C", "S"}, Map.of('C', "CHARCOAL", 'S', "STICK"));

        // Wooden Tools
        registerVanillaShaped("WOODEN_SWORD", 1, new String[]{"P", "P", "S"}, Map.of('P', "OAK_PLANKS", 'S', "STICK"));
        registerVanillaShaped("WOODEN_PICKAXE", 1, new String[]{"PPP", " S ", " S "}, Map.of('P', "OAK_PLANKS", 'S', "STICK"));
        registerVanillaShaped("WOODEN_AXE", 1, new String[]{"PP", "PS", " S"}, Map.of('P', "OAK_PLANKS", 'S', "STICK"));
        registerVanillaShaped("WOODEN_SHOVEL", 1, new String[]{"P", "S", "S"}, Map.of('P', "OAK_PLANKS", 'S', "STICK"));
        registerVanillaShaped("WOODEN_HOE", 1, new String[]{"PP", " S", " S"}, Map.of('P', "OAK_PLANKS", 'S', "STICK"));

        // Stone Tools
        registerVanillaShaped("STONE_SWORD", 1, new String[]{"C", "C", "S"}, Map.of('C', "COBBLESTONE", 'S', "STICK"));
        registerVanillaShaped("STONE_PICKAXE", 1, new String[]{"CCC", " S ", " S "}, Map.of('C', "COBBLESTONE", 'S', "STICK"));
        registerVanillaShaped("STONE_AXE", 1, new String[]{"CC", "CS", " S"}, Map.of('C', "COBBLESTONE", 'S', "STICK"));
        registerVanillaShaped("STONE_SHOVEL", 1, new String[]{"C", "S", "S"}, Map.of('C', "COBBLESTONE", 'S', "STICK"));
        registerVanillaShaped("STONE_HOE", 1, new String[]{"CC", " S", " S"}, Map.of('C', "COBBLESTONE", 'S', "STICK"));

        // Iron Tools
        registerVanillaShaped("IRON_SWORD", 1, new String[]{"I", "I", "S"}, Map.of('I', "IRON_INGOT", 'S', "STICK"));
        registerVanillaShaped("IRON_PICKAXE", 1, new String[]{"III", " S ", " S "}, Map.of('I', "IRON_INGOT", 'S', "STICK"));
        registerVanillaShaped("IRON_AXE", 1, new String[]{"II", "IS", " S"}, Map.of('I', "IRON_INGOT", 'S', "STICK"));
        registerVanillaShaped("IRON_SHOVEL", 1, new String[]{"I", "S", "S"}, Map.of('I', "IRON_INGOT", 'S', "STICK"));
        registerVanillaShaped("IRON_HOE", 1, new String[]{"II", " S", " S"}, Map.of('I', "IRON_INGOT", 'S', "STICK"));

        // Gold Tools
        registerVanillaShaped("GOLDEN_SWORD", 1, new String[]{"G", "G", "S"}, Map.of('G', "GOLD_INGOT", 'S', "STICK"));
        registerVanillaShaped("GOLDEN_PICKAXE", 1, new String[]{"GGG", " S ", " S "}, Map.of('G', "GOLD_INGOT", 'S', "STICK"));
        registerVanillaShaped("GOLDEN_AXE", 1, new String[]{"GG", "GS", " S"}, Map.of('G', "GOLD_INGOT", 'S', "STICK"));
        registerVanillaShaped("GOLDEN_SHOVEL", 1, new String[]{"G", "S", "S"}, Map.of('G', "GOLD_INGOT", 'S', "STICK"));
        registerVanillaShaped("GOLDEN_HOE", 1, new String[]{"GG", " S", " S"}, Map.of('G', "GOLD_INGOT", 'S', "STICK"));

        // Diamond Tools
        registerVanillaShaped("DIAMOND_SWORD", 1, new String[]{"D", "D", "S"}, Map.of('D', "DIAMOND", 'S', "STICK"));
        registerVanillaShaped("DIAMOND_PICKAXE", 1, new String[]{"DDD", " S ", " S "}, Map.of('D', "DIAMOND", 'S', "STICK"));
        registerVanillaShaped("DIAMOND_AXE", 1, new String[]{"DD", "DS", " S"}, Map.of('D', "DIAMOND", 'S', "STICK"));
        registerVanillaShaped("DIAMOND_SHOVEL", 1, new String[]{"D", "S", "S"}, Map.of('D', "DIAMOND", 'S', "STICK"));
        registerVanillaShaped("DIAMOND_HOE", 1, new String[]{"DD", " S", " S"}, Map.of('D', "DIAMOND", 'S', "STICK"));

        // Iron Armor
        registerVanillaShaped("IRON_HELMET", 1, new String[]{"III", "I I"}, Map.of('I', "IRON_INGOT"));
        registerVanillaShaped("IRON_CHESTPLATE", 1, new String[]{"I I", "III", "III"}, Map.of('I', "IRON_INGOT"));
        registerVanillaShaped("IRON_LEGGINGS", 1, new String[]{"III", "I I", "I I"}, Map.of('I', "IRON_INGOT"));
        registerVanillaShaped("IRON_BOOTS", 1, new String[]{"I I", "I I"}, Map.of('I', "IRON_INGOT"));

        // Gold Armor
        registerVanillaShaped("GOLDEN_HELMET", 1, new String[]{"GGG", "G G"}, Map.of('G', "GOLD_INGOT"));
        registerVanillaShaped("GOLDEN_CHESTPLATE", 1, new String[]{"G G", "GGG", "GGG"}, Map.of('G', "GOLD_INGOT"));
        registerVanillaShaped("GOLDEN_LEGGINGS", 1, new String[]{"GGG", "G G", "G G"}, Map.of('G', "GOLD_INGOT"));
        registerVanillaShaped("GOLDEN_BOOTS", 1, new String[]{"G G", "G G"}, Map.of('G', "GOLD_INGOT"));

        // Diamond Armor
        registerVanillaShaped("DIAMOND_HELMET", 1, new String[]{"DDD", "D D"}, Map.of('D', "DIAMOND"));
        registerVanillaShaped("DIAMOND_CHESTPLATE", 1, new String[]{"D D", "DDD", "DDD"}, Map.of('D', "DIAMOND"));
        registerVanillaShaped("DIAMOND_LEGGINGS", 1, new String[]{"DDD", "D D", "D D"}, Map.of('D', "DIAMOND"));
        registerVanillaShaped("DIAMOND_BOOTS", 1, new String[]{"D D", "D D"}, Map.of('D', "DIAMOND"));

        // Blocks
        registerVanillaShaped("COAL_BLOCK", 1, new String[]{"CCC", "CCC", "CCC"}, Map.of('C', "COAL"));
        registerVanillaShaped("IRON_BLOCK", 1, new String[]{"III", "III", "III"}, Map.of('I', "IRON_INGOT"));
        registerVanillaShaped("GOLD_BLOCK", 1, new String[]{"GGG", "GGG", "GGG"}, Map.of('G', "GOLD_INGOT"));
        registerVanillaShaped("REDSTONE_BLOCK", 1, new String[]{"RRR", "RRR", "RRR"}, Map.of('R', "REDSTONE"));
        registerVanillaShaped("LAPIS_BLOCK", 1, new String[]{"LLL", "LLL", "LLL"}, Map.of('L', "LAPIS_LAZULI"));
        registerVanillaShaped("DIAMOND_BLOCK", 1, new String[]{"DDD", "DDD", "DDD"}, Map.of('D', "DIAMOND"));
        registerVanillaShaped("EMERALD_BLOCK", 1, new String[]{"EEE", "EEE", "EEE"}, Map.of('E', "EMERALD"));

        // Blocks back to Ingots/Items
        registerVanillaShaped("COAL", 9, new String[]{"B"}, Map.of('B', "COAL_BLOCK"));
        registerVanillaShaped("IRON_INGOT", 9, new String[]{"B"}, Map.of('B', "IRON_BLOCK"));
        registerVanillaShaped("GOLD_INGOT", 9, new String[]{"B"}, Map.of('B', "GOLD_BLOCK"));
        registerVanillaShaped("REDSTONE", 9, new String[]{"B"}, Map.of('B', "REDSTONE_BLOCK"));
        registerVanillaShaped("LAPIS_LAZULI", 9, new String[]{"B"}, Map.of('B', "LAPIS_BLOCK"));
        registerVanillaShaped("DIAMOND", 9, new String[]{"B"}, Map.of('B', "DIAMOND_BLOCK"));
        registerVanillaShaped("EMERALD", 9, new String[]{"B"}, Map.of('B', "EMERALD_BLOCK"));

        // Miscellaneous
        registerVanillaShaped("SHEARS", 1, new String[]{" I", "I "}, Map.of('I', "IRON_INGOT"));
        registerVanillaShaped("BUCKET", 1, new String[]{"I I", " I "}, Map.of('I', "IRON_INGOT"));
        registerVanillaShapeless("FLINT_AND_STEEL", List.of("FLINT", "IRON_INGOT"));
        registerVanillaShaped("BOW", 1, new String[]{" S#", "S #", " S#"}, Map.of('S', "STICK", '#', "STRING"));
        registerVanillaShaped("ARROW", 4, new String[]{"F", "S", "W"}, Map.of('F', "FLINT", 'S', "STICK", 'W', "FEATHER"));
        registerVanillaShaped("FISHING_ROD", 1, new String[]{"  S", " S#", "S #"}, Map.of('S', "STICK", '#', "STRING"));
        registerVanillaShaped("BREAD", 1, new String[]{"WWW"}, Map.of('W', "WHEAT"));
        registerVanillaShaped("PAPER", 3, new String[]{"SSS"}, Map.of('S', "SUGAR_CANE"));
        registerVanillaShapeless("BOOK", List.of("PAPER", "PAPER", "PAPER", "LEATHER"));
        registerVanillaShaped("BOOKSHELF", 1, new String[]{"PPP", "BBB", "PPP"}, Map.of('P', "OAK_PLANKS", 'B', "BOOK"));
    }

    private static void registerVanillaShaped(String id, int resultAmount, String[] pattern, Map<Character, String> ingredientIds) {
        registerVanillaShaped(id, id, resultAmount, pattern, ingredientIds);
    }

    private static void registerVanillaShaped(String id, String resultId, int resultAmount, String[] pattern, Map<Character, String> ingredientIds) {
        Map<Character, RecipeIngredient> ingredients = new HashMap<>();
        for (Map.Entry<Character, String> entry : ingredientIds.entrySet()) {
            ingredients.put(entry.getKey(), new RecipeIngredient(entry.getValue(), 1));
        }
        ShapedRecipe recipe = new ShapedRecipe(id, resultId, resultAmount, SkyblockRecipe.RecipeType.NONE, pattern, ingredients);
        recipe.setCanCraft((player) -> new SkyblockRecipe.CraftingResult(true, null));
        register(recipe);
    }

    private static void registerVanillaShapeless(String id, List<String> ingredientIds) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        for (String ingredientId : ingredientIds) {
            ingredients.add(new RecipeIngredient(ingredientId, 1));
        }
        ShapelessRecipe recipe = new ShapelessRecipe(id, id, 1, SkyblockRecipe.RecipeType.NONE, ingredients);
        recipe.setCanCraft((player) -> new SkyblockRecipe.CraftingResult(true, null));
        register(recipe);
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

    public static void loadCustomRecipes() {
        File folder = ConfigPaths.skyblockPath("recipes");
        if (!folder.exists()) {
            folder.mkdirs();
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null) return;

        for (File file : files) {
            try (FileReader reader = new FileReader(file)) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                if (!json.has("type")) continue;
                String type = json.get("type").getAsString();
                String id = file.getName().substring(0, file.getName().length() - 5).toUpperCase();

                // Determine result
                if (!json.has("result")) continue;
                JsonObject resultObj = json.getAsJsonObject("result");
                String resultItemId;
                if (resultObj.has("id")) {
                    resultItemId = resultObj.get("id").getAsString().replace("minecraft:", "").toUpperCase();
                } else if (resultObj.has("item")) {
                    resultItemId = resultObj.get("item").getAsString().replace("minecraft:", "").toUpperCase();
                } else {
                    continue;
                }
                int count = resultObj.has("count") ? resultObj.get("count").getAsInt() : 1;

                if (type.equals("minecraft:crafting_shaped") || type.equals("crafting_shaped")) {
                    JsonArray patternArray = json.getAsJsonArray("pattern");
                    String[] pattern = new String[patternArray.size()];
                    for (int i = 0; i < patternArray.size(); i++) {
                        pattern[i] = patternArray.get(i).getAsString();
                    }

                    JsonObject keyObj = json.getAsJsonObject("key");
                    Map<Character, RecipeIngredient> keyIngredients = new HashMap<>();
                    for (Map.Entry<String, JsonElement> entry : keyObj.entrySet()) {
                        char character = entry.getKey().charAt(0);
                        JsonObject ingredientObj = entry.getValue().getAsJsonObject();
                        String ingredientId;
                        if (ingredientObj.has("item")) {
                            ingredientId = ingredientObj.get("item").getAsString().replace("minecraft:", "").toUpperCase();
                        } else if (ingredientObj.has("tag")) {
                            ingredientId = ingredientObj.get("tag").getAsString().replace("minecraft:", "").toUpperCase();
                        } else {
                            continue;
                        }
                        int amount = ingredientObj.has("count") ? ingredientObj.get("count").getAsInt() : 1;
                        keyIngredients.put(character, new RecipeIngredient(ingredientId, amount));
                    }

                    ShapedRecipe recipe = new ShapedRecipe(id, resultItemId, count, SkyblockRecipe.RecipeType.NONE, pattern, keyIngredients);
                    // Default to allowed check if not specified as custom skyblock locked recipe
                    recipe.setCanCraft((player) -> new SkyblockRecipe.CraftingResult(true, null));
                    register(recipe);
                } else if (type.equals("minecraft:crafting_shapeless") || type.equals("crafting_shapeless")) {
                    JsonArray ingredientsArray = json.getAsJsonArray("ingredients");
                    List<RecipeIngredient> shapelessIngredients = new ArrayList<>();
                    for (JsonElement ingredientEl : ingredientsArray) {
                        JsonObject ingredientObj = ingredientEl.getAsJsonObject();
                        String ingredientId;
                        if (ingredientObj.has("item")) {
                            ingredientId = ingredientObj.get("item").getAsString().replace("minecraft:", "").toUpperCase();
                        } else if (ingredientObj.has("tag")) {
                            ingredientId = ingredientObj.get("tag").getAsString().replace("minecraft:", "").toUpperCase();
                        } else {
                            continue;
                        }
                        int amount = ingredientObj.has("count") ? ingredientObj.get("count").getAsInt() : 1;
                        shapelessIngredients.add(new RecipeIngredient(ingredientId, amount));
                    }
                    ShapelessRecipe recipe = new ShapelessRecipe(id, resultItemId, count, SkyblockRecipe.RecipeType.NONE, shapelessIngredients);
                    recipe.setCanCraft((player) -> new SkyblockRecipe.CraftingResult(true, null));
                    register(recipe);
                }
            } catch (Exception e) {
                System.err.println("Failed to load recipe: " + file.getName());
                e.printStackTrace();
            }
        }
    }

    public static void loadYamlRecipes() {
        File folder = ConfigPaths.skyblockPath("recipes");
        if (!folder.exists()) {
            folder.mkdirs();
            return;
        }
        loadYamlRecipesRecursively(folder);
    }

    private static void loadYamlRecipesRecursively(File directory) {
        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                loadYamlRecipesRecursively(file);
            } else if (file.getName().endsWith(".yml") || file.getName().endsWith(".yaml")) {
                try (InputStream inputStream = new FileInputStream(file)) {
                    Yaml yaml = new Yaml();
                    Map<String, Object> data = yaml.load(inputStream);
                    if (data == null) continue;

                    String id = (String) data.get("id");
                    id = Objects.requireNonNullElseGet(id, () -> file.getName().substring(0, file.getName().lastIndexOf('.'))).toUpperCase();

                    String typeStr = (String) data.getOrDefault("type", "SHAPED");
                    
                    // Recipe category - map from parent directory name
                    String parentName = directory.getName().toUpperCase();
                    SkyblockRecipe.RecipeType category = SkyblockRecipe.RecipeType.NONE;
                    try {
                        category = SkyblockRecipe.RecipeType.valueOf(parentName);
                    } catch (IllegalArgumentException e) {
                        // ignore or default
                    }

                    // Result
                    Map<String, Object> resultObj = (Map<String, Object>) data.get("result");
                    if (resultObj == null) continue;
                    String resultItemId = (String) resultObj.get("item");
                    if (resultItemId == null) continue;
                    resultItemId = resultItemId.toUpperCase().replace("MINECRAFT:", "");
                    int amount = ((Number) resultObj.getOrDefault("amount", 1)).intValue();

                    // Requirements
                    Map<String, Integer> requiredCollections = new HashMap<>();
                    Map<String, Integer> requiredSkills = new HashMap<>();
                    Map<String, Object> reqs = (Map<String, Object>) data.get("requirements");
                    if (reqs != null) {
                        Map<String, Object> cols = (Map<String, Object>) reqs.get("collections");
                        if (cols != null) {
                            for (Map.Entry<String, Object> entry : cols.entrySet()) {
                                requiredCollections.put(entry.getKey().toUpperCase(), ((Number) entry.getValue()).intValue());
                            }
                        }
                        Map<String, Object> skills = (Map<String, Object>) reqs.get("skills");
                        if (skills != null) {
                            for (Map.Entry<String, Object> entry : skills.entrySet()) {
                                requiredSkills.put(entry.getKey().toUpperCase(), ((Number) entry.getValue()).intValue());
                            }
                        }
                    }

                    SkyblockRecipe recipe = null;
                    if (typeStr.equalsIgnoreCase("SHAPED")) {
                        List<String> patternList = (List<String>) data.get("pattern");
                        if (patternList == null) continue;
                        String[] pattern = patternList.toArray(new String[0]);

                        Map<String, Object> ingredientsMap = (Map<String, Object>) data.get("ingredients");
                        Map<Character, RecipeIngredient> shapedIngredients = new HashMap<>();
                        if (ingredientsMap != null) {
                            for (Map.Entry<String, Object> entry : ingredientsMap.entrySet()) {
                                char key = entry.getKey().charAt(0);
                                Object val = entry.getValue();
                                if (val instanceof String) {
                                    shapedIngredients.put(key, new RecipeIngredient(((String) val).toUpperCase(), 1));
                                } else if (val instanceof Map) {
                                    Map<String, Object> ingredientDetails = (Map<String, Object>) val;
                                    String item = (String) ingredientDetails.get("item");
                                    int count = ((Number) ingredientDetails.getOrDefault("amount", 1)).intValue();
                                    shapedIngredients.put(key, new RecipeIngredient(item.toUpperCase(), count));
                                }
                            }
                        }

                        recipe = new ShapedRecipe(id, resultItemId, amount, category, pattern, shapedIngredients);
                    } else if (typeStr.equalsIgnoreCase("SHAPELESS")) {
                        List<Object> ingredientsList = (List<Object>) data.get("ingredients");
                        if (ingredientsList == null) continue;

                        List<RecipeIngredient> shapelessIngredients = new ArrayList<>();
                        for (Object obj : ingredientsList) {
                            if (obj instanceof String) {
                                shapelessIngredients.add(new RecipeIngredient(((String) obj).toUpperCase(), 1));
                            } else if (obj instanceof Map) {
                                Map<String, Object> ingredientDetails = (Map<String, Object>) obj;
                                String item = (String) ingredientDetails.get("item");
                                int count = ((Number) ingredientDetails.getOrDefault("amount", 1)).intValue();
                                shapelessIngredients.add(new RecipeIngredient(item.toUpperCase(), count));
                            }
                        }
                        recipe = new ShapelessRecipe(id, resultItemId, amount, category, shapelessIngredients);
                    }

                    if (recipe != null) {
                        recipe.setRequiredCollections(requiredCollections);
                        recipe.setRequiredSkills(requiredSkills);

                        final Map<String, Integer> finalReqCols = requiredCollections;
                        final Map<String, Integer> finalReqSkills = requiredSkills;

                        recipe.setCanCraft((player) -> {
                            // Check collections
                            for (Map.Entry<String, Integer> entry : finalReqCols.entrySet()) {
                                String colId = entry.getKey();
                                int reqTier = entry.getValue();

                                var col = CollectionRegistry.get(colId);
                                if (col == null) {
                                    return new SkyblockRecipe.CraftingResult(false, "Unknown collection: " + colId);
                                }

                                int currentProgress = player.getActiveProfile().unlockedCollections.getOrDefault(colId, 0);
                                int currentTier = col.getTierFromProgress(currentProgress);
                                if (currentTier < reqTier) {
                                    return new SkyblockRecipe.CraftingResult(false, "Requires " + col.name() + " Collection Tier " + reqTier + "!");
                                }
                            }

                            // Check skill requirements
                            for (Map.Entry<String, Integer> entry : finalReqSkills.entrySet()) {
                                String skillName = entry.getKey();
                                int reqLevel = entry.getValue();

                                SkillType skillType;
                                try {
                                    skillType = SkillType.valueOf(skillName.toUpperCase());
                                } catch (IllegalArgumentException e) {
                                    return new SkyblockRecipe.CraftingResult(false, "Unknown skill: " + skillName);
                                }

                                int currentLevel = player.getSkillData().getLevel(skillType);
                                if (currentLevel < reqLevel) {
                                    return new SkyblockRecipe.CraftingResult(false, "Requires " + skillType.getDisplayName() + " Level " + reqLevel + "!");
                                }
                            }

                            return new SkyblockRecipe.CraftingResult(true, null);
                        });

                        register(recipe);
                    }
                } catch (Exception e) {
                    System.err.println("Failed to load YAML recipe: " + file.getName());
                    e.printStackTrace();
                }
            }
        }
    }

    public static void loadItemYamlRecipes() {
        File folder = new File("configuration/skyblock/items");
        if (folder.exists() && folder.isDirectory()) {
            loadItemYamlRecipesRecursively(folder);
        }
    }

    private static void loadItemYamlRecipesRecursively(File directory) {
        File[] files = directory.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isDirectory()) {
                loadItemYamlRecipesRecursively(file);
            } else if (file.getName().endsWith(".yml") || file.getName().endsWith(".yaml")) {
                try (InputStream inputStream = new FileInputStream(file)) {
                    Yaml yaml = new Yaml();
                    Map<String, Object> data = yaml.load(inputStream);
                    if (data != null && data.containsKey("items")) {
                        Object itemsObj = data.get("items");
                        if (itemsObj instanceof List<?> itemsList) {
                            for (Object itemObj : itemsList) {
                                if (itemObj instanceof Map) {
                                    Map<String, Object> itemMap = (Map<String, Object>) itemObj;
                                    String id = (String) itemMap.get("id");
                                    if (id != null) {
                                        Object componentsObj = itemMap.get("components");
                                        if (componentsObj instanceof List<?> componentsList) {
                                            for (Object compObj : componentsList) {
                                                if (compObj instanceof Map) {
                                                    Map<String, Object> compMap = (Map<String, Object>) compObj;
                                                    Object compId = compMap.get("id");
                                                    if ("DEFAULT_CRAFTABLE".equals(compId)) {
                                                        List<?> recipesList = (List<?>) compMap.get("recipes");
                                                        if (recipesList != null) {
                                                            for (Object recipeObj : recipesList) {
                                                                if (recipeObj instanceof Map) {
                                                                    try {
                                                                        parseAndRegisterItemRecipe(id, (Map<String, Object>) recipeObj);
                                                                    } catch (Exception e) {
                                                                        System.err.println("Failed to parse DEFAULT_CRAFTABLE recipe for item " + id + " in file: " + file.getName());
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Failed to load YAML item recipe file: " + file.getName());
                    e.printStackTrace();
                }
            }
        }
    }

    private static void parseAndRegisterItemRecipe(String itemId, Map<String, Object> recipeData) {
        String typeStr = (String) recipeData.getOrDefault("type", "SHAPED");
        
        String recipeTypeStr = (String) recipeData.getOrDefault("recipe-type", "NONE");
        SkyblockRecipe.RecipeType category = SkyblockRecipe.RecipeType.NONE;
        try {
            category = SkyblockRecipe.RecipeType.valueOf(recipeTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            // ignore
        }

        // Result
        Map<String, Object> resultObj = (Map<String, Object>) recipeData.get("result");
        String resultItemId = itemId;
        int amount = 1;
        if (resultObj != null) {
            String resId = (String) resultObj.get("type");
            if (resId == null) {
                resId = (String) resultObj.get("item");
            }
            if (resId != null) {
                resultItemId = resId.toUpperCase().replace("MINECRAFT:", "");
            }
            amount = ((Number) resultObj.getOrDefault("amount", 1)).intValue();
        }

        String recipeId = resultItemId.toUpperCase();
        if (RECIPES_BY_ID.containsKey(recipeId)) {
            int altSuffix = 1;
            while (RECIPES_BY_ID.containsKey(recipeId + "_ALT" + altSuffix)) {
                altSuffix++;
            }
            recipeId = recipeId + "_ALT" + altSuffix;
        }

        // Requirements
        Map<String, Integer> requiredCollections = new HashMap<>();
        Map<String, Integer> requiredSkills = new HashMap<>();
        Map<String, Object> reqs = (Map<String, Object>) recipeData.get("requirements");
        if (reqs != null) {
            Map<String, Object> cols = (Map<String, Object>) reqs.get("collections");
            if (cols != null) {
                for (Map.Entry<String, Object> entry : cols.entrySet()) {
                    requiredCollections.put(entry.getKey().toUpperCase(), ((Number) entry.getValue()).intValue());
                }
            }
            Map<String, Object> skills = (Map<String, Object>) reqs.get("skills");
            if (skills != null) {
                for (Map.Entry<String, Object> entry : skills.entrySet()) {
                    requiredSkills.put(entry.getKey().toUpperCase(), ((Number) entry.getValue()).intValue());
                }
            }
        }

        SkyblockRecipe recipe = null;
        if (typeStr.equalsIgnoreCase("SHAPED")) {
            List<String> patternList = (List<String>) recipeData.get("pattern");
            if (patternList == null) return;
            String[] pattern = patternList.toArray(new String[0]);

            Map<String, Object> ingredientsMap = (Map<String, Object>) recipeData.get("ingredients");
            Map<Character, RecipeIngredient> shapedIngredients = new HashMap<>();
            if (ingredientsMap != null) {
                for (Map.Entry<String, Object> entry : ingredientsMap.entrySet()) {
                    char key = entry.getKey().charAt(0);
                    Object val = entry.getValue();
                    if (val instanceof String) {
                        shapedIngredients.put(key, new RecipeIngredient(((String) val).toUpperCase(), 1));
                    } else if (val instanceof Map) {
                        Map<String, Object> ingredientDetails = (Map<String, Object>) val;
                        String item = (String) ingredientDetails.get("type");
                        if (item == null) {
                            item = (String) ingredientDetails.get("item");
                        }
                        if (item != null) {
                            int count = ((Number) ingredientDetails.getOrDefault("amount", 1)).intValue();
                            shapedIngredients.put(key, new RecipeIngredient(item.toUpperCase(), count));
                        }
                    }
                }
            }

            recipe = new ShapedRecipe(recipeId, resultItemId, amount, category, pattern, shapedIngredients);
        } else if (typeStr.equalsIgnoreCase("SHAPELESS")) {
            List<Object> ingredientsList = (List<Object>) recipeData.get("ingredients");
            if (ingredientsList == null) return;

            List<RecipeIngredient> shapelessIngredients = new ArrayList<>();
            for (Object obj : ingredientsList) {
                if (obj instanceof String) {
                    shapelessIngredients.add(new RecipeIngredient(((String) obj).toUpperCase(), 1));
                } else if (obj instanceof Map) {
                    Map<String, Object> ingredientDetails = (Map<String, Object>) obj;
                    String item = (String) ingredientDetails.get("type");
                    if (item == null) {
                        item = (String) ingredientDetails.get("item");
                    }
                    if (item != null) {
                        int count = ((Number) ingredientDetails.getOrDefault("amount", 1)).intValue();
                        shapelessIngredients.add(new RecipeIngredient(item.toUpperCase(), count));
                    }
                }
            }
            recipe = new ShapelessRecipe(recipeId, resultItemId, amount, category, shapelessIngredients);
        }

        if (recipe != null) {
            recipe.setRequiredCollections(requiredCollections);
            recipe.setRequiredSkills(requiredSkills);

            final Map<String, Integer> finalReqCols = requiredCollections;
            final Map<String, Integer> finalReqSkills = requiredSkills;

            recipe.setCanCraft((player) -> {
                for (Map.Entry<String, Integer> entry : finalReqCols.entrySet()) {
                    String colId = entry.getKey();
                    int reqTier = entry.getValue();

                    var col = CollectionRegistry.get(colId);
                    if (col == null) {
                        return new SkyblockRecipe.CraftingResult(false, "Unknown collection: " + colId);
                    }

                    int currentProgress = player.getActiveProfile().unlockedCollections.getOrDefault(colId, 0);
                    int currentTier = col.getTierFromProgress(currentProgress);
                    if (currentTier < reqTier) {
                        return new SkyblockRecipe.CraftingResult(false, "Requires " + col.name() + " Collection Tier " + reqTier + "!");
                    }
                }

                for (Map.Entry<String, Integer> entry : finalReqSkills.entrySet()) {
                    String skillName = entry.getKey();
                    int reqLevel = entry.getValue();

                    SkillType skillType;
                    try {
                        skillType = SkillType.valueOf(skillName.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        return new SkyblockRecipe.CraftingResult(false, "Unknown skill: " + skillName);
                    }

                    int currentLevel = player.getSkillData().getLevel(skillType);
                    if (currentLevel < reqLevel) {
                        return new SkyblockRecipe.CraftingResult(false, "Requires " + skillType.getDisplayName() + " Level " + reqLevel + "!");
                    }
                }

                return new SkyblockRecipe.CraftingResult(true, null);
            });

            register(recipe);
        }
    }
}
