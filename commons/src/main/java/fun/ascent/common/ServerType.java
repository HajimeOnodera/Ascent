package fun.ascent.common;

import lombok.Getter;

@Getter
public enum ServerType {
    HYPIXEL_LOBBY(false),
    SKYBLOCK(true);

    private final boolean isSkyblock;

    ServerType(boolean isSkyblock) {
        this.isSkyblock = isSkyblock;
    }

    public static boolean isServerType(String type) {
        for (ServerType a : values())
            if (type.equalsIgnoreCase(a.name())) return true;
        return false;
    }
    public static ServerType getSkyblockServer(String name) {
        if (!name.startsWith("SKYBLOCK_")) {
            return valueOf("SKYBLOCK_" + name.toUpperCase());
        } else {
            return valueOf(name);
        }
    }

    public String formatName() {
        return StringUtility.toNormalCase(name());
    }

    public String skyblockName() {
        return name().replace("SKYBLOCK_", "");
    }
}
