package fun.ascent.skyblock.player.collections;

import fun.ascent.skyblock.player.profiles.SkyblockProfile;

public class CustomUnlock extends CollectionUnlock {
    private final String award;

    public CustomUnlock(String award) {
        this.award = award;
    }

    @Override
    public String getDisplay() {
        return "§6" + award.replace("_", " ");
    }

    @Override
    public void apply(SkyblockProfile profile) {
        // Handle custom awards (e.g., bag upgrades)
    }
}
