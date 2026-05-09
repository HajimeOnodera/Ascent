package fun.ascent.skyblock.player.profiles;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.collections.CollData;
import fun.ascent.skyblock.player.collections.CollectionMap;
import fun.ascent.skyblock.world.World;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SkyblockProfile {

    public String profileName;
    public UUID profileID;
    public List<ProfilePlayer> profilePlayers;
    public CollectionMap collectionMap;
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
        if(collectionMap == null){
            collectionMap = new CollectionMap();
            //TODO: Notify Players of Collection Corruption
        }
    }

    public SkyblockProfile(List<SkyblockPlayer> players) {
        this.profileID = UUID.randomUUID();
        this.profileName = generateRandomProfileName();
        this.profilePlayers = new ArrayList<>();
        spawnPos = new Pos(7,100, 5);
        players.forEach(pl -> profilePlayers.add(new ProfilePlayer(this.profileID, pl)));
        this.collectionMap = new CollectionMap();
        generateIsland();
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


    public void updateCollection(String itemId, int amount) {
        this.collectionMap.collectionMap.forEach((category,data) -> {
            for(CollData collData : data) {
                if(collData.collection.ITEM_ID.equals(itemId)) {
                    List<CollData> dataa = new ArrayList<>();
                    dataa.remove(collData);
                    CollData dataNew = new CollData(collData.collection,collData.progress + amount);
                    collData.collection.checkForRewards(this,collData.progress,dataNew.progress);
                    dataa.add(dataNew);
                    collectionMap.collectionMap.put(category,dataa);
                }
            }
        });
    }

}
