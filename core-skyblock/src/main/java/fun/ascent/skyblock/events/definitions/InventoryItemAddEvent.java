package fun.ascent.skyblock.events.definitions;

import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minestom.server.event.Event;

@AllArgsConstructor
@Getter
public class InventoryItemAddEvent implements Event {

    private SkyblockItem skyblockItem;
    private SkyblockPlayer skyblockPlayer;
    private int amount;

}
