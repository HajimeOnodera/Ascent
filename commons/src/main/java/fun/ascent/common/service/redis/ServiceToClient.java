package fun.ascent.common.service.redis;

import fun.ascent.common.service.FromServiceChannels;
import org.json.JSONObject;

public interface ServiceToClient {
    FromServiceChannels getChannel();
    JSONObject onMessage(JSONObject message);
}
