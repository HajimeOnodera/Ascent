package fun.ascent.common.gui;

import fun.ascent.common.item.GUIClickableItem;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;

public class InventoryGUIListener {

    public static void register(GlobalEventHandler handler) {
        handler.addListener(InventoryPreClickEvent.class, event -> {
            Player player = event.getPlayer();
            InventoryGUI gui = InventoryGUI.GUI_MAP.get(player.getUuid());
            
            if (gui != null && event.getInventory() == gui.getInventory()) {
                event.setCancelled(true);
                int slot = event.getSlot();
                GUIItem item = gui.get(slot);
                
                if (item instanceof GUIClickableItem clickable) {
                    clickable.run(event, player);
                }
            }
        });

        handler.addListener(InventoryClickEvent.class, event -> {
            Player player = event.getPlayer();
            InventoryGUI gui = InventoryGUI.GUI_MAP.get(player.getUuid());

            if (gui != null && event.getInventory() == gui.getInventory()) {
                int slot = event.getSlot();
                GUIItem item = gui.get(slot);

                if (item instanceof GUIClickableItem clickable) {
                    clickable.runPost(event, player);
                }
            }
        });

        handler.addListener(InventoryCloseEvent.class, event -> {
            Player player = event.getPlayer();
            InventoryGUI gui = InventoryGUI.GUI_MAP.get(player.getUuid());

            if (gui != null && event.getInventory() == gui.getInventory()) {
                gui.onClose(event, InventoryGUI.CloseReason.PLAYER_EXITED);
                InventoryGUI.GUI_MAP.remove(player.getUuid());
            }
        });
    }
}
