package fun.ascent.skyblock.player.collections.vars;

import fun.ascent.skyblock.player.profiles.SkyblockProfile;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public abstract class RewardTask {

    public abstract void run(SkyblockProfile profile);

    public abstract List<String> rewardList();
}
