package fun.ascent.skyblock.player;

import net.minestom.server.entity.Player;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;

import java.util.List;
import java.util.UUID;

public class SkyblockPlayer extends Player {

    public List<SkyblockProfile> profileList;
    public UUID activeProfile;
    public transient SkyblockProfile currentProfile;

    public SkyblockPlayer(PlayerConnection playerConnection, GameProfile gameProfile) {
        super(playerConnection, gameProfile);
        this.loadActiveProfile();
    }

    public void loadActiveProfile() {
        if(profileList.isEmpty()) return;
        SkyblockProfile profile = getProfile(activeProfile);
        if(profile == null) return;
        currentProfile = profile;
    }

    public SkyblockProfile getProfile(UUID uuid) {
        for(SkyblockProfile profile : profileList) {
            if(profile.profileID.equals(uuid)) return profile;
        }
        return null;
    }
}
