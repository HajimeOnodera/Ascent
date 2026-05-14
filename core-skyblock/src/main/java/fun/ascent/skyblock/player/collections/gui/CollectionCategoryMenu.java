package fun.ascent.skyblock.player.collections.gui;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.collections.CollectionCategory;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static fun.ascent.common.StringUtility.text;

public class CollectionCategoryMenu {

    private static final int[] ITEM_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34
    };

    private static final int BACK_SLOT = 48;
    private static final int CLOSE_SLOT = 49;

    public static void open(SkyblockPlayer player, CollectionCategory category) {
        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, text("<green>" + category.getName() + " Collection"));

        CollectionMenuFormat.fill(inv);

        List<CollectionCategory.ItemCollection> collections = category.getCollections();
        for (int i = 0; i < collections.size() && i < ITEM_SLOTS.length; i++) {
            inv.setItemStack(ITEM_SLOTS[i], buildCollectionItem(player, collections.get(i)));
        }

        inv.setItemStack(BACK_SLOT, CollectionMenuFormat.backButton("Collection Menu"));
        inv.setItemStack(CLOSE_SLOT, CollectionMenuFormat.closeButton());

        inv.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            if (!(event.getPlayer() instanceof SkyblockPlayer pl)) return;
            event.setCancelled(true);

            int slot = event.getSlot();
            if (slot == CLOSE_SLOT) {
                pl.closeInventory();
                return;
            }
            if (slot == BACK_SLOT) {
                CollectionOverviewMenu.open(pl);
                return;
            }

            for (int i = 0; i < collections.size() && i < ITEM_SLOTS.length; i++) {
                if (slot == ITEM_SLOTS[i]) {
                    CollectionTierMenu.open(pl, collections.get(i));
                    return;
                }
            }
        });

        player.openInventory(inv);
    }

    private static ItemStack buildCollectionItem(SkyblockPlayer player, CollectionCategory.ItemCollection collection) {
        int progress = player.getActiveProfile().unlockedCollections.getOrDefault(collection.itemId(), 0);
        int currentTier = collection.getTierFromProgress(progress);
        CollectionCategory.CollectionReward nextReward = collection.getRewardAtTier(currentTier + 1);

        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>View all your " + collection.name() + " Collection"));
        lore.add(text("<gray>progression and rewards!"));
        lore.add(Component.text(" "));
        
        if (nextReward != null) {
            CollectionMenuFormat.addProgress(lore, progress, nextReward.requirement(), "<gray>Progress to " + collection.name() + " " + (currentTier + 1) + ": ");
            lore.add(Component.text(" "));
            lore.add(text("<gray>Rewards:"));
            nextReward.unlocks().forEach(u -> lore.add(text("  <green>" + u.getDisplay())));
        } else {
            lore.add(text("<green>MAXED OUT!"));
        }

        lore.add(Component.text(" "));
        lore.add(text("<yellow>Click to view rewards!"));

        return ItemStack.builder(collection.icon())
                .customName(text("<green>" + collection.name() + " " + (currentTier > 0 ? currentTier : "")))
                .lore(lore)
                .build();
    }
}
