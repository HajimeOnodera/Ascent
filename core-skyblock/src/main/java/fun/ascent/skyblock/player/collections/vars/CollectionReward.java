package fun.ascent.skyblock.player.collections.vars;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class CollectionReward {

    public int requiredExp;
    public RewardTask onReward;

    public List<String> rewardList() {
        return onReward.rewardList();
    }
}
