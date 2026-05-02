package fun.ascent.skyblock.menus;

import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;

public class SkyblockMenu {

    private static final int INFO_SLOT = 13;
    private static final int CLOSE_SLOT = 49;

    public static void open(SkyblockPlayer player) {
        Inventory inv = new Inventory(InventoryType.CHEST_6_ROW,
                MiniMessage.miniMessage().deserialize("SkyBlock Menu"));

        fill(inv);

        // Player Stats
        inv.setItemStack(INFO_SLOT, ItemStack.builder(Material.PLAYER_HEAD)
                .customName(MiniMessage.miniMessage().deserialize("<green>Your SkyBlock Profile"))
                .lore(List.of(MiniMessage.miniMessage().deserialize("<gray>View your stats, skills, and more!")))
                .build());

        // Close button
        inv.setItemStack(CLOSE_SLOT, ItemStack.builder(Material.BARRIER)
                .customName(MiniMessage.miniMessage().deserialize("<red>Close"))
                .build());

        inv.eventNode().addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);
            int slot = event.getSlot();
            if (slot == CLOSE_SLOT) {
                player.closeInventory();
            } else if (slot == INFO_SLOT) {
                player.sendMessage(MiniMessage.miniMessage()
                        .deserialize("<red>If you see this message, the buttons are working!"));
            }
        });

        player.openInventory(inv);
    }

    private static void fill(Inventory inventory) {
        ItemStack filler = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
                .customName(Component.empty())
                .build();
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItemStack(i, filler);
        }
    }
}
