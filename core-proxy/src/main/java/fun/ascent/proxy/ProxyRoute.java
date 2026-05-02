package fun.ascent.proxy;

import java.util.List;

record ProxyRoute(String targetServer, List<String> commands) {

    String displayName() {
        if (targetServer.isBlank()) {
            return targetServer;
        }
        return Character.toUpperCase(targetServer.charAt(0)) + targetServer.substring(1);
    }
}
