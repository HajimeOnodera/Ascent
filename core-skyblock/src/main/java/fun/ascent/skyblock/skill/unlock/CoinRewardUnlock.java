package fun.ascent.skyblock.skill.unlock;

import fun.ascent.skyblock.player.SkyblockPlayer;

public class CoinRewardUnlock extends SkillUnlock {

    private final int coins;

    public CoinRewardUnlock(int coins) {
        this.coins = coins;
    }

    @Override
    public String display() {
        return "§8+§6" + coins + " §7Coins";
    }

    @Override
    public void apply(SkyblockPlayer player) {
        // TODO integrate with coin system when added
    }
}