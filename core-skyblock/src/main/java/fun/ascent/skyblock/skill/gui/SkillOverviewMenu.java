package fun.ascent.skyblock.skill.gui;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import fun.ascent.skyblock.skill.PlayerSkillData;
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

public class SkillOverviewMenu {

    private static final int[] SKILL_SLOTS = {19, 20, 21, 22, 23, 24, 25, 28, 29, 30};
    private static final int CLOSE_SLOT = 49;

    public static void open(SkyblockPlayer player) {
        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW, "§aSkills");

        fillBorder(inv);

        ProfilePlayer data = player.getActiveProfileData();
        if (data != null) {
            SkillType[] types = SkillType.values();
            for (int i = 0; i < types.length && i < SKILL_SLOTS.length; i++) {
                SkillType type = types[i];
                inv.setItemStack(SKILL_SLOTS[i], buildSkillItem(type, data.skillData));
            }
        }

        inv.setItemStack(CLOSE_SLOT, closeButton());
        inv.eventNode().addListener(InventoryPreClickEvent.class, SkillOverviewMenu::handleClick);

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
        double progress = data.getProgressToNextLevel(type);

        List<Component> lore = new ArrayList<>();
        for (String line : type.definition().description()) {
            lore.add(Component.text(line));
        }
        lore.add(Component.text(" "));

        if (nextLevel != null) {
            SkillReward reward = type.definition().rewardAt(nextLevel);
            String progressText = String.format("§7Progress to Level %s: §e%.1f%%",
                    SkillReward.toRoman(nextLevel), progress * 100);
            lore.add(Component.text(progressText));
            lore.add(Component.text(data.progressBar(type)));
            lore.add(Component.text(" "));
            reward.toLore().forEach(s -> lore.add(Component.text(s)));
        } else {
            lore.add(Component.text("§aMAXED OUT!"));
        }

        lore.add(Component.text(" "));
        lore.add(Component.text("§eClick to view!"));

        String displayName = "§a" + type.definition().name() + " " + SkillReward.toRoman(level);

        return ItemStack.builder(type.definition().icon())
                .customName(Component.text(displayName))
                .lore(lore)
                .build();
    }

    private static void fillBorder(Inventory inv) {
        ItemStack filler = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
                .customName(Component.text(" "))
                .build();

        for (int i = 0; i < inv.getSize(); i++) {
            int row = i / 9;
            int col = i % 9;
            if (row == 0 || row == 5 || col == 0 || col == 8) {
                inv.setItemStack(i, filler);
            }
        }
    }

    private static ItemStack closeButton() {
        return ItemStack.builder(Material.BARRIER)
                .customName(Component.text("§cClose"))
                .build();
    }
}
