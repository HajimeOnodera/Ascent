package fun.ascent.skyblock.player.skill.impl;

import fun.ascent.skyblock.player.stats.Stats;
import fun.ascent.skyblock.player.skill.SkillDefinition;
import fun.ascent.skyblock.player.skill.SkillReward;
import fun.ascent.skyblock.player.skill.unlock.CoinRewardUnlock;
import fun.ascent.skyblock.player.skill.unlock.StatBoostUnlock;
import fun.ascent.skyblock.player.skill.unlock.XpBoostUnlock;
import net.minestom.server.item.Material;

import java.util.List;

public class FarmingSkill extends SkillDefinition {

    private static final SkillReward[] REWARDS;

    static {
        int[] table = standardTable();
        REWARDS = new SkillReward[60];
        for (int i = 0; i < 60; i++) {
            int level = i + 1;
            REWARDS[i] = switch (level) {
                case 1  -> new SkillReward(level, table[i], new XpBoostUnlock(4), new StatBoostUnlock(Stats.FARMING_FORTUNE, 2));
                case 5  -> new SkillReward(level, table[i], new XpBoostUnlock(4), new StatBoostUnlock(Stats.FARMING_FORTUNE, 4), new CoinRewardUnlock(1500));
                case 10 -> new SkillReward(level, table[i], new XpBoostUnlock(5), new StatBoostUnlock(Stats.FARMING_FORTUNE, 6), new CoinRewardUnlock(4000));
                case 15 -> new SkillReward(level, table[i], new XpBoostUnlock(6), new StatBoostUnlock(Stats.FARMING_FORTUNE, 8), new CoinRewardUnlock(8000));
                case 20 -> new SkillReward(level, table[i], new XpBoostUnlock(7), new StatBoostUnlock(Stats.FARMING_FORTUNE, 10), new CoinRewardUnlock(15000));
                case 25 -> new SkillReward(level, table[i], new XpBoostUnlock(8), new StatBoostUnlock(Stats.FARMING_FORTUNE, 12), new CoinRewardUnlock(25000));
                case 30 -> new SkillReward(level, table[i], new XpBoostUnlock(9), new StatBoostUnlock(Stats.FARMING_FORTUNE, 14), new CoinRewardUnlock(40000));
                case 50 -> new SkillReward(level, table[i], new XpBoostUnlock(15), new StatBoostUnlock(Stats.FARMING_FORTUNE, 20), new CoinRewardUnlock(150000));
                case 60 -> new SkillReward(level, table[i], new XpBoostUnlock(20), new StatBoostUnlock(Stats.FARMING_FORTUNE, 24), new CoinRewardUnlock(250000));
                default -> new SkillReward(level, table[i], new XpBoostUnlock(4 + level / 10), new StatBoostUnlock(Stats.FARMING_FORTUNE, 1));
            };
        }
    }

    @Override public String name() { return "Farming"; }
    @Override public Material icon() { return Material.GOLDEN_HOE; }
    @Override public List<String> description() {
        return List.of(
                "§7Harvest crops to gain Farming",
                "§7XP. Grants §6Farming Fortune§7,",
                "§7increasing your crop drops."
        );
    }
    @Override public SkillReward[] rewards() { return REWARDS; }
}