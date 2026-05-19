package fun.ascent.proxy.listener;

import com.velocitypowered.api.proxy.ProxyServer;
import fun.ascent.common.service.FromServiceChannels;
import fun.ascent.common.service.redis.ServiceToClient;
import org.json.JSONObject;

import java.util.UUID;

import static fun.ascent.common.StringUtility.text;

public class RedisSendMessageListener implements ServiceToClient {
    private final ProxyServer proxy;

    public RedisSendMessageListener(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public FromServiceChannels getChannel() {
        return FromServiceChannels.SEND_MESSAGE;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        if (!message.has("playerUUID") || !message.has("message")) {
            return new JSONObject().put("success", false).put("error", "Missing fields");
        }

        try {
            UUID playerUUID = UUID.fromString(message.getString("playerUUID"));
            String textMessage = message.getString("message");

            proxy.getPlayer(playerUUID).ifPresent(player -> {
                player.sendMessage(text(textMessage));
            });

            return new JSONObject().put("success", true);
        } catch (IllegalArgumentException e) {
            return new JSONObject().put("success", false).put("error", "Invalid UUID");
        }
    }
}
