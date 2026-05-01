package fun.ascent.lobby.command;

import fun.ascent.lobby.transfer.ProxyTransfer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public final class ServerTransferCommand extends Command {

    public ServerTransferCommand(String targetServer, String... aliases) {
        super(targetServer, aliases);

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only players can switch servers.");
                return;
            }

            ProxyTransfer.send(player, targetServer);
        });
    }
}
