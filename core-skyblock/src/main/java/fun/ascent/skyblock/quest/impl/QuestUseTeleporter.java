package fun.ascent.skyblock.quest.impl;

import fun.ascent.skyblock.events.PlayerRegionChangeEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.quest.*;
import fun.ascent.skyblock.world.region.RegionType;

import java.util.*;

public class QuestUseTeleporter extends Quest {

    @QuestEvent
    public void onRegionChange(PlayerRegionChangeEvent event) {
        if (event.getTo() != RegionType.VILLAGE) {
            return;
        }

        SkyblockPlayer player = event.getPlayer();
        if (player.getActiveProfileData() == null) return;

        QuestData data = player.getActiveProfileData().getQuestData();
        if (data.isCurrentlyActive(QuestUseTeleporter.class)) {
            data.endQuest(QuestUseTeleporter.class);
        }
    }

    @Override
    public String getID() {
        return "use_teleporter";
    }

    @Override
    public String getName() {
        return "Use the teleporter";
    }

    @Override
    public Map<String, Object> onStart(SkyblockPlayer player, ActiveQuest quest) {
        quest.getNewObjectiveText().forEach(player::sendMessage);
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyblockPlayer player, Map<String, Object> customData, ActiveQuest quest) {
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.PRIVATE_ISLAND);
    }
}
