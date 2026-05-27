package fun.ascent.skyblock.quest.impl;

import fun.ascent.skyblock.events.JerryClickedEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.quest.*;
import fun.ascent.skyblock.world.region.RegionType;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.SchedulerManager;
import net.minestom.server.timer.TaskSchedule;

import java.util.*;

public class QuestTalkJerry extends Quest {

    @QuestEvent
    public void onJerryClicked(JerryClickedEvent event) {
        SkyblockPlayer player = event.getPlayer();
        if (player.getActiveProfileData() == null) return;

        QuestData data = player.getActiveProfileData().getQuestData();
        Map.Entry<ActiveQuest, Boolean> entry = data.getQuest(QuestTalkJerry.class);
        if (entry == null || entry.getValue()) return;

        ActiveQuest activeQuest = entry.getKey();
        if (activeQuest.getCustomData().containsKey("talking")) {
            return;
        }

        activeQuest.getCustomData().put("talking", true);

        SchedulerManager scheduler = MinecraftServer.getSchedulerManager();
        player.sendMessage("§e[NPC] Jerry§f: Your SkyBlock island is part of a much larger universe.");
        
        scheduler.buildTask(() -> {
            if (player.isOnline()) {
                player.sendMessage("§e[NPC] Jerry§f: The SkyBlock universe is full of islands to explore and resources to discover!");
            }
        }).delay(TaskSchedule.tick(20)).schedule();

        scheduler.buildTask(() -> {
            if (player.isOnline()) {
                player.sendMessage("§e[NPC] Jerry§f: Use the §dPortal§f to warp to the first of those islands - the SkyBlock Hub!");
            }
        }).delay(TaskSchedule.tick(40)).schedule();

        scheduler.buildTask(() -> {
            if (player.isOnline()) {
                data.endQuest(QuestTalkJerry.class);
            }
        }).delay(TaskSchedule.tick(60)).schedule();
    }

    @Override
    public String getID() {
        return "talk_to_jerry";
    }

    @Override
    public String getName() {
        return "Talk to Jerry";
    }

    @Override
    public Map<String, Object> onStart(SkyblockPlayer player, ActiveQuest quest) {
        quest.getNewObjectiveText().forEach(player::sendMessage);
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyblockPlayer player, Map<String, Object> customData, ActiveQuest quest) {
        player.getActiveProfileData().getQuestData().startQuest(QuestUseTeleporter.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.PRIVATE_ISLAND);
    }
}
