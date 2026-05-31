package fun.ascent.skyblock.player.profiles;

import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.island.Island;
import fun.ascent.skyblock.island.IslandManager;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.collections.CollectionCategory;
import fun.ascent.skyblock.player.collections.CollectionRegistry;
import fun.ascent.skyblock.player.collections.gui.CollectionOverviewMenu;
import fun.ascent.skyblock.player.level.causes.LevelCause;
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
    @Getter
    @Setter
    public int minionsCrafted = 1;
    public Set<String> uniqueMinionsCrafted = new HashSet<>();
    private boolean islandLoaded = false;

    public void postLoad(){
        if(island == null){
            generateIsland();
        }
        if(spawnPos == null){spawnPos = new Pos(7,100, 5);}
        if(uniqueMinionsCrafted == null){
            uniqueMinionsCrafted = new HashSet<>();
        }
        if(uniqueMinionsCrafted.isEmpty()){
            uniqueMinionsCrafted.add("COBBLESTONE_GENERATOR_1");
        }
        minionsCrafted = uniqueMinionsCrafted.size();
        if(minionSlots <= 0){minionSlots = getMinionSlotsFromUnique(minionsCrafted);}
        if(minionsCrafted <= 0){minionsCrafted = 1;}
        if (unlockedCollections == null) {
            unlockedCollections = new HashMap<>();
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

            player.sendMessage(StringUtility.text("<blue>------------------------------------------------"));
            player.sendMessage(StringUtility.text("<green>You have just received <gold>" + StringUtility.commaify(totalInterest) + " coins<green> as bank interest!"));
            player.sendMessage(StringUtility.text("<blue>------------------------------------------------"));
        }
    }

    public int getMinionSlotsFromUnique(int uniqueCrafted) {
        if (uniqueCrafted < 5) return 5;
        if (uniqueCrafted < 15) return 6;
        if (uniqueCrafted < 30) return 7;
        if (uniqueCrafted < 50) return 8;
        if (uniqueCrafted < 75) return 9;
        if (uniqueCrafted < 100) return 10;
        if (uniqueCrafted < 125) return 11;
        if (uniqueCrafted < 150) return 12;
        if (uniqueCrafted < 175) return 13;
        if (uniqueCrafted < 200) return 14;
        if (uniqueCrafted < 225) return 15;
        if (uniqueCrafted < 250) return 16;
        if (uniqueCrafted < 275) return 17;
        if (uniqueCrafted < 300) return 18;
        if (uniqueCrafted < 350) return 19;
        if (uniqueCrafted < 400) return 20;
        if (uniqueCrafted < 450) return 21;
        if (uniqueCrafted < 500) return 22;
        if (uniqueCrafted < 550) return 23;
        if (uniqueCrafted < 600) return 24;
        if (uniqueCrafted < 650) return 25;
        return 26;
    }

    public void checkMinionSlotsUnlock(SkyblockPlayer player) {
        int previousSlots = this.minionSlots;
        int newSlots = getMinionSlotsFromUnique(this.minionsCrafted);
        if (newSlots > previousSlots) {
            this.minionSlots = newSlots;
            player.sendMessage("§a§lUNLOCKED A NEW MINION SLOT! §7(§e" + previousSlots + " §7➜ §e" + newSlots + "§7)");
            player.playSound(sound(SoundEvent.ENTITY_PLAYER_LEVELUP, Source.MASTER, 1f, 1.2f));
        } else {
            int nextSlot = newSlots + 1;
            if (nextSlot <= 26) {
                int requiredForNext = CollectionOverviewMenu.getRequiredUniqueMinions(nextSlot);
                int remaining = Math.max(0, requiredForNext - this.minionsCrafted);
                player.sendMessage("  §aCraft " + remaining + " more unique Minions to unlock your " + nextSlot + CollectionOverviewMenu.getOrdinalSuffix(nextSlot) + " Minion slot!");
            }
        }
    }

    public void registerMinionCraft(SkyblockPlayer player, String minionId) {
        String canonicalId = minionId.toUpperCase();
        if (!canonicalId.matches(".*_(MINION|GENERATOR)_\\d+")) {
            return;
        }
        if (uniqueMinionsCrafted == null) {
            uniqueMinionsCrafted = new HashSet<>();
        }
        if (uniqueMinionsCrafted.add(canonicalId)) {
            this.minionsCrafted = uniqueMinionsCrafted.size();
            
            String displayName = "Minion";
            int tierVal = 1;
            try {
                int lastUnderscore = canonicalId.lastIndexOf('_');
                tierVal = Integer.parseInt(canonicalId.substring(lastUnderscore + 1));
            } catch (Exception ignored) {}
            
            SkyblockItem item = ItemRegistry.getItem(canonicalId);
            if (item != null) {
                displayName = item.getDisplayName();
                String romanSuffix = " " + getRomanNumeral(tierVal);
                if (displayName.endsWith(romanSuffix)) {
                    displayName = displayName.substring(0, displayName.length() - romanSuffix.length());
                }
            } else {
                int genIdx = canonicalId.indexOf("_GENERATOR_");
                int minIdx = canonicalId.indexOf("_MINION_");
                int splitIdx = genIdx >= 0 ? genIdx : minIdx;
                if (splitIdx >= 0) {
                    String materialPart = canonicalId.substring(0, splitIdx);
                    displayName = ItemRegistry.formatName(materialPart) + " Minion";
                }
            }
            
            String romanTier = getRomanNumeral(tierVal);
            player.sendMessage("§aYou crafted a Tier §e" + romanTier + " " + displayName + "§a! That's a new one!");
            
            int xpReward = switch (tierVal) {
                case 7 -> 2;
                case 8 -> 3;
                case 9 -> 4;
                case 10 -> 6;
                case 11 -> 12;
                case 12 -> 24;
                default -> tierVal > 12 ? 24 : 1;
            };
            if (player.getActiveProfileData() != null) {
                player.getActiveProfileData().addSkyblockXp(xpReward, LevelCause.CRAFT_MINIONS_CAUSE);
            }
            
            checkMinionSlotsUnlock(player);
        }
    }

}
