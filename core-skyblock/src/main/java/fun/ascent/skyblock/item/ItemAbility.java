package fun.ascent.skyblock.item;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public record ItemAbility(String abilityName, AbilityType abilityType, List<String> description, int manaCost, int soulflowCost, int cooldownSeconds) {

    @Getter
    public enum AbilityType {
        RIGHT_CLICK("RIGHT CLICK"),
        LEFT_CLICK("LEFT CLICK"),
        SNEAK("SNEAK"),
        PASSIVE("PASSIVE");

        private final String displayName;

        AbilityType(String displayName) {
            this.displayName = displayName;
        }

    }

    public ItemAbility(String abilityName, AbilityType abilityType, List<String> description, int manaCost, int soulflowCost, int cooldownSeconds) {
        this.abilityName = abilityName;
        this.abilityType = abilityType;
        this.description = new ArrayList<>(description);
        this.manaCost = manaCost;
        this.soulflowCost = soulflowCost;
        this.cooldownSeconds = cooldownSeconds;

    }


    public List<String> buildLore() {
        List<String> lore = new ArrayList<>();
        lore.add("§6Ability: " + abilityName + " §e§l" + abilityType.getDisplayName());
        for (String line : description) {
            lore.add("§7" + line);
        }
        if (manaCost > 0) {
            lore.add("§8Mana Cost: §b" + manaCost);
        }
        if (soulflowCost > 0) {
            lore.add("§8Soulflow Cost: §3" + soulflowCost + "⸎");
        }
        if (cooldownSeconds > 0) {
            lore.add("§8Cooldown: §a" + cooldownSeconds + "s");
        }
        return lore;
    }
}
