package fun.ascent.skyblock.player.skill;

import lombok.Getter;
import net.minestom.server.item.Material;

public enum SkillType {

    COMBAT("Combat", Material.DIAMOND_SWORD),
    FARMING("Farming", Material.GOLDEN_HOE),
    MINING("Mining", Material.IRON_PICKAXE),
    FISHING("Fishing", Material.FISHING_ROD),
    FORAGING("Foraging", Material.IRON_AXE),
    ENCHANTING("Enchanting", Material.BOOKSHELF),
    ALCHEMY("Alchemy", Material.BREWING_STAND),
    TAMING("Taming", Material.LEAD),
    CARPENTRY("Carpentry", Material.CRAFTING_TABLE),
    RUNECRAFTING("Runecrafting", Material.MAGMA_CREAM);

    @Getter private final String displayName;
    @Getter private final Material icon;

    SkillType(String displayName, Material icon) {
        this.displayName = displayName;
        this.icon = icon;
    }

    public SkillDefinition definition() {
        return SkillRegistry.get(this);
    }
}