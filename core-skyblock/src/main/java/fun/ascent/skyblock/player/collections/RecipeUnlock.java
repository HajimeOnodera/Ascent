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
        if (item == null) {
            item = ItemRegistry.getItem(recipeName + "_1");
        }
        if (item == null) {
            item = ItemRegistry.getItem(recipeName + "_I");
        }
        if (item == null && recipeName.contains("MINION")) {
            String genName = recipeName.replace("MINION", "GENERATOR");
            item = ItemRegistry.getItem(genName);
            if (item == null) {
                item = ItemRegistry.getItem(genName + "_1");
            }
            if (item == null) {
                item = ItemRegistry.getItem(genName + "_I");
            }
        }
        String name = ItemRegistry.formatName(recipeName);
        name = name.replaceAll("<[^>]*>", "");
        name = name.replaceAll("§[0-9a-fk-orA-FK-OR]", "");

        String color = "§f";
        if (item != null && item.getRarity() != null) {
            color = switch (item.getRarity()) {
                case COMMON -> "§f";
                case UNCOMMON -> "§a";
                case RARE -> "§9";
                case EPIC -> "§5";
                case LEGENDARY -> "§6";
                case MYTHIC -> "§d";
                case DIVINE -> "§b";
                case SPECIAL, VERY_SPECIAL -> "§c";
                case ULTIMATE, ADMIN -> "§4";
            };
        }
        return color + name + " §8Recipe";
    }

    @Override
    public void apply(SkyblockProfile profile) {
        profile.unlockedRecipes.add(recipeName);
    }
}
