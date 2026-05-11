package fun.ascent.skyblock.player.profiles;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.collections.Collection;
import fun.ascent.skyblock.player.collections.CollectionRegistry;
import fun.ascent.skyblock.world.World;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SkyblockProfile {

    public String profileName;
    public UUID profileID;
    public List<ProfilePlayer> profilePlayers;
    public Map<String, Integer> unlockedCollections = new HashMap<>();
    public World island;
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
    }

    public SkyblockProfile(List<SkyblockPlayer> players) {
        this.profileID = UUID.randomUUID();
        this.profileName = generateRandomProfileName();
        this.profilePlayers = new ArrayList<>();
        spawnPos = new Pos(7,100, 5);
        players.forEach(pl -> profilePlayers.add(new ProfilePlayer(this.profileID, pl)));
        this.unlockedCollections = new HashMap<>();
        generateIsland();
    }

    public void updateCollection(String itemId, int amount) {
        Collection collectionDef = CollectionRegistry.get(itemId);
        if (collectionDef == null) return;
        int currentProgress = this.unlockedCollections.getOrDefault(itemId, 0);
        int newProgress = currentProgress + amount;
        this.unlockedCollections.put(itemId, newProgress);

        collectionDef.checkForRewards(this, currentProgress, newProgress);
    }

    public void generateIsland() {
        island = new World(profileID.toString(),
                new File("maps/players/" + profileID + ".polar"),
                new File("maps/privisland.polar"),
                true);

        island.getInstance();
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
