package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.events.definitions.InventoryItemAddEvent;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.item.PickupItemEvent;

public class PlayerItemPickupEvent extends SEvent<PickupItemEvent> {

    @Override
    public void onEvent(PickupItemEvent event) {
       if(event.getEntity() instanceof SkyblockPlayer player){
           MinecraftServer.getGlobalEventHandler().call(new InventoryItemAddEvent(
                   SkyblockItem.fromStack(event.getItemStack()),player,event.getItemStack().amount()
           ));
       }
    }
}
