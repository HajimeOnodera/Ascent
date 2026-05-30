package fun.ascent.skyblock.quest;

import fun.ascent.skyblock.player.SkyblockPlayer;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ActiveQuest {
    private String questID;
    private long startedTime;
    private long endedTime;
    private int progress;
    private Map<String, Object> customData;

    public ActiveQuest(String questID, int progress) {
        this.questID = questID;
        this.progress = progress;
        this.startedTime = System.currentTimeMillis();
        this.customData = new HashMap<>();
        this.endedTime = 0L;
    }

    public ActiveQuest(String questID, int progress, long startedTime, Map<String, Object> customData, long endedTime) {
        this.questID = questID;
        this.progress = progress;
        this.startedTime = startedTime;
        this.customData = customData;
        this.endedTime = endedTime;
    }

    @Override
    public String toString() {
        Quest quest = QuestData.getQuestClass(questID);
        return quest != null ? quest.getName() : questID;
    }

    public void checkIfQuestEnded(SkyblockPlayer player) {
        Quest quest = QuestData.getQuestClass(questID);
        if (quest instanceof QuestProgress progressQuest) {
            if (progress >= progressQuest.getMaxProgress()) {
                if (player.getActiveProfileData() != null) {
                    player.getActiveProfileData().getQuestData().endQuest(questID);
                }
            }
        }
    }

    public List<String> getObjectiveCompleteText(ArrayList<String> rewards) {
        return getObjectiveCompleteText("OBJECTIVE COMPLETE", rewards);
    }

    public List<String> getObjectiveCompleteText(String title, ArrayList<String> rewards) {
        if (rewards == null || rewards.isEmpty())
            return Arrays.asList(
                    "§7 ",
                    "§6§l  " + title,
                    "§7 ");

        ArrayList<String> display = new ArrayList<>(Arrays.asList(
                "§7 ",
                "§6§l  " + title,
                "§7 ",
                "§a§l  REWARD"
        ));
        display.addAll(rewards.stream().map(reward -> "§8  +§b" + reward).toList());
        display.add("§7 ");
        return display;
    }

    public List<String> getNewObjectiveText() {
        Quest quest = QuestData.getQuestClass(questID);
        String name = quest != null ? quest.getName() : questID;

        return Arrays.asList(
                "§7 ",
                "§6§l  NEW OBJECTIVE",
                "§f  " + name,
                "§7 ");
    }
}