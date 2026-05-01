package fun.ascent.skyblock.player.skill.impl;

import fun.ascent.skyblock.player.stats.Stats;
import fun.ascent.skyblock.player.skill.SkillDefinition;
import fun.ascent.skyblock.player.skill.SkillReward;
import fun.ascent.skyblock.player.skill.unlock.CoinRewardUnlock;
import fun.ascent.skyblock.player.skill.unlock.StatBoostUnlock;
import fun.ascent.skyblock.player.skill.unlock.XpBoostUnlock;
import net.minestom.server.item.Material;

import java.util.List;

public class CombatSkill extends SkillDefinition {

    private static final SkillReward[] REWARDS;

    static {
        int[] table = standardTable();
        REWARDS = new SkillReward[60];
        for (int i = 0; i < 60; i++) {
            int level = i + 1;
            REWARDS[i] = switch (level) {
                case 1  -> new SkillReward(level, table[i], new XpBoostUnlock(4), new StatBoostUnlock(Stats.CRIT_CHANCE, 1));
                case 2  -> new SkillReward(level, table[i], new XpBoostUnlock(4));
                case 3  -> new SkillReward(level, table[i], new XpBoostUnlock(4), new StatBoostUnlock(Stats.CRIT_DAMAGE, 1));
                case 4  -> new SkillReward(level, table[i], new XpBoostUnlock(4));
                case 5  -> new SkillReward(level, table[i], new XpBoostUnlock(4), new StatBoostUnlock(Stats.CRIT_CHANCE, 1), new CoinRewardUnlock(1500));
                case 6  -> new SkillReward(level, table[i], new XpBoostUnlock(5));
                case 7  -> new SkillReward(level, table[i], new XpBoostUnlock(5), new StatBoostUnlock(Stats.CRIT_DAMAGE, 2));
                case 8  -> new SkillReward(level, table[i], new XpBoostUnlock(5));
                case 9  -> new SkillReward(level, table[i], new XpBoostUnlock(5), new StatBoostUnlock(Stats.CRIT_CHANCE, 1));
                case 10 -> new SkillReward(level, table[i], new XpBoostUnlock(5), new StatBoostUnlock(Stats.CRIT_DAMAGE, 2), new CoinRewardUnlock(4000));
                case 15 -> new SkillReward(level, table[i], new XpBoostUnlock(6), new StatBoostUnlock(Stats.CRIT_CHANCE, 1), new CoinRewardUnlock(8000));
                case 20 -> new SkillReward(level, table[i], new XpBoostUnlock(7), new StatBoostUnlock(Stats.CRIT_DAMAGE, 3), new CoinRewardUnlock(12000));
                case 25 -> new SkillReward(level, table[i], new XpBoostUnlock(8), new StatBoostUnlock(Stats.CRIT_CHANCE, 1), new CoinRewardUnlock(20000));
                case 30 -> new SkillReward(level, table[i], new XpBoostUnlock(9), new StatBoostUnlock(Stats.CRIT_DAMAGE, 3), new CoinRewardUnlock(35000));
                case 35 -> new SkillReward(level, table[i], new XpBoostUnlock(10), new StatBoostUnlock(Stats.CRIT_CHANCE, 1), new CoinRewardUnlock(50000));
                case 40 -> new SkillReward(level, table[i], new XpBoostUnlock(12), new StatBoostUnlock(Stats.CRIT_DAMAGE, 4), new CoinRewardUnlock(75000));
                case 45 -> new SkillReward(level, table[i], new XpBoostUnlock(14), new StatBoostUnlock(Stats.CRIT_CHANCE, 1), new CoinRewardUnlock(100000));
                case 50 -> new SkillReward(level, table[i], new XpBoostUnlock(15), new StatBoostUnlock(Stats.CRIT_DAMAGE, 5), new StatBoostUnlock(Stats.FEROCITY, 1), new CoinRewardUnlock(150000));
                case 60 -> new SkillReward(level, table[i], new XpBoostUnlock(20), new StatBoostUnlock(Stats.CRIT_DAMAGE, 5), new StatBoostUnlock(Stats.FEROCITY, 2), new CoinRewardUnlock(250000));
                default -> new SkillReward(level, table[i], new XpBoostUnlock(4 + level / 10));
            };
        }
    }

    @Override public String name() { return "Combat"; }
    @Override public Material icon() { return Material.DIAMOND_SWORD; }
    @Override public List<String> description() {
        return List.of(
                "§7Increases your overall damage",
                "§7output. Grants §9Crit Damage",
                "§7and §9Crit Chance§7."
        );
    }
    @Override public SkillReward[] rewards() { return REWARDS; }
}