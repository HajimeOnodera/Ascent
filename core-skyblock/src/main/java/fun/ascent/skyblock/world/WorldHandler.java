package fun.ascent.skyblock.world;

import fun.ascent.common.world.PolarWorlds;
import fun.ascent.common.world.WorldRegistry;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.tag.Tag;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorldHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorldHandler.class);

    public static HashMap<String, World> worlds = new HashMap<>();
    public static InstanceManager instanceManager;
    public static WorldRegistry WORLD_REGISTRY = new WorldRegistry();
    private static final Pos STARTING_SPAWN = new Pos(0 , 77, -1,180,0);
    public static final Tag<String> worldID = WorldRegistry.WORLD_ID_TAG;

    public static void initialise() {
        instanceManager = MinecraftServer.getInstanceManager();
        worlds.clear();
        WORLD_REGISTRY = new WorldRegistry();
        createLobbyWorld();
    }

    public static InstanceContainer getLobby(){
        World world = worlds.get("lobby");
        if(world == null) return null;
        return world.getInstance();
    }

    public static void createLobbyWorld(){
        World world = new World("lobby",new File("maps/sbhub"),
                new File("maps/sbhub"),false);
        world.getInstance();
    }

    public static InstanceContainer createWorld(World world){
        InstanceContainer container = instanceManager.createInstanceContainer();
        container.setTag(worldID,world.name);
        try {
            if (!world.save) {
                int radius = world.name.equalsIgnoreCase("island") ? 3 : -1;
                container.setChunkLoader(PolarWorlds.setupMemoryPolarWorld(world.templateFile.toPath(), radius));
            } else {
                container.setChunkLoader(WorldLoaderUtils.setupTemplateWorld(world.templateFile.toPath(), world.worldFile.toPath()));
            }
            register(world, container);
            return container;
        } catch (Exception e) {
            LOGGER.error("CRITICAL ERROR While Creating World: {}", world.name, e);
            return null;
        }
    }

    public static void register(World world, InstanceContainer container){
        worlds.put(world.name, world);
        WORLD_REGISTRY.register(world.name, container);
    }

    public static Pos getLobbySpawn() {
        return STARTING_SPAWN;
    }

    public static SkyblockPlayer getPlayer(UUID playerUUID) {
        for (World world : worlds.values()) {
            Instance container = world.getInstance();
            if (container == null) continue;

            Player player = container.getPlayerByUuid(playerUUID);
            if (!(player instanceof SkyblockPlayer)) continue;
            return (SkyblockPlayer) player;
        }
        return null;
    }
}
