package fun.ascent.skyblock.world;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;

import java.util.HashMap;
import java.util.UUID;

public class WorldManager {

    public static String startingWorld;
    public static HashMap<String, UUID> worlds = new HashMap<>();
    public static InstanceManager instanceManager;
    private static final Pos STARTING_SPAWN = new Pos(0.5, 41, 0.5);

    public static InstanceContainer getStartingWorld(){
        if(startingWorld == null || instanceManager == null) return  null;
        if(!worlds.containsKey(startingWorld)) return null;
        return (InstanceContainer) instanceManager.getInstance(worlds.get(startingWorld));
    }

    public static Pos getStartingSpawn() {
        return STARTING_SPAWN;
    }

    public static void initialise(){
        instanceManager = MinecraftServer.getInstanceManager();

        InstanceContainer worldInstance = instanceManager.createInstanceContainer();
        startingWorld = "world";

        worldInstance.setChunkSupplier(LightingChunk::new);
        worldInstance.setGenerator(unit -> unit.modifier().fillHeight(0,40, Block.GRASS_BLOCK));
        registerWorld(startingWorld, worldInstance);
        System.out.println("[Proximity] Created Starting World ");

        org.reflections.Reflections reflections = new org.reflections.Reflections("fun.ascent.skyblock.npc.village");
        java.util.Set<Class<? extends fun.ascent.skyblock.npc.impl.NPCParameters>> npcClasses = reflections.getSubTypesOf(fun.ascent.skyblock.npc.impl.NPCParameters.class);

        for (Class<? extends fun.ascent.skyblock.npc.impl.NPCParameters> npcClass : npcClasses) {
            try {
                java.lang.reflect.Constructor<? extends fun.ascent.skyblock.npc.impl.NPCParameters> constructor = npcClass.getConstructor(net.minestom.server.instance.Instance.class);
                fun.ascent.skyblock.npc.impl.NPCParameters parameters = constructor.newInstance(worldInstance);
                fun.ascent.skyblock.npc.impl.SkyBlockNPC npc = new fun.ascent.skyblock.npc.impl.SkyBlockNPC(parameters);
                npc.spawn();
            } catch (Exception e) {
                System.err.println("[Skyblock] Failed to spawn NPC: " + npcClass.getSimpleName());
                e.printStackTrace();
            }
        }
    }

    public static void registerWorld(String worldName, Instance container){
        worlds.put(worldName, container.getUuid());
    }

    public static String getWorldName(Instance instance) {
        if (instance == null) return null;
        return worlds.entrySet().stream()
                .filter(entry -> entry.getValue().equals(instance.getUuid()))
                .map(java.util.Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
}
