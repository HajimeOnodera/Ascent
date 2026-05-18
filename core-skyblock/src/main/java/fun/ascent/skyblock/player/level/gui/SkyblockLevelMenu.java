package fun.ascent.skyblock.player.level.gui;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.level.SkyblockLevel;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
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

public class SkyblockLevelMenu {

    private static final int INFO_SLOT = 4;
    private static final int GUIDE_SLOT = 25;
    private static final int REWARDS_SLOT = 34;
    private static final int EMBLEMS_SLOT = 43;
    private static final int BACK_SLOT = 48;
    private static final int CLOSE_SLOT = 49;

    private static final int[] PROGRESSION_SLOTS = {19, 20, 21, 22, 23};

    public static void open(SkyblockPlayer player) {
        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, "<green>SkyBlock Levels");

        fillBackground(inv);

        ProfilePlayer profile = player.getActiveProfileData();
        if (profile == null) return;

        SkyblockLevel levelData = profile.level;
        int currentLevel = levelData.curLevel;
        double currentXp = levelData.progress.curProgress;

        // 1. Info slot (painting)
        inv.setItemStack(INFO_SLOT, buildRankingItem(currentLevel, currentXp));

        // 2. Progression slots (19 - 23)
        int startLevel = Math.max(1, currentLevel - 2);
        for (int i = 0; i < PROGRESSION_SLOTS.length; i++) {
            int targetLevel = startLevel + i;
            inv.setItemStack(PROGRESSION_SLOTS[i], buildProgressionItem(targetLevel, currentLevel));
        }

        // 3. Guide slot (map)
        inv.setItemStack(GUIDE_SLOT, buildGuideItem(player));

        // 4. Rewards slot (chest)
        inv.setItemStack(REWARDS_SLOT, buildRewardsOverviewItem(currentLevel));

        // 5. Emblems slot (nametag)
        inv.setItemStack(EMBLEMS_SLOT, buildEmblemsItem());

        // Navigation
        inv.setItemStack(BACK_SLOT, buildBackButton());
        inv.setItemStack(CLOSE_SLOT, buildCloseButton());

        inv.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            int slot = event.getSlot();

            if (slot == CLOSE_SLOT) {
                player.closeInventory();
            } else if (slot == BACK_SLOT) {
                player.closeInventory();
                SkyblockMenu.open(player);
            }
        });

        player.openInventory(inv);
    }

    private static ItemStack buildRankingItem(int level, double xp) {
        String color = SkyblockLevel.getLevelColour(level);
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>Your overall progress on the SkyBlock"));
        lore.add(text("<gray>network, tracked by your levels."));
        lore.add(Component.empty());
        lore.add(text("<gray>SkyBlock Level: " + color + "[" + level + "]"));
        lore.add(text("<gray>SkyBlock XP: <aqua>" + (int) xp + " XP"));
        lore.add(Component.empty());
        lore.add(text("<yellow>Click to view ranking details!"));

        return ItemStack.builder(Material.PAINTING)
                .customName(text("<green>Your SkyBlock Level Ranking"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildProgressionItem(int level, int currentLevel) {
        String levelColor = SkyblockLevel.getLevelColour(level);
        Material paneColor;
        String statusText;

        if (level < currentLevel) {
            paneColor = Material.RED_STAINED_GLASS_PANE;
            statusText = "<red>Completed";
        } else if (level == currentLevel) {
            paneColor = Material.LIME_STAINED_GLASS_PANE;
            statusText = "<green>Current Level";
        } else if (level == currentLevel + 1) {
            paneColor = Material.YELLOW_STAINED_GLASS_PANE;
            statusText = "<yellow>Next Level";
        } else {
            paneColor = Material.GRAY_STAINED_GLASS_PANE;
            statusText = "<gray>Locked";
        }

        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>Status: " + statusText));
        lore.add(Component.empty());
        lore.add(text("<green>Rewards:"));
        lore.add(text("  <dark_gray>+<red>5 <red>❤ Health"));
        if (level % 5 == 0) {
            lore.add(text("  <dark_gray>+<red>1 <red>❁ Strength"));
        }

        Map<String, ItemStack> unlocks = SkyblockLevel.getRewards(level, level);
        if (!unlocks.isEmpty()) {
            lore.add(Component.empty());
            lore.add(text("<gold>Special Unlocks:"));
            for (String unlock : unlocks.keySet()) {
                lore.add(text("  <gray>• " + unlock));
            }
        }

        return ItemStack.builder(paneColor)
                .customName(text(levelColor + "Level " + level))
                .lore(lore)
                .build();
    }

    private static ItemStack buildGuideItem(SkyblockPlayer player) {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>The SkyBlock Guide recommends what"));
        lore.add(text("<gray>to do next on your journey."));
        lore.add(Component.empty());
        lore.add(text("<green>Active Milestones:"));
        lore.add(text("  <gray>• Level up Combat, Mining, Farming"));
        lore.add(text("  <gray>• Unlock Collections & Recipes"));
        lore.add(text("  <gray>• Claim Minion slots"));
        lore.add(Component.empty());
        lore.add(text("<yellow>Click to open SkyBlock Guide!"));

        return ItemStack.builder(Material.FILLED_MAP)
                .customName(text("<green>SkyBlock Guide"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildRewardsOverviewItem(int currentLevel) {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>View all rewards and stat perks"));
        lore.add(text("<gray>unlocked from leveling up."));
        lore.add(Component.empty());
        lore.add(text("<gray>Total Max Health Boost: <red>+" + (currentLevel * 5) + " HP"));
        lore.add(text("<gray>Total Strength Boost: <red>+" + (currentLevel / 5) + " Strength"));
        lore.add(Component.empty());
        lore.add(text("<yellow>Click to view all Level Rewards!"));

        return ItemStack.builder(Material.CHEST)
                .customName(text("<green>Level Rewards"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildEmblemsItem() {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>Select prefix emblems to showcase"));
        lore.add(text("<gray>your achievements in chat."));
        lore.add(Component.empty());
        lore.add(text("<gray>Current Emblem: <green>Default"));
        lore.add(Component.empty());
        lore.add(text("<yellow>Click to change prefix Emblem!"));

        return ItemStack.builder(Material.NAME_TAG)
                .customName(text("<green>Chat Emblems"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildBackButton() {
        return ItemStack.builder(Material.ARROW)
                .customName(text("<green>Go Back"))
                .lore(List.of(text("<gray>To SkyBlock Menu")))
                .build();
    }

    private static ItemStack buildCloseButton() {
        return ItemStack.builder(Material.BARRIER)
                .customName(text("<red>Close"))
                .build();
    }

    private static void fillBackground(Inventory inv) {
        ItemStack pane = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
                .customName(Component.empty())
                .build();
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItemStack(i, pane);
        }
    }
}
