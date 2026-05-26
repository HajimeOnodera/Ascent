package fun.ascent.skyblock.player.collections.gui;

import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.collections.CollectionCategory;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

import static fun.ascent.common.StringUtility.text;

public class CollectionItemMenu {

    private static final int[] TIER_SLOTS = {
            18, 19, 20, 21, 22, 23, 24, 25, 26, 27
    };

    private static final int INFO_SLOT = 4;
    private static final int BACK_SLOT = 48;
    private static final int CLOSE_SLOT = 49;

    public static void open(SkyblockPlayer player, CollectionCategory.ItemCollection collection, CollectionCategory category) {
        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, text("<dark_gray>" + collection.name() + " Collection"));

        CollectionMenuFormat.fill(inv);

        int progress = player.getActiveProfile().unlockedCollections.getOrDefault(collection.itemId(), 0);
        int currentTier = collection.getTierFromProgress(progress);

        inv.setItemStack(INFO_SLOT, buildInfoItem(collection, progress, currentTier));

        List<CollectionCategory.CollectionReward> rewards = collection.rewards();
        for (int i = 0; i < rewards.size() && i < TIER_SLOTS.length; i++) {
            inv.setItemStack(TIER_SLOTS[i], buildTierItem(collection, rewards.get(i), i + 1, currentTier, progress));
        }

        inv.setItemStack(BACK_SLOT, CollectionMenuFormat.backButton(category.name() + " Collection"));
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
                CollectionCategoryMenu.open(pl, category);
            }
        });

        player.openInventory(inv);
    }

    private static ItemStack buildInfoItem(CollectionCategory.ItemCollection collection, int progress, int currentTier) {
        String roman = StringUtility.getAsRomanNumeral(currentTier);
        String name = collection.name() + (roman.isEmpty() ? "" : " " + roman);
        return ItemStack.builder(collection.icon())
                .customName(text("<yellow>" + name))
                .lore(List.of(
                        text("<gray>View all your " + collection.name() + " Collection"),
                        text("<gray>progress and rewards!"),
                        Component.text(" "),
                        text("<gray>Total Collected: <yellow>" + CollectionMenuFormat.formatNumber(progress))
                ))
                .build();
    }

    private static ItemStack buildTierItem(CollectionCategory.ItemCollection collection, CollectionCategory.CollectionReward reward, int tier, int playerTier, int currentProgress) {
        Material material;
        String color;

        if (playerTier >= tier) {
            material = Material.LIME_STAINED_GLASS_PANE;
            color = "<green>";
        } else if (playerTier == tier - 1) {
            material = Material.YELLOW_STAINED_GLASS_PANE;
            color = "<yellow>";
        } else {
            material = Material.RED_STAINED_GLASS_PANE;
            color = "<red>";
        }

        List<Component> lore = new ArrayList<>();
        
        // Progress: X%
        double progressRatio = reward.requirement() <= 0 ? 1.0 : Math.clamp((double) currentProgress / reward.requirement(), 0.0, 1.0);
        int percentInt = (int) Math.floor(progressRatio * 100);
        String percentColor = (percentInt >= 100) ? "<green>" : "<yellow>";
        lore.add(text("<gray>Progress: " + percentColor + percentInt + "%"));
        
        // Progress bar
        CollectionMenuFormat.addProgress(lore, currentProgress, reward.requirement());
        
        lore.add(Component.text(" "));
        lore.add(text("<gray>Rewards:"));
        reward.unlocks().forEach(u -> lore.add(text("  " + u.getDisplay())));
        
        lore.add(Component.text(" "));
        lore.add(text("<yellow>Click to view rewards!"));

        String roman = StringUtility.getAsRomanNumeral(tier);
        return ItemStack.builder(material)
                .amount(tier)
                .customName(text(color + collection.name() + " " + roman))
                .lore(lore)
                .build();
    }
}
