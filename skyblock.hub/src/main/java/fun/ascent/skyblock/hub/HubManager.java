package fun.ascent.skyblock.hub;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.world.WorldRegistry;
import fun.ascent.skyblock.npc.SkyblockNPCManager;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Set;

public class HubManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(HubManager.class);
    private static final Pos STARTING_SPAWN = new Pos(0, 77, -1, 180, 0);
    @Getter
    private static InstanceContainer hubInstance;

    public static void init() {
        LOGGER.info("Initializing SkyBlock HUB...");
        createHubWorld();
    }

    private static void createHubWorld() {
        hubInstance = MinecraftServer.getInstanceManager().createInstanceContainer();
        hubInstance.setTag(WorldRegistry.WORLD_ID_TAG, "hub");
        
        // This is a simplified version, you might want to use your World/Polar logic here
        // For now, I'm just setting it up so the NPCs have somewhere to live
        
        spawnHubNPCs(hubInstance);
    }

    private static void spawnHubNPCs(Instance instance) {
        Reflections reflections = new Reflections("fun.ascent.skyblock.hub.npc");
        Set<Class<? extends NpcDefinition>> npcs = reflections.getSubTypesOf(NpcDefinition.class);
        LOGGER.info("Trying to spawn {} NPCs in Hub", npcs.size());

        for (Class<? extends NpcDefinition> npc : npcs) {
            if (Modifier.isAbstract(npc.getModifiers()) || npc.isInterface()) {
                continue;
            }
            try {
                Constructor<? extends NpcDefinition> constructor = npc.getDeclaredConstructor(Instance.class);
                constructor.setAccessible(true);
                NpcDefinition definition = constructor.newInstance(instance);

                AscentNpc newNpc = new AscentNpc(definition);
                SkyblockNPCManager.registerNPC(newNpc);
                newNpc.spawn();

                LOGGER.info("Spawned Hub NPC: {} at {}", npc.getSimpleName(), newNpc.position());
            } catch (Exception e) {
                LOGGER.error("Failed to spawn Hub NPC: {}", npc.getSimpleName(), e);
            }
        }
    }

    public static Pos getSpawnPos() {
        return STARTING_SPAWN;
    }
}
