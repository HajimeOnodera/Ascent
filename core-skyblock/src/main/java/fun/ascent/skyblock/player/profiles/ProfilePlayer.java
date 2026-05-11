package fun.ascent.skyblock.player.profiles;

import fun.ascent.skyblock.hotm.HotmData;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.level.SkyblockLevel;
import fun.ascent.skyblock.player.skill.PlayerSkillData;
import fun.ascent.skyblock.player.stats.Stat;
import fun.ascent.skyblock.player.stats.StatBuilder;
import fun.ascent.skyblock.player.stats.Stats;
import fun.ascent.skyblock.world.WorldHandler;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static fun.ascent.common.StringUtility.text;
import static fun.ascent.skyblock.player.stats.Stats.*;

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

    public ProfilePlayer(UUID profileID, SkyblockPlayer player) {
        this.profileID = profileID;
        this.playerUUID = player.getUuid();
        this.skyblockPlayer = player;
    }



    public void postLoad() {
        if (stats == null) stats = new HashMap<>();
        if (skillData == null) skillData = new PlayerSkillData();
        if (level == null) level = new SkyblockLevel();
        if (hotmData == null) hotmData = new HotmData();
        hotmData.postLoad();
        if (playerUUID != null) {
            this.skyblockPlayer = WorldHandler.getPlayer(playerUUID);
        }
    }

    public void updateStats() {
        // TODO: Update player stats
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
        level.addExp(xp % 100);
        level.curLevel += xp / 100;

        sendLevelUpMessage(oldLevel, level.curLevel);
    }

    public void sendLevelUpMessage(int oldLevel, int curLevel) {
        Map<String, ItemStack> stringRewards = SkyblockLevel.getRewards(oldLevel, curLevel);
        int totalStrength = SkyblockLevel.getStrengthReward(oldLevel, curLevel);
        int totalHealth = SkyblockLevel.getHealthReward(oldLevel, curLevel);

        String newColour = SkyblockLevel.getLevelColour(curLevel);
        String oldColour = SkyblockLevel.getLevelColour(oldLevel);

        skyblockPlayer.sendMessage("");
        skyblockPlayer.sendMessage(text(text("<dark_aqua><bold>SKYBLOCK LEVEL UP")));
        skyblockPlayer.sendMessage(text(text(newColour + "Level <dark_gray>[" + oldColour + oldLevel + "<dark_gray>] -> [" + newColour + curLevel + "<dark_gray>]")));
        skyblockPlayer.sendMessage("");
        skyblockPlayer.sendMessage(text(text("<green><bold>REWARDS")));

        if (totalHealth > 0) {
            skyblockPlayer.sendMessage(text(text("  <dark_gray>+<red>" + totalHealth + " <red>❤ Health")));
            addToStat(HEALTH, totalHealth);
        }
        if (totalStrength > 0) {
            skyblockPlayer.sendMessage(text(text("  <dark_gray>+<red>" + totalStrength + " <red>❁ Strength")));
            addToStat(STRENGTH, totalStrength);
        }

        for (String rewardStr : stringRewards.keySet()) {
            skyblockPlayer.sendMessage(text(text("  <dark_gray>+" + rewardStr)));
        }

        skyblockPlayer.playSound(Sound.sound(
                Key.key("entity.player.levelup"),
                Sound.Source.PLAYER, 1f, 1f));
    }

    public void addToStat(Stats base, int amount) {
        Stat stat = StatBuilder.build(base);
        if (stats.containsKey(stat.id)) {
            stat = stats.get(stat.id);
        }
        stats.put(stat.id, stat.addCurValue(amount));
    }

}

