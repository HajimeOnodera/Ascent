package fun.ascent.skyblock.player.profiles;

import fun.ascent.database.SkyblockRepository;
import org.bson.Document;

import java.util.*;

public class SkyblockPersistence {

    public static void saveProfile(SkyblockProfile profile) {
        Document doc = new Document();
        doc.put("name", profile.profileName);
        doc.put("minion_slots", profile.minionSlots);
        
        // Save collections
        Document collections = new Document();
        collections.putAll(profile.unlockedCollections);
        doc.put("collections", collections);

        // Save members
        Document members = new Document();
        for (ProfilePlayer pp : profile.profilePlayers) {
            pp.update(); // Sync live player data to the data handler
            members.put(pp.playerUUID.toString(), pp.getDataHandler().save());
        }
        doc.put("members", members);

        SkyblockRepository.saveProfile(profile.profileID, doc);
    }

    public static SkyblockProfile loadProfile(UUID profileID) {
        Document doc = SkyblockRepository.getProfile(profileID);
        if (doc == null) return null;

        SkyblockProfile profile = new SkyblockProfile(new ArrayList<>());
        profile.profileID = profileID;
        profile.profileName = doc.getString("name");
        profile.minionSlots = doc.getInteger("minion_slots", 5);

        // Load collections
        Document collections = doc.get("collections", Document.class);
        if (collections != null) {
            for (String key : collections.keySet()) {
                profile.unlockedCollections.put(key, collections.getInteger(key));
            }
        }

        // Load members
        Document membersDoc = doc.get("members", Document.class);
        if (membersDoc != null) {
            profile.profilePlayers = new ArrayList<>();
            for (String uuidStr : membersDoc.keySet()) {
                UUID uuid = UUID.fromString(uuidStr);
                Document playerDoc = membersDoc.get(uuidStr, Document.class);
                
                ProfilePlayer pp = new ProfilePlayer(profileID, null);
                pp.playerUUID = uuid;
                pp.getDataHandler().load(playerDoc);
                
                profile.profilePlayers.add(pp);
            }
        }

        return profile;
    }
}
