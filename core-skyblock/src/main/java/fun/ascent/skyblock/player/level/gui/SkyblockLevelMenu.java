package fun.ascent.skyblock.player.level.gui;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.level.SkyBlockLevelRequirement;
import fun.ascent.skyblock.player.level.SkyblockLevel;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import fun.ascent.skyblock.menus.SkyblockMenu;
import fun.ascent.skyblock.player.level.unlocks.CustomLevelUnlock;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import fun.ascent.common.item.ItemStackCreator;
import java.util.ArrayList;
import java.util.List;

import static fun.ascent.common.StringUtility.text;

public class SkyblockLevelMenu {

    private static final int INFO_SLOT = 4;
    private static final int GUIDE_SLOT = 25;
    private static final int REWARDS_SLOT = 34;
    private static final int EMBLEMS_SLOT = 43;
    private static final int BACK_SLOT = 48;
    private static final int CLOSE_SLOT = 49;
    private static final int NEXT_MILESTONE_SLOT = 30;

    private static final int[] PROGRESSION_SLOTS = {19, 20, 21, 22, 23};

    public static void open(SkyblockPlayer player) {
        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, "SkyBlock Leveling");

        fillBackground(inv);

        ProfilePlayer profile = player.getActiveProfileData();
        if (profile == null) return;

        SkyblockLevel levelData = profile.level;
        int currentLevel = levelData.curLevel;
        double currentXp = levelData.progress.curProgress;
        inv.setItemStack(INFO_SLOT, buildRankingItem(currentLevel, currentXp));

        for (int i = 0; i < PROGRESSION_SLOTS.length; i++) {
            int targetLevel = currentLevel + i;
            inv.setItemStack(PROGRESSION_SLOTS[i], buildProgressionItem(targetLevel, currentLevel, currentXp));
        }

        // Set Next Milestone item at slot 30
        SkyBlockLevelRequirement nextMilestone = getNextMilestone(currentLevel);
        if (nextMilestone != null) {
            inv.setItemStack(NEXT_MILESTONE_SLOT, buildNextMilestoneItem(nextMilestone));
        }

