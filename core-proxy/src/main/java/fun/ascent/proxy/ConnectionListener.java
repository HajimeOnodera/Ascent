package fun.ascent.proxy;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
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
    }
}
