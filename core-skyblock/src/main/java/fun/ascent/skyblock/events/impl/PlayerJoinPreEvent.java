package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.world.WorldHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;

public class PlayerJoinPreEvent extends SEvent<AsyncPlayerConfigurationEvent> {

    @Override
    public void onEvent(AsyncPlayerConfigurationEvent event) {
        WorldHandler.getLobby();

        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        if (player.getActiveProfile() != null && player.getActiveProfile().island != null) {
            try {
                // Load and wait for island instance
                net.minestom.server.instance.InstanceContainer islandInstance = player.getActiveProfile().island.load().join();
                if (islandInstance != null) {
                    event.setSpawningInstance(islandInstance);
                    player.setRespawnPoint(player.getActiveProfile().getSpawnPos());
                    return;
                }
            } catch (Exception e) {
                System.err.println("[Skyblock] Failed to load island for " + player.getUsername() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Fallback to Lobby
        if (WorldHandler.getLobby() != null) {
            event.setSpawningInstance(WorldHandler.getLobby());
            player.setRespawnPoint(WorldHandler.getLobbySpawn());
        } else {
            // Last resort: find any instance
            System.err.println("[Skyblock] Lobby is null, falling back to any available instance!");
            net.minestom.server.MinecraftServer.getInstanceManager().getInstances().stream().findFirst().ifPresent(event::setSpawningInstance);
        }
    }
}
