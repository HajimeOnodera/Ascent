package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.entity.display.DroppedItemEntity;
import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.event.player.PlayerDisconnectEvent;

public class PlayerDisconnectCleanupEvent extends SEvent<PlayerDisconnectEvent> {

    @Override
    public void onEvent(PlayerDisconnectEvent event) {
        if (event.getPlayer() instanceof SkyblockPlayer player) {
            DroppedItemEntity.clearDroppedItems(player);
        }
    }
}
