package fun.ascent.lobby.gui;

import fun.ascent.common.gui.InventoryGUI;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;

import static fun.ascent.common.StringUtility.color;

public class CollectiblesGUI extends InventoryGUI {

    public CollectiblesGUI() {
        super("Collectibles", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        e.player().sendMessage(color("&cCollectibles menu is currently under development!"));
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
