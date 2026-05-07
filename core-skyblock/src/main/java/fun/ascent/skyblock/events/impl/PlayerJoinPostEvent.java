package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.events.SEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.event.player.PlayerSpawnEvent;

public class PlayerJoinPostEvent extends SEvent<PlayerSpawnEvent> {

    @Override
    public void onEvent(PlayerSpawnEvent e) {
        if(e.isFirstSpawn()){
//            if(ProfileManager.getProfile(player.getTag(SkyblockPlayer.sbProfileID)) == null){
//                ProfileManager.createProfile(player);
//            }
            e.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<yellow>Welcome to <green>Hypixel SkyBlock</green><yellow>!</yellow>"));
        }
    }
}
