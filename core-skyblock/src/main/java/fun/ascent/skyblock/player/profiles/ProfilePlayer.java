package fun.ascent.skyblock.player.profiles;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.level.SkyblockLevel;
import fun.ascent.skyblock.player.stats.Stat;
import fun.ascent.skyblock.player.stats.StatBuilder;
import fun.ascent.skyblock.player.stats.Stats;
import fun.ascent.skyblock.player.skill.PlayerSkillData;
import fun.ascent.skyblock.world.WorldManager;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.item.ItemStack;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class ProfilePlayer {

    // Player Information
    public UUID profileID;
    public UUID playerUUID;

    // This can be null so please use with careful consideration
    public transient SkyblockPlayer skyblockPlayer;

    // Player Capabilities
    public HashMap<String, Stat> stats = new HashMap<>();
    public PlayerSkillData skillData = new PlayerSkillData();

    // Skyblock Level
    public SkyblockLevel level = new SkyblockLevel();

    public ProfilePlayer(UUID profileID, SkyblockPlayer player) {
        this.profileID = profileID;
        this.playerUUID = player.getUuid();
        this.skyblockPlayer = player;
    }

    //WARN: Put Null Checks for All Variables Added
    public void postLoad(){
        if(stats == null) stats = new HashMap<>();
        if(skillData == null) skillData = new PlayerSkillData();
        if(level == null) level = new SkyblockLevel();
        if(playerUUID != null){
            this.skyblockPlayer = WorldManager.getPlayer(playerUUID);
        }
    }

    public void updateStats(){
        //TODO: Update player stats;
    }

    public void addSkyblockXp(int xp) {
        if (xp <= 0) return;

        int oldLevel = level.curLevel;

        int levelsToGain = xp / 100;
        int extraXp = xp % 100;
        level.addExp(extraXp);

        level.curLevel += levelsToGain;

        int newLevel = level.curLevel;

        for (int i = oldLevel + 1; i <= newLevel; i++) {
            if (this.skyblockPlayer != null) {
                sendLevelUpMessage(i - 1, i);
            }
        }
    }

    public void sendLevelUpMessage(int oldLevel, int curLevel) {
        Map<String, ItemStack> stringRewards = SkyblockLevel.getRewards(curLevel);
        int totalStrength = SkyblockLevel.getStrengthReward(curLevel);
        int totalHealth = SkyblockLevel.getHealthReward(curLevel);

        TextColor newColour = SkyblockLevel.getLevelColour(curLevel);

        String border = "§b§l" + "▬".repeat(42);

        //TODO: Send Level Up Message
        skyblockPlayer.sendMessage("  §a§lREWARDS");

        if (totalHealth > 0) {
            skyblockPlayer.sendMessage("    §8+§c" + totalHealth + " §c❤ Health");
            addToStat(Stats.HEALTH,totalHealth);
        }
        if (totalStrength > 0) {
            skyblockPlayer.sendMessage("    §8+§c" + totalStrength + " §c❁ Strength");
            addToStat(Stats.STRENGTH,totalStrength);
        }

        if (!stringRewards.isEmpty()) {
            for (String rewardStr : stringRewards.keySet()) {
                    skyblockPlayer.sendMessage("    " + rewardStr);
            }
        }

        skyblockPlayer.sendMessage(border);

         skyblockPlayer.playSound(Sound.sound(
                 Key.key("entity.player.levelup"),
                 Sound.Source.PLAYER, 1f, 1f)
         );
    }


    public void addToStat(Stats base, int amount) {
        Stat stat = StatBuilder.build(base);
        if(stats.containsKey(stat.id)){
            stat = stats.get(stat.id);
        }
        stats.put(stat.id, stat.addCurValue(amount));
    }
}