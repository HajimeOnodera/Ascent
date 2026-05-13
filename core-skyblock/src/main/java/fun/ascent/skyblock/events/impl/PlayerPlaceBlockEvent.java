package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.island.Island;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;

public class PlayerPlaceBlockEvent extends SEvent<PlayerBlockPlaceEvent> {

    @Override
    public void onEvent(PlayerBlockPlaceEvent event) {
        if (event.getPlayer() instanceof SkyblockPlayer pl) {
            if (pl.getGameMode() == GameMode.CREATIVE) return;

            String worldId = event.getInstance().getTag(Island.WORLD_ID_TAG);
            boolean isOwnIsland = pl.getActiveProfile() != null && worldId != null && worldId.equals(pl.getActiveProfile().profileID.toString());

            event.setCancelled(!isOwnIsland);
        }
    }
}
