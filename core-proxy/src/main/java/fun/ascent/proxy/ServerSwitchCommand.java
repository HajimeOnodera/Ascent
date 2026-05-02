package fun.ascent.proxy;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

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
            invocation.source().sendMessage(Component.text("Only players can switch servers.", NamedTextColor.RED));
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
            player.sendMessage(Component.text("That server is not registered on the proxy.", NamedTextColor.RED));
            return;
        }

        player.sendMessage(Component.text("Sending you to " + displayName + "...", NamedTextColor.YELLOW));
        player.createConnectionRequest(server).connect().thenAccept(result -> {
            if (!result.isSuccessful()) {
                player.sendMessage(Component.text("Could not connect to " + displayName + ".", NamedTextColor.RED));
            }
        });
    }
}
