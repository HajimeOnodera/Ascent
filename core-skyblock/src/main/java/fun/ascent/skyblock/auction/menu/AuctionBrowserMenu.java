package fun.ascent.skyblock.auction.menu;

import fun.ascent.skyblock.auction.AuctionItem;
import fun.ascent.skyblock.auction.AuctionRegistry;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;

import static fun.ascent.common.StringUtility.text;

public class AuctionBrowserMenu {

    private static final int[] slots = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    public static void open(SkyblockPlayer player, int page) {
        Inventory inventory = new Inventory(InventoryType.CHEST_6_ROW, text("<dark_gray>Auctions Browser"));

        // Fill borders
        ItemStack border = ItemStack.builder(Material.BLACK_STAINED_GLASS_PANE).customName(text(" ")).build();
        for (int i = 0; i < 54; i++) {
            if (i < 9 || i > 44 || i % 9 == 0 || i % 9 == 8) {
                inventory.setItemStack(i, border);
            }
        }

        List<AuctionItem> items = AuctionRegistry.allAuctions;

        int maxPages = Math.max(1, (int) Math.ceil((double) items.size() / slots.length));
        int startIndex = (page - 1) * slots.length;

        for (int i = 0; i < slots.length; i++) {
            if (startIndex + i < items.size()) {
                AuctionItem item = items.get(startIndex + i);
                
                String type = item.isBin ? "BIN" : "Auction";
                String priceStr = item.isBin ? item.startingBid + "" : item.highestBid > 0 ? item.highestBid + "" : item.startingBid + "";

                ItemStack displayItem = fun.ascent.skyblock.item.ItemRegistry.getItem(item.item.id()).buildItemStack().withCustomName(text("<yellow>" + item.item.name()))
                        .withLore(List.of(
                                text("<gray>Seller: <white>" + item.sellerName),
                                text("<gray>Type: <gold>" + type),
                                text("<gray>Price: <gold>" + priceStr + " Coins"),
                                text(""),
                                text("<yellow>Click to inspect!")
                        ));

                inventory.setItemStack(slots[i], displayItem);
            }
        }

        ItemStack back = ItemStack.builder(Material.ARROW).customName(text("<green>Go Back")).build();
        inventory.setItemStack(49, back);

        if (page > 1) {
            ItemStack prev = ItemStack.builder(Material.ARROW).customName(text("<green>Previous Page")).build();
            inventory.setItemStack(45, prev);
        }

        if (page < maxPages) {
            ItemStack next = ItemStack.builder(Material.ARROW).customName(text("<green>Next Page")).build();
            inventory.setItemStack(53, next);
        }

        inventory.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            int slot = event.getSlot();
            if (slot == 49) {
                AuctionMenu.open(player);
            } else if (slot == 45 && page > 1) {
                open(player, page - 1);
            } else if (slot == 53 && page < maxPages) {
                open(player, page + 1);
            } else {
                for (int i = 0; i < slots.length; i++) {
                    if (slot == slots[i] && startIndex + i < items.size()) {
                        AuctionItem clickedItem = items.get(startIndex + i);
                        AuctionInspectMenu.open(player, clickedItem);
                        break;
                    }
                }
            }
        });

        player.openInventory(inventory);
    }
}
