package fun.ascent.skyblock.player.skill.gui;

import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import fun.ascent.skyblock.player.skill.PlayerSkillData;
import fun.ascent.skyblock.player.skill.SkillDefinition;
import fun.ascent.skyblock.player.skill.SkillReward;
import fun.ascent.skyblock.player.skill.SkillType;
import fun.ascent.skyblock.player.bestiary.gui.BestiaryMenuFormat;
import fun.ascent.skyblock.player.bestiary.gui.BestiaryOverviewMenu;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

import static fun.ascent.common.StringUtility.text;

public class SkillDetailMenu {

    private static final int[] REWARD_SLOTS = {
            9, 18, 27, 28, 29, 20, 11, 2, 3, 4, 13, 22, 31, 32, 33, 24, 15, 6, 7, 8, 17, 26, 35, 44, 53
    };
    private static final int PAGE_BACK_SLOT = 48;
    private static final int CLOSE_SLOT = 49;
    private static final int NEXT_PAGE_SLOT = 50;
    private static final int SKILL_INFO_SLOT = 0;
    private static final int BESTIARY_SLOT = 39;

    public static void open(SkyblockPlayer player, SkillType type, int page) {
        ProfilePlayer profileData = player.getActiveProfileData();
        if (profileData == null) return;

        SkillDefinition def = type.definition();
        PlayerSkillData skillData = profileData.skillData;
        int currentLevel = skillData.getLevel(type);

        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, "<green>" + type.getDisplayName() + " Skill");

        SkillMenuFormat.fill(inv);

        inv.setItemStack(SKILL_INFO_SLOT, buildInfoItem(type, def, skillData));
        if (type == SkillType.COMBAT) {
            inv.setItemStack(BESTIARY_SLOT, buildBestiaryItem(profileData));
        }

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

        inv.setItemStack(PAGE_BACK_SLOT, hasPrevPage ? SkillMenuFormat.previousPageButton() : SkillMenuFormat.backButton());
        inv.setItemStack(CLOSE_SLOT, SkillMenuFormat.closeButton());
        if (hasNextPage) inv.setItemStack(NEXT_PAGE_SLOT, SkillMenuFormat.nextPageButton());
        inv.eventNode().addListener(InventoryPreClickEvent.class, event -> handleClick(event, type, page, hasNextPage, hasPrevPage));

        player.openInventory(inv);
    }

    private static void handleClick(InventoryPreClickEvent event, SkillType type, int page, boolean hasNextPage, boolean hasPrevPage) {
        if (!(event.getPlayer() instanceof SkyblockPlayer player)) return;
        event.setCancelled(true);

        int slot = event.getSlot();
        if (slot == CLOSE_SLOT) {
            player.closeInventory();
            return;
        }
        if (slot == PAGE_BACK_SLOT && hasPrevPage) {
            player.closeInventory();
            open(player, type, page - 1);
            return;
        }
        if (slot == PAGE_BACK_SLOT) {
            player.closeInventory();
            SkillOverviewMenu.open(player);
            return;
        }
        if (slot == BESTIARY_SLOT && type == SkillType.COMBAT) {
            BestiaryOverviewMenu.open(player);
            return;
        }
        if (slot == NEXT_PAGE_SLOT && hasNextPage) {
            player.closeInventory();
            open(player, type, page + 1);
        }
    }

    private static ItemStack buildInfoItem(SkillType type, SkillDefinition def, PlayerSkillData data) {
        Integer nextLevel = data.getNextLevel(type);

        List<Component> lore = new ArrayList<>();
        def.description().forEach(s -> lore.add(text(s)));
        lore.add(Component.text(" "));

        if (nextLevel != null) {
            SkillReward reward = def.rewardAt(nextLevel);
            SkillMenuFormat.addProgress(lore, data, type, reward.xpRequired(),
                    "<gray>Progress to Level " + SkillReward.toRoman(nextLevel) + ": ");
        } else {
            lore.add(text("<green>MAX LEVEL REACHED!"));
        }

        lore.add(Component.text(" "));
        lore.add(text("<gray>View all rewards for this"));
        lore.add(text("<gray>Skill below."));

        return ItemStackCreator.clearAttributes(ItemStack.builder(def.icon()))
                .customName(text("<green>" + def.name() + " Skill"))
                .lore(lore)
                .build();
    }

    private static ItemStack buildRewardItem(SkillType type, SkillReward reward, int currentLevel, PlayerSkillData data) {
        boolean unlocked = currentLevel >= reward.level();
        boolean isCurrent = currentLevel + 1 == reward.level();

        Material glass = unlocked ? Material.LIME_STAINED_GLASS_PANE
                : isCurrent ? Material.YELLOW_STAINED_GLASS_PANE
                  : Material.RED_STAINED_GLASS_PANE;

        String color = unlocked ? "<green>" : isCurrent ? "<yellow>" : "<red>";
        String title = color + type.getDisplayName() + " Level " + SkillReward.toRoman(reward.level());

        List<Component> lore = new ArrayList<>();
        reward.toLore().forEach(s -> lore.add(text(s)));

        if (unlocked) {
            lore.add(Component.text(" "));
            lore.add(text("<green>UNLOCKED"));
        } else if (isCurrent) {
            lore.add(Component.text(" "));
            SkillMenuFormat.addProgress(lore, data, type, reward.xpRequired(), "<gray>Progress: ");
        }

        return ItemStack.builder(glass)
                .customName(text(title))
                .lore(lore)
                .build();
    }

    private static ItemStack buildBestiaryItem(ProfilePlayer profileData) {
        var progress = profileData.getBestiaryProgress();
        List<Component> lore = new ArrayList<>(BestiaryMenuFormat.overallLore(progress));
        lore.add(Component.text(" "));
        lore.add(text("<yellow>Click to view!"));

        return ItemStack.builder(Material.WRITABLE_BOOK)
                .customName(text("Bestiary"))
                .lore(lore)
                .build();
    }

}

