package fun.ascent.skyblock.player.skill.impl;

import fun.ascent.skyblock.player.stats.Stats;
import fun.ascent.skyblock.player.skill.SkillDefinition;
import fun.ascent.skyblock.player.skill.SkillReward;
import fun.ascent.skyblock.player.skill.unlock.CoinRewardUnlock;
import fun.ascent.skyblock.player.skill.unlock.StatBoostUnlock;
import fun.ascent.skyblock.player.skill.unlock.XpBoostUnlock;
import net.minestom.server.item.Material;

import java.util.List;

public class AlchemySkill extends SkillDefinition {

    private static final SkillReward[] REWARDS;

    static {
        int[] table = standardTable();
        REWARDS = new SkillReward[60];
        for (int i = 0; i < 60; i++) {
            int level = i + 1;
            REWARDS[i] = switch (level) {
                case 1  -> new SkillReward(level, table[i], new XpBoostUnlock(4), new StatBoostUnlock(Stats.INTELLIGENCE, 1));
                case 5  -> new SkillReward(level, table[i], new XpBoostUnlock(4), new StatBoostUnlock(Stats.INTELLIGENCE, 2), new CoinRewardUnlock(1000));
                case 10 -> new SkillReward(level, table[i], new XpBoostUnlock(5), new StatBoostUnlock(Stats.INTELLIGENCE, 3), new CoinRewardUnlock(3000));
                case 20 -> new SkillReward(level, table[i], new XpBoostUnlock(7), new StatBoostUnlock(Stats.INTELLIGENCE, 5), new CoinRewardUnlock(12000));
                case 30 -> new SkillReward(level, table[i], new XpBoostUnlock(9), new StatBoostUnlock(Stats.INTELLIGENCE, 7), new CoinRewardUnlock(35000));
                case 50 -> new SkillReward(level, table[i], new XpBoostUnlock(15), new StatBoostUnlock(Stats.INTELLIGENCE, 12), new CoinRewardUnlock(120000));
                case 60 -> new SkillReward(level, table[i], new XpBoostUnlock(20), new StatBoostUnlock(Stats.INTELLIGENCE, 15), new StatBoostUnlock(Stats.HEALTH, 25), new CoinRewardUnlock(200000));
                default -> new SkillReward(level, table[i], new XpBoostUnlock(4 + level / 10), new StatBoostUnlock(Stats.INTELLIGENCE, 1));
            };
        }
    }

    @Override public String name() { return "Alchemy"; }
    @Override public Material icon() { return Material.BREWING_STAND; }
    @Override public List<String> description() {
        return List.of(
                "<gray>Brew potions to gain Alchemy",
                "<gray>XP. Grants <aqua>Intelligence<gray> and",
                "<gray>improves potion effects."
        );
    }
    @Override public SkillReward[] rewards() { return REWARDS; }
}
