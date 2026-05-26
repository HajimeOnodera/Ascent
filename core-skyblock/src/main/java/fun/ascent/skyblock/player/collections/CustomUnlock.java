package fun.ascent.skyblock.player.collections;

import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.player.profiles.SkyblockProfile;

public class CustomUnlock extends CollectionUnlock {
    private final String award;

    public CustomUnlock(String award) {
        this.award = award;
    }

    @Override
    public String getDisplay() {
        return "§6" + ItemRegistry.formatName(award);
    }

    @Override
    public void apply(SkyblockProfile profile) {
    }
}
