package fun.ascent.skyblock.quest.impl;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.quest.*;
import fun.ascent.skyblock.world.region.RegionType;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.item.Material;
import java.util.*;

public class QuestBreakOaklog extends QuestProgress {
    @Override public String getID() { return "break_oaklog"; }
    @Override public String getName() { return "Break 10 Oak Logs"; }
    @Override public int getMaxProgress() { return 10; }

    @QuestEvent
    public void onBreak(PlayerBlockBreakEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        if (player.getActiveProfileData() == null) return;

        Material mat = event.getBlock().registry().material();
        if (mat == Material.OAK_LOG) {
            QuestData data = player.getActiveProfileData().getQuestData();
            Map.Entry<ActiveQuest, Boolean> quest = data.getQuest(QuestBreakOaklog.class);
            if (quest != null && !quest.getValue()) {
                ActiveQuest active = quest.getKey();
                active.setProgress(active.getProgress() + 1);
                player.sendMessage("§aBreak Oak Logs Progress: §e" + active.getProgress() + "/" + getMaxProgress());
                active.checkIfQuestEnded(player);
            }
        }
    }

    @Override public Map<String, Object> onStart(SkyblockPlayer player, ActiveQuest quest) { quest.getNewObjectiveText().forEach(player::sendMessage); return new HashMap<>(); }
    @Override public void onEnd(SkyblockPlayer player, Map<String, Object> customData, ActiveQuest quest) {
        ArrayList<String> rewards = new ArrayList<>(List.of("10 SkyBlock XP"));
        quest.getObjectiveCompleteText(rewards).forEach(player::sendMessage);
        player.getActiveProfileData().getQuestData().startQuest(QuestTalkToLumberjackAgain.class);
    }
    @Override public Set<RegionType> getValidRegions() { return Collections.singleton(RegionType.HUB); }
    @Override public Double getAttachedSkyBlockXP() { return 10D; }
}
