package fun.ascent.skyblock.skill.impl;

import fun.ascent.skyblock.player.stats.Stats;
import fun.ascent.skyblock.skill.SkillDefinition;
import fun.ascent.skyblock.skill.SkillReward;
import fun.ascent.skyblock.skill.unlock.CoinRewardUnlock;
import fun.ascent.skyblock.skill.unlock.StatBoostUnlock;
import fun.ascent.skyblock.skill.unlock.XpBoostUnlock;
import net.minestom.server.item.Material;

import java.util.List;

public class TamingSkill extends SkillDefinition {

    private static final SkillReward[] REWARDS;

    static {
        int[] table = standardTable();
        REWARDS = new SkillReward[60];
        for (int i = 0; i < 60; i++) {
            int level = i + 1;
            REWARDS[i] = switch (level) {
                case 1  -> new SkillReward(level, table[i], new XpBoostUnlock(4), new StatBoostUnlock(Stats.PET_LUCK, 1));
                case 5  -> new SkillReward(level, table[i], new XpBoostUnlock(4), new StatBoostUnlock(Stats.PET_LUCK, 2), new CoinRewardUnlock(1000));
                case 10 -> new SkillReward(level, table[i], new XpBoostUnlock(5), new StatBoostUnlock(Stats.PET_LUCK, 3), new CoinRewardUnlock(3000));
                case 20 -> new SkillReward(level, table[i], new XpBoostUnlock(7), new StatBoostUnlock(Stats.PET_LUCK, 5), new CoinRewardUnlock(12000));
                case 30 -> new SkillReward(level, table[i], new XpBoostUnlock(9), new StatBoostUnlock(Stats.PET_LUCK, 7), new CoinRewardUnlock(35000));
                case 50 -> new SkillReward(level, table[i], new XpBoostUnlock(15), new StatBoostUnlock(Stats.PET_LUCK, 12), new CoinRewardUnlock(120000));
                case 60 -> new SkillReward(level, table[i], new XpBoostUnlock(20), new StatBoostUnlock(Stats.PET_LUCK, 15), new CoinRewardUnlock(200000));
                default -> new SkillReward(level, table[i], new XpBoostUnlock(4 + level / 10), new StatBoostUnlock(Stats.PET_LUCK, 1));
            };
        }
    }

    @Override public String name() { return "Taming"; }
    @Override public Material icon() { return Material.LEAD; }
    @Override public List<String> description() {
        return List.of(
                "§7Tame pets and level them up",
                "§7to gain Taming XP. Grants",
                "§d§7 §dPet Luck§7 at higher levels."
        );
    }
    @Override public SkillReward[] rewards() { return REWARDS; }
}