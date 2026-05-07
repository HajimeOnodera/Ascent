package fun.ascent.proxy;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import fun.ascent.common.user.UserManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.List;

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
            invocation.source().sendMessage(Component.text("Ascent proxy config reloaded. Restart the proxy to apply command alias changes.", NamedTextColor.GREEN));
            return;
        }

        invocation.source().sendMessage(Component.text("Usage: /ascentproxy <servers|reload>", NamedTextColor.RED));
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
        lines.add(Component.text("Ascent servers:", NamedTextColor.GOLD));
        lines.add(Component.text("Default: ", NamedTextColor.GRAY)
                .append(Component.text(plugin.config().defaultServer(), NamedTextColor.YELLOW)));

        for (ProxyRoute route : plugin.config().routes()) {
            boolean online = proxy.getServer(route.targetServer()).isPresent();
            NamedTextColor stateColor = online ? NamedTextColor.GREEN : NamedTextColor.RED;
            String state = online ? "registered" : "missing";

            lines.add(Component.text("- ", NamedTextColor.GRAY)
                    .append(Component.text(route.targetServer(), NamedTextColor.YELLOW))
                    .append(Component.text(" [" + state + "] ", stateColor))
                    .append(Component.text(route.commands().toString(), NamedTextColor.GRAY)));
        }

        lines.forEach(invocation.source()::sendMessage);
    }
}
