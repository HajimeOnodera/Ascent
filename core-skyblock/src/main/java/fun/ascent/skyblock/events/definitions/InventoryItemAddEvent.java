package fun.ascent.skyblock.events.definitions;

import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import lombok.Getter;
import net.minestom.server.event.Event;
import net.minestom.server.item.Material;

@Getter
public class InventoryItemAddEvent implements Event {

    private final SkyblockItem skyblockItem;
    private final SkyblockPlayer skyblockPlayer;
    private final int amount;
    private final Material material;

    public InventoryItemAddEvent(SkyblockItem skyblockItem, SkyblockPlayer skyblockPlayer, int amount) {
        this.skyblockItem = skyblockItem;
        this.skyblockPlayer = skyblockPlayer;
        this.amount = amount;
        this.material = skyblockItem != null ? skyblockItem.getMaterial() : Material.AIR;
    }

    public InventoryItemAddEvent(Material material, SkyblockPlayer skyblockPlayer, int amount) {
        this.skyblockItem = null;
        this.skyblockPlayer = skyblockPlayer;
        this.amount = amount;
        this.material = material;
    }
}

