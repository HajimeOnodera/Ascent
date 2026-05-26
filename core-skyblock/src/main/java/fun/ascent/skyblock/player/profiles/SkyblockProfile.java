package fun.ascent.skyblock.player.profiles;

import fun.ascent.skyblock.island.Island;
import fun.ascent.skyblock.island.IslandManager;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.collections.CollectionCategory;
import fun.ascent.skyblock.player.collections.CollectionRegistry;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.sound.SoundEvent;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static net.kyori.adventure.sound.Sound.*;

public class SkyblockProfile {

    public String profileName;
    public UUID profileID;
    public List<ProfilePlayer> profilePlayers;
    public Map<String, Integer> unlockedCollections = new HashMap<>();
    public Set<String> unlockedRecipes = new HashSet<>();
    public Island island;
    public double bankCoins = 0.0;
    public double bankLimit = 50000000.0;
    public long lastClaimedInterestMonth = 0;
    public List<BankTransaction> bankTransactions = new ArrayList<>();
    @Getter
    public Pos spawnPos;
    @Getter
    @Setter
    public int minionSlots = 5;
    private boolean islandLoaded = false;

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
        if (bankTransactions == null) {
            bankTransactions = new ArrayList<>();
        }
        if (bankLimit <= 0) {
            bankLimit = 50000000.0;
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

    private String getRomanNumeral(int number) {
        if (number <= 0) return "";
        return switch(number) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            case 8 -> "VIII";
            case 9 -> "IX";
            case 10 -> "X";
            default -> String.valueOf(number);
        };
    }

    public void updateCollection(String itemId, int amount) {
        CollectionCategory.ItemCollection collectionDef = CollectionRegistry.get(itemId);
        if (collectionDef == null) return;

        int currentProgress = this.unlockedCollections.getOrDefault(itemId, 0);
        int newProgress = currentProgress + amount;
        this.unlockedCollections.put(itemId, newProgress);

        if (currentProgress == 0 && newProgress > 0) {
            profilePlayers.forEach(pp -> {
                if (pp.skyblockPlayer != null) {
                    pp.skyblockPlayer.sendMessage("  §6§lCOLLECTION UNLOCKED §e" + collectionDef.name());
                    pp.skyblockPlayer.playSound(sound(SoundEvent.ENTITY_EXPERIENCE_ORB_PICKUP, Source.MASTER, 1f, 0.5f));
                }
            });
        }

        int oldTier = collectionDef.getTierFromProgress(currentProgress);
        int newTier = collectionDef.getTierFromProgress(newProgress);

        if (newTier > oldTier) {
            for (int currentTier = oldTier + 1; currentTier <= newTier; currentTier++) {
                CollectionCategory.CollectionReward reward = collectionDef.getRewardAtTier(currentTier);
                if (reward != null) {
                    String separator = "§e§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬";
                    String title = "  §6§lCOLLECTION LEVEL UP §e" + collectionDef.name() + " " + (currentTier == 1 ? getRomanNumeral(currentTier) : "§8" + getRomanNumeral(currentTier - 1) + "➜§e" + getRomanNumeral(currentTier));
                    
                    profilePlayers.forEach(pp -> {
                        if (pp.skyblockPlayer != null) {
                            pp.skyblockPlayer.sendMessage(separator);
                            pp.skyblockPlayer.sendMessage(title);
                            pp.skyblockPlayer.sendMessage(" ");
                            pp.skyblockPlayer.sendMessage("  §a§lREWARDS");
                            if (reward.unlocks() != null && !reward.unlocks().isEmpty()) {
                                reward.unlocks().forEach(u -> pp.skyblockPlayer.sendMessage("    §7" + u.getDisplay()));
                            }
                            pp.skyblockPlayer.sendMessage(separator);
                            pp.skyblockPlayer.playSound(sound(SoundEvent.ENTITY_PLAYER_LEVELUP, Source.MASTER, 1f, 1f));
                        }
                    });

                    reward.unlocks().forEach(u -> u.apply(this));
                }
            }
        }

    }

    public void generateIsland() {
        if (islandLoaded) return;
        islandLoaded = true;
        
        island = IslandManager.getIsland(profileID);
        
        String serverType = System.getenv().getOrDefault("ASCENT_SERVER_TYPE", "HUB");
        if (serverType.equalsIgnoreCase("ISLAND")) {
            System.out.println("[IslandInit] Starting load for " + profileID);
            island.load();
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

    public record BankTransaction(long timestamp, double amount, String originator, String type) {}

    public void claimInterest(SkyblockPlayer player) {
        long currentMonth = (System.currentTimeMillis() - 1560275700000L) / (1200L * 31 * 1000);
        if (this.lastClaimedInterestMonth == 0) {
            this.lastClaimedInterestMonth = currentMonth;
            return;
        }

        long monthsOwed = currentMonth - this.lastClaimedInterestMonth;
        if (monthsOwed <= 0) return;

        int times = (int) Math.min(monthsOwed, 2);
        double totalInterest = 0.0;
        double currentTempBalance = this.bankCoins;

        for (int i = 0; i < times; i++) {
            double interest = currentTempBalance * 0.02;
            if (currentTempBalance + interest > this.bankLimit) {
                interest = this.bankLimit - currentTempBalance;
            }
            if (interest <= 0) break;
            totalInterest += interest;
            currentTempBalance += interest;
        }

        this.lastClaimedInterestMonth = currentMonth;

        if (totalInterest > 0) {
            this.bankCoins += totalInterest;
            this.bankTransactions.add(new BankTransaction(
                System.currentTimeMillis(),
                totalInterest,
                "§cBank Interest",
                "INTEREST"
            ));

            if (this.bankTransactions.size() > 10) {
                this.bankTransactions = new ArrayList<>(this.bankTransactions.subList(this.bankTransactions.size() - 10, this.bankTransactions.size()));
            }

            player.sendMessage(fun.ascent.common.StringUtility.text("<blue>------------------------------------------------"));
            player.sendMessage(fun.ascent.common.StringUtility.text("<green>You have just received <gold>" + fun.ascent.common.StringUtility.commaify(totalInterest) + " coins<green> as bank interest!"));
            player.sendMessage(fun.ascent.common.StringUtility.text("<blue>------------------------------------------------"));
        }
    }

}
