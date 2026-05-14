package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.blocks.BlockManager;
import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.world.WorldHandler;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import fun.ascent.skyblock.world.region.Region;
import fun.ascent.skyblock.world.region.RegionType;
import fun.ascent.skyblock.world.region.RegionManager;
import net.minestom.server.item.Material;

public class PlayerBreakBlockEvent extends SEvent<PlayerBlockBreakEvent> {

    @Override
    public void onEvent(PlayerBlockBreakEvent event) {
        if(event.getPlayer() instanceof SkyblockPlayer pl){
            if (pl.getGameMode() == GameMode.CREATIVE) return;

            String worldId = event.getInstance().getTag(WorldHandler.worldID);
            String profileId = pl.getActiveProfile() != null ? pl.getActiveProfile().profileID.toString() : "null";
            
            boolean isOwnIsland = worldId != null && worldId.equals(profileId);
            boolean isHub = worldId == null || worldId.equalsIgnoreCase("lobby");

            if (!isOwnIsland && !isHub) {
                pl.sendMessage("§cYou cannot break blocks here! (World: " + worldId + ", Profile: " + profileId + ")");
                event.setCancelled(true);
                return;
            }

            // In the hub, we only allow breaking if it's a valid Skyblock resource
            if (isHub && !isOwnIsland) {
                Region region = RegionManager.getRegion(event.getInstance(), event.getBlockPosition());
                RegionType type = region != null ? region.getType() : RegionType.HUB;
                if (BlockManager.getBlock(Material.fromKey(event.getBlock().key()), type) == null) {
                    event.setCancelled(true);
                    return;
                }
            }

            event.setCancelled(false);

            BlockManager.handleBlockBreak(
                    pl,
                    event.getInstance(),
                    event.getBlockPosition().asPos(),
                    event.getBlock()
            );
        }
    }
}
