package fun.ascent.skyblock.player.collections.categories;

import fun.ascent.skyblock.player.collections.CollectionCategory;
import fun.ascent.skyblock.player.collections.RecipeUnlock;
import fun.ascent.skyblock.player.collections.XpUnlock;
import fun.ascent.skyblock.player.skill.SkillType;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public class CombatCategory extends CollectionCategory {
    public CombatCategory() {
        super("Combat", Material.STONE_SWORD, CollectionType.COMBAT);
    }

    @Override
    public List<ItemCollection> getCollections() {
        List<ItemCollection> collections = new ArrayList<>();

        // ROTTEN FLESH
        collections.add(new ItemCollection("ROTTEN_FLESH", "Rotten Flesh", Material.ROTTEN_FLESH, List.of(
            new CollectionReward(100, List.of(new XpUnlock(SkillType.COMBAT, 10), new RecipeUnlock("Zombie Minion I"))),
            new CollectionReward(250, List.of(new XpUnlock(SkillType.COMBAT, 20), new RecipeUnlock("Zombie Charm"))),
            new CollectionReward(500, List.of(new XpUnlock(SkillType.COMBAT, 50), new RecipeUnlock("Zombie Minion II")))
        )));

        // BONE
        collections.add(new ItemCollection("BONE", "Bone", Material.BONE, List.of(
            new CollectionReward(100, List.of(new XpUnlock(SkillType.COMBAT, 10), new RecipeUnlock("Skeleton Minion I"))),
            new CollectionReward(250, List.of(new XpUnlock(SkillType.COMBAT, 20), new RecipeUnlock("Enchanted Bone"))),
            new CollectionReward(500, List.of(new XpUnlock(SkillType.COMBAT, 50), new RecipeUnlock("Skeleton Minion II")))
        )));

        // STRING
        collections.add(new ItemCollection("STRING", "String", Material.STRING, List.of(
            new CollectionReward(100, List.of(new XpUnlock(SkillType.COMBAT, 10), new RecipeUnlock("Spider Minion I"))),
            new CollectionReward(250, List.of(new XpUnlock(SkillType.COMBAT, 20), new RecipeUnlock("Enchanted String"))),
            new CollectionReward(500, List.of(new XpUnlock(SkillType.COMBAT, 50), new RecipeUnlock("Spider Minion II")))
        )));

        // ENDER PEARL
        collections.add(new ItemCollection("ENDER_PEARL", "Ender Pearl", Material.ENDER_PEARL, List.of(
            new CollectionReward(100, List.of(new XpUnlock(SkillType.COMBAT, 50), new RecipeUnlock("Enderman Minion I"))),
            new CollectionReward(250, List.of(new XpUnlock(SkillType.COMBAT, 100), new RecipeUnlock("Ender Chest"))),
            new CollectionReward(500, List.of(new XpUnlock(SkillType.COMBAT, 250), new RecipeUnlock("Enderman Minion II")))
        )));

        return collections;
    }
}
