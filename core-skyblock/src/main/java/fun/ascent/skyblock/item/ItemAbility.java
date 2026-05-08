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
        lore.add("<gold>Ability: " + abilityName + " <yellow><bold>" + abilityType.getDisplayName());
        for (String line : description) {
            lore.add("<gray>" + line);
        }
        if (manaCost > 0) {
            lore.add("<dark_gray>Mana Cost: <aqua>" + manaCost);
        }
        if (soulflowCost > 0) {
            lore.add("<dark_gray>Soulflow Cost: <dark_aqua>" + soulflowCost + "⸎");
        }
        if (cooldownSeconds > 0) {
            lore.add("<dark_gray>Cooldown: <green>" + cooldownSeconds + "s");
        }
        return lore;
    }
}

