package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.event.player.PlayerBlockBreakEvent;

public class PlayerBreakBlockEvent extends SEvent<PlayerBlockBreakEvent> {

    @Override
    public void onEvent(PlayerBlockBreakEvent event) {
        if(event.getPlayer() instanceof SkyblockPlayer pl){
            event.setCancelled(true);

            fun.ascent.skyblock.blocks.BlockManager.handleBlockBreak(
                    pl,
                    event.getInstance(),
                    event.getBlockPosition().asPos(),
                    event.getBlock()
            );

        }
    }
}
