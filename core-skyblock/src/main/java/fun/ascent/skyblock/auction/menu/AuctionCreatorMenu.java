package fun.ascent.skyblock.auction.menu;

import fun.ascent.skyblock.auction.AuctionItem;
import fun.ascent.skyblock.auction.AuctionRegistry;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.item.SkyblockItemData;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import static fun.ascent.common.StringUtility.text;

public class AuctionCreatorMenu {

    public static void open(SkyblockPlayer player) {
        Inventory inventory = new Inventory(InventoryType.CHEST_6_ROW, text("<dark_gray>Create Auction"));

        // Fill borders
        ItemStack border = ItemStack.builder(Material.BLACK_STAINED_GLASS_PANE).customName(text(" ")).build();
        for (int i = 0; i < 54; i++) {
            inventory.setItemStack(i, border);
        }

        ItemStack handItem = player.getItemInMainHand();
        SkyblockItemData data = null;
        
        // Very basic detection. In a real system, you'd pull the SkyblockItemData from the NBT.
        if (!handItem.isAir()) {
            for (SkyblockItem item : ItemRegistry.getAllItems()) {
                if (item.getMaterial() == handItem.material()) {
                    data = item.convertToItemData();
                    break;
                }
            }
        }

        if (data == null) {
            ItemStack error = ItemStack.builder(Material.BARRIER)
                    .customName(text("<red>No Item Detected"))
                    .lore(text("<gray>You must hold a valid Skyblock item in"), text("<gray>your main hand to auction it."))
                    .build();
            inventory.setItemStack(13, error);
        } else {
            ItemStack displayItem = fun.ascent.skyblock.item.ItemRegistry.getItem(data.id()).buildItemStack();
            inventory.setItemStack(13, displayItem);

            ItemStack createBin = ItemStack.builder(Material.GOLD_BLOCK)
                    .customName(text("<gold>Create BIN Auction"))
                    .lore(text("<gray>List this item for a fixed"), text("<gray>price of 500 Coins (Default)."))
                    .build();
            inventory.setItemStack(29, createBin);

            ItemStack createBid = ItemStack.builder(Material.IRON_BLOCK)
                    .customName(text("<gold>Create Traditional Auction"))
                    .lore(text("<gray>List this item starting at"), text("<gray>100 Coins (Default)."))
                    .build();
            inventory.setItemStack(33, createBid);
        }

        ItemStack back = ItemStack.builder(Material.ARROW).customName(text("<green>Go Back")).build();
        inventory.setItemStack(49, back);

        final SkyblockItemData finalData = data;
        inventory.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            int slot = event.getSlot();
            if (slot == 49) {
                AuctionMenu.open(player);
            } else if (slot == 29 && finalData != null) {
                AuctionItem auction = new AuctionItem(finalData, player.getUuid(), player.getUsername(), 500.0, 0.0, null, true, System.currentTimeMillis() + 86400000L);
                AuctionRegistry.addAuction(auction);
                player.setItemInMainHand(ItemStack.AIR);
                player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Created BIN Auction for " + finalData.name() + "!"));
                player.closeInventory();
            } else if (slot == 33 && finalData != null) {
                AuctionItem auction = new AuctionItem(finalData, player.getUuid(), player.getUsername(), 100.0, 0.0, null, false, System.currentTimeMillis() + 86400000L);
                AuctionRegistry.addAuction(auction);
                player.setItemInMainHand(ItemStack.AIR);
                player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Created Traditional Auction for " + finalData.name() + "!"));
                player.closeInventory();
            }
        });

        player.openInventory(inventory);
    }
}