        inv.setItemStack(GUIDE_SLOT, buildGuideItem());
        inv.setItemStack(REWARDS_SLOT, buildRewardsOverviewItem());
        inv.setItemStack(EMBLEMS_SLOT, buildEmblemsItem());
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
            } else if (slot == GUIDE_SLOT) {
                player.closeInventory();
                SkyblockLevelGuideMenu.open(player);
            } else if (slot == REWARDS_SLOT) {
                player.closeInventory();
                SkyblockLevelRewardsMenu.open(player);
            } else if (slot == EMBLEMS_SLOT) {
                player.closeInventory();
                SkyblockLevelEmblemsMenu.open(player);
            } else if (slot == NEXT_MILESTONE_SLOT) {
                if (nextMilestone != null) {
                    player.closeInventory();
                    SkyblockLevelDetailsMenu.open(player, nextMilestone.getLevel());
                }
            } else {
                for (int i = 0; i < PROGRESSION_SLOTS.length; i++) {
                    if (slot == PROGRESSION_SLOTS[i]) {
                        int targetLevel = currentLevel + i;
                        player.closeInventory();
                        SkyblockLevelDetailsMenu.open(player, targetLevel);
                        break;
                    }
                }
            }
        });

        player.openInventory(inv);
    }

    private static SkyBlockLevelRequirement getNextMilestone(int currentLevel) {
        for (int l = currentLevel + 1; l <= SkyBlockLevelRequirement.getMaxLevel(); l++) {
            SkyBlockLevelRequirement req = SkyBlockLevelRequirement.getLevel(l);
            if (req != null) {
                boolean isMilestone = req.isMilestone() || req.getUnlocks().stream().anyMatch(unlock -> unlock instanceof CustomLevelUnlock);
                if (isMilestone) {
                    return req;
                }
            }
        }
        return null;
    }

    private static ItemStack buildNextMilestoneItem(SkyBlockLevelRequirement req) {
        ItemStack.Builder itemStackBuilder = null;
        for (var unlock : req.getUnlocks()) {
            if (unlock instanceof CustomLevelUnlock customUnlock) {
                itemStackBuilder = customUnlock.getAward().getItem();
                break;
            }
        }
        if (itemStackBuilder == null && req.getPrefixItem() != null) {
            itemStackBuilder = ItemStack.builder(req.getPrefixItem());
        }
        if (itemStackBuilder == null) {
            itemStackBuilder = ItemStack.builder(Material.NETHER_STAR);
        }

        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>Unlocks at: <gold>Level " + req.getLevel()));
        lore.add(Component.empty());
        lore.add(text("<gray>Rewards:"));

        List<String> unlockStrings = new ArrayList<>();
        req.getUnlocks().forEach(unlock -> unlockStrings.addAll(unlock.getDisplay(null, req.getLevel())));

        if (unlockStrings.isEmpty()) {
            unlockStrings.add("§8 +§a5 §c❤ Health");
        }

        for (String unlockStr : unlockStrings) {
            String formatted = unlockStr.replace("§8", "<dark_gray>")
                                      .replace("§a", "<green>")
                                      .replace("§c", "<red>")
                                      .replace("§e", "<yellow>")
                                      .replace("§6", "<gold>")
                                      .replace("§f", "<white>")
                                      .replace("§7", "<gray>");
            lore.add(text(formatted));
        }

        lore.add(Component.empty());
        lore.add(text("<yellow>Click to view rewards!"));

        return ItemStackCreator.clearAttributes(itemStackBuilder
                .customName(text("<gold>Next Milestone: Level " + req.getLevel()))
                .lore(lore))
                .build();
    }

    private static ItemStack buildRankingItem(int level, double xp) {
        String color = SkyblockLevel.getLevelColour(level);
        List<Component> lore = new ArrayList<>();
        lore.add(text("<dark_gray>Classic Mode"));
        lore.add(Component.empty());
        lore.add(text("<gray>Your level: " + color + level));
        lore.add(text("<gray>You have: <aqua>" + (int) xp + " XP"));
        lore.add(Component.empty());
        lore.add(text("<yellow>Click to view ranking details!"));

        return ItemStack.builder(Material.PAINTING)
                .customName(text("<green>Your SkyBlock Level Ranking"))
                .lore(lore)
                .build();
    }

    private static String getProgressBar(double current) {
        double percent = current / (double) 100;
        int completed = (int) (percent * 12);
        completed = Math.clamp(completed, 0, 12);

        StringBuilder bar = new StringBuilder();
        bar.append("<green><st>");
        bar.repeat("─", Math.max(0, completed));
        bar.append("</st></green><white><st>");
        bar.repeat("─", Math.max(0, 12 - completed));
        bar.append("</st></white>");
        return bar.toString();
    }

    private static ItemStack buildProgressionItem(int level, int currentLevel, double currentXp) {
        String levelColor = SkyblockLevel.getLevelColour(level);
        Material material;

        SkyBlockLevelRequirement req = SkyBlockLevelRequirement.getLevel(level);
        boolean isMilestone = req != null && (req.isMilestone() || req.getUnlocks().stream().anyMatch(unlock -> unlock instanceof CustomLevelUnlock));

        if (level == currentLevel) {
            material = isMilestone ? Material.LIME_STAINED_GLASS : Material.LIME_STAINED_GLASS_PANE;
        } else if (level == currentLevel + 1) {
            material = isMilestone ? Material.YELLOW_STAINED_GLASS : Material.YELLOW_STAINED_GLASS_PANE;
        } else if (level < currentLevel) {
            material = isMilestone ? Material.LIME_STAINED_GLASS : Material.LIME_STAINED_GLASS_PANE;
        } else {
            material = isMilestone ? Material.RED_STAINED_GLASS : Material.RED_STAINED_GLASS_PANE;
        }

        List<Component> lore = new ArrayList<>();
        if (level == currentLevel) {
            lore.add(text("<gray>Your Level"));
            lore.add(Component.empty());
        } else if (level == currentLevel + 1) {
            lore.add(text("<gray>Next Level"));
            lore.add(Component.empty());
        }

        List<String> unlockStrings = new ArrayList<>();
        if (req != null) {
            req.getUnlocks().forEach(unlock -> unlockStrings.addAll(unlock.getDisplay(null, level)));
        }

        if (unlockStrings.isEmpty()) {
            unlockStrings.add("§8 +§a5 §c❤ Health");
            if (level % 5 == 0) {
                unlockStrings.add("§8 +§a1 §c❁ Strength");
            }
        }

        if (unlockStrings.size() == 1) {
            lore.add(text("<gray>Reward:"));
        } else {
            lore.add(text("<gray>Rewards:"));
        }

        for (String unlockStr : unlockStrings) {
            String formatted = unlockStr.replace("§8", "<dark_gray>")
                                      .replace("§a", "<green>")
                                      .replace("§c", "<red>")
                                      .replace("§e", "<yellow>")
                                      .replace("§6", "<gold>")
                                      .replace("§f", "<white>")
                                      .replace("§7", "<gray>");
            lore.add(text(formatted));
        }

        if (level == currentLevel + 1) {
            lore.add(Component.empty());
            lore.add(text("<gray>Progress to Level Up:"));
            String bar = getProgressBar(currentXp);
            lore.add(text(bar + " <aqua>" + (int) currentXp + "</aqua><dark_gray>/</dark_gray><aqua>100 XP</aqua>"));
        }

        lore.add(Component.empty());
        if (level == currentLevel || level < currentLevel) {
            lore.add(text("<green><bold>UNLOCKED</bold></green>"));
            lore.add(Component.empty());
        }
        lore.add(text("<yellow>Click to view rewards!"));

        return ItemStackCreator.clearAttributes(ItemStack.builder(material)
                .customName(text(levelColor + "Level " + level))
                .lore(lore))
                .build();
    }

    private static ItemStack buildGuideItem() {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>Your <gold>SkyBlock Guide <gray>tracks the"));
        lore.add(text("<gray>progress you have made through"));
        lore.add(text("<gray>SkyBlock."));
        lore.add(Component.empty());
        lore.add(text("<gray>Complete tasks within your current"));
        lore.add(text("<gray>game stage to increase your"));
        lore.add(text("<aqua>SkyBlock Level <gray>and become a <pink>Master"));
        lore.add(text("<gray>of SkyBlock!"));
        lore.add(Component.empty());
        lore.add(text("<yellow>Click to view!"));

        return ItemStack.builder(Material.FILLED_MAP)
                .customName(text("<green>SkyBlock Guide"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildRewardsOverviewItem() {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>View all the rewards you can unlock"));
        lore.add(text("<gray>by leveling up your SkyBlock Level"));
        lore.add(Component.empty());
        lore.add(text("<yellow>Click to view rewards!"));

        return ItemStack.builder(Material.CHEST)
                .customName(text("<green>Leveling Rewards"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildEmblemsItem() {
        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>Add some spice by having an emblem"));
        lore.add(text("<gray>next to your name in chat and in tab!"));
        lore.add(Component.empty());
        lore.add(text("<gray>Emblems are unlocked through"));
        lore.add(text("<gray>various activites such as leveling up"));
        lore.add(text("<gray>or completing achievements!"));
        lore.add(Component.empty());
        lore.add(text("<gray>Emblems also show important data"));
        lore.add(text("<gray>associated with them in chat!"));
        lore.add(Component.empty());
        lore.add(text("<yellow>Click to view!"));

        return ItemStack.builder(Material.NAME_TAG)
                .customName(text("<green>Prefix Emblems"))
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
