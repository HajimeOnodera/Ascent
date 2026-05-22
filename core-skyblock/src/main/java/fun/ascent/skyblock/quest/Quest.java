package fun.ascent.skyblock.quest;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.world.region.RegionType;
import java.util.Map;
import java.util.Set;

public abstract class Quest {
    public abstract String getID();

    public abstract String getName();

    public abstract Map<String, Object> onStart(SkyblockPlayer player, ActiveQuest activeQuest);

    public abstract void onEnd(SkyblockPlayer player, Map<String, Object> customData, ActiveQuest activeQuest);

    public abstract Set<RegionType> getValidRegions();

    public Double getAttachedSkyBlockXP() {
        return 0D;
    }

    public boolean hasStartedOrCompleted(SkyblockPlayer player) {
        if (player.getActiveProfileData() == null) return false;
        QuestData data = player.getActiveProfileData().getQuestData();
        if (data == null) return false;
        return data.getActiveQuests().stream().anyMatch(q -> q.getQuestID().equals(getID())) ||
               data.getCompletedQuests().stream().anyMatch(q -> q.getQuestID().equals(getID()));
    }
}
