package fun.ascent.skyblock.proxy;

import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.network.NetworkBuffer;

public final class ProxyTransferCommand extends Command {

    public ProxyTransferCommand(String targetServer, String... aliases) {
        super(targetServer, aliases);

        setDefaultExecutor((sender, ctx) -> {
            if (!(sender instanceof SkyblockPlayer player)) {
                sender.sendMessage("Only players can switch servers.");
                return;
            }

            player.sendPluginMessage("bungeecord:main", NetworkBuffer.makeArray(buffer -> {
                buffer.write(NetworkBuffer.STRING_IO_UTF8, "Connect");
                buffer.write(NetworkBuffer.STRING_IO_UTF8, targetServer);
            }));
        });
    }
}
