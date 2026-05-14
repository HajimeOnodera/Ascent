package fun.ascent.skyblock.events.definitions;

import fun.ascent.skyblock.island.Island;
import net.minestom.server.event.Event;
import net.minestom.server.instance.Instance;

public record IslandLoadEvent(Island island, Instance instance) implements Event {
}
