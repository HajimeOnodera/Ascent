package fun.ascent.skyblock.player.profiles;

import fun.ascent.database.SkyblockRepository;
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

    public static SkyblockProfile createProfile(SkyblockPlayer player) {
        SkyblockProfile profile = new SkyblockProfile(List.of(player));
        register(profile);
        player.setActiveProfile(profile.profileID);
        System.out.println("1: " + profile.profileName + " : " + profile.profileID);
        return profile;
    }

    public static void saveProfile(UUID profileID) {
        SkyblockProfile profile = profiles.get(profileID);
        if (profile != null) {
            SkyblockPersistence.saveProfile(profile);
        }
    }

    public static void loadProfilesForPlayer(SkyblockPlayer player) {
        List<org.bson.Document> profileDocs = SkyblockRepository.getProfilesForPlayer(player.getUuid());
        for (org.bson.Document doc : profileDocs) {
            UUID profileID = UUID.fromString(doc.getString("_id"));
            SkyblockProfile profile = SkyblockPersistence.loadProfile(profileID);
            if (profile != null) {
                register(profile);
                player.addProfile(profile);
            }
        }
    }

    public static void register(SkyblockProfile profile){
        profiles.put(profile.profileID, profile);
        profile.profilePlayers.forEach(p -> {
            if (p.skyblockPlayer != null) {
                p.skyblockPlayer.addProfile(profile);
            }
        });
        SkyblockPersistence.saveProfile(profile);
    }
}
