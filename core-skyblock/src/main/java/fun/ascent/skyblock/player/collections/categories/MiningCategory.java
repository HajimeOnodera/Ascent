package fun.ascent.skyblock.player.collections.categories;

import fun.ascent.skyblock.player.collections.CollectionCategory;
import fun.ascent.skyblock.player.collections.RecipeUnlock;
import fun.ascent.skyblock.player.collections.XpUnlock;
import fun.ascent.skyblock.player.skill.SkillType;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public class MiningCategory extends CollectionCategory {
    public MiningCategory() {
        super("Mining", Material.STONE_PICKAXE, CollectionType.MINING);
    }

    @Override
    public List<ItemCollection> getCollections() {
        List<ItemCollection> collections = new ArrayList<>();

        // COBBLESTONE
        collections.add(new ItemCollection("COBBLESTONE", "Cobblestone", Material.COBBLESTONE, List.of(
            new CollectionReward(100, List.of(new XpUnlock(SkillType.MINING, 10), new RecipeUnlock("Cobblestone Minion I"))),
            new CollectionReward(250, List.of(new XpUnlock(SkillType.MINING, 20))),
            new CollectionReward(500, List.of(new XpUnlock(SkillType.MINING, 50), new RecipeUnlock("Auto Smelter"))),
            new CollectionReward(1000, List.of(new XpUnlock(SkillType.MINING, 100), new RecipeUnlock("Cobblestone Minion II"))),
            new CollectionReward(2500, List.of(new XpUnlock(SkillType.MINING, 250), new RecipeUnlock("Compactor")))
        )));

        // COAL
        collections.add(new ItemCollection("COAL", "Coal", Material.COAL, List.of(
            new CollectionReward(100, List.of(new XpUnlock(SkillType.MINING, 10), new RecipeUnlock("Coal Minion I"))),
            new CollectionReward(250, List.of(new XpUnlock(SkillType.MINING, 20), new RecipeUnlock("Enchanted Coal"))),
            new CollectionReward(500, List.of(new XpUnlock(SkillType.MINING, 50), new RecipeUnlock("Coal Minion II")))
        )));

        // IRON
        collections.add(new ItemCollection("IRON_INGOT", "Iron Ingot", Material.IRON_INGOT, List.of(
            new CollectionReward(100, List.of(new XpUnlock(SkillType.MINING, 10), new RecipeUnlock("Iron Minion I"))),
            new CollectionReward(250, List.of(new XpUnlock(SkillType.MINING, 20), new RecipeUnlock("Enchanted Iron Ingot"))),
            new CollectionReward(500, List.of(new XpUnlock(SkillType.MINING, 50), new RecipeUnlock("Iron Minion II")))
        )));

        // GOLD
        collections.add(new ItemCollection("GOLD_INGOT", "Gold Ingot", Material.GOLD_INGOT, List.of(
            new CollectionReward(100, List.of(new XpUnlock(SkillType.MINING, 10), new RecipeUnlock("Gold Minion I"))),
            new CollectionReward(250, List.of(new XpUnlock(SkillType.MINING, 20), new RecipeUnlock("Enchanted Gold Ingot"))),
            new CollectionReward(500, List.of(new XpUnlock(SkillType.MINING, 50), new RecipeUnlock("Gold Minion II")))
        )));

        // DIAMOND
        collections.add(new ItemCollection("DIAMOND", "Diamond", Material.DIAMOND, List.of(
            new CollectionReward(100, List.of(new XpUnlock(SkillType.MINING, 20), new RecipeUnlock("Diamond Minion I"))),
            new CollectionReward(250, List.of(new XpUnlock(SkillType.MINING, 50), new RecipeUnlock("Enchanted Diamond"))),
            new CollectionReward(500, List.of(new XpUnlock(SkillType.MINING, 100), new RecipeUnlock("Diamond Minion II")))
        )));

        // LAPIS
        collections.add(new ItemCollection("LAPIS_LAZULI", "Lapis Lazuli", Material.LAPIS_LAZULI, List.of(
            new CollectionReward(100, List.of(new XpUnlock(SkillType.MINING, 10), new RecipeUnlock("Lapis Minion I"))),
            new CollectionReward(250, List.of(new XpUnlock(SkillType.MINING, 20), new RecipeUnlock("Experience Bottle"))),
            new CollectionReward(500, List.of(new XpUnlock(SkillType.MINING, 50), new RecipeUnlock("Lapis Minion II"))),
            new CollectionReward(1000, List.of(new XpUnlock(SkillType.MINING, 100), new RecipeUnlock("Grand Experience Bottle")))
        )));

        // REDSTONE
        collections.add(new ItemCollection("REDSTONE", "Redstone", Material.REDSTONE, List.of(
            new CollectionReward(100, List.of(new XpUnlock(SkillType.MINING, 10), new RecipeUnlock("Redstone Minion I"))),
            new CollectionReward(250, List.of(new XpUnlock(SkillType.MINING, 20), new RecipeUnlock("Enchanted Redstone"))),
            new CollectionReward(500, List.of(new XpUnlock(SkillType.MINING, 50), new RecipeUnlock("Redstone Minion II")))
        )));

        // EMERALD
        collections.add(new ItemCollection("EMERALD", "Emerald", Material.EMERALD, List.of(
            new CollectionReward(100, List.of(new XpUnlock(SkillType.MINING, 20), new RecipeUnlock("Emerald Minion I"))),
            new CollectionReward(250, List.of(new XpUnlock(SkillType.MINING, 50), new RecipeUnlock("Enchanted Emerald"))),
            new CollectionReward(500, List.of(new XpUnlock(SkillType.MINING, 100), new RecipeUnlock("Emerald Minion II")))
        )));

        return collections;
    }
}
