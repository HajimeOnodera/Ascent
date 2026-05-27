package fun.ascent.skyblock.player.profiles;

import fun.ascent.skyblock.data.SkyblockDataHandler;
import fun.ascent.skyblock.data.impl.DatapointObject;
import fun.ascent.skyblock.hotm.HotmData;
import fun.ascent.skyblock.player.bestiary.BestiaryProgress;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.level.SkyBlockLevelRequirement;
import fun.ascent.skyblock.player.level.SkyblockLevel;
import fun.ascent.skyblock.player.level.unlocks.SkyBlockLevelStatisticUnlock;
import fun.ascent.skyblock.player.skill.PlayerSkillData;
import fun.ascent.skyblock.player.stats.Stat;
import fun.ascent.skyblock.player.stats.StatBuilder;
import fun.ascent.skyblock.player.stats.Stats;
import fun.ascent.skyblock.world.WorldHandler;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.text.Component;
import java.time.Duration;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import fun.ascent.skyblock.player.stats.playerStat.Speed;
import fun.ascent.skyblock.quest.QuestData;

import static fun.ascent.common.StringUtility.text;

public class ProfilePlayer {

    public UUID profileID;
    public UUID playerUUID;

    public double playerCoins = 0;
    public double playerBits = 0;
    public double playerGold = 0;

    public transient SkyblockPlayer skyblockPlayer;

    public HashMap<String, Stat> stats = new HashMap<>();
    public PlayerSkillData skillData = new PlayerSkillData();
    public SkyblockLevel level = new SkyblockLevel();
    public HotmData hotmData = new HotmData();
    public BestiaryProgress bestiaryProgress = new BestiaryProgress();
    public fun.ascent.skyblock.quest.QuestData questData = new fun.ascent.skyblock.quest.QuestData();

    public fun.ascent.skyblock.quest.QuestData getQuestData() {
        if (questData == null) {
            questData = new fun.ascent.skyblock.quest.QuestData();
        }
        if (skyblockPlayer != null) {
            questData.setSkyblockPlayer(skyblockPlayer);
        }
        return questData;
    }

    public List<ItemStack> inventory = new ArrayList<>();
    public List<ItemStack> armor = new ArrayList<>();
    public List<ItemStack> enderchest = new ArrayList<>();

    private SkyblockDataHandler dataHandler;

    public ProfilePlayer(UUID profileID, UUID playerUUID) {
        this.profileID = profileID;
        this.playerUUID = playerUUID;
        this.dataHandler = new SkyblockDataHandler(this.playerUUID, this.profileID);
    }

    public ProfilePlayer(UUID profileID, SkyblockPlayer player) {
        this.profileID = profileID;
        this.playerUUID = player != null ? player.getUuid() : null;
        this.skyblockPlayer = player;
        this.dataHandler = new SkyblockDataHandler(this.playerUUID, this.profileID);
    }

    public void update() {
        if (skyblockPlayer == null) return;
        
        // Only sync live data if this is the active profile
        if (skyblockPlayer.getActiveProfile() != null && skyblockPlayer.getActiveProfile().profileID.equals(this.profileID)) {
            ItemStack[] items = skyblockPlayer.getInventory().getItemStacks();
            this.inventory = new ArrayList<>(items.length);
            for (ItemStack item : items) {
                this.inventory.add(item != null ? item : ItemStack.AIR);
            }
            
            this.armor = List.of(
                    skyblockPlayer.getEquipment(EquipmentSlot.BOOTS),
                    skyblockPlayer.getEquipment(EquipmentSlot.LEGGINGS),
                    skyblockPlayer.getEquipment(EquipmentSlot.CHESTPLATE),
                    skyblockPlayer.getEquipment(EquipmentSlot.HELMET)
            );
            // Sync to handler
            dataHandler.syncFromPlayer(skyblockPlayer);
        }
    }



    public void postLoad() {
        if (stats == null) stats = new HashMap<>();
        if (skillData == null) skillData = new PlayerSkillData();
        if (level == null) level = new SkyblockLevel();
        if (hotmData == null) hotmData = new HotmData();
        if (bestiaryProgress == null) bestiaryProgress = new BestiaryProgress();
        hotmData.postLoad();

        if (getDataHandler() != null) {
            var questsDp = getDataHandler().get(SkyblockDataHandler.Data.QUESTS, DatapointObject.class);
            if (questsDp != null && questsDp.getValue() != null) {
                this.questData = (QuestData) questsDp.getValue();
            }
            
            var levelDp = getDataHandler().get(SkyblockDataHandler.Data.LEVEL, DatapointObject.class);
            if (levelDp != null && levelDp.getValue() != null) {
                this.level = (SkyblockLevel) levelDp.getValue();
            }

            var hotmDp = getDataHandler().get(SkyblockDataHandler.Data.HOTM, DatapointObject.class);
            if (hotmDp != null && hotmDp.getValue() != null) {
                this.hotmData = (HotmData) hotmDp.getValue();
                this.hotmData.postLoad();
            }

            var bestiaryDp = getDataHandler().get(SkyblockDataHandler.Data.BESTIARY, DatapointObject.class);
            if (bestiaryDp != null && bestiaryDp.getValue() != null) {
                this.bestiaryProgress = (BestiaryProgress) bestiaryDp.getValue();
            }
        }

        if (questData == null) questData = new QuestData();
        if (playerUUID != null) {
            this.skyblockPlayer = WorldHandler.getPlayer(playerUUID);
            if (this.skyblockPlayer != null) {
                questData.setSkyblockPlayer(this.skyblockPlayer);
            }
        }
    }

