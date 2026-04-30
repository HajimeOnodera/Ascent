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
        
        fun.ascent.skyblock.npc.impl.SkyBlockNPC banker = new fun.ascent.skyblock.npc.impl.SkyBlockNPC(new fun.ascent.skyblock.npc.village.BankerNPC(worldInstance));
        banker.spawn();
        
        fun.ascent.skyblock.npc.impl.SkyBlockNPC lumberjack = new fun.ascent.skyblock.npc.impl.SkyBlockNPC(new fun.ascent.skyblock.npc.village.LumberjackNPC(worldInstance));
        lumberjack.spawn();
    }

    public static void registerWorld(String worldName, Instance container){
        worlds.put(worldName, container.getUuid());
    }

}
