package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.world.WorldHandler;
import fun.ascent.skyblock.world.location.SkyblockLocation;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.event.player.PlayerMoveEvent;

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
        }

        if (event.getNewPosition().y() < 0) {
            event.getPlayer().teleport(WorldHandler.getLobbySpawn());
        }
    }
}
