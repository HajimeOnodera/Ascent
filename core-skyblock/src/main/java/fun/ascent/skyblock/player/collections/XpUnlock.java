package fun.ascent.skyblock.player.collections;

import fun.ascent.skyblock.player.profiles.SkyblockProfile;

public class XpUnlock extends CollectionUnlock {
    private final int amount;

    public XpUnlock(int amount) {
        this.amount = amount;
    }

    @Override
    public String getDisplay() {
        return "§8+§b" + amount + " SkyBlock XP";
    }

    @Override
    public void apply(SkyblockProfile profile) {
        profile.profilePlayers.forEach(pp -> pp.addSkyblockXp(amount));
    }
}
