package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.dungeon.DungeonFloor;
import fun.ascent.skyblock.dungeon.DungeonInstance;
import fun.ascent.skyblock.dungeon.DungeonManager;
import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.world.WorldHandler;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.InstanceContainer;

public class PlayerJoinPreEvent extends SEvent<AsyncPlayerConfigurationEvent> {

    @Override
    public void onEvent(AsyncPlayerConfigurationEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        String serverType = System.getenv().getOrDefault("ASCENT_SERVER_TYPE", "HUB");

        if (serverType.equalsIgnoreCase("DUNGEON")) {
            // Create a dungeon instance for the player
            DungeonInstance dungeon = DungeonManager.get().createDungeon(DungeonFloor.FLOOR_7);
            DungeonManager.get().trackPlayer(player.getUuid(), dungeon);
            event.setSpawningInstance(dungeon.instance());
            player.setRespawnPoint(dungeon.spawnPosition());
            return;
        }

        if (serverType.equalsIgnoreCase("ISLAND") && player.getActiveProfile() != null && player.getActiveProfile().island != null) {
            try {
                InstanceContainer islandInstance = player.getActiveProfile().island.load().join();
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
            System.err.println("[Skyblock] Lobby is null, falling back to any available instance!");
            MinecraftServer.getInstanceManager().getInstances().stream().findFirst().ifPresent(event::setSpawningInstance);
        }
    }
}
