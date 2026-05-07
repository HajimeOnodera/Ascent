package fun.ascent.skyblock.player.profiles;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.world.World;
import fun.ascent.skyblock.world.WorldHandler;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.InstanceContainer;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SkyblockProfile {

    public String profileName;
    public UUID profileID;
    public List<ProfilePlayer> profilePlayers;
    public World island;

    public SkyblockProfile(List<SkyblockPlayer> players) {
        this.profileID = UUID.randomUUID();
        this.profileName = generateRandomProfileName();
        this.profilePlayers = new ArrayList<>();
        players.forEach(pl -> profilePlayers.add(new ProfilePlayer(this.profileID,pl)));
        generateIsland(players);
    }

    public void generateIsland(List<SkyblockPlayer> players) {
        island = new World(profileID.toString(),
                new File("maps/players/"+profileID+".polar"),new File("maps/prv-island"),true);
        InstanceContainer container = island.getInstance();
        Pos spawnPos = new Pos(0,41,0);
        players.forEach(p -> {
            p.setInstance(container);
            p.teleport(spawnPos);
            p.updatePlayer();
        });
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

}
