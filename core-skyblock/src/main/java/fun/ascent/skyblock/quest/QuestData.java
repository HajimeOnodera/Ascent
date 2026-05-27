package fun.ascent.skyblock.quest;

import fun.ascent.skyblock.player.SkyblockPlayer;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.Nullable;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;

import java.util.*;

@Getter
public class QuestData {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuestData.class);
    private static final HashMap<String, Quest> questRegistry = new HashMap<>();

    private final List<ActiveQuest> activeQuests = new ArrayList<>();
    private final List<ActiveQuest> completedQuests = new ArrayList<>();
    
    @Setter
    private transient SkyblockPlayer skyblockPlayer;

    public Map.Entry<ActiveQuest, Boolean> getQuest(String questID) {
        for (ActiveQuest quest : activeQuests) {
            if (quest.getQuestID().equalsIgnoreCase(questID)) {
                return Map.entry(quest, false);
            }
        }

        for (ActiveQuest quest : completedQuests) {
            if (quest.getQuestID().equalsIgnoreCase(questID)) {
                return Map.entry(quest, true);
            }
        }

        return null;
    }

    public Map.Entry<ActiveQuest, Boolean> getQuest(Class<? extends Quest> questClass) {
        String questID = getQuestIDFromClass(questClass);
        if (questID == null) return null;
        return getQuest(questID);
    }

    public boolean isCurrentlyActive(String questID) {
        return activeQuests.stream().anyMatch(quest -> quest.getQuestID().equals(questID));
    }

    public boolean isCurrentlyActive(Class<? extends Quest> questClass) {
        String questID = getQuestIDFromClass(questClass);
        if (questID == null) return false;
        return isCurrentlyActive(questID);
    }

    public boolean hasCompleted(String questID) {
        return completedQuests.stream().anyMatch(quest -> quest.getQuestID().equals(questID));
    }

    public boolean hasCompleted(Class<? extends Quest> questClass) {
        String questID = getQuestIDFromClass(questClass);
        if (questID == null) return false;
        return hasCompleted(questID);
    }

    public void startQuest(Class<? extends Quest> questClass) {
        String questID = getQuestIDFromClass(questClass);
        if (questID == null) return;

        if (activeQuests.stream().anyMatch(quest -> quest.getQuestID().equals(questID))) {
            return;
        }
        if (completedQuests.stream().anyMatch(quest -> quest.getQuestID().equals(questID))) {
            return;
        }

        Quest quest = getQuestFromCache(questID);
        if (quest == null) return;

        ActiveQuest activeQuest = new ActiveQuest(questID, 0);
        
        Map<String, Object> data = quest.onStart(getSkyblockPlayer(), activeQuest);
        if (data != null) {
            activeQuest.setCustomData(data);
        }

        activeQuests.add(activeQuest);
    }

    public void endQuest(String questID) {
        Optional<ActiveQuest> activeQuestOpt = activeQuests.stream()
                .filter(quest -> quest.getQuestID().equals(questID))
                .findFirst();

        if (activeQuestOpt.isEmpty()) {
            return;
        }

        ActiveQuest activeQuest = activeQuestOpt.get();
        Quest quest = getQuestFromCache(questID);
        if (quest != null) {
            quest.onEnd(getSkyblockPlayer(), activeQuest.getCustomData(), activeQuest);
        }

        activeQuest.setEndedTime(System.currentTimeMillis());
        activeQuests.remove(activeQuest);
        completedQuests.add(activeQuest);

        QuestSet questSet = QuestSet.getFromQuest(questID);
        if (questSet != null && questSet.hasCompleted(getSkyblockPlayer())) {
            questSet.grantRewards(getSkyblockPlayer());
            if (getSkyblockPlayer() != null) {
                ArrayList<String> setRewards = new ArrayList<>();
                for (String r : questSet.getRewardsDisplay()) {
                    if (r != null && !r.trim().isEmpty()) {
                        setRewards.add(r);
                    }
                }
                activeQuest.getObjectiveCompleteText(setRewards).forEach(getSkyblockPlayer()::sendMessage);
                
                getSkyblockPlayer().playSound(Sound.sound(
                        Key.key("ui.toast.challenge_complete"),
                        Sound.Source.PLAYER, 0.75f, 1f));
            }
        } else {
            if (getSkyblockPlayer() != null) {
                activeQuest.getObjectiveCompleteText(new ArrayList<>()).forEach(getSkyblockPlayer()::sendMessage);
                getSkyblockPlayer().playSound(Sound.sound(
                        Key.key("entity.experience_orb.pickup"),
                        Sound.Source.PLAYER, 0.5f, 1.2f));
            }
        }
    }

    public void endQuest(Class<? extends Quest> questClass) {
        String questID = getQuestIDFromClass(questClass);
        if (questID != null) {
            endQuest(questID);
        }
    }

    public void cancelQuest(Class<? extends Quest> questClass) {
        String questID = getQuestIDFromClass(questClass);
        if (questID == null) return;
        activeQuests.removeIf(quest -> quest.getQuestID().equals(questID));
    }

    public @Nullable QuestProgress getAsProgressQuest(String questID) {
        Quest quest = getQuestFromCache(questID);
        return (quest instanceof QuestProgress) ? (QuestProgress) quest : null;
    }

    public @Nullable QuestProgress getAsProgressQuest(Class<? extends Quest> questClass) {
        String questID = getQuestIDFromClass(questClass);
        if (questID == null) return null;
        return getAsProgressQuest(questID);
    }

    public static Quest getQuestFromCache(String questID) {
        return questRegistry.get(questID);
    }

    public static Quest getQuestClass(String questID) {
        return questRegistry.get(questID);
    }

    public static List<String> getAllQuestIDs() {
        return new ArrayList<>(questRegistry.keySet());
    }

    public static void registerQuest(Class<? extends Quest> questClass) {
        try {
            Quest quest = questClass.getDeclaredConstructor().newInstance();
            questRegistry.put(quest.getID(), quest);
            QuestEventHandler.registerQuest(quest);
        } catch (Exception e) {
            LOGGER.error("Failed to register quest: " + questClass.getName(), e);
        }
    }

    @Nullable
    private static String getQuestIDFromClass(Class<? extends Quest> questClass) {
        try {
            Quest quest = questRegistry.values().stream()
                    .filter(q -> q.getClass().equals(questClass))
                    .findFirst()
                    .orElse(null);
            
            if (quest != null) return quest.getID();

            return questClass.getDeclaredConstructor().newInstance().getID();
        } catch (Exception e) {
            return null;
        }
    }
}
