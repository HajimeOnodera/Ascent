package fun.ascent.skyblock.player.profiles;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.level.SkyblockLevel;
import fun.ascent.skyblock.player.stats.Stat;
import fun.ascent.skyblock.player.stats.StatBuilder;
import fun.ascent.skyblock.player.stats.Stats;
import fun.ascent.skyblock.player.skill.PlayerSkillData;
import fun.ascent.skyblock.utility.AlignUtils;
import fun.ascent.skyblock.world.WorldHandler;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.item.ItemStack;

import java.util.HashMap;
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
            this.skyblockPlayer = WorldHandler.getPlayer(playerUUID);
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
        sendLevelUpMessage(oldLevel,newLevel);

    }

    public void sendLevelUpMessage(int oldLevel, int curLevel) {
        Map<String, ItemStack> stringRewards = SkyblockLevel.getRewards(oldLevel,curLevel);
        int totalStrength = SkyblockLevel.getStrengthReward(oldLevel,curLevel);
        int totalHealth = SkyblockLevel.getHealthReward(oldLevel,curLevel);

        String newColour = SkyblockLevel.getLevelColour(curLevel);
        String oldColour = SkyblockLevel.getLevelColour(oldLevel);

        //TODO: Send Level Up Message
        skyblockPlayer.sendMessage("");
        skyblockPlayer.sendMessage(AlignUtils.alignCenter(
                LegacyComponentSerializer.legacySection().deserialize("§3§lSKYBLOCK LEVEL UP")
        ));
        skyblockPlayer.sendMessage(LegacyComponentSerializer.legacySection().deserialize(
                center(newColour + "Level §8[" + oldColour + oldLevel + "§8] ➞ [" + newColour + curLevel + "§8]")));
        skyblockPlayer.sendMessage("");
        skyblockPlayer.sendMessage(AlignUtils.alignCenter(LegacyComponentSerializer.legacySection().deserialize("§a§lREWARDS")));

        if (totalHealth > 0) {
            skyblockPlayer.sendMessage(center("  §8+§c" + totalHealth + " §c❤ Health"));
            addToStat(Stats.HEALTH,totalHealth);
        }
        if (totalStrength > 0) {
            skyblockPlayer.sendMessage(center("  §8+§c" + totalStrength + " §c❁ Strength"));
            addToStat(Stats.STRENGTH,totalStrength);
        }

        if (!stringRewards.isEmpty()) {
            for (String rewardStr : stringRewards.keySet()) {
                    skyblockPlayer.sendMessage(center("  §8+" + rewardStr));
            }
        }

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

    public static String center(String message) {
        String stripped = message.replaceAll("§[0-9a-fk-or]", "");
        int chatWidth = 320;
        int messageWidth = stripped.length() * 6;
        int spaces = Math.max(0, (chatWidth - messageWidth) / 2) / 4;

        StringBuilder padding = new StringBuilder();
        for (int i = 0; i < spaces; i++) {
            padding.append(" ");
        }

        return padding.toString() + message;
    }
}