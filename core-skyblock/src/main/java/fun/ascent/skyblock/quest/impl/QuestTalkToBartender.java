package fun.ascent.skyblock.quest.impl;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.quest.*;
import fun.ascent.skyblock.world.region.RegionType;
import java.util.*;

public class QuestTalkToBartender extends Quest {
    @Override public String getID() { return "talk_to_bartender"; }
    @Override public String getName() { return "Talk to the Bartender"; }
    @Override public Map<String, Object> onStart(SkyblockPlayer player, ActiveQuest quest) { quest.getNewObjectiveText().forEach(player::sendMessage); return new HashMap<>(); }
    @Override public void onEnd(SkyblockPlayer player, Map<String, Object> customData, ActiveQuest quest) {
        ArrayList<String> rewards = new ArrayList<>(List.of("10 SkyBlock XP"));
        quest.getObjectiveCompleteText(rewards).forEach(player::sendMessage);
    }
    @Override public Set<RegionType> getValidRegions() { return Collections.singleton(RegionType.HUB); }
    @Override public Double getAttachedSkyBlockXP() { return 10D; }
}
