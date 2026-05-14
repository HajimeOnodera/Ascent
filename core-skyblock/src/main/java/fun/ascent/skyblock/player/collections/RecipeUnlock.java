package fun.ascent.skyblock.player.collections;

import fun.ascent.skyblock.player.profiles.SkyblockProfile;

public class RecipeUnlock extends CollectionUnlock {
    private final String recipeName;

    public RecipeUnlock(String recipeName) {
        this.recipeName = recipeName;
    }

    @Override
    public String getDisplay() {
        return "§e" + recipeName + " §7Recipe";
    }

    @Override
    public void apply(SkyblockProfile profile) {
        profile.unlockedRecipes.add(recipeName);
    }
}
