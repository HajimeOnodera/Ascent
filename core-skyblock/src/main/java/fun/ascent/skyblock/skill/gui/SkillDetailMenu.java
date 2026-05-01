package fun.ascent.skyblock.skill.gui;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import fun.ascent.skyblock.skill.PlayerSkillData;
import fun.ascent.skyblock.skill.SkillDefinition;
import fun.ascent.skyblock.skill.SkillReward;
import fun.ascent.skyblock.skill.SkillType;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public class SkillDetailMenu {

    private static final int[] REWARD_SLOTS = {
            9, 18, 27, 28, 29, 20, 11, 2, 3, 4, 13, 22, 31, 32, 33, 24, 15, 6, 7, 8, 17, 26, 35, 44, 53
    };
    private static final int NEXT_PAGE_SLOT = 50;
    private static final int PREV_PAGE_SLOT = 48;
    private static final int BACK_SLOT = 45;
    private static final int SKILL_INFO_SLOT = 0;

    public static void open(SkyblockPlayer player, SkillType type, int page) {
        ProfilePlayer profileData = player.getActiveProfileData();
        if (profileData == null) return;

        SkillDefinition def = type.definition();
        PlayerSkillData skillData = profileData.skillData;
        int currentLevel = skillData.getLevel(type);

        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, "§a" + type.getDisplayName() + " Skill");

        fillBorder(inv);

        inv.setItemStack(SKILL_INFO_SLOT, buildInfoItem(type, def, skillData));
        inv.setItemStack(BACK_SLOT, buildBackButton());

        List<SkillReward> allRewards = List.of(def.rewards());
        int perPage = REWARD_SLOTS.length;
        int start = page * perPage;
        int end = Math.min(allRewards.size(), start + perPage);
        List<SkillReward> pageRewards = allRewards.subList(start, end);

        for (int i = 0; i < pageRewards.size(); i++) {
            SkillReward reward = pageRewards.get(i);
            inv.setItemStack(REWARD_SLOTS[i], buildRewardItem(type, reward, currentLevel, skillData));
        }

        boolean hasNextPage = end < allRewards.size();
        boolean hasPrevPage = page > 0;

        if (hasNextPage) inv.setItemStack(NEXT_PAGE_SLOT, buildNavButton("§eNext Page", Material.ARROW));
        if (hasPrevPage) inv.setItemStack(PREV_PAGE_SLOT, buildNavButton("§ePrevious Page", Material.ARROW));
        inv.eventNode().addListener(InventoryPreClickEvent.class, event -> handleClick(event, type, page, hasNextPage, hasPrevPage));

        player.openInventory(inv);
    }

    private static void handleClick(InventoryPreClickEvent event, SkillType type, int page, boolean hasNextPage, boolean hasPrevPage) {
        if (!(event.getPlayer() instanceof SkyblockPlayer player)) return;
        event.setCancelled(true);

        int slot = event.getSlot();
        if (slot == BACK_SLOT) {
            player.closeInventory();
            SkillOverviewMenu.open(player);
            return;
        }
        if (slot == NEXT_PAGE_SLOT && hasNextPage) {
            player.closeInventory();
            open(player, type, page + 1);
            return;
        }
        if (slot == PREV_PAGE_SLOT && hasPrevPage) {
            player.closeInventory();
            open(player, type, page - 1);
        }
    }

    private static ItemStack buildInfoItem(SkillType type, SkillDefinition def, PlayerSkillData data) {
        int level = data.getLevel(type);
        Integer nextLevel = data.getNextLevel(type);

        List<Component> lore = new ArrayList<>();
        def.description().forEach(s -> lore.add(Component.text(s)));
        lore.add(Component.text(" "));

        if (nextLevel != null) {
            SkillReward reward = def.rewardAt(nextLevel);
            lore.add(Component.text(String.format("§7Progress to Level %s: §e%.1f%%",
                    SkillReward.toRoman(nextLevel), data.getProgressToNextLevel(type) * 100)));
            lore.add(Component.text(data.progressBar(type)));

            String xpInLevel = String.format("§e%.0f§6/§e%.0f",
                    data.getXpIntoCurrentLevel(type), (double) reward.xpRequired());
            lore.add(Component.text("§7XP: " + xpInLevel));
        } else {
            lore.add(Component.text("§aMAX LEVEL REACHED!"));
        }

        return ItemStack.builder(def.icon())
                .customName(Component.text("§a" + def.name() + " Skill"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildRewardItem(SkillType type, SkillReward reward, int currentLevel, PlayerSkillData data) {
        boolean unlocked = currentLevel >= reward.level();
        boolean isCurrent = currentLevel + 1 == reward.level();

        Material glass = unlocked ? Material.LIME_STAINED_GLASS_PANE
                : isCurrent ? Material.YELLOW_STAINED_GLASS_PANE
                  : Material.RED_STAINED_GLASS_PANE;

        String color = unlocked ? "§a" : isCurrent ? "§e" : "§c";
        String title = color + type.getDisplayName() + " Level " + SkillReward.toRoman(reward.level());

        List<Component> lore = new ArrayList<>();
        reward.toLore().forEach(s -> lore.add(Component.text(s)));

        if (unlocked) {
            lore.add(Component.text(" "));
            lore.add(Component.text("§aUNLOCKED"));
        } else if (isCurrent) {
            lore.add(Component.text(" "));
            lore.add(Component.text(data.progressBar(type)));
        }

        return ItemStack.builder(glass)
                .customName(Component.text(title))
                .lore(lore)
                .build();
    }

    private static ItemStack buildNavButton(String name, Material material) {
        return ItemStack.builder(material)
                .customName(Component.text(name))
                .build();
    }

    private static ItemStack buildBackButton() {
        return ItemStack.builder(Material.ARROW)
                .customName(Component.text("§aGo Back"))
                .lore(List.of(Component.text("§7Return to Skills Menu")))
                .build();
    }

    private static void fillBorder(Inventory inv) {
        ItemStack filler = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
                .customName(Component.text(" "))
                .build();

        int size = inv.getSize();
        for (int i = 0; i < size; i++) {
            int row = i / 9;
            int col = i % 9;
            if (row == 0 || row == 5 || col == 0 || col == 8) {
                inv.setItemStack(i, filler);
            }
        }
    }
}
