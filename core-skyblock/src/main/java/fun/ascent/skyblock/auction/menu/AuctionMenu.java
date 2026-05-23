package fun.ascent.skyblock.auction.menu;

import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import static fun.ascent.common.StringUtility.text;

public class AuctionMenu {

    public static void open(SkyblockPlayer player) {
        Inventory inventory = new Inventory(InventoryType.CHEST_6_ROW, text("<dark_gray>Auction House"));

        // Fill borders
        ItemStack border = ItemStack.builder(Material.BLACK_STAINED_GLASS_PANE).customName(text(" ")).build();
        for (int i = 0; i < 54; i++) {
            if (i < 9 || i > 44 || i % 9 == 0 || i % 9 == 8) {
                inventory.setItemStack(i, border);
            }
        }

        // Browse Auctions
        ItemStack browse = ItemStack.builder(Material.GOLDEN_HORSE_ARMOR)
                .customName(text("<gold>Browse Auctions"))
                .lore(text("<gray>Find items for sale by players."))
                .build();
        inventory.setItemStack(11, browse);

        // View Bids
        ItemStack bids = ItemStack.builder(Material.GOLD_NUGGET)
                .customName(text("<gold>View Bids"))
                .lore(text("<gray>View your active bids."))
                .build();
        inventory.setItemStack(13, bids);

        // Manage Auctions
        ItemStack manage = ItemStack.builder(Material.ANVIL)
                .customName(text("<gold>Manage Auctions"))
                .lore(text("<gray>Create or claim your own auctions."))
                .build();
        inventory.setItemStack(15, manage);

        ItemStack close = ItemStack.builder(Material.BARRIER).customName(text("<red>Close")).build();
        inventory.setItemStack(49, close);

        inventory.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            int slot = event.getSlot();
            if (slot == 49) {
                player.closeInventory();
            } else if (slot == 11) {
                AuctionBrowserMenu.open(player, 1);
            } else if (slot == 13) {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Bids menu coming soon."));
            } else if (slot == 15) {
                AuctionCreatorMenu.open(player);
            }
        });

        player.openInventory(inventory);
    }
}
