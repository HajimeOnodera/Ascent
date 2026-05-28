package fun.ascent.skyblock.player.skill.gui;

import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import fun.ascent.skyblock.player.skill.PlayerSkillData;
import fun.ascent.skyblock.player.skill.SkillReward;
import fun.ascent.skyblock.player.skill.SkillType;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static fun.ascent.common.StringUtility.text;

public class SkillOverviewMenu {

    private static final int[] SKILL_SLOTS = {
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 32, 33, 34
    };
    private static final int INFO_SLOT = 4;
    private static final int BACK_SLOT = 48;
    private static final int CLOSE_SLOT = 49;

    public static void open(SkyblockPlayer player) {
        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, "Your Skills");

        SkillMenuFormat.fill(inv);
        inv.setItemStack(INFO_SLOT, SkillMenuFormat.infoButton());
        inv.setItemStack(BACK_SLOT, SkillMenuFormat.backButton());

        ProfilePlayer data = player.getActiveProfileData();
        if (data != null) {
            SkillType[] types = SkillType.values();
            for (int i = 0; i < types.length && i < SKILL_SLOTS.length; i++) {
                SkillType type = types[i];
                inv.setItemStack(SKILL_SLOTS[i], buildSkillItem(type, data.skillData));
            }
        }

        inv.setItemStack(CLOSE_SLOT, SkillMenuFormat.closeButton());
        inv.eventNode().addListener(InventoryPreClickEvent.class, SkillOverviewMenu::handleClick);

        player.openInventory(inv);
    }

    private static void handleClick(InventoryPreClickEvent event) {
        if (!(event.getPlayer() instanceof SkyblockPlayer player))
            return;
        event.setCancelled(true);

        int slot = event.getSlot();
        if (slot == CLOSE_SLOT || slot == BACK_SLOT) {
            player.closeInventory();
            return;
        }

        for (int i = 0; i < SKILL_SLOTS.length; i++) {
            if (slot == SKILL_SLOTS[i]) {
                SkillType type = SkillType.values()[i];
                player.closeInventory();
                SkillDetailMenu.open(player, type, 0);
                return;
            }
        }
    }

    private static ItemStack buildSkillItem(SkillType type, PlayerSkillData data) {
        int level = data.getLevel(type);
        Integer nextLevel = data.getNextLevel(type);

        List<Component> lore = new ArrayList<>();
        for (String line : type.definition().description()) {
            lore.add(text(line));
        }
        lore.add(Component.text(" "));

        if (nextLevel != null) {
            SkillReward reward = type.definition().rewardAt(nextLevel);
            SkillMenuFormat.addProgress(lore, data, type, reward.xpRequired(),
                    "<gray>Progress to Level " + SkillReward.toRoman(nextLevel) + ": ");
            lore.add(Component.text(" "));
            reward.toLore().forEach(s -> lore.add(text(s)));
        } else {
            lore.add(text("<green>MAXED OUT!"));
        }

        lore.add(Component.text(" "));
        lore.add(text("<yellow>Click to view!"));

        return ItemStackCreator.clearAttributes(ItemStack.builder(type.definition().icon()))
                .customName(text(SkillMenuFormat.skillNameWithLevel(type, level)))
                .lore(lore)
                .build();
    }
}

