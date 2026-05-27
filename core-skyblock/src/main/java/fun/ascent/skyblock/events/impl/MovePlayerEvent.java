package fun.ascent.skyblock.events.impl;

import fun.ascent.common.redis.RedisManager;
import fun.ascent.common.redis.ServerLookup;
import fun.ascent.common.util.ProxyTransfer;
import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfileManager;
import fun.ascent.skyblock.world.WorldHandler;
import fun.ascent.skyblock.world.region.Region;
import fun.ascent.skyblock.world.region.RegionManager;
import fun.ascent.skyblock.world.region.RegionType;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class MovePlayerEvent extends SEvent<PlayerMoveEvent> {

    @Override
    public void onEvent(PlayerMoveEvent event) {
        if (!(event.getPlayer() instanceof SkyblockPlayer sbPlayer)) return;

        if (sbPlayer.getActiveProfile() != null) {
            Instance container = event.getInstance();
            
            // Handle Portals
            Block blockAtNewPos = container.getBlock(event.getNewPosition());
            if (blockAtNewPos.id() == Block.NETHER_PORTAL.id() || blockAtNewPos.id() == Block.END_PORTAL.id()) {
                String serverType = System.getenv().getOrDefault("ASCENT_SERVER_TYPE", "HUB");
                
                if (serverType.equalsIgnoreCase("HUB")) {
                    Region cur = RegionManager.getRegion(container,sbPlayer.getPosition());
                    if(cur.getType() != RegionType.VILLAGE) return;
                    // In Hub, portal sends to Island server
                    String targetServer = ServerLookup.findAnyByPrefix("island");
                    if (targetServer != null) {
                        sbPlayer.sendMessage("§aSending you to your island...");
                        if (sbPlayer.getActiveProfile() != null) {
                            ProfileManager.saveProfile(sbPlayer.getActiveProfile().profileID);
                        }
                        if (RedisManager.isInitialized()) {
                            RedisManager.get().setTransferTarget(sbPlayer.getUuid().toString(), "island");
                        }
                        ProxyTransfer.send(sbPlayer, targetServer);
                    }
                } else if (serverType.equalsIgnoreCase("ISLAND")) {
                    // On Island, portal sends to Hub
                    String targetServer = ServerLookup.findAnyByPrefix("skyblock");
                    if (targetServer != null) {
                        sbPlayer.sendMessage("§aReturning to SkyBlock Hub...");
                        if (sbPlayer.getActiveProfile() != null) {
                            ProfileManager.saveProfile(sbPlayer.getActiveProfile().profileID);
                        }
                        if (RedisManager.isInitialized()) {
                            RedisManager.get().setTransferTarget(sbPlayer.getUuid().toString(), "hub");
                        }
                        ProxyTransfer.send(sbPlayer, targetServer);
                    }
                }
            }
        }

        if (event.getNewPosition().y() < 0) {
            if (WorldHandler.getLobby() != null) {
                event.getPlayer().teleport(WorldHandler.getLobbySpawn());
            }
        }
    }
}
