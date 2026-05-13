package fun.ascent.skyblock.player.collections.categories;

import fun.ascent.skyblock.player.collections.CollectionCategory;
import fun.ascent.skyblock.player.collections.RecipeUnlock;
import fun.ascent.skyblock.player.collections.XpUnlock;
import fun.ascent.skyblock.player.skill.SkillType;
import net.minestom.server.item.Material;

import java.util.List;

public class FarmingCategory extends CollectionCategory {
    public FarmingCategory() {
        super("Farming", Material.GOLDEN_HOE, CollectionType.FARMING);
    }

    @Override
    public List<ItemCollection> getCollections() {
        return List.of(
            new ItemCollection("WHEAT", "Wheat", Material.WHEAT, List.of(
                new CollectionReward(100, List.of(new XpUnlock(SkillType.FARMING, 10), new RecipeUnlock("Wheat Minion I"))),
                new CollectionReward(250, List.of(new XpUnlock(SkillType.FARMING, 20), new RecipeUnlock("Enchanted Bread"))),
                new CollectionReward(500, List.of(new XpUnlock(SkillType.FARMING, 50), new RecipeUnlock("Wheat Minion II")))
            ))
        );
    }
}
