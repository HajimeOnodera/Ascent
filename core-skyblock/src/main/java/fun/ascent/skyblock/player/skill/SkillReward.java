package fun.ascent.skyblock.player.skill;

import fun.ascent.skyblock.player.skill.unlock.SkillUnlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record SkillReward(int level, int xpRequired, List<SkillUnlock> unlocks) {

    public SkillReward {
        if (level <= 0) {
            throw new IllegalArgumentException("Skill reward level must be positive");
        }
        if (xpRequired < 0) {
            throw new IllegalArgumentException("Skill reward XP requirement cannot be negative");
        }
        unlocks = List.copyOf(Objects.requireNonNull(unlocks, "unlocks"));
    }

    public SkillReward(int level, int xpRequired, SkillUnlock... unlocks) {
        this(level, xpRequired, Arrays.asList(unlocks));
    }

    public List<String> toLore() {
        List<String> lore = new ArrayList<>();
        lore.add("<gray>Level " + toRoman(level) + " Rewards:");
        for (SkillUnlock unlock : unlocks) {
            lore.add("<gray>  " + unlock.display());
        }
        return lore;
    }

    public static String toRoman(int num) {
        if (num <= 0) return "";

        String[] thousands = {"", "M", "MM", "MMM"};
        String[] hundreds  = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] tens      = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] ones      = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

        return thousands[num / 1000]
                + hundreds[(num % 1000) / 100]
                + tens[(num % 100) / 10]
                + ones[num % 10];
    }
}

