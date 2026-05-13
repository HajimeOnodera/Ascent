package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.SkyblockProfile;
import fun.ascent.skyblock.world.WorldHandler;
import fun.ascent.skyblock.world.location.SkyblockLocation;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;

public class MovePlayerEvent extends SEvent<PlayerMoveEvent> {

    @Override
    public void onEvent(PlayerMoveEvent event) {
        if (event.getPlayer() instanceof SkyblockPlayer sbPlayer) {
            SkyblockLocation newLoc = SkyblockLocation.getLocation(event.getInstance(), event.getNewPosition());
            SkyblockLocation oldLoc = SkyblockLocation.getLocation(event.getInstance(), event.getPlayer().getPosition());

            if (newLoc != oldLoc && !newLoc.canGo(sbPlayer)) {
                String msg = newLoc.getRequirementMessage();
                if (msg != null) sbPlayer.sendMessage(MiniMessage.miniMessage().deserialize(msg));
                event.setCancelled(true);
                return;
            }

            if (sbPlayer.getActiveProfile() != null) {
                SkyblockProfile profile = sbPlayer.getActiveProfile();
                Instance container = event.getInstance();
                if (event.getInstance().getTag(WorldHandler.worldID).equals(profile.island.getName())){
                    if(WorldHandler.getLobby() == null) {
                        System.out.println("[WORLD] Lobby World is NULL");
                        return;
                    }
                    if(container.getBlock(event.getNewPosition()).name().equals(Block.NETHER_PORTAL.name())){
                        event.getPlayer().setInstance(WorldHandler.getLobby(),WorldHandler.getLobbySpawn());
                    }
                }
                if(event.getInstance().getTag(WorldHandler.worldID).equals("lobby")){
                    InstanceContainer playerIsland = profile.island.getInstance();
                    if(container.getBlock(event.getNewPosition()).name().equals(Block.END_PORTAL.name())){
                        if(newLoc == SkyblockLocation.VILLAGE){
                            sbPlayer.setInstance(playerIsland,profile.getSpawnPos());
                        }
                    }
                }
            }
        }
        if (event.getNewPosition().y() < 0) {
            if(WorldHandler.getLobby() == null) {
                System.out.println("[WORLD] Lobby World is NULL");
                return;
            }
            event.getPlayer().setInstance(WorldHandler.getLobby(),WorldHandler.getLobbySpawn());
        }
    }
}
