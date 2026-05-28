package fun.ascent.skyblock.player.level.gui;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.level.SkyBlockLevelRequirement;
import fun.ascent.skyblock.player.level.CustomLevelAward;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import fun.ascent.common.item.ItemStackCreator;
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
        Inventory inv = new Inventory(InventoryType.CHEST_4_ROW, "Leveling Rewards");
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
                SkyblockLevelFeatureRewardsMenu.open(player);
            } else if (slot == PREFIX_SLOT) {
                player.closeInventory();
                SkyblockLevelPrefixRewardsMenu.open(player);
            }
        });

        player.openInventory(inv);
    }

    private static List<Component> getLoadingBarDisplay(int unlocked, int total) {
        List<Component> toReturn = new ArrayList<>();
        double percentage = total == 0 ? 0 : (unlocked / (double) total) * 100;
        toReturn.add(text("<gray>Rewards Unlocked: <aqua>" + String.format("%.0f%%", percentage)));

        String baseLoadingBar = "─────────────────";
        int maxBarLength = baseLoadingBar.length();
        int completedLength = total == 0 ? 0 : (int) ((unlocked / (double) total) * maxBarLength);
        
        String completedLoadingBar = "<aqua><st>" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength)) + "</st></aqua>";
        String uncompletedLoadingBar = "<gray><st>" + baseLoadingBar.substring(Math.min(completedLength, maxBarLength)) + "</st></gray>";
        toReturn.add(text(completedLoadingBar + uncompletedLoadingBar + " <aqua>" + unlocked + "</aqua><gray>/</gray><aqua>" + total + "</aqua>"));
        
        return toReturn;
    }

    private static ItemStack buildFeatureItem(int level) {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>Specific game features such as the"));
        lore.add(text("<gray>Bazaar or Community Shop."));
        lore.add(Component.empty());
        
        int unlocked = CustomLevelAward.getFromLevel(level).size();
        int total = CustomLevelAward.getTotalLevelAwards();
        lore.addAll(getLoadingBarDisplay(unlocked, total));
        lore.add(Component.empty());
        lore.add(text("<yellow>Click to view rewards!"));

        return ItemStackCreator.clearAttributes(ItemStack.builder(Material.NETHER_STAR)
                .customName(text("<green>Feature Rewards"))
                .lore(lore))
                .build();
    }

    private static ItemStack buildPrefixItem(SkyBlockLevelRequirement req, int level) {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>New colors for your level prefix"));
        lore.add(text("<gray>shown in TAB and in chat!"));
        lore.add(Component.empty());

        int unlocked = req != null ? req.getPreviousPrefixChanges().size() : 0;
        int total = SkyBlockLevelRequirement.getAllPrefixChanges().size();
        lore.addAll(getLoadingBarDisplay(unlocked, total));
        lore.add(Component.empty());
        lore.add(text("<yellow>Click to view rewards!"));

        return ItemStackCreator.clearAttributes(ItemStack.builder(Material.GRAY_DYE)
                .customName(text("<green>Prefix Color Rewards"))
                .lore(lore))
                .build();
    }

    private static ItemStack buildEmblemItem(int level) {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>Suffix prefix Chat Emblems unlocked"));
        lore.add(text("<gray>via leveling milestones."));
        lore.add(Component.empty());

        int unlocked = level >= 15 ? 3 : level >= 10 ? 2 : level >= 5 ? 1 : 0;
        int total = 3;
        lore.addAll(getLoadingBarDisplay(unlocked, total));
        lore.add(Component.empty());
        lore.add(text("<yellow>Click to change prefix Emblem!"));

        return ItemStackCreator.clearAttributes(ItemStack.builder(Material.NAME_TAG)
                .customName(text("<green>Emblem Suffix Rewards"))
                .lore(lore))
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

        return ItemStackCreator.clearAttributes(ItemStackCreator.clearAttributes(ItemStack.builder(Material.DIAMOND_HELMET))
                .customName(text("<green>Statistic Rewards"))
                .lore(lore))
                .build();
    }

    private static ItemStack buildBackButton() {
        return ItemStackCreator.clearAttributes(ItemStack.builder(Material.ARROW)
                .customName(text("<green>Go Back"))
                .lore(List.of(text("<gray>To SkyBlock Levels"))))
                .build();
    }

    private static ItemStack buildCloseButton() {
        return ItemStackCreator.clearAttributes(ItemStack.builder(Material.BARRIER)
                .customName(text("<red>Close")))
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
