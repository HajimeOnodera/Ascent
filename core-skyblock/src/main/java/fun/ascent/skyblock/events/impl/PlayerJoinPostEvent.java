package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfileManager;
import fun.ascent.skyblock.world.WorldHandler;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.event.player.PlayerSpawnEvent;

public class PlayerJoinPostEvent extends SEvent<PlayerSpawnEvent> {

    @Override
    public void onEvent(PlayerSpawnEvent e) {
        if(e.isFirstSpawn()){
            SkyblockPlayer player = (SkyblockPlayer) e.getPlayer();
            if(ProfileManager.getProfile(player.getTag(SkyblockPlayer.sbProfileID)) == null){
                ProfileManager.createProfile(player);
            }
            e.getPlayer().teleport(WorldHandler.getLobbySpawn());
            e.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<yellow>Welcome to <green>Hypixel SkyBlock</green><yellow>!</yellow>"));
        }
    }
}
