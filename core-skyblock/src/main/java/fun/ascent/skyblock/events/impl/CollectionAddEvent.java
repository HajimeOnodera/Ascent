package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.events.definitions.InventoryItemAddEvent;
import fun.ascent.skyblock.player.profiles.SkyblockProfile;

public class CollectionAddEvent extends SEvent<InventoryItemAddEvent> {
    @Override
    public void onEvent(InventoryItemAddEvent event) {
        if(event.getAmount() < 0 || event.getSkyblockItem() == null) return;
        SkyblockProfile profile = event.getSkyblockPlayer().getActiveProfile();
        if(profile == null) return;
        profile.updateCollection(event.getSkyblockItem().getItemId(),event.getAmount());
    }
}
