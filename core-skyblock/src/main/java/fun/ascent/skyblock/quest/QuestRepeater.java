package fun.ascent.skyblock.quest;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.Task;
import fun.ascent.skyblock.player.SkyblockPlayer;
import java.util.ArrayList;
import java.util.List;

public interface QuestRepeater {
    Task getTask(Scheduler scheduler);

    default List<SkyblockPlayer> getPlayersWithQuestActive() {
        List<SkyblockPlayer> toReturn = new ArrayList<>();
        Quest quest = (Quest) this;

        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach((player) -> {
            if (player instanceof SkyblockPlayer sp) {
                if (sp.getActiveProfileData() != null) {
                    QuestData data = sp.getActiveProfileData().getQuestData();
                    if (data != null && data.isCurrentlyActive(quest.getClass())) {
                        toReturn.add(sp);
                    }
                }
            }
        });

        return toReturn;
    }

    default List<SkyblockPlayer> getPlayersWithQuestNotStarted() {
        List<SkyblockPlayer> toReturn = new ArrayList<>();
        Quest quest = (Quest) this;

        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach((player) -> {
            if (player instanceof SkyblockPlayer sp) {
                if (sp.getActiveProfileData() != null) {
                    QuestData data = sp.getActiveProfileData().getQuestData();
                    if (data != null && !data.hasCompleted(quest.getClass()) && !data.isCurrentlyActive(quest.getClass())) {
                        toReturn.add(sp);
                    }
                }
            }
        });

        return toReturn;
    }

    default List<SkyblockPlayer> getPlayersWithQuestCompleted() {
        List<SkyblockPlayer> toReturn = new ArrayList<>();
        Quest quest = (Quest) this;

        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach((player) -> {
            if (player instanceof SkyblockPlayer sp) {
                if (sp.getActiveProfileData() != null) {
                    QuestData data = sp.getActiveProfileData().getQuestData();
                    if (data != null && data.hasCompleted(quest.getClass())) {
                        toReturn.add(sp);
                    }
                }
            }
        });

        return toReturn;
    }
}
