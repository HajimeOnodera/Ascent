package fun.ascent.skyblock.events;

import fun.ascent.skyblock.world.WorldHandler;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.event.Event;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.*;

public class EventManager {

    public static GlobalEventHandler handler = MinecraftServer.getGlobalEventHandler();
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private static final Map<Class<? extends Event>, List<SEvent<? extends Event>>> dispatchers = new HashMap<>();

    public static void initialise(){

        registerEventsFromReflection();
        System.out.println("[Proximity] Registered " + dispatchers.size() + " event dispatchers.");
    }

    public static void registerEventsFromReflection(){
        Reflections reflections = new Reflections("fun.ascent.skyblock.events.impl");
        Set<Class<? extends SEvent>> events = reflections.getSubTypesOf(SEvent.class);
        for (Class<? extends SEvent> event : events) {
            if (Modifier.isAbstract(event.getModifiers()) || event.isInterface()) {
                continue;
            }
            try {
                SEvent definition  = event.getDeclaredConstructor().newInstance();
                registerEvent(definition);
            } catch (Exception e) {
                System.err.println("[Skyblock] Failed to register CMD: " + event.getSimpleName());
                e.printStackTrace();
            }

        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Event> void registerEvent(SEvent<T> event) {
        Class<T> type = event.getEventType();

        if (!dispatchers.containsKey(type)) {
            dispatchers.put(type, new ArrayList<>());

            handler.addListener(type, minestoSEvent -> {
                List<SEvent<? extends Event>> listeners = dispatchers.get(type);
                for (SEvent<? extends Event> listener : listeners) {
                    ((SEvent<T>) listener).onEvent(minestoSEvent);
                }
            });
        }

        dispatchers.get(type).add(event);
    }

}
