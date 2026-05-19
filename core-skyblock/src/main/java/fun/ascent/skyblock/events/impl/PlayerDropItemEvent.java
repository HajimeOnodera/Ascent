package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.entity.display.DroppedItemEntity;
import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.item.ItemDropEvent;

public class PlayerDropItemEvent extends SEvent<ItemDropEvent> {

    @Override
    public void onEvent(ItemDropEvent event) {
        // Never allow dropping menu items
        var menuTagVal = event.getItemStack().getTag(PlayerJoinPostEvent.menuTag);
        if (menuTagVal != null && menuTagVal) {
            event.setCancelled(true);
            return;
        }

        // Don't drop while an inventory GUI is open
        if (event.getPlayer().getOpenInventory() != null) {
            event.setCancelled(true);
            return;
        }

        if (!(event.getPlayer() instanceof SkyblockPlayer player)) return;

        // Send SkyBlock-style drop warning messages
        boolean hideMessages = player.hasDropAlertsDisabled();
        if (!hideMessages) {
            Component hoverText = Component.text("§eClick here to disable the alert!");

            player.sendMessage(
                    Component.text("§e⚠ §aYour drops can't be seen by other players in §bSkyBlock§a!")
                            .hoverEvent(HoverEvent.showText(hoverText))
                            .clickEvent(ClickEvent.runCommand("/toggledropalert"))
            );
            player.sendMessage(
                    Component.text("§aOnly you can pickup your dropped items!")
                            .hoverEvent(HoverEvent.showText(hoverText))
                            .clickEvent(ClickEvent.runCommand("/toggledropalert"))
            );
            player.sendMessage(
                    Component.text("§eClick here to disable this alert forever!")
                            .hoverEvent(HoverEvent.showText(hoverText))
                            .clickEvent(ClickEvent.runCommand("/toggledropalert"))
            );
        }

        // Spawn the per-player dropped item entity
        DroppedItemEntity droppedItem = new DroppedItemEntity(event.getItemStack(), player);
        Pos pos = Pos.fromPoint(player.getPosition().add(0, 1, 0));

        // Apply throw velocity in the direction the player is looking
        droppedItem.setVelocity(player.getPosition().direction()
                .mul(5)
                .add(0, 1.5, 0)
        );

        droppedItem.setInstance(player.getInstance(), pos);
    }
}
