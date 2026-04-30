package fun.ascent.skyblock.events;

import fun.ascent.skyblock.world.WorldManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {

    public static GlobalEventHandler handler = MinecraftServer.getGlobalEventHandler();
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private static final Map<Class<? extends Event>, List<SEvent<? extends Event>>> dispatchers = new HashMap<>();

    public static void initialise(){
        handler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            if (WorldManager.getStartingWorld() == null) {
                return;
            }
            event.setSpawningInstance(WorldManager.getStartingWorld());
            event.getPlayer().setRespawnPoint(WorldManager.getStartingSpawn());
        });
        handler.addListener(PlayerSpawnEvent.class, event -> {
            if (!event.isFirstSpawn()) {
                return;
            }
            event.getPlayer().teleport(WorldManager.getStartingSpawn());
            event.getPlayer().sendMessage(MINI_MESSAGE.deserialize("<yellow>Welcome to <green>Hypixel SkyBlock</green><yellow>!</yellow>"));
        });
        handler.addListener(PlayerMoveEvent.class, event -> {
            if (event.getNewPosition().y() >= 0) {
                return;
            }
            event.getPlayer().teleport(WorldManager.getStartingSpawn());
        });
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
