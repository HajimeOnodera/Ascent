package fun.ascent.skyblock.quest.impl;

import fun.ascent.skyblock.events.PlayerKillMobEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.quest.*;
import fun.ascent.skyblock.world.region.RegionType;
import java.util.*;

public class QuestKillZombies extends QuestProgress {
    @Override public String getID() { return "kill_zombies"; }
    @Override public String getName() { return "Kill 5 Zombies"; }
    @Override public int getMaxProgress() { return 5; }

    @QuestEvent
    public void onKill(PlayerKillMobEvent event) {
        SkyblockPlayer player = event.getPlayer();
        if (player.getActiveProfileData() == null) return;

        String name = event.getKilledMob().getEntityType().name();
        if (name.toLowerCase().contains("zombie")) {
            QuestData data = player.getActiveProfileData().getQuestData();
            Map.Entry<ActiveQuest, Boolean> quest = data.getQuest(QuestKillZombies.class);
            if (quest != null && !quest.getValue()) {
                ActiveQuest active = quest.getKey();
                active.setProgress(active.getProgress() + 1);
                player.sendMessage("§aKill Zombies Progress: §e" + active.getProgress() + "/" + getMaxProgress());
                active.checkIfQuestEnded(player);
            }
        }
    }

    @Override public Map<String, Object> onStart(SkyblockPlayer player, ActiveQuest quest) { quest.getNewObjectiveText().forEach(player::sendMessage); return new HashMap<>(); }
    @Override public void onEnd(SkyblockPlayer player, Map<String, Object> customData, ActiveQuest quest) {
        player.getActiveProfileData().getQuestData().startQuest(QuestTalkToBartender.class);
    }
    @Override public Set<RegionType> getValidRegions() { return Collections.singleton(RegionType.HUB); }
}
