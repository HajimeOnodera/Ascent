package fun.ascent.skyblock.config;

import java.io.File;

public final class ConfigPaths {
    private ConfigPaths() {
    }

    public static File skyblockPath(String relativePath) {
        File primary = new File("./configuration/skyblock", relativePath);
        if (primary.exists()) {
            return primary;
        }

        File moduleLocal = new File("./core-skyblock/configuration/skyblock", relativePath);
        if (moduleLocal.exists()) {
            return moduleLocal;
        }

        return primary;
    }
}
