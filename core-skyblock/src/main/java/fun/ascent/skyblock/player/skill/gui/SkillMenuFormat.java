<<<<<<<< HEAD:core-skyblock/src/main/java/fun/ascent/skyblock/menus/SkillMenuFormat.java
package fun.ascent.skyblock.menus;
========
package fun.ascent.skyblock.player.skill.gui;
>>>>>>>> origin/main:core-skyblock/src/main/java/fun/ascent/skyblock/player/skill/gui/SkillMenuFormat.java

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
        double progress = requirement <= 0 ? 1.0 : Math.max(0.0, Math.min(1.0, current / requirement));
        int filled = (int) Math.round(progress * PROGRESS_BAR_SEGMENTS);

        lore.add(Component.text(prefix + "§e" + String.format(Locale.US, "%.2f", progress * 100) + "§6%"));
        lore.add(Component.text(
                "§2§m" + "─".repeat(Math.min(filled, PROGRESS_BAR_SEGMENTS))
                        + "§7§m" + "─".repeat(Math.max(PROGRESS_BAR_SEGMENTS - filled, 0))
                        + "§r §e" + formatNumber(current)
                        + "§6/§e" + formatNumber(requirement)
        ));
    }

    static ItemStack closeButton() {
        return ItemStack.builder(Material.BARRIER)
                .customName(Component.text("§cClose"))
                .build();
    }

    static ItemStack backButton() {
        return ItemStack.builder(Material.ARROW)
                .customName(Component.text("§aGo Back"))
                .lore(List.of(Component.text("§7To Skills Menu")))
                .build();
    }

    static ItemStack infoButton() {
        return ItemStack.builder(Material.DIAMOND_SWORD)
                .customName(Component.text("§aSkills"))
                .lore(List.of(
                        Component.text("§7View your Skill progression"),
                        Component.text("§7and rewards."),
                        Component.text(" "),
                        Component.text("§eClick a Skill to view rewards!")
                ))
                .build();
    }

    static ItemStack nextPageButton() {
        return ItemStack.builder(Material.ARROW)
                .customName(Component.text("§aNext Page"))
                .lore(List.of(Component.text("§7View the next page of rewards.")))
                .build();
    }

    static ItemStack previousPageButton() {
        return ItemStack.builder(Material.ARROW)
                .customName(Component.text("§aPrevious Page"))
                .lore(List.of(Component.text("§7View the previous page of rewards.")))
                .build();
    }

    static String skillNameWithLevel(SkillType type, int level) {
        String roman = SkillReward.toRoman(level);
        return "§a" + type.definition().name() + (roman.isEmpty() ? "" : " " + roman);
    }

    private static String formatNumber(double value) {
        return NUMBER_FORMAT.format((long) Math.floor(value));
    }
}
