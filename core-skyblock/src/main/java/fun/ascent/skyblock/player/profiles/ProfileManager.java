package fun.ascent.skyblock.player.profiles;

import fun.ascent.common.redis.RedisManager;
import fun.ascent.database.SkyblockRepository;
import fun.ascent.skyblock.player.SkyblockPlayer;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ProfileManager {

    public static HashMap<UUID,SkyblockProfile> profiles = new HashMap<>();

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
        profile.generateIsland();
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
        List<Document> profileDocs = SkyblockRepository.getProfilesForPlayer(player.getUuid());
        for (Document doc : profileDocs) {
            UUID profileID = UUID.fromString(doc.getString("_id"));
            
            // ALWAYS wait for save lock from previous server
            if (RedisManager.isInitialized()) {
                long start = System.currentTimeMillis();
                while (RedisManager.get().isSaving(profileID.toString())) {
                    if (System.currentTimeMillis() - start > 5000) {
                        System.err.println("[Profile] TIMEOUT waiting for save lock for " + profileID);
                        break;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {}
                }
            }
            
            // ALWAYS reload from DB to ensure latest data
            SkyblockProfile profile = SkyblockPersistence.loadProfile(profileID);
            
            if (profile != null) {
                register(profile);
                player.addProfile(profile);
            }
        }
    }

    public static void saveAllProfiles() {
        for (SkyblockProfile profile : profiles.values()) {
            SkyblockPersistence.saveProfile(profile);
        }
    }

    public static void unloadProfileForPlayer(SkyblockPlayer player) {
        for (SkyblockProfile profile : new ArrayList<>(player.getPlayerProfiles().values())) {
            boolean activeElsewhere = false;
            for (ProfilePlayer pp : profile.profilePlayers) {
                if (pp.skyblockPlayer != null && pp.skyblockPlayer != player && pp.skyblockPlayer.isOnline()) {
                    activeElsewhere = true;
                    break;
                }
            }
            
            if (!activeElsewhere) {
                profiles.remove(profile.profileID);
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
