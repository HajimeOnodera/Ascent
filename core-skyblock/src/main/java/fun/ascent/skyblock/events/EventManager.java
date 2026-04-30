package fun.ascent.skyblock.events;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.GlobalEventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {

    public static GlobalEventHandler handler = MinecraftServer.getGlobalEventHandler();

    private static final Map<Class<? extends Event>, List<SEvent<? extends Event>>> dispatchers = new HashMap<>();

    public static void initialise(){
        System.out.println("[Proximity] Registered " + dispatchers.size() + " event dispatchers.");
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
