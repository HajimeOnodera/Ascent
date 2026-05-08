package fun.ascent.skyblock.player.skill.impl;

import fun.ascent.skyblock.player.skill.SkillDefinition;
import fun.ascent.skyblock.player.skill.SkillReward;
import fun.ascent.skyblock.player.skill.unlock.CoinRewardUnlock;
import fun.ascent.skyblock.player.skill.unlock.XpBoostUnlock;
import net.minestom.server.item.Material;

import java.util.List;

public class CarpentrySkill extends SkillDefinition {

    private static final SkillReward[] REWARDS;

    static {
        int[] table = carpentryTable();
        REWARDS = new SkillReward[50];
        for (int i = 0; i < 50; i++) {
            int level = i + 1;
            REWARDS[i] = switch (level) {
                case 5  -> new SkillReward(level, table[i], new XpBoostUnlock(3), new CoinRewardUnlock(500));
                case 10 -> new SkillReward(level, table[i], new XpBoostUnlock(4), new CoinRewardUnlock(2000));
                case 20 -> new SkillReward(level, table[i], new XpBoostUnlock(5), new CoinRewardUnlock(6000));
                case 30 -> new SkillReward(level, table[i], new XpBoostUnlock(6), new CoinRewardUnlock(15000));
                case 40 -> new SkillReward(level, table[i], new XpBoostUnlock(8), new CoinRewardUnlock(30000));
                case 50 -> new SkillReward(level, table[i], new XpBoostUnlock(10), new CoinRewardUnlock(60000));
                default -> new SkillReward(level, table[i], new XpBoostUnlock(2));
            };
        }
    }

    @Override public String name() { return "Carpentry"; }
    @Override public Material icon() { return Material.CRAFTING_TABLE; }
    @Override public List<String> description() {
        return List.of(
                "<gray>Craft items to gain Carpentry",
                "<gray>XP. This skill has no direct",
                "<gray>stat bonuses, but unlocks unique",
                "<gray>cosmetic rewards."
        );
    }
    @Override public SkillReward[] rewards() { return REWARDS; }
}
