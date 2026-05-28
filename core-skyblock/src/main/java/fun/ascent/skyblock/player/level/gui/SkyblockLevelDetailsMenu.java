package fun.ascent.skyblock.player.level.gui;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.level.SkyBlockLevelRequirement;
import fun.ascent.skyblock.player.level.SkyBlockLevelUnlock;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static fun.ascent.common.StringUtility.text;

public class SkyblockLevelDetailsMenu {

    private static final int BACK_SLOT = 30;
    private static final int CLOSE_SLOT = 31;

    private static final Map<Integer, List<Integer>> SLOTS_MAP = Map.of(
            1, List.of(13),
            2, List.of(12, 14),
            3, List.of(11, 13, 15),
            4, List.of(10, 12, 14, 16),
            5, List.of(11, 12, 13, 14, 15)
    );

    public static void open(SkyblockPlayer player, int targetLevel) {
        SkyBlockLevelRequirement req = SkyBlockLevelRequirement.getLevel(targetLevel);
        if (req == null) {
            player.sendMessage(text("<red>Could not load details for Level " + targetLevel));
            return;
        }

        Inventory inv = new Inventory(InventoryType.CHEST_4_ROW, "Level " + targetLevel + " Unlocks");
        fillBackground(inv);
        List<SkyBlockLevelUnlock> unlocks = req.getUnlocks();
        int size = Math.min(5, unlocks.size());
        List<Integer> slots = SLOTS_MAP.get(size);

        if (slots != null) {
            for (int i = 0; i < size; i++) {
                SkyBlockLevelUnlock unlock = unlocks.get(i);
                int slot = slots.get(i);
                ItemStack.Builder displayBuilder = unlock.getItemDisplay(player, targetLevel);
                if (displayBuilder != null) {
                    inv.setItemStack(slot, displayBuilder.build());
                }
            }
        }

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
            }
        });

        player.openInventory(inv);
    }

    private static ItemStack buildBackButton() {
        return ItemStack.builder(Material.ARROW)
                .customName(text("<green>Go Back"))
                .lore(List.of(text("<gray>To SkyBlock Levels")))
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
