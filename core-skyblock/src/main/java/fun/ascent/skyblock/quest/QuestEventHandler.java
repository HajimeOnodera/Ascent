package fun.ascent.skyblock.quest;

import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.PlayerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class QuestEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuestEventHandler.class);

    @SuppressWarnings("unchecked")
    public static void registerQuest(Quest quest) {
        for (Method method : quest.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(QuestEvent.class)) {
                QuestEvent questEvent = method.getAnnotation(QuestEvent.class);
                if (method.getParameterCount() != 1) {
                    LOGGER.error("Quest event handler method must have exactly one parameter: " + quest.getClass().getName() + "#" + method.getName());
                    continue;
                }

                Class<?> paramType = method.getParameterTypes()[0];
                if (!Event.class.isAssignableFrom(paramType)) {
                    LOGGER.error("Quest event handler parameter must extend Minestom Event: " + quest.getClass().getName() + "#" + method.getName());
                    continue;
                }

                Class<? extends Event> eventType = (Class<? extends Event>) paramType;

                MinecraftServer.getGlobalEventHandler().addListener(eventType, event -> {
                    SkyblockPlayer player = getPlayerFromEvent(event);
                    if (player == null) return;

                    if (player.getActiveProfileData() == null) return;
                    QuestData data = player.getActiveProfileData().getQuestData();
                    if (data == null) return;

                    // Check if quest is active before dispatching event
                    if (data.isCurrentlyActive(quest.getClass())) {
                        try {
                            if (questEvent.isAsync()) {
                                Thread.startVirtualThread(() -> {
                                    try {
                                        method.invoke(quest, event);
                                    } catch (Exception ex) {
                                        LOGGER.error("Exception in async quest event handler: " + quest.getClass().getName() + "#" + method.getName(), ex);
                                    }
                                });
                            } else {
                                method.invoke(quest, event);
                            }
                        } catch (Exception ex) {
                            LOGGER.error("Exception in quest event handler: " + quest.getClass().getName() + "#" + method.getName(), ex);
                        }
                    }
                });
            }
        }
    }

    private static SkyblockPlayer getPlayerFromEvent(Event event) {
        if (event instanceof PlayerEvent pe) {
            if (pe.getPlayer() instanceof SkyblockPlayer sp) return sp;
        }
        try {
            Method getPlayerMethod = event.getClass().getMethod("getPlayer");
            Object playerObj = getPlayerMethod.invoke(event);
            if (playerObj instanceof SkyblockPlayer sp) {
                return sp;
            }
        } catch (Exception ignored) {}
        return null;
    }
}
