package fun.ascent.skyblock.player;


import fun.ascent.skyblock.utility.RandomUtils;

import java.util.List;
import java.util.UUID;

// This is where the main implementation will be
public class SkyblockProfile {

    public UUID profileID;
    public String profileName;
    public String playerName;

    public transient SkyblockPlayer activePlayer;

    public SkyblockProfile(SkyblockPlayer activePlayer) {
        this.profileID = UUID.randomUUID();
        this.profileName = generateRandomProfile();
        this.activePlayer = activePlayer;
        this.playerName = activePlayer.getUsername();
    }

    public String generateRandomProfile(){
        List<String> names = List.of(
                "Apple", "Banana", "Blueberry", "Coconut", "Cucumber",
                "Grapes", "Kiwi", "Lemon", "Lime", "Mango", "Orange", "Papaya", "Peach",
                "Pear", "Pineapple", "Pomegranate", "Raspberry", "Strawberry", "Tomato",
                "Watermelon", "Zucchini");
        int num = RandomUtils.getRandomInt(names.size() - 1);
        return names.get(num);
    }
}
