package fun.ascent.skyblock.auction.menu;

import fun.ascent.skyblock.auction.AuctionItem;
import fun.ascent.skyblock.auction.AuctionRegistry;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;

import static fun.ascent.common.StringUtility.text;

public class AuctionInspectMenu {

    public static void open(SkyblockPlayer player, AuctionItem item) {
        Inventory inventory = new Inventory(InventoryType.CHEST_6_ROW, text("<dark_gray>Inspect Auction"));

        // Fill borders
        ItemStack border = ItemStack.builder(Material.BLACK_STAINED_GLASS_PANE).customName(text(" ")).build();
        for (int i = 0; i < 54; i++) {
            inventory.setItemStack(i, border);
        }

        String type = item.isBin ? "BIN" : "Auction";
        String priceStr = item.isBin ? item.startingBid + "" : item.highestBid > 0 ? item.highestBid + "" : item.startingBid + "";

        ItemStack displayItem = fun.ascent.skyblock.item.ItemRegistry.getItem(item.item.id()).buildItemStack().withCustomName(text("<yellow>" + item.item.name()))
                .withLore(List.of(
                        text("<gray>Seller: <white>" + item.sellerName),
                        text("<gray>Type: <gold>" + type),
                        text("<gray>Price: <gold>" + priceStr + " Coins")
                ));
        inventory.setItemStack(13, displayItem);

        if (item.isBin) {
            ItemStack buyNow = ItemStack.builder(Material.GOLD_NUGGET)
                    .customName(text("<gold>Buy It Now"))
                    .lore(text("<gray>Purchase this item instantly"), text("<gray>for <gold>" + item.startingBid + " Coins<gray>."))
                    .build();
            inventory.setItemStack(31, buyNow);
        } else {
            ItemStack placeBid = ItemStack.builder(Material.GOLD_NUGGET)
                    .customName(text("<gold>Submit Bid"))
                    .lore(text("<gray>Place a new bid on this item."))
                    .build();
            inventory.setItemStack(31, placeBid);
        }

        ItemStack back = ItemStack.builder(Material.ARROW).customName(text("<green>Go Back")).build();
        inventory.setItemStack(49, back);

        inventory.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            int slot = event.getSlot();
            if (slot == 49) {
                AuctionBrowserMenu.open(player, 1);
            } else if (slot == 31) {
                if (item.sellerUUID.equals(player.getUuid())) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You cannot buy/bid on your own auction!"));
                    return;
                }
                if (item.isBin) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<green>You purchased " + item.item.name() + " for " + item.startingBid + " Coins!"));
                    AuctionRegistry.removeAuction(item);
                    player.getInventory().addItemStack(fun.ascent.skyblock.item.ItemRegistry.getItem(item.item.id()).buildItemStack());
                    player.closeInventory();
                } else {
                    double nextBid = item.highestBid > 0 ? item.highestBid + 100 : item.startingBid;
                    item.highestBid = nextBid;
                    item.highestBidder = player.getUuid();
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<green>You placed a bid of " + nextBid + " Coins!"));
                    open(player, item); // Refresh UI
                }
            }
        });

        player.openInventory(inventory);
    }
}
