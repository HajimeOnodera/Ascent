package fun.ascent.proxy;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import fun.ascent.common.service.ProxyService;
import fun.ascent.common.service.ServiceType;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Optional;
import java.util.function.Function;

public final class ProxyServiceGateway<E> {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private final ServiceType serviceType;
    private final String displayName;
    private final ProxyService service;
    private final Function<E, Object> requestFactory;

    public ProxyServiceGateway(ServiceType serviceType, String displayName, Function<E, Object> requestFactory) {
        this.serviceType = serviceType;
        this.displayName = displayName;
        this.service = new ProxyService(serviceType);
        this.requestFactory = requestFactory;
    }

    public boolean ensureAvailable(Player player) {
        ServiceRegistryManager registry = CoreProxy.getServiceRegistry();
        if (registry != null && registry.isServiceOnline(serviceType)) {
            return false;
        }

        sendError(player, "The " + displayName + " service is currently offline. Please try again later.");
        return true;
    }

    public Optional<Player> findOnlinePlayer(ProxyServer proxy, Player requester, String playerName, String notFoundMessage) {
        Optional<Player> player = proxy.getPlayer(playerName);
        if (player.isEmpty()) {
            sendError(requester, notFoundMessage);
        }
        return player;
    }

    public void send(E event) {
        service.handleRequest(requestFactory.apply(event));
    }

    public void sendError(Player player, String message) {
        player.sendMessage(MINI_MESSAGE.deserialize("<red>" + message));
    }
}
