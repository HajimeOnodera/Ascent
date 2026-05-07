package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.world.WorldHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;

public class PlayerJoinPreEvent extends SEvent<AsyncPlayerConfigurationEvent> {

    @Override
    public void onEvent(AsyncPlayerConfigurationEvent event) {
        if (WorldHandler.getLobby() == null) {
            return;
        }
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        if (player.getActiveProfile() != null && player.getActiveProfile().island != null) {

            event.setSpawningInstance(player.getActiveProfile().island.getInstance());
            player.setRespawnPoint(player.getActiveProfile().getSpawnPos());

        } else {
            event.setSpawningInstance(WorldHandler.getLobby());
            player.setRespawnPoint(WorldHandler.getLobbySpawn());
        }
    }
}
