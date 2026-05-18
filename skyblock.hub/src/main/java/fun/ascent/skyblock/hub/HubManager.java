package fun.ascent.skyblock.hub;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.skyblock.npc.SkyblockNPCManager;
import lombok.Getter;
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
    private static boolean initialized = false;
    @Getter
    private static InstanceContainer hubInstance;

    public static void init() {
        if (initialized) {
            LOGGER.warn("HubManager.init() called but already initialized!");
            return;
        }
        initialized = true;
        LOGGER.info("Initializing SkyBlock HUB...");
        createHubWorld();
    }

    private static void createHubWorld() {
        hubInstance = fun.ascent.skyblock.world.WorldHandler.getLobby();
        if (hubInstance == null) {
            LOGGER.error("Failed to initialize Hub: Lobby instance is null!");
            return;
        }
        
        spawnHubNPCs(hubInstance);
    }

    private static void spawnHubNPCs(Instance instance) {
        Reflections reflections = new Reflections("fun.ascent.skyblock.hub.npc");
        Set<Class<? extends NpcDefinition>> npcs = reflections.getSubTypesOf(NpcDefinition.class);
        LOGGER.info("Trying to spawn {} NPCs in Hub", npcs.size());

        java.util.Set<String> spawned = new java.util.HashSet<>();
        for (Class<? extends NpcDefinition> npc : npcs) {
            if (Modifier.isAbstract(npc.getModifiers()) || npc.isInterface()) {
                continue;
            }
            if (spawned.contains(npc.getName())) continue;
            spawned.add(npc.getName());
            
            try {
                Constructor<? extends NpcDefinition> constructor = npc.getDeclaredConstructor(Instance.class);
                constructor.setAccessible(true);
                NpcDefinition definition = constructor.newInstance(instance);

                if (SkyblockNPCManager.getNPCbyID(definition.id()) != null) {
                    LOGGER.warn("NPC {} is already registered, skipping...", definition.id());
                    continue;
                }

                AscentNpc newNpc = new AscentNpc(definition);
                SkyblockNPCManager.registerNPC(newNpc);

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
