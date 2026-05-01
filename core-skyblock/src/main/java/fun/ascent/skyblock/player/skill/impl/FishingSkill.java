package fun.ascent.skyblock.player.skill.impl;

import fun.ascent.skyblock.player.stats.Stats;
import fun.ascent.skyblock.player.skill.SkillDefinition;
import fun.ascent.skyblock.player.skill.SkillReward;
import fun.ascent.skyblock.player.skill.unlock.CoinRewardUnlock;
import fun.ascent.skyblock.player.skill.unlock.StatBoostUnlock;
import fun.ascent.skyblock.player.skill.unlock.XpBoostUnlock;
import net.minestom.server.item.Material;

import java.util.List;

public class FishingSkill extends SkillDefinition {

    private static final SkillReward[] REWARDS;

    static {
        int[] table = standardTable();
        REWARDS = new SkillReward[60];
        for (int i = 0; i < 60; i++) {
            int level = i + 1;
            REWARDS[i] = switch (level) {
                case 1  -> new SkillReward(level, table[i], new XpBoostUnlock(4), new StatBoostUnlock(Stats.SEA_CREATURE_CHANCE, 1));
                case 5  -> new SkillReward(level, table[i], new XpBoostUnlock(4), new StatBoostUnlock(Stats.FISHING_SPEED, 3), new CoinRewardUnlock(1500));
                case 10 -> new SkillReward(level, table[i], new XpBoostUnlock(5), new StatBoostUnlock(Stats.SEA_CREATURE_CHANCE, 1), new CoinRewardUnlock(4000));
                case 20 -> new SkillReward(level, table[i], new XpBoostUnlock(7), new StatBoostUnlock(Stats.FISHING_SPEED, 5), new CoinRewardUnlock(15000));
                case 25 -> new SkillReward(level, table[i], new XpBoostUnlock(8), new StatBoostUnlock(Stats.SEA_CREATURE_CHANCE, 1), new CoinRewardUnlock(25000));
                case 30 -> new SkillReward(level, table[i], new XpBoostUnlock(9), new StatBoostUnlock(Stats.FISHING_SPEED, 8), new CoinRewardUnlock(40000));
                case 50 -> new SkillReward(level, table[i], new XpBoostUnlock(15), new StatBoostUnlock(Stats.SEA_CREATURE_CHANCE, 4), new StatBoostUnlock(Stats.FISHING_SPEED, 15), new CoinRewardUnlock(150000));
                case 60 -> new SkillReward(level, table[i], new XpBoostUnlock(20), new StatBoostUnlock(Stats.SEA_CREATURE_CHANCE, 5), new StatBoostUnlock(Stats.FISHING_SPEED, 20), new CoinRewardUnlock(250000));
                default -> new SkillReward(level, table[i], new XpBoostUnlock(4 + level / 10));
            };
        }
    }

    @Override public String name() { return "Fishing"; }
    @Override public Material icon() { return Material.FISHING_ROD; }
    @Override public List<String> description() {
        return List.of(
                "§7Fish to gain Fishing XP. Grants",
                "§b§7 §bSea Creature Chance §7and",
                "§bFishing Speed§7."
        );
    }
    @Override public SkillReward[] rewards() { return REWARDS; }
}