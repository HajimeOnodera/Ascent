package fun.ascent.skyblock.quest;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.quest.impl.*;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum QuestSet {
    GETTING_STARTED(QuestBreakLog.class, QuestCraftWorkbench.class, QuestCraftWoodenPickaxe.class, QuestTalkJerry.class, QuestUseTeleporter.class),
    SAVING_UP(QuestTalkToBanker.class, QuestDepositCoinsInBank.class),
    LIBRARY_CARD(QuestTalkToLibrarian.class),
    AUCTIONEER(QuestTalkToAuctionMaster.class),
    TIME_TO_MINE(QuestTalkToBlacksmith.class, QuestMineCoal.class, QuestTalkToBlacksmithAgain.class),
    TIME_TO_STRIKE(QuestKillZombies.class, QuestTalkToBartender.class),
    TIMBER(QuestTalkToLumberjack.class, QuestBreakOaklog.class, QuestTalkToLumberjackAgain.class),
    FIRST_HARVEST(QuestTalkToFarmer.class, QuestCollectWheat.class, QuestTalkToFarmerAgain.class),
    BACK_AT_THE_BARNYARD(QuestTalkToFarmHand.class, QuestCraftWheatMinion.class, QuestTalkToFarmhandAgain.class),
    CARPENTRY(QuestGiveWoolToCarpenter.class),
    LOST_AND_FOUND(QuestFindLazyMinerPickaxe.class, QuestTalkToLazyMiner.class);

    private final Class<? extends Quest>[] quests;

    @SafeVarargs
    QuestSet(Class<? extends Quest>... quests) {
        this.quests = quests;
    }

    public static QuestSet getFromQuest(String questID) {
        for (QuestSet questSet : QuestSet.values()) {
            for (Class<? extends Quest> questClass : questSet.quests) {
                try {
                    Quest quest = QuestData.getQuestClass(questID);
                    if (quest != null && quest.getClass().equals(questClass)) {
                        return questSet;
                    }
                    // Fallback to fresh instantiation check if not yet registered
                    if (questClass.getDeclaredConstructor().newInstance().getID().equalsIgnoreCase(questID)) {
                        return questSet;
                    }
                } catch (Exception ignored) {}
            }
        }
        return null;
    }

    public boolean hasCompleted(SkyblockPlayer player) {
        if (player.getActiveProfileData() == null) return false;
        QuestData data = player.getActiveProfileData().getQuestData();
        if (data == null) return false;

        for (Class<? extends Quest> questClass : quests) {
            if (!data.hasCompleted(questClass)) {
                return false;
            }
        }
        return true;
    }

    public @Nullable Class<? extends Quest> getNextQuest(SkyblockPlayer player) {
        if (player.getActiveProfileData() == null) return null;
        QuestData data = player.getActiveProfileData().getQuestData();
        if (data == null) return null;

        for (Class<? extends Quest> questClass : quests) {
            if (!data.hasCompleted(questClass)) {
                return questClass;
            }
        }
        return null;
    }
}
