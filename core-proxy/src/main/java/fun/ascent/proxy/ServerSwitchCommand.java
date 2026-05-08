package fun.ascent.proxy;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import static fun.ascent.common.StringUtility.escapeMiniMessage;
import static fun.ascent.common.StringUtility.text;

final class ServerSwitchCommand implements SimpleCommand {

    private final ProxyServer proxy;
    private final String targetServer;
    private final String displayName;

    ServerSwitchCommand(ProxyServer proxy, String targetServer, String displayName) {
        this.proxy = proxy;
        this.targetServer = targetServer;
        this.displayName = displayName;
    }

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            invocation.source().sendMessage(text("<red>Only players can switch servers."));
            return;
        }

        RegisteredServer server = proxy.getAllServers().stream()
                .filter(s -> {
                    String name = s.getServerInfo().getName();
                    return name.equals(targetServer) || name.startsWith(targetServer + "-");
                })
                .min(java.util.Comparator.comparingInt(s -> s.getPlayersConnected().size()))
                .orElse(null);

        if (server == null) {
            player.sendMessage(text("<red>That server is not registered on the proxy."));
            return;
        }

        player.sendMessage(text("<yellow>Sending you to " + escapeMiniMessage(displayName) + "..."));
        player.createConnectionRequest(server).connect().thenAccept(result -> {
            if (!result.isSuccessful()) {
                player.sendMessage(text("<red>Could not connect to " + escapeMiniMessage(displayName) + "."));
            }
        });
    }
}
