package fun.ascent.skyblock.bazaar.menu;

import fun.ascent.skyblock.bazaar.vars.BazaarCategory;
import fun.ascent.skyblock.player.SkyblockPlayer;

public class BazaarMenu {

    public static void open(SkyblockPlayer player) {
        // Hypixel's Bazaar doesn't have a standalone main menu anymore, it defaults to the Farming category
        BazaarCategoryMenu.open(player, BazaarCategory.FARMING, 1);
    }
}
