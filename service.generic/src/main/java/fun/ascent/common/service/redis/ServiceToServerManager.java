package fun.ascent.common.service.redis;

import fun.ascent.common.service.FromServiceChannels;
import fun.ascent.common.service.ServiceType;
import org.json.JSONObject;

import java.util.UUID;

public class ServiceToServerManager {
    private static ServiceType currentServiceType;

    public static void initialize(ServiceType serviceType) {
        currentServiceType = serviceType;
    }

    public static void sendToAllServers(FromServiceChannels channel, JSONObject message) {
        String channelName = "service_broadcast_" + channel.getChannelName();
        String messageContent = currentServiceType.name() + ";" + UUID.randomUUID() + ";" + message.toString();

        ServiceRedisManager.publish(channelName, messageContent);
    }

    public static void sendToServer(UUID serverUUID, FromServiceChannels channel, JSONObject message) {
        String channelName = "service_" + channel.getChannelName() + "_" + serverUUID.toString();
        String messageContent = currentServiceType.name() + ";" + UUID.randomUUID() + ";" + message.toString();

        ServiceRedisManager.publish(channelName, messageContent);
    }
}
