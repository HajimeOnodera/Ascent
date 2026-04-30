package fun.ascent.skyblock.player;

import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import fun.ascent.skyblock.player.profiles.SkyblockProfile;
import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkyblockPlayer extends Player {

    @Getter private final Map<UUID, SkyblockProfile> playerProfiles = new HashMap<>();
    @Getter private SkyblockProfile activeProfile;
    @Getter private ProfilePlayer activeProfileData;

    public SkyblockPlayer(PlayerConnection playerConnection, GameProfile gameProfile) {
        super(playerConnection, gameProfile);
    }

    public void addProfile(SkyblockProfile profile) {
        this.playerProfiles.put(profile.profileID, profile);
    }

    public void setActiveProfile(UUID profileID) {
        SkyblockProfile targetProfile = playerProfiles.get(profileID);
        if (targetProfile == null) return;
        this.activeProfile = targetProfile;

        for (ProfilePlayer pp : targetProfile.profilePlayers) {
            if (pp.playerUUID.equals(this.getUuid())) {
                this.activeProfileData = pp;
                break;
            }
        }

        //TODO: Apply profile effects
    }


}
