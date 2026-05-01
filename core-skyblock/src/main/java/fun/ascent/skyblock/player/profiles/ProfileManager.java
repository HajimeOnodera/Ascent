package fun.ascent.skyblock.player.profiles;

import fun.ascent.skyblock.player.SkyblockPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ProfileManager {

    public static HashMap<UUID,SkyblockProfile> profiles = new HashMap<>();

    public static void initialise(){
        profiles.clear();
        //TODO: Load Profiles from DB:
    }

    public static SkyblockProfile getProfile(UUID tag) {
        if(tag == null) return null;
        return profiles.get(tag);
    }

    public static ProfilePlayer getPlayerProfile(SkyblockPlayer player) {
        if(player.getActiveProfileData() != null) return player.getActiveProfileData();
        SkyblockProfile profile = getProfile(player.getTag(SkyblockPlayer.sbProfileID));
        if(profile == null) return null;
        return player.getPlayerProfile(profile);
    }

    public static void createProfile(SkyblockPlayer player) {
        SkyblockProfile profile = new SkyblockProfile(List.of(player));
        register(profile);
        player.setActiveProfile(profile.profileID);
        System.out.println("1: " + profile.profileName + " : " + profile.profileID);
    }

    public static void register(SkyblockProfile profile){
        profiles.put(profile.profileID, profile);
        profile.profilePlayers.forEach(p -> p.skyblockPlayer.addProfile(profile));
    }
}
