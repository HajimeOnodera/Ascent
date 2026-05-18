package fun.ascent.skyblock.player.skill;

import lombok.Getter;
import net.minestom.server.item.Material;

public enum SkillType {

    COMBAT("Combat", Material.STONE_SWORD, false),
    FARMING("Farming", Material.GOLDEN_HOE, false),
    MINING("Mining", Material.STONE_PICKAXE, false),
    FISHING("Fishing", Material.FISHING_ROD, false),
    FORAGING("Foraging", Material.JUNGLE_SAPLING, false),
    ENCHANTING("Enchanting", Material.ENCHANTING_TABLE, false),
    ALCHEMY("Alchemy", Material.BREWING_STAND, false),
    TAMING("Taming", Material.POLAR_BEAR_SPAWN_EGG, false),
    CARPENTRY("Carpentry", Material.CRAFTING_TABLE, false),
    RUNECRAFTING("Runecrafting", Material.MAGMA_CREAM, true),
    SOCIAL("Social", Material.EMERALD, true),
    HUNTING("Hunting", Material.LEAD, false),
    DUNGEONEERING("Dungeoneering", Material.PLAYER_HEAD, true);


    @Getter private final String displayName;
    @Getter private final Material icon;
    @Getter private final boolean isCosmetic;

    SkillType(String displayName, Material icon, boolean isCosmetic) {
        this.displayName = displayName;
        this.icon = icon;
        this.isCosmetic = isCosmetic;
    }

    public SkillDefinition definition() {
        return SkillRegistry.get(this);
    }
}
