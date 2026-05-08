package fun.ascent.skyblock.player.profiles;

import fun.ascent.common.ChatUtility;
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
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfilePlayer {

    public UUID profileID;
    public UUID playerUUID;

    public transient SkyblockPlayer skyblockPlayer;

    public HashMap<String, Stat> stats = new HashMap<>();
    public PlayerSkillData skillData = new PlayerSkillData();
    public SkyblockLevel level = new SkyblockLevel();
    public HotmData hotmData = new HotmData();

    public ProfilePlayer(UUID profileID, SkyblockPlayer player) {
        this.profileID = profileID;
        this.playerUUID = player.getUuid();
        this.skyblockPlayer = player;
        loadDefaultStats();
    }

    public void loadDefaultStats() {
        for(Stats stat : Stats.values()) {
            addToStat(stat,0);
        }
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
        skyblockPlayer.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
                ChatUtility.FontInfo.center("§3§lSKYBLOCK LEVEL UP")));
        skyblockPlayer.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
                ChatUtility.FontInfo.center(newColour + "Level §8[" + oldColour + oldLevel + "§8] ➞ [" + newColour + curLevel + "§8]")));
        skyblockPlayer.sendMessage("");
        skyblockPlayer.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
                ChatUtility.FontInfo.center("§a§lREWARDS")));

        if (totalHealth > 0) {
            skyblockPlayer.sendMessage(ChatUtility.FontInfo.center("  §8+§c" + totalHealth + " §c❤ Health"));
            addToStat(Stats.HEALTH, totalHealth);
        }
        if (totalStrength > 0) {
            skyblockPlayer.sendMessage(ChatUtility.FontInfo.center("  §8+§c" + totalStrength + " §c❁ Strength"));
            addToStat(Stats.STRENGTH, totalStrength);
        }

        for (String rewardStr : stringRewards.keySet()) {
            skyblockPlayer.sendMessage(ChatUtility.FontInfo.center("  §8+" + rewardStr));
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