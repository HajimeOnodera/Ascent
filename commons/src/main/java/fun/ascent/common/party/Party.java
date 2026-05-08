package fun.ascent.common.party;

import fun.ascent.common.protocol.Serializer;

import java.util.List;
import java.util.UUID;

public interface Party {
    Serializer<? extends Party> getSerializer();

    List<UUID> getParticipants();
}