    public void updateStats() {
        if (skyblockPlayer == null) return;
        Speed.apply(skyblockPlayer);
    }


    public double getCoins() {
        return playerCoins;
    }

    public double getBits() {
        return playerBits;
    }

    public double getGold() {
        return playerGold;
    }

    public void addSkyblockXp(int xp) {
        if (xp <= 0) return;

        int oldLevel = level.curLevel;
        for (int i = 0; i < xp; i++) {
            level.addExp(1);
        }
        int newLevel = level.curLevel;

        if (newLevel > oldLevel) {
            sendLevelUpMessage(oldLevel, newLevel);
        } else {
            if (skyblockPlayer != null) {
                skyblockPlayer.sendActionBar(text("<dark_aqua>+" + xp + " SkyBlock XP <dark_gray>(" + level.progress.curProgress + "/100)"));
                skyblockPlayer.playSound(Sound.sound(
                        Key.key("entity.experience_orb.pickup"),
                        Sound.Source.PLAYER, 0.5f, 1.5f));
            }
        }
    }

    public void sendLevelUpMessage(int oldLevel, int curLevel) {
        if (skyblockPlayer == null) return;
        Map<String, ItemStack> stringRewards = SkyblockLevel.getRewards(oldLevel, curLevel);
        Map<Stats, Double> statRewards = getLevelStatRewards(oldLevel, curLevel);

        String newColour = SkyblockLevel.getLevelColour(curLevel);
        String oldColour = SkyblockLevel.getLevelColour(oldLevel);

        String border = "<dark_aqua><bold>" + "▬".repeat(38);
        skyblockPlayer.sendMessage(text(border));
        skyblockPlayer.sendMessage(text("  <dark_aqua><bold>SKYBLOCK LEVEL UP"));
        skyblockPlayer.sendMessage(text("  " + newColour + "Level <dark_gray>[" + oldColour + oldLevel + "<dark_gray>] ➜ [" + newColour + curLevel + "<dark_gray>]"));
        skyblockPlayer.sendMessage(Component.empty());
        skyblockPlayer.sendMessage(text("  <green><bold>REWARDS"));

        for (Map.Entry<Stats, Double> entry : statRewards.entrySet()) {
            Stats stat = entry.getKey();
            double amount = entry.getValue();
            if (amount <= 0) continue;

            String displayAmount = amount == Math.rint(amount)
                    ? String.valueOf((int) amount)
                    : String.format("%.1f", amount);
            skyblockPlayer.sendMessage(text("    <dark_gray>+<green>" + displayAmount + " " + stat.getStatColor() + stat.getStatFormattedDisplay()));
            addToStat(stat, amount);
        }

        for (String rewardStr : stringRewards.keySet()) {
            skyblockPlayer.sendMessage(text("    <dark_gray>+" + rewardStr));
        }

        skyblockPlayer.sendMessage(text(border));

        // Premium level-up Title + Subtitle!
        Title titleObj = Title.title(
                text("<dark_aqua><bold>LEVEL UP!"),
                text(newColour + "Level " + oldLevel + " <gray>➜ " + newColour + curLevel),
                Title.Times.times(Duration.ofMillis(300), Duration.ofMillis(2500), Duration.ofMillis(400))
        );
        skyblockPlayer.showTitle(titleObj);

        skyblockPlayer.playSound(Sound.sound(
                Key.key("entity.player.levelup"),
                Sound.Source.PLAYER, 1f, 1f));
    }

    private Map<Stats, Double> getLevelStatRewards(int oldLevel, int curLevel) {
        Map<Stats, Double> statRewards = new LinkedHashMap<>();
        for (int level = oldLevel + 1; level <= curLevel; level++) {
            SkyBlockLevelRequirement requirement = SkyBlockLevelRequirement.getLevel(level);
            if (requirement == null) continue;

            for (SkyBlockLevelStatisticUnlock unlock : requirement.getStatisticUnlocks()) {
                unlock.getStatistics().forEach((stat, amount) ->
                        statRewards.merge(stat, amount, Double::sum));
            }
        }
        return statRewards;
    }

    public void addToStat(Stats base, double amount) {
        Stat stat = StatBuilder.build(base);
        if (stats.containsKey(stat.id)) {
            stat = stats.get(stat.id);
        }
        stat.setMaxValue(stat.getMaxValue() + amount);
        stats.put(stat.id, stat.addCurValue(amount));
    }

    public SkyblockDataHandler getDataHandler() {
        if (dataHandler == null) {
            dataHandler = new SkyblockDataHandler(playerUUID, profileID);
        }
        return dataHandler;
    }

    public BestiaryProgress getBestiaryProgress() {
        if (bestiaryProgress == null) {
            bestiaryProgress = new BestiaryProgress();
        }
        return bestiaryProgress;
    }

}

