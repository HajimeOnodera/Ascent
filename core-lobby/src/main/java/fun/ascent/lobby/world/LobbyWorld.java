package fun.ascent.lobby.world;

import fun.ascent.common.world.PolarWorlds;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;

import java.nio.file.Path;

public record LobbyWorld(InstanceContainer instance, Pos spawn) {

    private static final Path ANVIL_LOBBY_WORLD = Path.of("maps", "hypixel_lobby");
    private static final Path POLAR_LOBBY_WORLD = Path.of("maps", "hypixel_lobby.polar");
    private static final Pos LOBBY_SPAWN = new Pos(-52, 99, 0.5, -90, 0);

    public static LobbyWorld create() {
        InstanceContainer instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setChunkSupplier(LightingChunk::new);
        if (PolarWorlds.loadOrConvert(instance, POLAR_LOBBY_WORLD, ANVIL_LOBBY_WORLD, "[Lobby]")) {
            return new LobbyWorld(instance, LOBBY_SPAWN);
        }

        instance.setGenerator(unit -> {
            unit.modifier().fillHeight(0, 74, Block.STONE);
            unit.modifier().fillHeight(74, 75, Block.SMOOTH_STONE);
        });
        return new LobbyWorld(instance, LOBBY_SPAWN);
    }
}
