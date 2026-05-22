package fun.ascent.skyblock.quest;

import fun.ascent.skyblock.quest.impl.*;

public class QuestRegistry {
    public static void init() {
        QuestData.registerQuest(QuestBreakLog.class);
        QuestData.registerQuest(QuestCraftWorkbench.class);
        QuestData.registerQuest(QuestCraftWoodenPickaxe.class);
        QuestData.registerQuest(QuestTalkJerry.class);
        QuestData.registerQuest(QuestUseTeleporter.class);
        QuestData.registerQuest(QuestTalkToBanker.class);
        QuestData.registerQuest(QuestDepositCoinsInBank.class);
        QuestData.registerQuest(QuestTalkToLibrarian.class);
        QuestData.registerQuest(QuestTalkToAuctionMaster.class);
        QuestData.registerQuest(QuestTalkToBlacksmith.class);
        QuestData.registerQuest(QuestMineCoal.class);
        QuestData.registerQuest(QuestTalkToBlacksmithAgain.class);
        QuestData.registerQuest(QuestKillZombies.class);
        QuestData.registerQuest(QuestTalkToBartender.class);
        QuestData.registerQuest(QuestTalkToLumberjack.class);
        QuestData.registerQuest(QuestBreakOaklog.class);
        QuestData.registerQuest(QuestTalkToLumberjackAgain.class);
        QuestData.registerQuest(QuestTalkToFarmer.class);
        QuestData.registerQuest(QuestCollectWheat.class);
        QuestData.registerQuest(QuestTalkToFarmerAgain.class);
        QuestData.registerQuest(QuestTalkToFarmHand.class);
        QuestData.registerQuest(QuestCraftWheatMinion.class);
        QuestData.registerQuest(QuestTalkToFarmhandAgain.class);
        QuestData.registerQuest(QuestGiveWoolToCarpenter.class);
        QuestData.registerQuest(QuestFindLazyMinerPickaxe.class);
        QuestData.registerQuest(QuestTalkToLazyMiner.class);
    }
}
