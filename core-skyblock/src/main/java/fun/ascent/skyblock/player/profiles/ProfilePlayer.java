package fun.ascent.skyblock.player.profiles;

import fun.ascent.skyblock.data.SkyblockDataHandler;
import fun.ascent.skyblock.data.impl.DatapointObject;
import fun.ascent.skyblock.hotm.HotmData;
import fun.ascent.skyblock.player.bestiary.BestiaryProgress;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.level.SkyBlockLevelRequirement;
import fun.ascent.skyblock.player.level.SkyblockLevel;
import fun.ascent.skyblock.player.level.causes.LevelCause;
import fun.ascent.skyblock.player.level.unlocks.SkyBlockLevelStatisticUnlock;
import fun.ascent.skyblock.player.skill.PlayerSkillData;
import fun.ascent.skyblock.player.stats.Stat;
import fun.ascent.skyblock.player.stats.StatBuilder;
import fun.ascent.skyblock.player.stats.Stats;
import fun.ascent.skyblock.world.WorldHandler;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
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

    public long boosterCookieExpires = 0L;
    public boolean claimedFreeCookie = false;
    public double cookieBits = 0.0;
    public int bitsTimerSeconds = 0;
    public double bitsMultiplier = 1.0;

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

    public double getGold() {
        return playerGold;
    }

    public void addSkyblockXp(int xp) {
        addSkyblockXp(xp, LevelCause.MISSION_CAUSE);
    }

    public void addSkyblockXp(int xp, LevelCause cause) {
        if (xp <= 0) return;

        int oldLevel = level.curLevel;
        int beforeProgress = level.progress.curProgress;
        
        level.addXP(xp);
        
        int newLevel = level.curLevel;

        if (newLevel > oldLevel) {
            sendLevelUpMessage(oldLevel, newLevel);
        } else {
            if (skyblockPlayer != null) {
                String msg = cause.formatMessage(xp, beforeProgress);
                fun.ascent.skyblock.player.actionbar.ActionBar.of(skyblockPlayer.getUuid()).setAbsoluteOverride(msg, 60);
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

        skyblockPlayer.sendMessage(text(centerText("§3§lSKYBLOCK LEVEL UP")));
        skyblockPlayer.sendMessage(text(centerText("§7Level " + oldLevel + " §8➜ " + newColour + "[" + curLevel + "]")));
        skyblockPlayer.sendMessage(Component.empty());
        skyblockPlayer.sendMessage(text(centerText("§6§lREWARDS")));

        for (Map.Entry<Stats, Double> entry : statRewards.entrySet()) {
            Stats stat = entry.getKey();
            double amount = entry.getValue();
            if (amount <= 0) continue;

            String displayAmount = amount == Math.rint(amount)
                    ? String.valueOf((int) amount)
                    : String.format("%.1f", amount);
            
            String symbolPart = stat.getStatSymbol() != null && !stat.getStatSymbol().isEmpty() ? " " + stat.getStatColor() + stat.getStatSymbol() : "";
            skyblockPlayer.sendMessage(text(centerText("§8+" + displayAmount + symbolPart + " " + stat.getStatColor() + stat.getStatFormattedDisplay())));
            addToStat(stat, amount);
        }

        for (String rewardStr : stringRewards.keySet()) {
            skyblockPlayer.sendMessage(text(centerText("§8+" + rewardStr)));
        }
        skyblockPlayer.sendMessage(Component.empty());



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

    private String centerText(String text) {
        String clean = text.replaceAll("§[0-9a-fk-orxX]", "")
                           .replaceAll("<[^>]+>", "");
        int cleanLength = clean.length();
        int chatWidth = 52;
        if (cleanLength >= chatWidth) return text;
        int spaces = (chatWidth - cleanLength) / 2;
        return " ".repeat(spaces) + text;
    }

}

