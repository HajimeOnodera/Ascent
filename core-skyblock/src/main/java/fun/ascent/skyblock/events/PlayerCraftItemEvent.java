package fun.ascent.skyblock.events;

import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.event.Event;
import net.minestom.server.item.ItemStack;

public class PlayerCraftItemEvent implements Event {
    private final SkyblockPlayer player;
    private final ItemStack craftedItem;

    public PlayerCraftItemEvent(SkyblockPlayer player, ItemStack craftedItem) {
        this.player = player;
        this.craftedItem = craftedItem;
    }

    public SkyblockPlayer getPlayer() {
        return player;
    }

    public ItemStack getCraftedItem() {
        return craftedItem;
    }
}
