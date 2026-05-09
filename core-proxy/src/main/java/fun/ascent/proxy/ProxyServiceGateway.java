package fun.ascent.proxy;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import fun.ascent.common.service.ProxyService;
import fun.ascent.common.service.ServiceType;

import java.util.Optional;
import java.util.function.Function;

import static fun.ascent.common.StringUtility.*;

public final class ProxyServiceGateway<E> {
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

    /**
     * Checks service availability. If offline, blocks the command and shows
     * an error to the player.
     *
     * @return true if the service is unavailable (command should be aborted)
     */
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
        player.sendMessage(text("<red>" + message));
    }
}
