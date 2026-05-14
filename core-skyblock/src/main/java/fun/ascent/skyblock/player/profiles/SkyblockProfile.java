package fun.ascent.skyblock.player.profiles;

import fun.ascent.common.npc.AscentNpc;
import org.bson.Document;

import fun.ascent.skyblock.island.Island;
import fun.ascent.skyblock.island.IslandManager;
import fun.ascent.skyblock.minion.base.SkyblockMinion;
import fun.ascent.skyblock.minion.model.MinionType;
import fun.ascent.skyblock.minion.service.MinionFactory;
import fun.ascent.skyblock.minion.service.MinionManager;
import fun.ascent.skyblock.minion.service.MinionPersistence;
import fun.ascent.skyblock.npc.SkyblockNPCManager;
import fun.ascent.skyblock.npc.island.JerryNPC;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.collections.CollectionCategory;
import fun.ascent.skyblock.player.collections.CollectionRegistry;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SkyblockProfile {

    public String profileName;
    public UUID profileID;
    public List<ProfilePlayer> profilePlayers;
    public Map<String, Integer> unlockedCollections = new HashMap<>();
    public Set<String> unlockedRecipes = new HashSet<>();
    public Island island;
    @Getter
    public Pos spawnPos;
    @Getter
    @Setter
    public int minionSlots = 5;

    public void postLoad(){
        if(island == null){
            generateIsland();
            //TODO: Notify Players of island corruption
        }
        if(spawnPos == null){spawnPos = new Pos(7,100, 5);}
        if(minionSlots <= 0){minionSlots = 5;}
        if (unlockedCollections == null) {
            unlockedCollections = new HashMap<>();
            //TODO: Notify Players of Collections corruption
        }
        if (unlockedRecipes == null) {
            unlockedRecipes = new HashSet<>();
        }
    }

    public SkyblockProfile(List<SkyblockPlayer> players) {
        this.profileID = UUID.randomUUID();
        this.profileName = generateRandomProfileName();
        this.profilePlayers = new ArrayList<>();
        spawnPos = new Pos(7,100, 5);
        players.forEach(pl -> profilePlayers.add(new ProfilePlayer(this.profileID, pl)));
        this.unlockedCollections = new HashMap<>();
    }

    public void updateCollection(String itemId, int amount) {
        CollectionCategory.ItemCollection collectionDef = CollectionRegistry.get(itemId);
        if (collectionDef == null) return;

        int currentProgress = this.unlockedCollections.getOrDefault(itemId, 0);
        int newProgress = currentProgress + amount;
        this.unlockedCollections.put(itemId, newProgress);

        // Check for tier ups
        int oldTier = collectionDef.getTierFromProgress(currentProgress);
        int newTier = collectionDef.getTierFromProgress(newProgress);

        if (newTier > oldTier) {
            for (int currentTier = oldTier + 1; currentTier <= newTier; currentTier++) {
                CollectionCategory.CollectionReward reward = collectionDef.getRewardAtTier(currentTier);
                if (reward != null) {
                    String message = "§6§lCOLLECTION LEVEL UP §e" + collectionDef.name() + " " + currentTier;
                    
                    profilePlayers.forEach(pp -> {
                        if (pp.skyblockPlayer != null) {
                            pp.skyblockPlayer.sendMessage(message);
                            reward.unlocks().forEach(u -> pp.skyblockPlayer.sendMessage("§7  Unlocked: " + u.getDisplay()));
                        }
                    });
                    
                    // Apply unlocks (one time per profile)
                    reward.unlocks().forEach(u -> u.apply(this));
                }
            }
        }
    }

    public void generateIsland() {
        island = IslandManager.getIsland(profileID);
        
        String serverType = System.getenv().getOrDefault("ASCENT_SERVER_TYPE", "HUB");
        if (serverType.equalsIgnoreCase("ISLAND")) {
            System.out.println("[IslandInit] Starting load for " + profileID);
            island.load().thenAccept(instance -> {
                System.out.println("[IslandInit] Load finished for " + profileID + ", instance: " + (instance != null));
                if (instance == null) return;
                
                // Wait for instance to be ready and spawn defaults
                MinecraftServer.getSchedulerManager().submitTask(() -> {
                    System.out.println("[IslandInit] Executing spawn task for " + profileID);
                    restoreIslandData(instance);
                    spawnIslandDefaults(instance);
                    return TaskSchedule.stop();
                }, ExecutionType.TICK_END);
            });
        }
    }

    private void restoreIslandData(Instance instance) {
        for (Document minionDoc : island.getMinionData()) {
            SkyblockMinion minion = MinionPersistence.deserialize(minionDoc, instance);
            minion.spawn();
            MinionManager.registerMinion(minion);
        }
    }

    public void saveIsland() {
        if (island == null || !island.isLoaded()) return;
        
        // Prepare minion data
        List<Document> minionDocs = new ArrayList<>();
        Collection<SkyblockMinion> minions = MinionManager.getOwnedMinions(profileID);
        for (SkyblockMinion minion : minions) {
            if (minion.getInstance() == island.getInstance()) {
                minionDocs.add(MinionPersistence.serialize(minion));
            }
        }
        island.setMinionData(minionDocs);
        island.save();
    }

    private void spawnIslandDefaults(Instance instance) {
        String serverType = System.getenv().getOrDefault("ASCENT_SERVER_TYPE", "HUB");
        System.out.println("[IslandInit] Spawning defaults for profile " + profileID + " on server " + serverType);

        Pos jerryPos = new Pos(9.5, 100, 34, 180, 0);

        String jerryId = "jerry_" + profileID;
        if (SkyblockNPCManager.getNPCbyID(jerryId) == null) {
            System.out.println("[IslandInit] Spawning Jerry " + jerryId);
            AscentNpc jerry = new AscentNpc(new JerryNPC(instance, jerryPos, jerryId));
            SkyblockNPCManager.registerNPC(jerry);
            jerry.spawn();
        } else {
            System.out.println("[IslandInit] Jerry already exists: " + jerryId);
        }

        Pos minionPos = new Pos(3.5, 100, 36.5, -90, 0);

        Collection<SkyblockMinion> owned = MinionManager.getOwnedMinions(profileID);
        System.out.println("[IslandInit] Profile " + profileID + " has " + owned.size() + " minions in memory");
        
        if (owned.stream().noneMatch(m -> m.getInstance() == instance)) {
            System.out.println("[IslandInit] Spawning default Cobblestone minion");
            SkyblockMinion minion = MinionFactory.create(profileID, MinionType.COBBLESTONE, 1, instance, minionPos);
            minion.spawn();
            MinionManager.registerMinion(minion);
        }
    }

    public String generateRandomProfileName(){
        List<String> names = List.of(
                "Apple", "Banana", "Blueberry", "Coconut", "Cucumber",
                "Grapes", "Kiwi", "Lemon", "Lime", "Mango", "Orange", "Papaya", "Peach",
                "Pear", "Pineapple", "Pomegranate", "Raspberry", "Strawberry", "Tomato",
                "Watermelon", "Zucchini");
        int num = ThreadLocalRandom.current().nextInt(names.size());
        return names.get(num);
    }


    public ProfilePlayer getPlayer(SkyblockPlayer pl) {
        for (ProfilePlayer player : this.profilePlayers) {
            if(player.playerUUID.equals(pl.getUuid())) return player;
        }
        return null;
    }


}
