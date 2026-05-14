package fun.ascent.skyblock.bazaar.menu;

import fun.ascent.skyblock.bazaar.vars.BazaarCategory;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;

public class BazaarMenu {

    public static void open(SkyblockPlayer player){
        Inventory inventory = new Inventory(InventoryType.CHEST_6_ROW, BazaarCategory.FARMING.getTitle());

        player.openInventory(inventory);
    }

}
