package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.blocks.BlockManager;
import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.world.WorldHandler;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerBlockBreakEvent;

public class PlayerBreakBlockEvent extends SEvent<PlayerBlockBreakEvent> {

    @Override
    public void onEvent(PlayerBlockBreakEvent event) {
        if(event.getPlayer() instanceof SkyblockPlayer pl){
            if (pl.getGameMode() == GameMode.CREATIVE) return;

            String worldId = event.getInstance().getTag(WorldHandler.worldID);
            boolean isOwnIsland = pl.getActiveProfile() != null && worldId != null && worldId.equals(pl.getActiveProfile().profileID.toString());

            // Allow breaking on own island, but still process SkyblockBlock logic if it exists
            event.setCancelled(!isOwnIsland);

            BlockManager.handleBlockBreak(
                    pl,
                    event.getInstance(),
                    event.getBlockPosition().asPos(),
                    event.getBlock()
            );
        }
    }
}
