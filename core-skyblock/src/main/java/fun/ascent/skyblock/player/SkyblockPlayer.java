package fun.ascent.skyblock.player;

import com.google.gson.GsonBuilder;
import fun.ascent.skyblock.player.profiles.ProfileManager;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import fun.ascent.skyblock.player.profiles.SkyblockProfile;
import fun.ascent.skyblock.player.skill.PlayerSkillData;
import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SkyblockPlayer extends Player {

    @Getter private final Map<UUID, SkyblockProfile> playerProfiles = new HashMap<>();
    @Getter private SkyblockProfile activeProfile;
    @Getter private ProfilePlayer activeProfileData;

    public static final Tag<UUID> sbProfileID = Tag.UUID("profile_id");


    public SkyblockPlayer(PlayerConnection playerConnection, GameProfile gameProfile) {
        super(playerConnection, gameProfile);
        loadProfiles();

        if (playerProfiles.isEmpty()) {
            SkyblockProfile profile = new SkyblockProfile(List.of(this));
            addProfile(profile);
            setActiveProfile(profile.profileID);
        }
    }

    public SkyblockProfile loadProfileFromPlayer(){
       return ProfileManager.getProfile(this.getTag(sbProfileID));
    }

    public void loadProfiles() {
        // TODO: Load from database
    }

    public void addProfile(SkyblockProfile profile) {
        System.out.println(profile.profileName + " : " + profile.profileID);
        playerProfiles.put(profile.profileID, profile);
    }

    public void setActiveProfile(UUID profileID) {
        System.out.println(playerProfiles.size());
        SkyblockProfile target = playerProfiles.get(profileID);
        if (target == null) return;

        this.activeProfile = target;
        System.out.println("UUID to Match: " + this.getUuid());

        for (ProfilePlayer pp : target.profilePlayers) {
            if (pp.playerUUID.equals(getUuid())) {
                this.activeProfileData = pp;
                System.out.println("Profile Set Correctly");
                updatePlayer();
                break;
            }
            System.out.println("Not Right UUID: " + pp.playerUUID);
        }
        setTag(sbProfileID,this.activeProfile.profileID);

    }

    public void updatePlayer(){
        this.activeProfileData.updateStats();
    }

    @Nullable
    public PlayerSkillData getSkillData() {
        return activeProfileData != null ? activeProfileData.skillData : null;
    }

    public ProfilePlayer getPlayerProfile(SkyblockProfile profile) {
        for (ProfilePlayer pp : profile.profilePlayers) {
            if (pp.playerUUID.equals(getUuid())) {
                return pp;
            }
        }
        return null;
    }
}