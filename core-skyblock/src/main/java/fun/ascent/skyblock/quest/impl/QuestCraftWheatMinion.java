package fun.ascent.skyblock.quest.impl;

import fun.ascent.skyblock.events.PlayerCraftItemEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.quest.*;
import fun.ascent.skyblock.world.region.RegionType;
import java.util.*;

public class QuestCraftWheatMinion extends Quest {

    @QuestEvent
    public void onCraft(PlayerCraftItemEvent event) {
        SkyblockPlayer sp = event.getPlayer();
        if (sp.getActiveProfileData() == null) return;

        // Check if the item is a wheat minion
        String id = event.getCraftedItem().toString();
        if (id.toLowerCase().contains("wheat_minion") || id.toLowerCase().contains("minion")) {
            sp.getActiveProfileData().getQuestData().endQuest(QuestCraftWheatMinion.class);
        }
    }

    @Override public String getID() { return "craft_wheat_minion"; }
    @Override public String getName() { return "Craft a Wheat Minion"; }
    @Override public Map<String, Object> onStart(SkyblockPlayer player, ActiveQuest quest) { quest.getNewObjectiveText().forEach(player::sendMessage); return new HashMap<>(); }
    @Override public void onEnd(SkyblockPlayer player, Map<String, Object> customData, ActiveQuest quest) {
        quest.getObjectiveCompleteText(new ArrayList<>()).forEach(player::sendMessage);
        player.getActiveProfileData().getQuestData().startQuest(QuestTalkToFarmhandAgain.class);
    }
    @Override public Set<RegionType> getValidRegions() { return Collections.singleton(RegionType.HUB); }
}
