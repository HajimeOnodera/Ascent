package fun.ascent.proxy;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import fun.ascent.common.Ascent;
import fun.ascent.common.service.redis.ServerOutboundMessage;
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
    private ServerRegistryManager registryManager;
    private ServiceRegistryManager serviceRegistryManager;
    private static CoreProxy instance;

    @Inject
    public CoreProxy(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxy = proxy;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        instance = this;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        reload();
        
        Ascent.initialize();

        registerCommands();
        logger.info("Ascent proxy loaded with {} server route(s).", config.routes().size());

        // Start dynamic Redis-based server discovery
        registryManager = new ServerRegistryManager(proxy, logger);
        registryManager.start();

        // Start service registry for tracking service health
        serviceRegistryManager = new ServiceRegistryManager(proxy, logger);
        serviceRegistryManager.start();

        proxy.getEventManager().register(this, new ConnectionListener());
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        if (registryManager != null) {
            registryManager.stop();
        }
        if (serviceRegistryManager != null) {
            serviceRegistryManager.stop();
        }
    }

    @Subscribe
    public void onPlayerChooseInitialServer(PlayerChooseInitialServerEvent event) {
        String defaultServer = config.defaultServer();
        proxy.getAllServers().stream()
                .filter(s -> {
                    String name = s.getServerInfo().getName();
                    return name.equals(defaultServer) || name.startsWith(defaultServer + "-");
                })
                .min(java.util.Comparator.comparingInt(s -> s.getPlayersConnected().size())).ifPresent(event::setInitialServer);

    }

    public void reload() {
        config = ProxyConfig.load(dataDirectory, logger);
    }

    private void registerCommands() {
        proxy.getCommandManager().register(
                proxy.getCommandManager().metaBuilder("server")
                        .aliases("network")
                        .plugin(this)
                        .build(),
                new ProxyAdminCommand(this, proxy)
        );

        proxy.getCommandManager().register(
                proxy.getCommandManager().metaBuilder("friend")
                        .aliases("f")
                        .plugin(this)
                        .build(),
                new FriendCommand(proxy)
        );

        proxy.getCommandManager().register(
                proxy.getCommandManager().metaBuilder("rank")
                        .plugin(this)
                        .build(),
                new RankCommand(proxy)
        );

        proxy.getCommandManager().register(
                proxy.getCommandManager().metaBuilder("adminme")
                        .plugin(this)
                        .build(),
                new AdminMeCommand()
        );

        ServerOutboundMessage.registerClientListener(new RedisPropagateFriendEvent(proxy));

        proxy.getCommandManager().register(
                proxy.getCommandManager().metaBuilder("party")
                        .aliases("p")
                        .plugin(this)
                        .build(),
                new PartyCommand(proxy)
        );

        ServerOutboundMessage.registerClientListener(new RedisPropagatePartyEvent(proxy));

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

    public static ServiceRegistryManager getServiceRegistry() {
        return instance != null ? instance.serviceRegistryManager : null;
    }
}
