package fun.ascent.skyblock.bazaar.menu;

import fun.ascent.skyblock.bazaar.vars.BazaarCategory;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;

public class BazaarCategoryMenu {

    public static void open(SkyblockPlayer player, BazaarCategory activeCategory, int page) {
        Inventory inventory = new Inventory(InventoryType.CHEST_6_ROW, MiniMessage.miniMessage().deserialize(activeCategory.titleMiniMessage));

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

        java.util.List<fun.ascent.skyblock.bazaar.BazaarItemFamily> families = fun.ascent.skyblock.bazaar.BazaarRegistry.familyList.get(activeCategory);
        int maxPages = Math.max(1, (int) Math.ceil((double) families.size() / slots.length));
        int startIndex = (page - 1) * slots.length;

        for (int i = 0; i < slots.length; i++) {
            if (startIndex + i < families.size()) {
                fun.ascent.skyblock.bazaar.BazaarItemFamily family = families.get(startIndex + i);
                
                ItemStack itemStack = ItemStack.builder(family.icon)
                        .customName(MiniMessage.miniMessage().deserialize("<yellow>" + family.name))
                        .lore(List.of(
                                MiniMessage.miniMessage().deserialize("<gray>Click to view products!")
                        ))
                        .build();

                inventory.setItemStack(slots[i], itemStack);
            } else {
                inventory.setItemStack(slots[i], ItemStack.of(Material.AIR));
            }
        }

        // Bottom row icons
        inventory.setItemStack(46, ItemStack.builder(Material.CHEST).customName(MiniMessage.miniMessage().deserialize("<green>Sell Inventory Now")).build());
        inventory.setItemStack(49, ItemStack.builder(Material.BARRIER).customName(MiniMessage.miniMessage().deserialize("<red>Close")).build());
        inventory.setItemStack(50, ItemStack.builder(Material.WRITABLE_BOOK).customName(MiniMessage.miniMessage().deserialize("<yellow>Orders")).build());
        inventory.setItemStack(51, ItemStack.builder(Material.FILLED_MAP).customName(MiniMessage.miniMessage().deserialize("<yellow>Search")).build());
        inventory.setItemStack(52, ItemStack.builder(Material.REDSTONE_TORCH).customName(MiniMessage.miniMessage().deserialize("<yellow>Information")).build());

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
                p.closeInventory();
            } else if (slot == 51) {
                BazaarSearch.startSearch((SkyblockPlayer) p);
            } else if (slot == 45 && page > 1) {
                open((SkyblockPlayer) p, activeCategory, page - 1);
            } else if (slot == 53 && page < maxPages) {
                open((SkyblockPlayer) p, activeCategory, page + 1);
            } else if (slot % 9 == 0 && slot < 45) {
                // Category switch
                BazaarCategory target = BazaarCategory.values()[slot / 9];
                if (target != activeCategory) {
                    open((SkyblockPlayer) p, target, 1);
                }
            } else {
                for (int i = 0; i < slots.length; i++) {
                    if (slot == slots[i] && startIndex + i < families.size()) {
                        fun.ascent.skyblock.bazaar.BazaarItemFamily clickedFamily = families.get(startIndex + i);
                        // Open Family Menu
                        BazaarFamilyMenu.open((SkyblockPlayer) p, activeCategory, clickedFamily);
                        break;
                    }
                }
            }
        });

        player.openInventory(inventory);
    }
}
