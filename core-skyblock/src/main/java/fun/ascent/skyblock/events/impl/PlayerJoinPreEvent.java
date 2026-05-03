package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.world.WorldHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;

public class PlayerJoinPreEvent extends SEvent<AsyncPlayerConfigurationEvent> {

    @Override
    public void onEvent(AsyncPlayerConfigurationEvent event) {
        if (WorldHandler.getLobby() == null) {
            return;
        }
        event.setSpawningInstance(WorldHandler.getLobby());
        event.getPlayer().setRespawnPoint(WorldHandler.getLobbySpawn());
    }
}
