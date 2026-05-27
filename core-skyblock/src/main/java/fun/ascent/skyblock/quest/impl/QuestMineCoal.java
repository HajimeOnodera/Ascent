package fun.ascent.skyblock.quest.impl;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.quest.*;
import fun.ascent.skyblock.world.region.RegionType;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.item.Material;
import java.util.*;

public class QuestMineCoal extends QuestProgress {
    @Override public String getID() { return "mine_coal"; }
    @Override public String getName() { return "Mine 10 Coal Ores"; }
    @Override public int getMaxProgress() { return 10; }

    @QuestEvent
    public void onBreak(PlayerBlockBreakEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        if (player.getActiveProfileData() == null) return;

        Material mat = Material.fromKey(event.getBlock().key());
        if (mat == Material.COAL_ORE || mat == Material.DEEPSLATE_COAL_ORE) {
            QuestData data = player.getActiveProfileData().getQuestData();
            Map.Entry<ActiveQuest, Boolean> quest = data.getQuest(QuestMineCoal.class);
            if (quest != null && !quest.getValue()) {
                ActiveQuest active = quest.getKey();
                active.setProgress(active.getProgress() + 1);
                player.sendMessage("§aMine Coal Ores Progress: §e" + active.getProgress() + "/" + getMaxProgress());
                active.checkIfQuestEnded(player);
            }
        }
    }

    @Override public Map<String, Object> onStart(SkyblockPlayer player, ActiveQuest quest) { quest.getNewObjectiveText().forEach(player::sendMessage); return new HashMap<>(); }
    @Override public void onEnd(SkyblockPlayer player, Map<String, Object> customData, ActiveQuest quest) {
        player.getActiveProfileData().getQuestData().startQuest(QuestTalkToBlacksmithAgain.class);
    }
    @Override public Set<RegionType> getValidRegions() { return Collections.singleton(RegionType.HUB); }
}
