package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.events.SEvent;
import net.minestom.server.event.item.ItemDropEvent;

public class PlayerDropItemEvent extends SEvent<ItemDropEvent> {

    @Override
    public void onEvent(ItemDropEvent event) {
        if(event.getItemStack().getTag(PlayerJoinPostEvent.menuTag)){
            event.setCancelled(true);
        }
    }
}
