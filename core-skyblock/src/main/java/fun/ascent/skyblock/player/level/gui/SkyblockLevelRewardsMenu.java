package fun.ascent.skyblock.player.level.gui;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.level.SkyBlockLevelRequirement;
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

public class SkyblockLevelRewardsMenu {

    private static final int FEATURE_SLOT = 11;
    private static final int PREFIX_SLOT = 12;
    private static final int EMBLEM_SLOT = 13;
    private static final int STATS_SLOT = 14;

    private static final int BACK_SLOT = 30;
    private static final int CLOSE_SLOT = 31;

    public static void open(SkyblockPlayer player) {
        Inventory inv = new Inventory(InventoryType.CHEST_4_ROW, "Level Rewards");
        fillBackground(inv);

        int level = player.getActiveProfileData() != null ? player.getActiveProfileData().level.curLevel : 0;
        SkyBlockLevelRequirement req = SkyBlockLevelRequirement.getLevel(level);

        inv.setItemStack(FEATURE_SLOT, buildFeatureItem(level));
        inv.setItemStack(PREFIX_SLOT, buildPrefixItem(req, level));
        inv.setItemStack(EMBLEM_SLOT, buildEmblemItem(level));
        inv.setItemStack(STATS_SLOT, buildStatsItem(level));
        inv.setItemStack(BACK_SLOT, buildBackButton());
        inv.setItemStack(CLOSE_SLOT, buildCloseButton());

        inv.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            int slot = event.getSlot();

            if (slot == CLOSE_SLOT) {
                player.closeInventory();
            } else if (slot == BACK_SLOT) {
                player.closeInventory();
                SkyblockLevelMenu.open(player);
            } else if (slot == EMBLEM_SLOT) {
                player.closeInventory();
                SkyblockLevelEmblemsMenu.open(player);
            } else if (slot == FEATURE_SLOT) {
                player.closeInventory();
                SkyblockLevelGuideMenu.open(player);
            }
        });

        player.openInventory(inv);
    }

    private static ItemStack buildFeatureItem(int level) {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>View game features and milestones you've"));
        lore.add(text("<gray>unlocked from leveling up."));
        lore.add(Component.empty());
        lore.add(text("<green>Highlighted Unlocks:"));
        lore.add(text("  <gray>• Access to Community Shop <dark_gray>(Level 3)"));
        lore.add(text("  <gray>• Access to Wardrobe & Garden <dark_gray>(Level 5)"));
        lore.add(text("  <gray>• Access to Bazaar <dark_gray>(Level 7)"));
        lore.add(Component.empty());
        lore.add(text("<yellow>Click to view SkyBlock Guide!"));

        return ItemStack.builder(Material.NETHER_STAR)
                .customName(text("<green>Feature Unlocks"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildPrefixItem(SkyBlockLevelRequirement req, int level) {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>Lists all Level Chat prefix colors"));
        lore.add(text("<gray>unlocked by your SkyBlock Level."));
        lore.add(Component.empty());

        if (req != null) {
            Map.Entry<SkyBlockLevelRequirement, String> nextPrefix = req.getNextPrefixChange();
            lore.add(text("<green>Next Prefix Upgrade:"));
            if (nextPrefix == null) {
                lore.add(text("  <gray>None (Max Prefix Color reached!)"));
            } else {
                String nextColor = nextPrefix.getValue();
                int nextLevel = nextPrefix.getKey().asInt();
                lore.add(text("  " + nextColor + "Prefix Color <gray>at Level " + nextLevel));
            }
            lore.add(Component.empty());
        }

        lore.add(text("<green>Prefix color thresholds:"));
        lore.add(text("  <gray>• Level 0: <gray>Gray Prefix"));
        lore.add(text("  <gray>• Level 40: <white>White Prefix"));
        lore.add(text("  <gray>• Level 80: <yellow>Yellow Prefix"));
        lore.add(text("  <gray>• Level 120: <green>Green Prefix"));
        lore.add(text("  <gray>• Level 160: <dark_green>Dark Green Prefix"));
        lore.add(text("  <gray>• Level 200: <aqua>Aqua Prefix"));
        lore.add(text("  <gray>• Level 240: <dark_aqua>Cyan Prefix"));
        lore.add(text("  <gray>• Level 280: <blue>Blue Prefix"));
        lore.add(text("  <gray>• Level 320: <light_purple>Pink Prefix"));
        lore.add(text("  <gray>• Level 360: <dark_purple>Purple Prefix"));
        lore.add(text("  <gray>• Level 400: <gold>Gold Prefix"));
        lore.add(text("  <gray>• Level 440: <red>Red Prefix"));
        lore.add(text("  <gray>• Level 480: <dark_red>Dark Red Prefix"));

        return ItemStack.builder(Material.GRAY_DYE)
                .customName(text("<green>Prefix Color Rewards"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildEmblemItem(int level) {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>Suffix prefix Chat Emblems unlocked"));
        lore.add(text("<gray>via leveling milestones."));
        lore.add(Component.empty());
        lore.add(text("<gray>Unlocked Emblems: <green>" + (level >= 10 ? "2" : level >= 5 ? "1" : "0") + " / 3"));
        lore.add(Component.empty());
        lore.add(text("<yellow>Click to change prefix Emblem!"));

        return ItemStack.builder(Material.NAME_TAG)
                .customName(text("<green>Emblem Suffix Rewards"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildStatsItem(int level) {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>Leveling up provides direct passive"));
        lore.add(text("<gray>statistic boosts to your profile."));
        lore.add(Component.empty());
        lore.add(text("<green>Active Stat Boosts:"));
        lore.add(text("  <gray>• Max Health: <red>+" + (level * 5) + " ❤ HP"));
        lore.add(text("  <gray>• Strength: <red>+" + (level / 5) + " ❁ Strength"));
        lore.add(Component.empty());
        lore.add(text("<gray>Boost perks rate:"));
        lore.add(text("  <gray>• Health: <red>+5 ❤ <gray>per SkyBlock Level"));
        lore.add(text("  <gray>• Strength: <red>+1 ❁ <gray>every 5 SkyBlock Levels"));

        return ItemStack.builder(Material.DIAMOND_HELMET)
                .customName(text("<green>Statistic Rewards"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildBackButton() {
        return ItemStack.builder(Material.ARROW)
                .customName(text("<green>Go Back"))
                .lore(List.of(text("<gray>To SkyBlock Levels")))
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
