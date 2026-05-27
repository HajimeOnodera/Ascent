package fun.ascent.skyblock.quest.impl;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.quest.*;
import fun.ascent.skyblock.world.region.RegionType;
import java.util.*;

public class QuestTalkToFarmHand extends Quest {
    @Override public String getID() { return "talk_to_farmhand"; }
    @Override public String getName() { return "Talk to the Farmhand"; }
    @Override public Map<String, Object> onStart(SkyblockPlayer player, ActiveQuest quest) { quest.getNewObjectiveText().forEach(player::sendMessage); return new HashMap<>(); }
    @Override public void onEnd(SkyblockPlayer player, Map<String, Object> customData, ActiveQuest quest) {
        quest.getObjectiveCompleteText(new ArrayList<>()).forEach(player::sendMessage);
        player.getActiveProfileData().getQuestData().startQuest(QuestCraftWheatMinion.class);
    }
    @Override public Set<RegionType> getValidRegions() { return Collections.singleton(RegionType.HUB); }
}
