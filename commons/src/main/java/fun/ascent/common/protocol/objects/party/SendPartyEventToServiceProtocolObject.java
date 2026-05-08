package fun.ascent.common.protocol.objects.party;

import fun.ascent.common.party.PartyEvent;
import fun.ascent.common.protocol.ProtocolObject;
import fun.ascent.common.protocol.Serializer;
import org.json.JSONObject;

public class SendPartyEventToServiceProtocolObject extends ProtocolObject
        <SendPartyEventToServiceProtocolObject.SendPartyEventToServiceMessage,
        SendPartyEventToServiceProtocolObject.SendPartyEventToServiceResponse> {


    @Override
    public Serializer<SendPartyEventToServiceMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(SendPartyEventToServiceMessage value) {
                JSONObject json = new JSONObject();
                json.put("event", value.event.getSerializer().serialize(value.event));
                json.put("eventType", value.event.getClass().getSimpleName());
                return json.toString();
            }

            @Override
            public SendPartyEventToServiceMessage deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                String eventType = jsonObject.getString("eventType");
                PartyEvent event = PartyEvent.findFromType(eventType);
                PartyEvent deserializedEvent = (PartyEvent) event.getSerializer().deserialize(jsonObject.getString("event"));
                return new SendPartyEventToServiceMessage(deserializedEvent);
            }

            @Override
            public SendPartyEventToServiceMessage clone(SendPartyEventToServiceMessage value) {
                return new SendPartyEventToServiceMessage(value.event);
            }
        };
    }

    @Override
    public Serializer<SendPartyEventToServiceResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(SendPartyEventToServiceResponse value) {
                return Boolean.toString(value.success);
            }

            @Override
            public SendPartyEventToServiceResponse deserialize(String json) {
                return new SendPartyEventToServiceResponse(json.equals("true"));
            }

            @Override
            public SendPartyEventToServiceResponse clone(SendPartyEventToServiceResponse value) {
                return new SendPartyEventToServiceResponse(value.success);
            }
        };
    }

    public record SendPartyEventToServiceMessage(PartyEvent event) {
    }

    public record SendPartyEventToServiceResponse(boolean success) {
    }
}
