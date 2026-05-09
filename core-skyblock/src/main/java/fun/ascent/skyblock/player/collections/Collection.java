package fun.ascent.skyblock.player.collections;

import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.collections.vars.CollectionCategory;
import fun.ascent.skyblock.player.collections.vars.CollectionReward;
import fun.ascent.skyblock.player.profiles.SkyblockProfile;
import fun.ascent.skyblock.world.WorldHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static fun.ascent.skyblock.player.skill.SkillReward.toRoman;

public class Collection {

    public String ITEM_ID;
    public CollectionCategory category;
    public List<CollectionReward> rewards;

    public Collection(String sbID,CollectionCategory category,CollectionReward... rewards){
        this.ITEM_ID = sbID;
        this.category = category;
        this.rewards = Arrays.asList(rewards);
        this.rewards.sort(Comparator.comparingInt(c -> c.requiredExp));
    }

    public void checkForRewards(SkyblockProfile profile,int oldProgress, int progress) {
        for(CollectionReward reward : rewards){
            if(reward.requiredExp > oldProgress && reward.requiredExp < progress){
                reward.onReward.run(profile);
                sendCollectionMessage(reward.rewardList(),rewards.indexOf(reward)+1,profile);
            }
        }
    }

    public void sendCollectionMessage(List<String>rewardList,int newLevel,SkyblockProfile profile){
        Component line = StringUtility.createLine('▬',53, NamedTextColor.YELLOW);
        Component level_up = StringUtility.text(
                "<gold> COLLECTION LEVEL UP <yellow>" + ItemRegistry.getItem(this.ITEM_ID).getDisplayName() + " <dark_gray>" +
                        toRoman(newLevel - 1) + "➜<yellow>"+ toRoman(newLevel)
        );
        Component rewardHeader = StringUtility.text("<green> REWARDS");

        profile.profilePlayers.forEach(player -> {
            player.addSkyblockXp(4);
            SkyblockPlayer player1 = WorldHandler.getPlayer(player.playerUUID);
            if(player1 == null) return;
            player1.sendMessage(line);
            player1.sendMessage(level_up);
            player1.sendMessage(Component.empty());
            player1.sendMessage(rewardHeader);
            rewardList.forEach(reward -> player1.sendMessage(StringUtility.text(" " + reward)));
            player1.sendMessage(StringUtility.text(" <dark_gray>+<aqua>4 SkyBlock XP"));
            player1.sendMessage(line);
        });
    }
}
