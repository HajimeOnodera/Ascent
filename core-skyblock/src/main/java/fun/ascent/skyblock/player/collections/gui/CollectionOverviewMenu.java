package fun.ascent.skyblock.player.collections.gui;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.collections.CollectionCategory;
import fun.ascent.skyblock.player.collections.CollectionRegistry;
import fun.ascent.skyblock.menus.SkyblockMenu;
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
            CollectionCategory.CollectionType.BOSS, 31
    );

    private static final int INFO_SLOT = 4;
    private static final int BACK_SLOT = 48;
    private static final int CLOSE_SLOT = 49;
    private static final int CRAFTED_MINIONS_SLOT = 50;

    public static void open(SkyblockPlayer player) {
        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, text("Collections"));

        CollectionMenuFormat.fill(inv);

        inv.setItemStack(INFO_SLOT, buildInfoItem(player));

        Map<CollectionCategory.CollectionType, CollectionCategory> categories = CollectionRegistry.getCategories();
        for (Map.Entry<CollectionCategory.CollectionType, Integer> entry : CATEGORY_SLOTS.entrySet()) {
            CollectionCategory category = categories.get(entry.getKey());
            if (category != null) {
                inv.setItemStack(entry.getValue(), buildCategoryItem(player, category));
            } else {
                inv.setItemStack(entry.getValue(), buildLockedCategoryItem(entry.getKey()));
            }
        }

        inv.setItemStack(BACK_SLOT, CollectionMenuFormat.backButton("SkyBlock Menu"));
        inv.setItemStack(CLOSE_SLOT, CollectionMenuFormat.closeButton());
        inv.setItemStack(CRAFTED_MINIONS_SLOT, fun.ascent.common.item.ItemStackCreator.getStackHead("§aCrafted Minions",
                "ebcc099f3a00ece0e5c4b31d31c828e52b06348d0a4eac11f3fcbef3c05cb407", 1,
                "§7View all the unique minions that you",
                "§7have crafted.",
                "",
                "§eClick to view!").build());

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

        if (slot == BACK_SLOT) {
            SkyblockMenu.open(player);
            return;
        }

        if (slot == CRAFTED_MINIONS_SLOT) {
            player.sendMessage("§cMinion recipes and crafted minions view is coming soon!");
            player.playSound(net.kyori.adventure.sound.Sound.sound(net.minestom.server.sound.SoundEvent.BLOCK_NOTE_BLOCK_PLING, net.kyori.adventure.sound.Sound.Source.MASTER, 1f, 0.5f));
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
        CollectionMenuFormat.addProgress(lore, unlocked, total);

        lore.add(Component.text(" "));
        lore.add(text("<dark_gray>Also accessible via /collection."));
        lore.add(Component.text(" "));
        lore.add(text("<yellow>Click to show rankings!"));

        return ItemStack.builder(Material.PAINTING)
                .customName(text("<green>Collections"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildCategoryItem(SkyblockPlayer player, CollectionCategory category) {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>View your " + category.name() + " Collections!"));
        lore.add(Component.text(" "));

        int total = category.collections().size();
        int unlocked = 0;
        if (player.getActiveProfile() != null) {
            for (CollectionCategory.ItemCollection col : category.collections()) {
                if (player.getActiveProfile().unlockedCollections.getOrDefault(col.itemId(), 0) > 0) {
                    unlocked++;
                }
            }
        }

        double ratio = total == 0 ? 0.0 : (double) unlocked / total;
        int percent = (int) (ratio * 100);

        lore.add(text("<gray>Collections Unlocked: <yellow>" + percent + "<gold>%"));

        String baseBar = "─────────────────";
        int maxLen = baseBar.length();
        int filled = (int) Math.round(ratio * maxLen);

        String completed = filled > 0 ? "§2§m" + baseBar.substring(0, Math.min(filled, maxLen)) : "";
        String remaining = "§7§m" + baseBar.substring(Math.min(filled, maxLen));

        lore.add(text(completed + remaining + "§r <yellow>" + unlocked + "<gold>/<yellow>" + total));
        lore.add(Component.text(" "));
        lore.add(text("<yellow>Click to view!"));

        return ItemStack.builder(category.icon())
                .customName(text("<green>" + category.name() + " Collections"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildLockedCategoryItem(CollectionCategory.CollectionType type) {
        return ItemStack.builder(Material.BARRIER)
                .customName(text("<red>" + type.getDisplayName() + " Collections"))
                .lore(List.of(text("<gray>Coming soon!")))
                .build();
    }
}
