package fun.ascent.skyblock.player;

import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import fun.ascent.skyblock.player.profiles.SkyblockProfile;
import fun.ascent.skyblock.player.skill.PlayerSkillData;
import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SkyblockPlayer extends Player {

    @Getter private final Map<UUID, SkyblockProfile> playerProfiles = new HashMap<>();
    @Getter private SkyblockProfile activeProfile;
    @Getter private ProfilePlayer activeProfileData;

    public SkyblockPlayer(PlayerConnection playerConnection, GameProfile gameProfile) {
        super(playerConnection, gameProfile);
        loadProfiles();

        if (playerProfiles.isEmpty()) {
            SkyblockProfile profile = new SkyblockProfile(List.of(this));
            addProfile(profile);
            setActiveProfile(profile.profileID);
        }
    }

    public void loadProfiles() {
        // TODO: Load from database
    }

    public void addProfile(SkyblockProfile profile) {
        playerProfiles.put(profile.profileID, profile);
    }

    public void setActiveProfile(UUID profileID) {
        SkyblockProfile target = playerProfiles.get(profileID);
        if (target == null) return;

        this.activeProfile = target;

        for (ProfilePlayer pp : target.profilePlayers) {
            if (pp.playerUUID.equals(getUuid())) {
                this.activeProfileData = pp;
                updatePlayer();
                break;
            }
        }
    }

    public void updatePlayer(){
        this.activeProfileData.updateStats();
    }

    @Nullable
    public PlayerSkillData getSkillData() {
        return activeProfileData != null ? activeProfileData.skillData : null;
    }
}