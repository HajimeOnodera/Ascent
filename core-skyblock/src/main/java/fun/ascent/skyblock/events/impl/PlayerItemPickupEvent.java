package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.entity.display.DroppedItemEntity;
import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.packet.server.play.CollectItemPacket;

public class PlayerItemPickupEvent extends SEvent<PlayerMoveEvent> {

    @Override
    public void onEvent(PlayerMoveEvent event) {
        if (!(event.getPlayer() instanceof SkyblockPlayer player)) return;

        DroppedItemEntity.getDroppedItems().computeIfPresent(player, (unused, list) -> {
            list.removeIf(item -> {
                if (item.isRemoved()) return true;

                if (item.canPickup()
                        && item.getPosition().distance(player.getPosition()) <= 1.5
                        && !item.isRemoved()) {

                    // Send collect animation packet (item flies toward the player)
                    ItemStack droppedStack = item.getDroppedItemStack();
                    player.sendPacket(new CollectItemPacket(
                            item.getEntityId(),
                            player.getEntityId(),
                            droppedStack.amount()
                    ));

                    // Add item to inventory
                    player.getInventory().addItemStack(droppedStack);

                    // Remove the entity from the world
                    item.remove();
                    return true; // Remove from tracked list
                }
                return false;
            });
            return list.isEmpty() ? null : list;
        });
    }
}
