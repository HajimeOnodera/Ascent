package fun.ascent.skyblock.player.profiles;

import fun.ascent.common.redis.RedisManager;
import fun.ascent.database.SkyblockRepository;
import org.bson.Document;

import java.util.*;

public class SkyblockPersistence {

    public static void saveProfile(SkyblockProfile profile) {
        // Set Redis lock to signal we are saving IMMEDIATELY
        if (RedisManager.isInitialized()) {
            RedisManager.get().setSavingLock(profile.profileID.toString());
        }
        
        try {
            Document doc = new Document();
            doc.put("name", profile.profileName);
            doc.put("minion_slots", profile.minionSlots);
            doc.put("minions_crafted", profile.minionsCrafted);
            doc.put("unique_minions_crafted", new ArrayList<>(profile.uniqueMinionsCrafted));
            doc.put("lastUpdated", System.currentTimeMillis());
            
            // Save collections
            Document collections = new Document();
            collections.putAll(profile.unlockedCollections);
            doc.put("collections", collections);
            doc.put("recipes", new ArrayList<>(profile.unlockedRecipes));

            // Save bank details
            doc.put("bank_coins", profile.bankCoins);
            doc.put("bank_limit", profile.bankLimit);
            doc.put("last_claimed_interest_month", profile.lastClaimedInterestMonth);

            List<Document> bankTxDocs = new ArrayList<>();
            for (SkyblockProfile.BankTransaction tx : profile.bankTransactions) {
                Document txDoc = new Document();
                txDoc.put("timestamp", tx.timestamp());
                txDoc.put("amount", tx.amount());
                txDoc.put("originator", tx.originator());
                txDoc.put("type", tx.type());
                bankTxDocs.add(txDoc);
            }
            doc.put("bank_transactions", bankTxDocs);

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
            if (RedisManager.isInitialized()) {
                RedisManager.get().clearSavingLock(profile.profileID.toString());
            }
        }
    }

    public static SkyblockProfile loadProfile(UUID profileID) {
        Document doc = SkyblockRepository.getProfile(profileID);
        if (doc == null) return null;

        SkyblockProfile profile = new SkyblockProfile(new ArrayList<>());
        profile.profileID = profileID;
        profile.profileName = doc.getString("name");
        profile.minionSlots = doc.getInteger("minion_slots", 5);
        profile.minionsCrafted = doc.getInteger("minions_crafted", 1);
        List<String> uniqueMinions = doc.getList("unique_minions_crafted", String.class);
        if (uniqueMinions != null) {
            profile.uniqueMinionsCrafted.addAll(uniqueMinions);
        }

        // Load bank details
        profile.bankCoins = doc.containsKey("bank_coins") ? doc.getDouble("bank_coins") : 0.0;
        profile.bankLimit = doc.containsKey("bank_limit") ? doc.getDouble("bank_limit") : 50000000.0;
        profile.lastClaimedInterestMonth = doc.containsKey("last_claimed_interest_month") ? doc.getLong("last_claimed_interest_month") : 0L;

        List<Document> bankTxDocs = doc.getList("bank_transactions", Document.class);
        if (bankTxDocs != null) {
            profile.bankTransactions = new ArrayList<>();
            for (Document txDoc : bankTxDocs) {
                profile.bankTransactions.add(new SkyblockProfile.BankTransaction(
                        txDoc.containsKey("timestamp") ? txDoc.getLong("timestamp") : 0L,
                        txDoc.containsKey("amount") ? txDoc.getDouble("amount") : 0.0,
                        txDoc.containsKey("originator") ? txDoc.getString("originator") : "",
                        txDoc.containsKey("type") ? txDoc.getString("type") : "UNKNOWN"
                ));
            }
        }

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
