package fun.ascent.skyblock.player.collections;

import fun.ascent.skyblock.player.profiles.SkyblockProfile;

public abstract class CollectionUnlock {
    public abstract String getDisplay();
    public abstract void apply(SkyblockProfile profile);

    public enum Type {
        RECIPE,
        XP,
        CUSTOM
    }
}
