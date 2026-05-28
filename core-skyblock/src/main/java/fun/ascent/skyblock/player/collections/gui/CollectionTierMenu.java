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

import static fun.ascent.common.StringUtility.text;

public class CollectionTierMenu {

    public static void open(SkyblockPlayer player, CollectionCategory.ItemCollection collection) {
        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, text("<grey>" + collection.name() + " Collection"));

        // Fill background
        ItemStack filler = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE).customName(Component.empty()).build();
        for (int i = 0; i < 54; i++) {
            inv.setItemStack(i, filler);
        }

        // Info item
        int collected = player.getActiveProfile().unlockedCollections.getOrDefault(collection.itemId(), 0);
        inv.setItemStack(4, ItemStack.builder(collection.icon())
                .customName(text("<yellow>" + collection.name()))
                .lore(
                        text("<gray>View all your " + collection.name() + " Collection"),
                        text("<gray>progress and rewards!"),
                        Component.empty(),
                        text("<gray>Total Collected: <yellow>" + String.format("%,d", collected))
                )
                .build());

        // Milestones
        int[] slots = {19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
        List<CollectionCategory.CollectionReward> rewards = collection.rewards();

        for (int i = 0; i < rewards.size() && i < slots.length; i++) {
            CollectionCategory.CollectionReward reward = rewards.get(i);
            int slot = slots[i];
            
            boolean completed = collected >= reward.requirement();
            boolean next = (i == 0 && collected < reward.requirement()) || (i > 0 && collected >= rewards.get(i-1).requirement() && collected < reward.requirement());
            
            Material material = completed ? Material.LIME_STAINED_GLASS_PANE : (next ? Material.YELLOW_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);
            String color = completed ? "<green>" : (next ? "<yellow>" : "<red>");
            
            List<Component> lore = new ArrayList<>();
            lore.add(text("<gray>Requirement: <yellow>" + String.format("%,d", reward.requirement()) + " " + collection.name()));
            lore.add(Component.empty());
            lore.add(text("<gray>Rewards:"));
            reward.unlocks().forEach(u -> lore.add(text("  <green>" + u.getDisplay())));
            lore.add(Component.empty());
            
            if (completed) {
                lore.add(text("<green>UNLOCKED"));
            } else {
                lore.add(text("<gray>Progress: <yellow>" + String.format("%.1f", (collected / (double) reward.requirement()) * 100) + "%"));
                lore.add(getProgressBar(collected, reward.requirement()));
                lore.add(Component.empty());
                lore.add(text("<red>LOCKED"));
            }

            inv.setItemStack(slot, ItemStack.builder(material)
                    .customName(text(color + collection.name() + " " + (i + 1)))
                    .lore(lore)
                    .build());
        }

        inv.setItemStack(49, ItemStack.builder(Material.BARRIER).customName(text("<red>Close")).build());
        inv.setItemStack(48, ItemStack.builder(Material.ARROW).customName(text("<green>Go Back")).build());

        inv.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            int slot = event.getSlot();
            if (slot == 49) player.closeInventory();
            if (slot == 48) {
                // Find category to go back to
                CollectionCategory category = CollectionRegistry.getCategoryFor(collection);
                if (category != null) CollectionCategoryMenu.open(player, category);
            }
        });

        player.openInventory(inv);
    }

    private static Component getProgressBar(int progress, int total) {
        String bar = "────────────────────";
        double ratio = (double) progress / total;
        int completed = (int) (ratio * bar.length());
        return text("<green><st>" + bar.substring(0, completed) + "</st></green><gray><st>" + bar.substring(completed) + "</st></gray> <yellow>" + progress + "<gold>/<yellow>" + total);
    }
}
