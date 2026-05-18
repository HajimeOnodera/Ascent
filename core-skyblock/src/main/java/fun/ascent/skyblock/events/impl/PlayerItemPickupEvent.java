package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.events.definitions.InventoryItemAddEvent;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.tag.Tag;

public class PlayerItemPickupEvent extends SEvent<PickupItemEvent> {

    private static final Tag<Boolean> BLOCK_DROP = Tag.Boolean("block_drop");

    @Override
    public void onEvent(PickupItemEvent event) {
       if(event.getEntity() instanceof SkyblockPlayer player){
           if (event.getItemEntity().hasTag(BLOCK_DROP) && event.getItemEntity().getTag(BLOCK_DROP)) {
               return;
           }

           SkyblockItem skyItem = SkyblockItem.fromStack(event.getItemStack());
           if (skyItem != null) {
               MinecraftServer.getGlobalEventHandler().call(new InventoryItemAddEvent(
                       skyItem, player, event.getItemStack().amount()
               ));
           } else {
               MinecraftServer.getGlobalEventHandler().call(new InventoryItemAddEvent(
                       event.getItemStack().material(), player, event.getItemStack().amount()
               ));
           }
       }
    }
}


