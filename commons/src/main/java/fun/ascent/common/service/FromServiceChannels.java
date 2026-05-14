package fun.ascent.common.service;

import lombok.Getter;

@Getter
public enum FromServiceChannels {
    PROPAGATE_FRIEND_EVENT("propagate_friend_event"),
    PROPAGATE_PARTY_EVENT("propagate_party_event"),
    SEND_MESSAGE("send_message"),
    GET_SKYBLOCK_DATA("get_skyblock_data"),
    UPDATE_PLAYER_DATA("update_player_data"),
    LOCK_PLAYER_DATA("lock_player_data"),
    UNLOCK_PLAYER_DATA("unlock_player_data"),
    KICK_FROM_GUI("kick_from_gui"),
    GAME_INFORMATION("game_information"),
    ;

    private final String channelName;

    FromServiceChannels(String channelName) {
        this.channelName = channelName;
    }

    public static FromServiceChannels getChannelName(String channel) {
        for (FromServiceChannels serviceChannel : FromServiceChannels.values()) {
            if (serviceChannel.channelName.equalsIgnoreCase(channel)) {
                return serviceChannel;
            }
        }
        throw new IllegalArgumentException("Unknown channelName: " + channel);
    }
}
