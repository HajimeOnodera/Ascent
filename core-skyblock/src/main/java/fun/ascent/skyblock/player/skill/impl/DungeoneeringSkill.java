package fun.ascent.skyblock.player.skill.impl;

import fun.ascent.skyblock.player.stats.Stats;
import fun.ascent.skyblock.player.skill.SkillDefinition;
import fun.ascent.skyblock.player.skill.SkillReward;
import fun.ascent.skyblock.player.skill.unlock.CoinRewardUnlock;
import fun.ascent.skyblock.player.skill.unlock.StatBoostUnlock;
import fun.ascent.skyblock.player.skill.unlock.XpBoostUnlock;
import net.minestom.server.item.Material;

import java.util.List;

public class DungeoneeringSkill extends SkillDefinition {

    private static final SkillReward[] REWARDS;

    static {
        int[] table = standardTable();
        REWARDS = new SkillReward[60];
        for (int i = 0; i < 60; i++) {
            int level = i + 1;
            REWARDS[i] = switch (level) {
                case 1  -> new SkillReward(level, table[i], new XpBoostUnlock(4), new StatBoostUnlock(Stats.HEALTH, 2));
                case 2  -> new SkillReward(level, table[i], new XpBoostUnlock(4));
                case 3  -> new SkillReward(level, table[i], new XpBoostUnlock(4), new StatBoostUnlock(Stats.DEFENSE, 1));
                case 4  -> new SkillReward(level, table[i], new XpBoostUnlock(4));
                case 5  -> new SkillReward(level, table[i], new XpBoostUnlock(4), new StatBoostUnlock(Stats.HEALTH, 2), new CoinRewardUnlock(1500));
                case 6  -> new SkillReward(level, table[i], new XpBoostUnlock(5));
                case 7  -> new SkillReward(level, table[i], new XpBoostUnlock(5), new StatBoostUnlock(Stats.DEFENSE, 2));
                case 8  -> new SkillReward(level, table[i], new XpBoostUnlock(5));
                case 9  -> new SkillReward(level, table[i], new XpBoostUnlock(5), new StatBoostUnlock(Stats.HEALTH, 3));
                case 10 -> new SkillReward(level, table[i], new XpBoostUnlock(5), new StatBoostUnlock(Stats.DEFENSE, 2), new CoinRewardUnlock(4000));
                case 15 -> new SkillReward(level, table[i], new XpBoostUnlock(6), new StatBoostUnlock(Stats.INTELLIGENCE, 5), new CoinRewardUnlock(8000));
                case 20 -> new SkillReward(level, table[i], new XpBoostUnlock(7), new StatBoostUnlock(Stats.HEALTH, 5), new CoinRewardUnlock(12000));
                case 25 -> new SkillReward(level, table[i], new XpBoostUnlock(8), new StatBoostUnlock(Stats.DEFENSE, 3), new CoinRewardUnlock(20000));
                case 30 -> new SkillReward(level, table[i], new XpBoostUnlock(9), new StatBoostUnlock(Stats.INTELLIGENCE, 10), new CoinRewardUnlock(35000));
                case 35 -> new SkillReward(level, table[i], new XpBoostUnlock(10), new StatBoostUnlock(Stats.HEALTH, 8), new CoinRewardUnlock(50000));
                case 40 -> new SkillReward(level, table[i], new XpBoostUnlock(12), new StatBoostUnlock(Stats.DEFENSE, 5), new CoinRewardUnlock(75000));
                case 45 -> new SkillReward(level, table[i], new XpBoostUnlock(14), new StatBoostUnlock(Stats.INTELLIGENCE, 15), new CoinRewardUnlock(100000));
                case 50 -> new SkillReward(level, table[i], new XpBoostUnlock(15), new StatBoostUnlock(Stats.HEALTH, 15), new StatBoostUnlock(Stats.SPEED, 1), new CoinRewardUnlock(150000));
                case 60 -> new SkillReward(level, table[i], new XpBoostUnlock(20), new StatBoostUnlock(Stats.HEALTH, 20), new StatBoostUnlock(Stats.SPEED, 2), new CoinRewardUnlock(250000));
                default -> new SkillReward(level, table[i], new XpBoostUnlock(4 + level / 10));
            };
        }
    }

    @Override public String name() { return "Dungeoneering"; }
    @Override public Material icon() { return Material.PLAYER_HEAD; }
    @Override public List<String> description() {
        return List.of(
                "<gray>Conquer Catacombs and defeat dungeon",
                "<gray>bosses to earn Dungeoneering XP.",
                "<gray>Grants <blue>Health, Defense, <gray>and <blue>Intelligence<gray>."
        );
    }
    @Override public SkillReward[] rewards() { return REWARDS; }
}
