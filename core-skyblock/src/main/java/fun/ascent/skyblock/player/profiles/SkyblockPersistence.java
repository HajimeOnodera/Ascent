package fun.ascent.skyblock.player.profiles;

import fun.ascent.common.redis.RedisManager;
import fun.ascent.database.SkyblockRepository;
import org.bson.Document;

import java.util.*;

public class SkyblockPersistence {

    public static void saveProfile(SkyblockProfile profile) {
        // Set Redis lock to signal we are saving IMMEDIATELY
        RedisManager.get().setSavingLock(profile.profileID.toString());
        
        try {
            Document doc = new Document();
            doc.put("name", profile.profileName);
            doc.put("minion_slots", profile.minionSlots);
            doc.put("lastUpdated", System.currentTimeMillis());
            
            // Save collections
            Document collections = new Document();
            collections.putAll(profile.unlockedCollections);
            doc.put("collections", collections);
            doc.put("recipes", new ArrayList<>(profile.unlockedRecipes));

            // Save members
            Document members = new Document();
            for (ProfilePlayer pp : profile.profilePlayers) {
                pp.update(); // Sync live player data to the data handler
                members.put(pp.playerUUID.toString(), pp.getDataHandler().save());
            }
            doc.put("members", members);

            // Save island state
            if (profile.island != null) {
                profile.island.save();
            }

            SkyblockRepository.saveProfile(profile.profileID, doc);
        } finally {
            // Clear Redis lock when done
            RedisManager.get().clearSavingLock(profile.profileID.toString());
        }
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
        List<String> recipes = doc.getList("recipes", String.class);
        if (recipes != null) {
            profile.unlockedRecipes.addAll(recipes);
        }

        // Load members
        Document membersDoc = doc.get("members", Document.class);
        if (membersDoc != null) {
            profile.profilePlayers = new ArrayList<>();
            for (String uuidStr : membersDoc.keySet()) {
                UUID uuid = UUID.fromString(uuidStr);
                Document playerDoc = membersDoc.get(uuidStr, Document.class);
                
                ProfilePlayer pp = new ProfilePlayer(profileID, uuid);
                pp.getDataHandler().load(playerDoc);
                
                profile.profilePlayers.add(pp);
            }
        }

        profile.postLoad();
        profile.generateIsland();
        return profile;
    }
}
