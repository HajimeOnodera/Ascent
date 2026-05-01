package fun.ascent.skyblock.player.skill.impl;

import fun.ascent.skyblock.player.skill.SkillDefinition;
import fun.ascent.skyblock.player.skill.SkillReward;
import fun.ascent.skyblock.player.skill.unlock.XpBoostUnlock;
import net.minestom.server.item.Material;

import java.util.List;

public class RunecraftingSkill extends SkillDefinition {

    private static final SkillReward[] REWARDS;

    static {
        int[] table = runecraftingTable();
        REWARDS = new SkillReward[25];
        for (int i = 0; i < 25; i++) {
            int level = i + 1;
            REWARDS[i] = new SkillReward(level, table[i], new XpBoostUnlock(level <= 5 ? 1 : level <= 15 ? 2 : 3));
        }
    }

    @Override public String name() { return "Runecrafting"; }
    @Override public Material icon() { return Material.MAGMA_CREAM; }
    @Override public List<String> description() {
        return List.of(
                "§7Sacrifice runes at the Runic",
                "§7Pedestal to gain Runecrafting",
                "§7XP and unlock higher tier runes."
        );
    }
    @Override public SkillReward[] rewards() { return REWARDS; }
}