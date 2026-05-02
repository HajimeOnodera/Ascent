package fun.ascent.skyblock.world;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.world.WorldRegistry;
import fun.ascent.skyblock.npc.SkyblockNPCManager;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class WorldManager {

    public static String startingWorld;
    public static HashMap<String, UUID> worlds = new HashMap<>();
    public static InstanceManager instanceManager;
    private static final Pos STARTING_SPAWN = new Pos(0.5, 41, 0.5);
    private static final WorldRegistry REGISTRY = new WorldRegistry();

    public static InstanceContainer getStartingWorld() {
        if (startingWorld == null || instanceManager == null) return null;
        if (!worlds.containsKey(startingWorld)) return null;
        return (InstanceContainer) instanceManager.getInstance(worlds.get(startingWorld));
    }

    public static Pos getStartingSpawn() {
        return STARTING_SPAWN;
    }

    public static SkyblockPlayer getPlayer(UUID playerUUID) {
        for (UUID world : worlds.values()) {
            Instance container = instanceManager.getInstance(world);
            if (container == null) continue;

            Player player = container.getPlayerByUuid(playerUUID);
            if (!(player instanceof SkyblockPlayer)) continue;
            return (SkyblockPlayer) player;
        }
        return null;
    }

    public static void initialise() {
        instanceManager = MinecraftServer.getInstanceManager();

        InstanceContainer worldInstance = instanceManager.createInstanceContainer();
        startingWorld = "world";

        worldInstance.setChunkSupplier(LightingChunk::new);
        worldInstance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));
        registerWorld(startingWorld, worldInstance);
        System.out.println("[Proximity] Created Starting World ");

        Reflections reflections = new Reflections("fun.ascent.skyblock.npc.village");
        Set<Class<? extends NpcDefinition>> npcClasses = reflections.getSubTypesOf(NpcDefinition.class);

        for (Class<? extends NpcDefinition> npcClass : npcClasses) {
            try {
                Constructor<? extends NpcDefinition> constructor = npcClass.getConstructor(Instance.class);
                NpcDefinition definition = constructor.newInstance(worldInstance);
                SkyblockNPCManager.registerNPC(new AscentNpc(definition));
            } catch (Exception e) {
                System.err.println("[Skyblock] Failed to spawn NPC: " + npcClass.getSimpleName());
                e.printStackTrace();
            }
        }
    }

    public static void registerWorld(String worldName, Instance container) {
        REGISTRY.register(worldName, container);
        worlds.put(worldName, container.getUuid());
    }

    public static String getWorldName(Instance instance) {
        return REGISTRY.name(instance).orElse(null);
    }
}
