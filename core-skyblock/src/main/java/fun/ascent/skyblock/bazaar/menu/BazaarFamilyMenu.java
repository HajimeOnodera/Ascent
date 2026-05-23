package fun.ascent.skyblock.bazaar.menu;

import fun.ascent.skyblock.bazaar.BazaarItem;
import fun.ascent.skyblock.bazaar.BazaarItemFamily;
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

public class BazaarFamilyMenu {

    public static void open(SkyblockPlayer player, BazaarCategory activeCategory, BazaarItemFamily family) {
        Inventory inventory = new Inventory(InventoryType.CHEST_6_ROW, MiniMessage.miniMessage().deserialize("<dark_gray>Bazaar ➔ " + family.name));

        // Borders and Filler
        ItemStack filler = ItemStack.builder(activeCategory.pane).customName(Component.empty()).build();
        for (int i = 0; i < 54; i++) {
            if (i < 9 || i > 44 || i % 9 == 0 || i % 9 == 8) {
                inventory.setItemStack(i, filler);
            }
        }

        // Left Column Categories
        int catIndex = 0;
        for (BazaarCategory category : BazaarCategory.values()) {
            int slot = catIndex * 9;
            if (category == activeCategory) {
                ItemStack icon = ItemStack.builder(category.icon).customName(MiniMessage.miniMessage().deserialize("<green>" + category.name)).lore(List.of(MiniMessage.miniMessage().deserialize("<gray>You are currently viewing"), MiniMessage.miniMessage().deserialize("<gray>this category."))).build();
                inventory.setItemStack(slot, icon);
            } else {
                ItemStack icon = ItemStack.builder(category.icon).customName(MiniMessage.miniMessage().deserialize("<yellow>" + category.name)).lore(List.of(MiniMessage.miniMessage().deserialize("<gray>Click to view!"))).build();
                inventory.setItemStack(slot, icon);
            }
            catIndex++;
        }

        // Add items from family
        int[] slots = {
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43
        };

        for (int i = 0; i < slots.length; i++) {
            if (i < family.productIds.size()) {
                String productId = family.productIds.get(i);
                SkyblockItem sbItem = ItemRegistry.getItem(productId);
                Material material = sbItem != null ? sbItem.getMaterial() : Material.STONE;
                String displayName = sbItem != null ? sbItem.getDisplayName() : productId;

                Price price = BazaarPriceRegistry.itemPrices.get(productId);
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

        // Navigation
        ItemStack back = ItemStack.builder(Material.ARROW)
                .customName(MiniMessage.miniMessage().deserialize("<green>Go Back"))
                .lore(List.of(MiniMessage.miniMessage().deserialize("<gray>To " + activeCategory.name)))
                .build();
        inventory.setItemStack(49, back);

        inventory.eventNode().addListener(net.minestom.server.event.inventory.InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            int slot = event.getSlot();
            net.minestom.server.entity.Player p = event.getPlayer();

            if (slot == 49) {
                BazaarCategoryMenu.open((SkyblockPlayer) p, activeCategory, 1);
            } else if (slot % 9 == 0 && slot < 45) {
                BazaarCategory target = BazaarCategory.values()[slot / 9];
                if (target != activeCategory) {
                    BazaarCategoryMenu.open((SkyblockPlayer) p, target, 1);
                }
            } else {
                for (int i = 0; i < slots.length; i++) {
                    if (slot == slots[i] && i < family.productIds.size()) {
                        String productId = family.productIds.get(i);
                        BazaarItem clickedItem = new BazaarItem(productId, false, activeCategory);
                        // Open Item Details menu
                        BazaarItemMenu.open((SkyblockPlayer) p, clickedItem, activeCategory, family);
                        break;
                    }
                }
            }
        });

        player.openInventory(inventory);
    }
}
