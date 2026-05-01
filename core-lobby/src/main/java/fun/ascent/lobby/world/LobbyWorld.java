package fun.ascent.lobby.world;

import net.hollowcube.polar.AnvilPolar;
import net.hollowcube.polar.PolarLoader;
import net.hollowcube.polar.PolarWriter;
import net.hollowcube.polar.PolarWorld;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public record LobbyWorld(InstanceContainer instance, Pos spawn) {

    private static final Path ANVIL_LOBBY_WORLD = Path.of("maps", "hypixel_prototype_lobby");
    private static final Path POLAR_LOBBY_WORLD = Path.of("maps", "hypixel_prototype_lobby.polar");
    private static final Pos LOBBY_SPAWN = new Pos(10.5, 75, 0.5, 180, 0);

    public static LobbyWorld create() {
        InstanceContainer instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setChunkSupplier(LightingChunk::new);
        if (loadPolarWorld(instance)) {
            return new LobbyWorld(instance, LOBBY_SPAWN);
        }

        instance.setGenerator(unit -> {
            unit.modifier().fillHeight(0, 74, Block.STONE);
            unit.modifier().fillHeight(74, 75, Block.SMOOTH_STONE);
        });
        return new LobbyWorld(instance, LOBBY_SPAWN);
    }

    private static boolean loadPolarWorld(InstanceContainer instance) {
        try {
            ensurePolarWorld();
            if (!Files.isRegularFile(POLAR_LOBBY_WORLD)) {
                System.err.println("[Lobby] No lobby map found. Falling back to generated platform.");
                return false;
            }

            instance.setChunkLoader(new PolarLoader(POLAR_LOBBY_WORLD).setLoadLighting(true));
            System.out.println("[Lobby] Loaded Polar lobby world from " + POLAR_LOBBY_WORLD.toAbsolutePath());
            return true;
        } catch (Exception e) {
            System.err.println("[Lobby] Failed to load Polar lobby world. Falling back to generated platform.");
            e.printStackTrace();
            return false;
        }
    }

    private static void ensurePolarWorld() throws IOException {
        if (Files.isRegularFile(POLAR_LOBBY_WORLD)) {
            return;
        }
        if (!Files.isDirectory(ANVIL_LOBBY_WORLD)) {
            return;
        }

        Files.createDirectories(POLAR_LOBBY_WORLD.getParent());
        System.out.println("[Lobby] Converting Anvil lobby map to Polar: " + ANVIL_LOBBY_WORLD.toAbsolutePath());
        PolarWorld world = AnvilPolar.anvilToPolar(ANVIL_LOBBY_WORLD);
        Files.write(POLAR_LOBBY_WORLD, PolarWriter.write(world));
        System.out.println("[Lobby] Wrote Polar lobby map to " + POLAR_LOBBY_WORLD.toAbsolutePath());
    }
}
