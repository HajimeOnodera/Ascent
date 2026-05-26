package fun.ascent.skyblock.quest;

import fun.ascent.skyblock.events.PlayerRegionChangeEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.quest.impl.*;
import fun.ascent.skyblock.world.region.RegionType;
import net.minestom.server.MinecraftServer;

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

        MinecraftServer.getGlobalEventHandler().addListener(
            PlayerRegionChangeEvent.class,
            event -> {
                SkyblockPlayer player = event.getPlayer();
                if (player.getActiveProfileData() == null) return;
                QuestData questData = player.getActiveProfileData().getQuestData();
                if (questData == null) return;

                if (event.getTo() == RegionType.BANK) {
                    if (!questData.hasCompleted(QuestTalkToBanker.class) &&
                        !questData.isCurrentlyActive(QuestTalkToBanker.class)) {
                        questData.startQuest(QuestTalkToBanker.class);
                    }
                }

                else if (event.getFrom() == RegionType.BANK) {
                    if (questData.isCurrentlyActive(QuestTalkToBanker.class)) {
                        questData.cancelQuest(QuestTalkToBanker.class);
                    }
                }
            }
        );
    }
}
