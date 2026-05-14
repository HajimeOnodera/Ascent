package fun.ascent.skyblock.player.collections.gui;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.collections.CollectionCategory;
import fun.ascent.skyblock.player.collections.CollectionRegistry;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static fun.ascent.common.StringUtility.text;

public class CollectionOverviewMenu {

    private static final Map<CollectionCategory.CollectionType, Integer> CATEGORY_SLOTS = Map.of(
            CollectionCategory.CollectionType.FARMING, 20,
            CollectionCategory.CollectionType.MINING, 21,
            CollectionCategory.CollectionType.COMBAT, 22,
            CollectionCategory.CollectionType.FORAGING, 23,
            CollectionCategory.CollectionType.FISHING, 24,
            CollectionCategory.CollectionType.BOSS, 31,
            CollectionCategory.CollectionType.OTHER, 32
    );

    private static final int INFO_SLOT = 4;
    private static final int CLOSE_SLOT = 49;

    public static void open(SkyblockPlayer player) {
        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, text("<green>Collection"));

        CollectionMenuFormat.fill(inv);

        inv.setItemStack(INFO_SLOT, buildInfoItem(player));

        Map<CollectionCategory.CollectionType, CollectionCategory> categories = CollectionRegistry.getCategories();
        for (Map.Entry<CollectionCategory.CollectionType, Integer> entry : CATEGORY_SLOTS.entrySet()) {
            CollectionCategory category = categories.get(entry.getKey());
            if (category != null) {
                inv.setItemStack(entry.getValue(), buildCategoryItem(category));
            } else {
                inv.setItemStack(entry.getValue(), buildLockedCategoryItem(entry.getKey()));
            }
        }

        inv.setItemStack(CLOSE_SLOT, CollectionMenuFormat.closeButton());
        inv.eventNode().addListener(InventoryPreClickEvent.class, CollectionOverviewMenu::handleClick);

        player.openInventory(inv);
    }

    private static void handleClick(InventoryPreClickEvent event) {
        if (!(event.getPlayer() instanceof SkyblockPlayer player)) return;
        event.setCancelled(true);

        int slot = event.getSlot();
        if (slot == CLOSE_SLOT) {
            player.closeInventory();
            return;
        }

        for (Map.Entry<CollectionCategory.CollectionType, Integer> entry : CATEGORY_SLOTS.entrySet()) {
            if (slot == entry.getValue()) {
                CollectionCategory category = CollectionRegistry.getCategory(entry.getKey());
                if (category != null) {
                    CollectionCategoryMenu.open(player, category);
                }
                return;
            }
        }
    }

    private static ItemStack buildInfoItem(SkyblockPlayer player) {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>View all of the items available in"));
        lore.add(text("<gray>SkyBlock. Collect more of an item to"));
        lore.add(text("<gray>unlock rewards on your way to"));
        lore.add(text("<gray>becoming a master of SkyBlock!"));
        lore.add(Component.text(" "));

        int unlocked = player.getActiveProfile().unlockedCollections.size();
        int total = CollectionRegistry.getTotalCollectionsCount();
        double percent = total == 0 ? 0 : (double) unlocked / total * 100;

        lore.add(text("<gray>Collections Unlocked: <yellow>" + String.format("%.1f", percent) + "<gold>%"));
        CollectionMenuFormat.addProgress(lore, unlocked, total, "");

        lore.add(Component.text(" "));
        lore.add(text("<yellow>Click to view!"));

        return ItemStack.builder(Material.PAINTING)
                .customName(text("<green>Collections"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildCategoryItem(CollectionCategory category) {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>View your " + category.getName() + " Collections!"));
        lore.add(Component.text(" "));
        lore.add(text("<yellow>Click to view!"));

        return ItemStack.builder(category.getIcon())
                .customName(text("<green>" + category.getName() + " Collection"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildLockedCategoryItem(CollectionCategory.CollectionType type) {
        return ItemStack.builder(Material.BARRIER)
                .customName(text("<red>" + type.getDisplayName() + " Collection"))
                .lore(List.of(text("<gray>Coming soon!")))
                .build();
    }
}
