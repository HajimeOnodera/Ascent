package fun.ascent.skyblock.bazaar.listener;

import fun.ascent.skyblock.bazaar.ui.*;
import fun.ascent.skyblock.bazaar.ui.buySell.*;
import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.inventory.Inventory;

import java.util.UUID;

public class GuiCloseListener extends SEvent<InventoryCloseEvent> {

    @Override
    public void onEvent(InventoryCloseEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        net.minestom.server.MinecraftServer.getSchedulerManager().scheduleNextTick(() -> {
            net.minestom.server.inventory.AbstractInventory open = player.getOpenInventory();
            if (isntBazaarInventory(open)) {
                clearBazaarData(player);
            }
        });
    }

    public static boolean isntBazaarInventory(net.minestom.server.inventory.AbstractInventory inventory) {
        if (inventory instanceof Inventory inv) {
            String title = fun.ascent.common.StringUtility.getTextFromComponent(inv.getTitle()).toLowerCase();
            return !title.contains("bazaar") && !title.contains("confirm instant buy") && !title.contains("➜") && !title.contains("confirm");
        }
        return true;
    }

    public static void clearBazaarData(SkyblockPlayer player) {
        UUID uuid = player.getUuid();
        BazaarSearchMenu.searchMap.remove(player);
        BazaarSearchMenu.searchResults.remove(player);
        BazaarCategoryMenu.curCategory.remove(player);
        BazaarChildCategoryMenu.curCategory.remove(player);
        BazaarItemBuyMenu.curItem.remove(player);
        BazaarInstantSellMenu.items.remove(uuid);
        BazaarInstantSellMenu.amounts.remove(uuid);
        BazaarInstantBuyMenu.amount.remove(uuid);
        BazaarInstantBuyMenu.items.remove(uuid);
    }
}
