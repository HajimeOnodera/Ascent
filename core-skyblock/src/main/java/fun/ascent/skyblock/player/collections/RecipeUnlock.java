package fun.ascent.skyblock.player.collections;

import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.profiles.SkyblockProfile;

public class RecipeUnlock extends CollectionUnlock {
    private final String recipeName;

    public RecipeUnlock(String recipeName) {
        this.recipeName = recipeName;
    }

    @Override
    public String getDisplay() {
        SkyblockItem item = ItemRegistry.getItem(recipeName);
        String name = (item != null) ? item.getDisplayName() : ItemRegistry.formatName(recipeName);
        name = name.replaceAll("<[^>]*>", "");
        name = name.replaceAll("§[0-9a-fk-orA-FK-OR]", "");
        return "§f" + name + " §8Recipe";
    }

    @Override
    public void apply(SkyblockProfile profile) {
        profile.unlockedRecipes.add(recipeName);
    }
}
