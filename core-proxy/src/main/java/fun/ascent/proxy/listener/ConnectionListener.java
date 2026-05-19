package fun.ascent.proxy.listener;

import fun.ascent.proxy.manager.*;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import fun.ascent.common.user.User;
import fun.ascent.common.user.UserManager;
import fun.ascent.common.StringUtility;

public class ConnectionListener {

    @Subscribe(order = PostOrder.FIRST)
    public void onLogin(PostLoginEvent event) {
        Player player = event.getPlayer();
        User user = UserManager.getUser(player.getUniqueId());
        user.setName(player.getUsername());
        UserManager.saveUser(user);

        // Start tracking this session's playtime
        PlaytimeTracker.onLogin(player.getUniqueId());

        // Notify services
        FriendManager.sendJoinEvent(player);
        PartyManager.sendRejoinEvent(player);
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        Player player = event.getPlayer();

        // Persist session playtime to MongoDB
        PlaytimeTracker.onDisconnect(player.getUniqueId());

        // Notify services
        FriendManager.sendLeaveEvent(player);
        PartyManager.sendDisconnectEvent(player);
    }

    @Subscribe
    public void onServerPreConnect(ServerPreConnectEvent event) {
        Player player = event.getPlayer();
        RegisteredServer targetServer = event.getResult().getServer().orElse(event.getOriginalServer());
        if (targetServer != null) {
            String name = targetServer.getServerInfo().getName();

            // Only send the transition message if they are moving from a previous server (i.e. not proxy join)
            if (event.getPreviousServer() != null) {
                player.sendMessage(StringUtility.text("<gray>Sending to server " + name + "..."));
                PartyManager.sendSwitchServerEvent(player, name);
            }
        }
    }
}
