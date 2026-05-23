package fun.ascent.skyblock.bazaar.menu;

import fun.ascent.skyblock.bazaar.BazaarItem;
import fun.ascent.skyblock.bazaar.price.BazaarPriceRegistry;
import fun.ascent.skyblock.bazaar.price.Price;
import fun.ascent.skyblock.bazaar.vars.BazaarCategory;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;

public class BazaarSearchMenu {

    public static void open(SkyblockPlayer player, String query, List<BazaarItem> items, int page) {
        Inventory inventory = new Inventory(InventoryType.CHEST_6_ROW, MiniMessage.miniMessage().deserialize("<dark_gray>Search: " + query));

        ItemStack filler = ItemStack.builder(Material.BLACK_STAINED_GLASS_PANE).customName(Component.empty()).build();
        for (int i = 0; i < 54; i++) {
            if (i < 9 || i > 44 || i % 9 == 0 || i % 9 == 8) {
                inventory.setItemStack(i, filler);
            }
        }

        int[] slots = {
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43
        };

        int maxPages = Math.max(1, (int) Math.ceil((double) items.size() / slots.length));
        int startIndex = (page - 1) * slots.length;

        for (int i = 0; i < slots.length; i++) {
            if (startIndex + i < items.size()) {
                BazaarItem bItem = items.get(startIndex + i);
                SkyblockItem sbItem = ItemRegistry.getItem(bItem.getProductId());
                Material material = sbItem != null ? sbItem.getMaterial() : Material.STONE;
                String displayName = sbItem != null ? sbItem.getDisplayName() : bItem.getProductId();

                Price price = BazaarPriceRegistry.itemPrices.get(bItem.getProductId());
                double buyPrice = price != null ? price.getBuyPrice() : 0.0;
                double sellPrice = price != null ? price.getSellPrice() : 0.0;

                ItemStack itemStack = ItemStack.builder(material)
                        .customName(MiniMessage.miniMessage().deserialize("<yellow>" + displayName))
                        .lore(List.of(
                                MiniMessage.miniMessage().deserialize("<gray>Buy Price: <gold>" + String.format("%.1f", buyPrice)),
                                MiniMessage.miniMessage().deserialize("<gray>Sell Price: <gold>" + String.format("%.1f", sellPrice)),
                                Component.empty(),
                                MiniMessage.miniMessage().deserialize("<yellow>Click to view!")
                        ))
                        .build();

                inventory.setItemStack(slots[i], itemStack);
            }
        }

        ItemStack back = ItemStack.builder(Material.ARROW)
                .customName(MiniMessage.miniMessage().deserialize("<green>Go Back"))
                .lore(List.of(MiniMessage.miniMessage().deserialize("<gray>To Bazaar")))
                .build();
        inventory.setItemStack(49, back);

        if (page > 1) {
            inventory.setItemStack(45, ItemStack.builder(Material.ARROW).customName(MiniMessage.miniMessage().deserialize("<yellow>Previous Page")).build());
        }

        if (page < maxPages) {
            inventory.setItemStack(53, ItemStack.builder(Material.ARROW).customName(MiniMessage.miniMessage().deserialize("<yellow>Next Page")).build());
        }

        inventory.eventNode().addListener(net.minestom.server.event.inventory.InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            int slot = event.getSlot();
            net.minestom.server.entity.Player p = event.getPlayer();

            if (slot == 49) {
                BazaarCategoryMenu.open((SkyblockPlayer) p, BazaarCategory.FARMING, 1);
            } else if (slot == 45 && page > 1) {
                open((SkyblockPlayer) p, query, items, page - 1);
            } else if (slot == 53 && page < maxPages) {
                open((SkyblockPlayer) p, query, items, page + 1);
            } else {
                for (int i = 0; i < slots.length; i++) {
                    if (slot == slots[i] && startIndex + i < items.size()) {
                        BazaarItem clickedItem = items.get(startIndex + i);
                        BazaarItemMenu.open((SkyblockPlayer) p, clickedItem, BazaarCategory.FARMING, null);
                        break;
                    }
                }
            }
        });

        player.openInventory(inventory);
    }
}
