package fun.ascent.skyblock.config;

import java.io.File;

public final class ConfigPaths {
    private ConfigPaths() {
    }

    public static File skyblockPath(String relativePath) {
        return new File("./configuration/skyblock", relativePath);
    }
}
