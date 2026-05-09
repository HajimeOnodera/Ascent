package fun.ascent.proxy.config;

import java.util.List;

public record ProxyRoute(String targetServer, List<String> commands) {

    public String displayName() {
        if (targetServer.isBlank()) {
            return targetServer;
        }
        return Character.toUpperCase(targetServer.charAt(0)) + targetServer.substring(1);
    }
}
