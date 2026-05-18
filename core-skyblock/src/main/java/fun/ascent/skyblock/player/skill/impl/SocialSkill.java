package fun.ascent.skyblock.player.skill.impl;

import fun.ascent.skyblock.player.skill.SkillDefinition;
import fun.ascent.skyblock.player.skill.SkillReward;
import fun.ascent.skyblock.player.skill.unlock.CoinRewardUnlock;
import fun.ascent.skyblock.player.skill.unlock.XpBoostUnlock;
import net.minestom.server.item.Material;

import java.util.List;

public class SocialSkill extends SkillDefinition {

    private static final SkillReward[] REWARDS;

    static {
        int[] table = runecraftingTable();
        REWARDS = new SkillReward[25];
        for (int i = 0; i < 25; i++) {
            int level = i + 1;
            REWARDS[i] = new SkillReward(level, table[i], 
                    new XpBoostUnlock(level <= 5 ? 1 : level <= 15 ? 2 : 3),
                    new CoinRewardUnlock(level * 500)
            );
        }
    }

    @Override public String name() { return "Social"; }
    @Override public Material icon() { return Material.EMERALD; }
    @Override public List<String> description() {
        return List.of(
                "<gray>Interact with players, visit islands,",
                "<gray>and host social events to earn Social XP.",
                "<gray>Grants <blue>Social Wisdom <gray>and cosmetic perks."
        );
    }
    @Override public SkillReward[] rewards() { return REWARDS; }
}
