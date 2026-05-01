package fun.ascent.proxy;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.List;

@Plugin(
        id = "ascent-proxy",
        name = "Ascent Proxy",
        version = "1.0.0",
        authors = {"Ascent"}
)
public final class CoreProxy {

    private final ProxyServer proxy;
    private final Logger logger;
    private final Path dataDirectory;
    private ProxyConfig config;

    @Inject
    public CoreProxy(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxy = proxy;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        reload();
        registerCommands();
        logger.info("Ascent proxy loaded with {} server route(s).", config.routes().size());
    }

    public void reload() {
        config = ProxyConfig.load(dataDirectory, logger);
    }

    private void registerCommands() {
        proxy.getCommandManager().register(
                proxy.getCommandManager().metaBuilder("ascentproxy")
                        .aliases("aproxy", "network")
                        .plugin(this)
                        .build(),
                new ProxyAdminCommand(this, proxy)
        );

        for (ProxyRoute route : config.routes()) {
            List<String> commands = route.commands();
            if (commands.isEmpty()) {
                continue;
            }

            String primary = commands.getFirst();
            String[] aliases = commands.stream().skip(1).toArray(String[]::new);

            proxy.getCommandManager().register(
                    proxy.getCommandManager().metaBuilder(primary)
                            .aliases(aliases)
                            .plugin(this)
                            .build(),
                    new ServerSwitchCommand(proxy, route.targetServer(), route.displayName())
            );
        }
    }

    public ProxyConfig config() {
        return config;
    }

    public boolean hasServer(String serverName) {
        return proxy.getServer(serverName).isPresent();
    }

    public List<String> configuredServers() {
        return config.routes().stream()
                .map(ProxyRoute::targetServer)
                .distinct()
                .toList();
    }

    public RegisteredServer requireServer(String serverName) {
        return proxy.getServer(serverName).orElseThrow();
    }
}
