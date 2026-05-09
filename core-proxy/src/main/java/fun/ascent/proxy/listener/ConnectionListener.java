package fun.ascent.proxy.listener;

import fun.ascent.proxy.manager.*;
import fun.ascent.proxy.config.*;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import fun.ascent.common.user.User;
import fun.ascent.common.user.UserManager;

public class ConnectionListener {

    @Subscribe(order = PostOrder.FIRST)
    public void onLogin(PostLoginEvent event) {
        Player player = event.getPlayer();
        User user = UserManager.getUser(player.getUniqueId());
        user.setName(player.getUsername());
        UserManager.saveUser(user);

        // Start tracking this session's playtime
        PlaytimeTracker.onLogin(player.getUniqueId());
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        Player player = event.getPlayer();

        // Persist session playtime to MongoDB
        PlaytimeTracker.onDisconnect(player.getUniqueId());
    }
}
