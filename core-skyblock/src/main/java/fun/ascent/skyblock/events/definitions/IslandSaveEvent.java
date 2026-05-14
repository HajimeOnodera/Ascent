package fun.ascent.skyblock.events.definitions;

import fun.ascent.skyblock.island.Island;
import net.minestom.server.event.Event;

public record IslandSaveEvent(Island island) implements Event {
}
