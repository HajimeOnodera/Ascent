package fun.ascent.skyblock.bazaar.listener;

import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.inventory.Inventory;

public class GuiListener extends SEvent<InventoryOpenEvent> {

    @Override
    public void onEvent(InventoryOpenEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        if (event.getInventory() instanceof Inventory inv) {
            if (GuiCloseListener.isntBazaarInventory(inv)) {
                GuiCloseListener.clearBazaarData(player);
            } else {
                // If they open a top-level category menu or search menu, clear transient selections (amounts, selected items etc.)
                String title = fun.ascent.common.StringUtility.getTextFromComponent(inv.getTitle()).toLowerCase();
                if (title.contains("➔") && !title.contains("instant")) {
                    // Player is navigating categories, clear old buy/sell inputs so they don't persist
                    java.util.UUID uuid = player.getUuid();
                    fun.ascent.skyblock.bazaar.ui.buySell.BazaarInstantBuyMenu.amount.remove(uuid);
                    fun.ascent.skyblock.bazaar.ui.buySell.BazaarInstantSellMenu.amounts.remove(uuid);
                }
            }
        }
    }
}
