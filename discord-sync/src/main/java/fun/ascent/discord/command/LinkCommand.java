package fun.ascent.discord.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import fun.ascent.discord.manager.LinkManager;

import static fun.ascent.common.StringUtility.text;

public final class LinkCommand implements SimpleCommand {

    private final LinkManager manager;

    public LinkCommand(LinkManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) return;

        String[] args = invocation.arguments();
        if (args.length != 1) {
            DiscordCommand.sendLinkUsage(player);
            return;
        }

        manager.attemptLink(player, args[0]);
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source() instanceof Player;
    }
}
