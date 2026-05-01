package fun.ascent.lobby.world;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;

public record LobbyWorld(InstanceContainer instance, Pos spawn) {

    public static LobbyWorld create() {
        InstanceContainer instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setChunkSupplier(LightingChunk::new);
        instance.setGenerator(unit -> {
            unit.modifier().fillHeight(0, 40, Block.STONE);
            unit.modifier().fillHeight(40, 41, Block.SMOOTH_STONE);
        });

        return new LobbyWorld(instance, new Pos(0.5, 41, 0.5, 0, 0));
    }
}
