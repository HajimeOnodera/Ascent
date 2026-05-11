package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.menus.SkyblockMenu;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.event.player.PlayerUseItemEvent;

public class PlayerItemUseEvent extends SEvent<PlayerUseItemEvent> {

    @Override
    public void onEvent(PlayerUseItemEvent event) {
        var value = event.getItemStack().getTag(PlayerJoinPostEvent.menuTag);
        if(value != null && value){
            if(event.getPlayer() instanceof SkyblockPlayer pl){
                if(pl.getActiveProfile() != null && pl.getActiveProfileData() != null) {
                    SkyblockMenu.open(pl);
                }
            }
        }
    }
}
