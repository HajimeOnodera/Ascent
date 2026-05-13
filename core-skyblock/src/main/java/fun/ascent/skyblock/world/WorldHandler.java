package fun.ascent.skyblock.world;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.world.PolarWorlds;
import fun.ascent.common.world.WorldRegistry;
import fun.ascent.skyblock.npc.SkyblockNPCManager;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.tag.Tag;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class WorldHandler {

    public static HashMap<String, World> worlds = new HashMap<>();
    public static InstanceManager instanceManager;
    public static WorldRegistry WORLD_REGISTRY = new WorldRegistry();
    private static final Pos STARTING_SPAWN = new Pos(0 , 77, -1,180,0);
    public static final Tag<String> worldID = Tag.String("world");

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

    public static void shutdown() {
        for(World world : worlds.values()){
            if(world.save){
                world.getInstance().saveChunksToStorage();
            }
        }
    }

    public static void createLobbyWorld(){
        World world = new World("lobby",new File("maps/sbhub"),
                new File("maps/sbhub"),false);
        world.getInstance();
        spawnNPCS(world);
    }

    public static void spawnNPCS(World world){
        Reflections reflections = new Reflections("fun.ascent.skyblock.npc.village");
        Set<Class<? extends NpcDefinition>> npcs = reflections.getSubTypesOf(NpcDefinition.class);
        System.out.println("[NPC] Trying to spawn " + npcs.size() + " NPCS");

        for (Class<? extends NpcDefinition> npc : npcs) {
            if (Modifier.isAbstract(npc.getModifiers()) || npc.isInterface()) {
                continue;
            }
            try {
                Constructor<? extends NpcDefinition> constructor = npc.getDeclaredConstructor(Instance.class);
                constructor.setAccessible(true);
                NpcDefinition definition = constructor.newInstance(world.getInstance());

                AscentNpc newNpc = new AscentNpc(definition);

                SkyblockNPCManager.registerNPC(newNpc);
                newNpc.spawn();

                System.out.println("[NPC] Spawned " + npc.getSimpleName() + " at " + newNpc.position());

            } catch (NoSuchMethodException e) {
                System.err.println("[Skyblock] Failed to spawn NPC: " + npc.getSimpleName() + " (Missing Instance Constructor)");
            } catch (Exception e) {
                System.err.println("[Skyblock] Failed to register NPC: " + npc.getSimpleName());
                e.printStackTrace();
            }
        }
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
        } catch (IOException e) {
            System.out.println("[SKYBLOCK] Error While Creating World: " + world.name);
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

    public static String getWorldName(Instance instance) {
        return WORLD_REGISTRY.name(instance).orElse(null);
    }
}
