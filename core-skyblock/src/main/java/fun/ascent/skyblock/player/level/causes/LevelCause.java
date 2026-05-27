package fun.ascent.skyblock.player.level.causes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum LevelCause {
    MISSION_CAUSE("Complete Objective"),
    SKILL_CAUSE("Skill Level Up"),
    COLLECTION_CAUSE("Collections"),
    FAIRY_SOULS_CAUSE("Fairy Souls"),
    MUSEUM_DONATION_CAUSE("Museum Donation"),
    ACCESSORY_CAUSE("Accessory"),
    PETS_CAUSE("Pets"),
    CRAFT_MINIONS_CAUSE("Crafted Minion"),
    BANK_UPGRADE_CAUSE("Bank Upgrade"),
    FAST_TRAVEL_CAUSE("Fast Travel"),
    SLAYER_LEVEL_UP_CAUSE("Slayer Level Up"),
    DEFEAT_SLAYER_BOSS_CAUSE("Defeat Slayer Boss"),
    BESTIARY_PROGRESSION_CAUSE("Bestiary Progression"),
    GARDEN("Garden")
    ;

    private final String displayName;

    @Override
    public String toString() {
        return name();
    }

    public String formatMessage(int amount, int progress) {
        return "<aqua>+" + amount + " SkyBlock XP <gray>(" + displayName + ") <aqua>(" + (progress + amount) + "/100)";
    }
}
