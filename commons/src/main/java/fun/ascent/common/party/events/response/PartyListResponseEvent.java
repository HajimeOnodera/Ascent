package fun.ascent.common.party.events.response;

import fun.ascent.common.party.FullParty;
import fun.ascent.common.party.PartyResponseEvent;
import fun.ascent.common.protocol.Serializer;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class PartyListResponseEvent extends PartyResponseEvent {
    private final FullParty party;
    private final UUID requester;
    private final List<MemberInfo> members;

    public PartyListResponseEvent(FullParty party, UUID requester, List<MemberInfo> members) {
        super(party);
        this.party = party;
        this.requester = requester;
        this.members = members;
    }

    @Override
    public Serializer<PartyListResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyListResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();
                json.put("party", partySerializer.serialize(value.getParty()));
                json.put("requester", value.getRequester().toString());
                JSONArray membersArray = new JSONArray();
                for (MemberInfo member : value.getMembers()) {
                    JSONObject memberJson = new JSONObject();
                    memberJson.put("uuid", member.uuid().toString());
                    memberJson.put("name", member.name());
                    memberJson.put("role", member.role().name());
                    memberJson.put("online", member.online());
                    membersArray.put(memberJson);
                }
                json.put("members", membersArray);
                return json.toString();
            }

            @Override
            public PartyListResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID requester = UUID.fromString(jsonObject.getString("requester"));
                List<MemberInfo> members = jsonObject.getJSONArray("members").toList().stream()
                        .map(obj -> {
                            JSONObject memberJson = new JSONObject(obj.toString());
                            return new MemberInfo(
                                    UUID.fromString(memberJson.getString("uuid")),
                                    memberJson.getString("name"),
                                    FullParty.Role.valueOf(memberJson.getString("role")),
                                    memberJson.getBoolean("online")
                            );
                        }).toList();
                return new PartyListResponseEvent(party, requester, members);
            }

            @Override
            public PartyListResponseEvent clone(PartyListResponseEvent value) {
                return new PartyListResponseEvent(value.getParty(), value.getRequester(), value.getMembers());
            }
        };
    }

    public record MemberInfo(UUID uuid, String name, FullParty.Role role, boolean online) {}
}
