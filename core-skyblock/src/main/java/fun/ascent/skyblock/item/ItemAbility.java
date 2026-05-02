package fun.ascent.skyblock.item;

import java.util.ArrayList;
import java.util.List;

public class ItemAbility {

    public enum AbilityType {
        RIGHT_CLICK("RIGHT CLICK"),
        LEFT_CLICK("LEFT CLICK"),
        SNEAK("SNEAK"),
        PASSIVE("PASSIVE");

        private final String displayName;

        AbilityType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() { return displayName; }
    }

    private final String abilityName;
    private final AbilityType abilityType;
    private final List<String> description;
    private final int manaCost;
    private final int soulflowCost;
    private final int cooldownSeconds;


    public ItemAbility(String abilityName, AbilityType abilityType, List<String> description, int manaCost, int soulflowCost, int cooldownSeconds) {
        this.abilityName = abilityName;
        this.abilityType = abilityType;
        this.description = new ArrayList<>(description);
        this.manaCost = manaCost;
        this.soulflowCost = soulflowCost;
        this.cooldownSeconds = cooldownSeconds;

    }

    public String getAbilityName() { return abilityName; }
    public AbilityType getAbilityType() { return abilityType; }
    public List<String> getDescription() { return description; }
    public int getManaCost() { return manaCost; }
    public int getSoulflowCost() { return soulflowCost; }
    public int getCooldownSeconds() { return cooldownSeconds; }


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
