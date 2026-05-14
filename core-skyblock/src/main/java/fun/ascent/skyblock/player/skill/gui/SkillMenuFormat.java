package fun.ascent.skyblock.player.skill.gui;

import fun.ascent.skyblock.player.skill.PlayerSkillData;
import fun.ascent.skyblock.player.skill.SkillReward;
import fun.ascent.skyblock.player.skill.SkillType;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static fun.ascent.common.StringUtility.text;

final class SkillMenuFormat {

    private static final int PROGRESS_BAR_SEGMENTS = 17;
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getIntegerInstance(Locale.US);

    private SkillMenuFormat() {
    }

    static void fill(Inventory inventory) {
        ItemStack filler = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
                .customName(Component.text(" "))
                .build();

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            inventory.setItemStack(slot, filler);
        }
    }

    static void addProgress(List<Component> lore, PlayerSkillData data, SkillType type, double requirement, String prefix) {
        double current = data.getXpIntoCurrentLevel(type);
        double progress = requirement <= 0 ? 1.0 : Math.clamp(current / requirement, 0.0, 1.0);
        int filled = (int) Math.round(progress * PROGRESS_BAR_SEGMENTS);

        lore.add(text(prefix + "<yellow>" + String.format(Locale.US, "%.2f", progress * 100) + "<gold>%"));
        lore.add(text(
                "<dark_green><strikethrough>" + "─".repeat(Math.min(filled, PROGRESS_BAR_SEGMENTS))
                        + "<gray><strikethrough>" + "─".repeat(Math.max(PROGRESS_BAR_SEGMENTS - filled, 0))
                        + "<reset> <yellow>" + formatNumber(current)
                        + "<gold>/<yellow>" + formatNumber(requirement)
        ));
    }

    static ItemStack closeButton() {
        return ItemStack.builder(Material.BARRIER)
                .customName(text("<red>Close"))
                .build();
    }

    static ItemStack backButton() {
        return ItemStack.builder(Material.ARROW)
                .customName(text("<green>Go Back"))
                .lore(List.of(text("<gray>To Skills Menu")))
                .build();
    }

    static ItemStack infoButton() {
        return ItemStack.builder(Material.DIAMOND_SWORD)
                .customName(text("<green>Skills"))
                .lore(List.of(
                        text("<gray>View your Skill progression"),
                        text("<gray>and rewards."),
                        Component.text(" "),
                        text("<yellow>Click a Skill to view rewards!")
                ))
                .build();
    }

    static ItemStack nextPageButton() {
        return ItemStack.builder(Material.ARROW)
                .customName(text("<green>Next Page"))
                .lore(List.of(text("<gray>View the next page of rewards.")))
                .build();
    }

    static ItemStack previousPageButton() {
        return ItemStack.builder(Material.ARROW)
                .customName(text("<green>Previous Page"))
                .lore(List.of(text("<gray>View the previous page of rewards.")))
                .build();
    }

    static String skillNameWithLevel(SkillType type, int level) {
        String roman = SkillReward.toRoman(level);
        return "<green>" + type.definition().name() + (roman.isEmpty() ? "" : " " + roman);
    }

    private static String formatNumber(double value) {
        return NUMBER_FORMAT.format((long) Math.floor(value));
    }
}

