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

    public static void addProgress(List<Component> lore, int current, int requirement) {
        double progress = requirement <= 0 ? 1.0 : Math.clamp((double) current / requirement, 0.0, 1.0);
        int filled = (int) Math.round(progress * PROGRESS_BAR_SEGMENTS);

        lore.add(text(
                "<green><strikethrough>" + "─".repeat(Math.min(filled, PROGRESS_BAR_SEGMENTS))
                        + "<gray><strikethrough>" + "─".repeat(Math.max(PROGRESS_BAR_SEGMENTS - filled, 0))
                        + "<reset> <yellow>" + formatNumber(current)
                        + "<gold>/<yellow>" + formatRequirement(requirement)
        ));
    }

    public static String formatRequirement(double value) {
        if (value < 1000) {
            return String.valueOf((int) value);
        }
        if (value >= 1_000_000_000) {
            double bill = value / 1_000_000_000.0;
            return (bill == (int) bill ? String.valueOf((int) bill) : String.format(Locale.US, "%.1f", bill)) + "b";
        }
        if (value >= 1_000_000) {
            double mill = value / 1_000_000.0;
            return (mill == (int) mill ? String.valueOf((int) mill) : String.format(Locale.US, "%.1f", mill)) + "m";
        }
        double k = value / 1000.0;
        return (k == (int) k ? String.valueOf((int) k) : String.format(Locale.US, "%.1f", k)) + "k";
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
