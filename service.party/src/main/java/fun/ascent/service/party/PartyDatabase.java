package fun.ascent.service.party;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import fun.ascent.common.party.FullParty;
import fun.ascent.common.party.PendingParty;
import fun.ascent.common.service.MongoDB;
import fun.ascent.database.MongoProvider;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record PartyDatabase(String playerId) implements MongoDB {
    public static MongoCollection<Document> partyDataCollection;
    public static MongoCollection<Document> pendingInvitesCollection;

    @Override
    public void connect(String connectionString) {
        if (MongoProvider.isInitialized()) {
            MongoProvider.connect(connectionString);
        }

        MongoDatabase database = MongoProvider.getDatabase();
        partyDataCollection = database.getCollection("party-data");
        pendingInvitesCollection = database.getCollection("party-pending-invites");
    }

    public FullParty getParty(UUID partyUuid) {
        Document doc = partyDataCollection.find(Filters.eq("_id", partyUuid.toString())).first();
        if (doc == null) {
            return null;
        }
        String data = doc.getString("data");
        return FullParty.getStaticSerializer().deserialize(data);
    }

    public FullParty getPartyByMember(UUID memberUuid) {
        Document doc = partyDataCollection.find(Filters.eq("members.uuid", memberUuid.toString())).first();
        if (doc == null) {
            return null;
        }
        String data = doc.getString("data");
        return FullParty.getStaticSerializer().deserialize(data);
    }

    public void saveParty(FullParty party) {
        String serialized = party.getSerializer().serialize(party);
        String id = party.getUuid().toString();

        Document query = new Document("_id", id);
        Document existing = partyDataCollection.find(query).first();

        if (existing != null) {
            partyDataCollection.updateOne(query, Updates.set("data", serialized));
        } else {
            Document newDoc = new Document("_id", id);
            newDoc.append("data", serialized);
            partyDataCollection.insertOne(newDoc);
        }
    }

    public void deleteParty(UUID partyUuid) {
        partyDataCollection.deleteOne(Filters.eq("_id", partyUuid.toString()));
    }

    public List<PendingParty> getPendingInvitesFor(UUID playerUuid) {
        List<PendingParty> invites = new ArrayList<>();
        FindIterable<Document> results = pendingInvitesCollection.find(Filters.eq("invitee", playerUuid.toString()));
        for (Document doc : results) {
            String data = doc.getString("data");
            invites.add(PendingParty.getStaticSerializer().deserialize(data));
        }
        return invites;
    }

    public List<PendingParty> getPendingInvitesFrom(UUID leaderUuid) {
        List<PendingParty> invites = new ArrayList<>();
        FindIterable<Document> results = pendingInvitesCollection.find(Filters.eq("leader", leaderUuid.toString()));
        for (Document doc : results) {
            String data = doc.getString("data");
            invites.add(PendingParty.getStaticSerializer().deserialize(data));
        }
        return invites;
    }

    public List<PendingParty> getAllPendingInvites() {
        List<PendingParty> invites = new ArrayList<>();
        FindIterable<Document> results = pendingInvitesCollection.find();
        for (Document doc : results) {
            String data = doc.getString("data");
            invites.add(PendingParty.getStaticSerializer().deserialize(data));
        }
        return invites;
    }

    public void addPendingInvite(PendingParty invite) {
        String id = invite.resultPartyUUID().toString();
        String serialized = invite.getSerializer().serialize(invite);

        Document doc = new Document("_id", id);
        doc.append("invitee", invite.invitee().toString());
        doc.append("leader", invite.leader().toString());
        doc.append("data", serialized);
        doc.append("timestamp", System.currentTimeMillis());
        pendingInvitesCollection.insertOne(doc);
    }

    public void removePendingInvite(UUID inviteUUID) {
        pendingInvitesCollection.deleteOne(Filters.eq("_id", inviteUUID.toString()));
    }

    public void removePendingInvitesFor(UUID playerUuid) {
        pendingInvitesCollection.deleteMany(Filters.eq("invitee", playerUuid.toString()));
    }

    public PendingParty getPendingInvite(UUID inviteUUID) {
        Document doc = pendingInvitesCollection.find(Filters.eq("_id", inviteUUID.toString())).first();
        if (doc == null) {
            return null;
        }
        return PendingParty.getStaticSerializer().deserialize(doc.getString("data"));
    }

    public boolean hasPendingInvite(UUID invitee, UUID leader) {
        Document doc = pendingInvitesCollection.find(
            Filters.and(
                Filters.eq("invitee", invitee.toString()),
                Filters.eq("leader", leader.toString())
            )
        ).first();
        return doc != null;
    }

    @Override
    public void set(String key, Object value) {
        insertOrUpdate(key, value);
    }

    @Override
    public Object get(String key, Object def) {
        Document doc = partyDataCollection.find(Filters.eq("_id", playerId)).first();
        if (doc == null) {
            return def;
        }
        return doc.get(key);
    }

    @Override
    public void insertOrUpdate(String key, Object value) {
        if (exists()) {
            Document query = new Document("_id", playerId);
            Document found = partyDataCollection.find(query).first();
            if (found != null) {
                partyDataCollection.updateOne(found, Updates.set(key, value));
            }
            return;
        }
        Document newDoc = new Document("_id", playerId);
        newDoc.append(key, value);
        partyDataCollection.insertOne(newDoc);
    }

    @Override
    public boolean remove(String id) {
        Document query = new Document("_id", id);
        Document found = partyDataCollection.find(query).first();
        if (found == null) {
            return false;
        }
        partyDataCollection.deleteOne(query);
        return true;
    }

    public boolean exists() {
        Document query = new Document("_id", playerId);
        Document found = partyDataCollection.find(query).first();
        return found != null;
    }
}
