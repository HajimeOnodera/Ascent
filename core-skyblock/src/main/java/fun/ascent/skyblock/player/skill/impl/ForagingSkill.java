package fun.ascent.skyblock.player.skill.impl;

import fun.ascent.skyblock.player.stats.Stats;
import fun.ascent.skyblock.player.skill.SkillDefinition;
import fun.ascent.skyblock.player.skill.SkillReward;
import fun.ascent.skyblock.player.skill.unlock.CoinRewardUnlock;
import fun.ascent.skyblock.player.skill.unlock.StatBoostUnlock;
import fun.ascent.skyblock.player.skill.unlock.XpBoostUnlock;
import net.minestom.server.item.Material;

import java.util.List;

public class ForagingSkill extends SkillDefinition {

    private static final SkillReward[] REWARDS;

    static {
        int[] table = standardTable();
        REWARDS = new SkillReward[60];
        for (int i = 0; i < 60; i++) {
            int level = i + 1;
            REWARDS[i] = switch (level) {
                case 1  -> new SkillReward(level, table[i], new XpBoostUnlock(4), new StatBoostUnlock(Stats.FORAGING_FORTUNE, 2));
                case 5  -> new SkillReward(level, table[i], new XpBoostUnlock(4), new StatBoostUnlock(Stats.FORAGING_FORTUNE, 3), new CoinRewardUnlock(1500));
                case 10 -> new SkillReward(level, table[i], new XpBoostUnlock(5), new StatBoostUnlock(Stats.FORAGING_FORTUNE, 5), new CoinRewardUnlock(4000));
                case 20 -> new SkillReward(level, table[i], new XpBoostUnlock(7), new StatBoostUnlock(Stats.FORAGING_FORTUNE, 7), new CoinRewardUnlock(15000));
                case 30 -> new SkillReward(level, table[i], new XpBoostUnlock(9), new StatBoostUnlock(Stats.FORAGING_FORTUNE, 10), new StatBoostUnlock(Stats.STRENGTH, 5), new CoinRewardUnlock(40000));
                case 50 -> new SkillReward(level, table[i], new XpBoostUnlock(15), new StatBoostUnlock(Stats.FORAGING_FORTUNE, 15), new StatBoostUnlock(Stats.STRENGTH, 15), new CoinRewardUnlock(150000));
                case 60 -> new SkillReward(level, table[i], new XpBoostUnlock(20), new StatBoostUnlock(Stats.FORAGING_FORTUNE, 18), new StatBoostUnlock(Stats.STRENGTH, 20), new CoinRewardUnlock(250000));
                default -> new SkillReward(level, table[i], new XpBoostUnlock(4 + level / 10), new StatBoostUnlock(Stats.FORAGING_FORTUNE, 1));
            };
        }
    }

    @Override public String name() { return "Foraging"; }
    @Override public Material icon() { return Material.IRON_AXE; }
    @Override public List<String> description() {
        return List.of(
                "§7Chop wood to gain Foraging",
                "§7XP. Grants §6Foraging Fortune",
                "§7and §cStrength§7."
        );
    }
    @Override public SkillReward[] rewards() { return REWARDS; }
}