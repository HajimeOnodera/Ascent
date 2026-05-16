package fun.ascent.discord;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import fun.ascent.discord.command.DiscordCommand;
import fun.ascent.discord.command.LinkCommand;
import fun.ascent.discord.command.UnlinkCommand;
import fun.ascent.discord.data.LinkRepository;
import fun.ascent.discord.manager.LinkManager;
import org.slf4j.Logger;

@Plugin(
        id = "discord-sync",
        name = "DiscordSync",
        version = "1.0.0",
        dependencies = {@Dependency(id = "ascent-proxy")}
)
public final class DiscordSync {

    private static Logger instanceLog;
    private final ProxyServer proxy;
    private final Logger log;

    @Inject
    public DiscordSync(ProxyServer proxy, Logger log) {
        this.proxy = proxy;
        this.log = log;
        instanceLog = log;
    }

    @Subscribe
    public void onInit(ProxyInitializeEvent event) {
        var repo = new LinkRepository();
        var manager = new LinkManager(repo);

        proxy.getCommandManager().register(
                proxy.getCommandManager().metaBuilder("discord").plugin(this).build(),
                new DiscordCommand(manager)
        );

        proxy.getCommandManager().register(
                proxy.getCommandManager().metaBuilder("link").plugin(this).build(),
                new LinkCommand(manager)
        );

        proxy.getCommandManager().register(
                proxy.getCommandManager().metaBuilder("unlink").plugin(this).build(),
                new UnlinkCommand(manager)
        );

        log.info("DiscordSync loaded.");
    }

    public static Logger logger() {
        return instanceLog;
    }
}
