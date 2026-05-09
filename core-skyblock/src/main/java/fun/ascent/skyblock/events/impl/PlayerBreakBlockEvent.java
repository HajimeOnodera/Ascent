package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.events.definitions.InventoryItemAddEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerBlockBreakEvent;

public class PlayerBreakBlockEvent extends SEvent<PlayerBlockBreakEvent> {

    @Override
    public void onEvent(PlayerBlockBreakEvent event) {
        if(event.getPlayer() instanceof SkyblockPlayer pl){
            //TODO: Calculate Enchantments
            //TODO: Use a BlockManager to get block attributes
            MinecraftServer.getGlobalEventHandler().call(new InventoryItemAddEvent(
                    null,pl,1
            ));
        }
    }
}
