package fun.ascent.skyblock.player.skill.impl;

import fun.ascent.skyblock.player.stats.Stats;
import fun.ascent.skyblock.player.skill.SkillDefinition;
import fun.ascent.skyblock.player.skill.SkillReward;
import fun.ascent.skyblock.player.skill.unlock.CoinRewardUnlock;
import fun.ascent.skyblock.player.skill.unlock.StatBoostUnlock;
import fun.ascent.skyblock.player.skill.unlock.XpBoostUnlock;
import net.minestom.server.item.Material;

import java.util.List;

public class EnchantingSkill extends SkillDefinition {

    private static final SkillReward[] REWARDS;

    static {
        int[] table = standardTable();
        REWARDS = new SkillReward[60];
        for (int i = 0; i < 60; i++) {
            int level = i + 1;
            REWARDS[i] = switch (level) {
                case 1  -> new SkillReward(level, table[i], new XpBoostUnlock(4), new StatBoostUnlock(Stats.INTELLIGENCE, 1));
                case 5  -> new SkillReward(level, table[i], new XpBoostUnlock(4), new StatBoostUnlock(Stats.INTELLIGENCE, 2), new CoinRewardUnlock(1500));
                case 10 -> new SkillReward(level, table[i], new XpBoostUnlock(5), new StatBoostUnlock(Stats.INTELLIGENCE, 3), new CoinRewardUnlock(4000));
                case 20 -> new SkillReward(level, table[i], new XpBoostUnlock(7), new StatBoostUnlock(Stats.INTELLIGENCE, 5), new CoinRewardUnlock(15000));
                case 30 -> new SkillReward(level, table[i], new XpBoostUnlock(9), new StatBoostUnlock(Stats.INTELLIGENCE, 7), new StatBoostUnlock(Stats.ABILITY_DAMAGE, 1), new CoinRewardUnlock(40000));
                case 50 -> new SkillReward(level, table[i], new XpBoostUnlock(15), new StatBoostUnlock(Stats.INTELLIGENCE, 12), new StatBoostUnlock(Stats.ABILITY_DAMAGE, 3), new CoinRewardUnlock(150000));
                case 60 -> new SkillReward(level, table[i], new XpBoostUnlock(20), new StatBoostUnlock(Stats.INTELLIGENCE, 15), new StatBoostUnlock(Stats.ABILITY_DAMAGE, 5), new CoinRewardUnlock(250000));
                default -> new SkillReward(level, table[i], new XpBoostUnlock(4 + level / 10), new StatBoostUnlock(Stats.INTELLIGENCE, 1));
            };
        }
    }

    @Override public String name() { return "Enchanting"; }
    @Override public Material icon() { return Material.BOOKSHELF; }
    @Override public List<String> description() {
        return List.of(
                "<gray>Enchant items and use anvils to",
                "<gray>gain Enchanting XP. Grants",
                "<aqua><gray> <aqua>Intelligence<gray>."
        );
    }
    @Override public SkillReward[] rewards() { return REWARDS; }
}
