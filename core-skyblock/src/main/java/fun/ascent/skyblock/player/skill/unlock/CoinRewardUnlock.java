package fun.ascent.skyblock.player.skill.unlock;

import fun.ascent.skyblock.player.SkyblockPlayer;

public class CoinRewardUnlock extends SkillUnlock {

    private final int coins;

    public CoinRewardUnlock(int coins) {
        this.coins = coins;
    }

    @Override
    public String display() {
        return "<dark_gray>+<gold>" + coins + " <gray>Coins";
    }

    @Override
    public void apply(SkyblockPlayer player) {
        if (player.getActiveProfileData() == null) return;
        player.setCoins(player.getCoins() + coins);
    }
}
