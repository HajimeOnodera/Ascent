package fun.ascent.proxy;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import fun.ascent.common.user.UserManager;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

import static fun.ascent.common.StringUtility.escapeMiniMessage;
import static fun.ascent.common.StringUtility.text;

final class ProxyAdminCommand implements SimpleCommand {

    private final CoreProxy plugin;
    private final ProxyServer proxy;

    ProxyAdminCommand(CoreProxy plugin, ProxyServer proxy) {
        this.plugin = plugin;
        this.proxy = proxy;
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        if (invocation.source() instanceof Player player) {
            return UserManager.getUser(player.getUniqueId()).getRank().isStaff();
        }
        return false; // Console is blocked
    }

    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length == 0 || args[0].equalsIgnoreCase("servers")) {
            sendServers(invocation);
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reload();
            invocation.source().sendMessage(text("<green>Ascent proxy config reloaded. Restart the proxy to apply command alias changes."));
            return;
        }

        invocation.source().sendMessage(text("<red>Usage: /ascentproxy <servers|reload>"));
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        if (invocation.arguments().length <= 1) {
            return List.of("servers", "reload");
        }
        return List.of();
    }

    private void sendServers(Invocation invocation) {
        List<Component> lines = new ArrayList<>();
        lines.add(text("<gold>Ascent servers:"));
        lines.add(text("<gray>Default: <yellow>" + escapeMiniMessage(plugin.config().defaultServer())));

        for (ProxyRoute route : plugin.config().routes()) {
            boolean online = proxy.getServer(route.targetServer()).isPresent();
            String stateColor = online ? "<green>" : "<red>";
            String state = online ? "registered" : "missing";

            lines.add(text("<gray>- <yellow>" + escapeMiniMessage(route.targetServer())
                    + stateColor + " [" + state + "] <gray>" + escapeMiniMessage(route.commands().toString())));
        }

        lines.forEach(invocation.source()::sendMessage);
    }
}
