package fun.ascent.common.world;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.InstanceContainer;

public record LoadedWorld(String name, InstanceContainer instance, Pos spawn) {
}
