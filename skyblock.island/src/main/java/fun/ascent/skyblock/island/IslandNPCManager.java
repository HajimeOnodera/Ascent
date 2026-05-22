package fun.ascent.skyblock.island;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.skyblock.events.definitions.IslandLoadEvent;
import fun.ascent.skyblock.events.definitions.IslandSaveEvent;
import fun.ascent.skyblock.npc.SkyblockNPCManager;
import fun.ascent.skyblock.world.WorldHandler;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.instance.Instance;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class IslandNPCManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(IslandNPCManager.class);
    private static boolean initialized = false;

    public static void init(GlobalEventHandler handler) {
        if (initialized) {
            LOGGER.warn("IslandNPCManager.init() called but already initialized!");
            return;
        }
        initialized = true;

        handler.addListener(IslandLoadEvent.class, event -> spawnIslandNPCs(event.instance()));

        handler.addListener(IslandSaveEvent.class, event -> removeIslandNPCs(event.island().getInstance()));
    }

    private static void spawnIslandNPCs(Instance instance) {
        Reflections reflections = new Reflections(new org.reflections.util.ConfigurationBuilder()
                .setUrls(org.reflections.util.ClasspathHelper.forPackage("fun.ascent.skyblock.island.npc", IslandNPCManager.class.getClassLoader()))
                .addClassLoaders(IslandNPCManager.class.getClassLoader()));
        Set<Class<? extends NpcDefinition>> npcs = reflections.getSubTypesOf(NpcDefinition.class);
        LOGGER.info("Trying to spawn {} NPCs for island {}", npcs.size(), instance.getTag(WorldHandler.worldID));

        Set<String> spawned = new HashSet<>();
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
                    LOGGER.warn("NPC {} is already registered for this island, skipping...", definition.id());
                    continue;
                }

                AscentNpc newNpc = new AscentNpc(definition);
                SkyblockNPCManager.registerNPC(newNpc);

                LOGGER.info("Spawned Island NPC: {} at {} with ID {}", npc.getSimpleName(), newNpc.position(), definition.id());
            } catch (Exception e) {
                LOGGER.error("Failed to spawn Island NPC: {}", npc.getSimpleName(), e);
            }
        }
    }

    private static void removeIslandNPCs(Instance instance) {
        Reflections reflections = new Reflections(new org.reflections.util.ConfigurationBuilder()
                .setUrls(org.reflections.util.ClasspathHelper.forPackage("fun.ascent.skyblock.island.npc", IslandNPCManager.class.getClassLoader()))
                .addClassLoaders(IslandNPCManager.class.getClassLoader()));
        Set<Class<? extends NpcDefinition>> npcs = reflections.getSubTypesOf(NpcDefinition.class);
        
        for (Class<? extends NpcDefinition> npc : npcs) {
            if (Modifier.isAbstract(npc.getModifiers()) || npc.isInterface()) {
                continue;
            }
            
            try {
                Constructor<? extends NpcDefinition> constructor = npc.getDeclaredConstructor(Instance.class);
                constructor.setAccessible(true);
                NpcDefinition definition = constructor.newInstance(instance);

                // Unregister the NPC from the global NPC manager
                SkyblockNPCManager.removeNPCbyID(definition.id());
                LOGGER.info("Removed Island NPC: {}", definition.id());
            } catch (Exception e) {
                LOGGER.error("Failed to remove Island NPC: {}", npc.getSimpleName(), e);
            }
        }
    }
}
