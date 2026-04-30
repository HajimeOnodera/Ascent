package fun.ascent.skyblock.player.profiles;

import fun.ascent.skyblock.player.stats.Stats;

import java.util.EnumMap;
import java.util.UUID;

public class ProfilePlayer {

    public UUID profileID;
    public UUID playerUUID;
    public EnumMap<Stats, Integer> stats = new EnumMap<>(Stats.class);

    public ProfilePlayer(UUID profileID, UUID playerUUID) {
        this.profileID = profileID;
        this.playerUUID = playerUUID;
    }
}
