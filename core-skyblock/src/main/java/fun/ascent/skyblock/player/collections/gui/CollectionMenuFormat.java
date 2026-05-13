package fun.ascent.skyblock.player.collections.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static fun.ascent.common.StringUtility.text;

public final class CollectionMenuFormat {

    private static final int PROGRESS_BAR_SEGMENTS = 17;
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getIntegerInstance(Locale.US);

    private CollectionMenuFormat() {
    }

    public static void fill(Inventory inventory) {
        ItemStack filler = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
                .customName(Component.text(" "))
                .build();

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            inventory.setItemStack(slot, filler);
        }
    }

    public static void addProgress(List<Component> lore, int current, int requirement, String prefix) {
        double progress = requirement <= 0 ? 1.0 : Math.clamp((double) current / requirement, 0.0, 1.0);
        int filled = (int) Math.round(progress * PROGRESS_BAR_SEGMENTS);

        lore.add(text(prefix + "<yellow>" + String.format(Locale.US, "%.2f", progress * 100) + "<gold>%"));
        lore.add(text(
                "<dark_green><strikethrough>" + "─".repeat(Math.min(filled, PROGRESS_BAR_SEGMENTS))
                        + "<gray><strikethrough>" + "─".repeat(Math.max(PROGRESS_BAR_SEGMENTS - filled, 0))
                        + "<reset> <yellow>" + formatNumber(current)
                        + "<gold>/<yellow>" + formatNumber(requirement)
        ));
    }

    public static ItemStack closeButton() {
        return ItemStack.builder(Material.BARRIER)
                .customName(text("<red>Close"))
                .build();
    }

    public static ItemStack backButton(String target) {
        return ItemStack.builder(Material.ARROW)
                .customName(text("<green>Go Back"))
                .lore(List.of(text("<gray>To " + target)))
                .build();
    }

    public static String formatNumber(double value) {
        return NUMBER_FORMAT.format((long) Math.floor(value));
    }
}
