package fun.ascent.lobby.transfer;

import net.minestom.server.entity.Player;
import net.minestom.server.network.NetworkBuffer;

public final class ProxyTransfer {

    private ProxyTransfer() {
    }

    public static void send(Player player, String targetServer) {
        player.sendPluginMessage("bungeecord:main", NetworkBuffer.makeArray(buffer -> {
            buffer.write(NetworkBuffer.STRING_IO_UTF8, "Connect");
            buffer.write(NetworkBuffer.STRING_IO_UTF8, targetServer);
        }));
    }
}
