package fun.ascent.discord.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import fun.ascent.discord.manager.LinkManager;

public final class UnlinkCommand implements SimpleCommand {

    private final LinkManager manager;

    public UnlinkCommand(LinkManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) return;
        if (invocation.arguments().length != 0) {
            DiscordCommand.sendUnlinkUsage(player);
            return;
        }
        manager.attemptUnlink(player);
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source() instanceof Player;
    }
}
