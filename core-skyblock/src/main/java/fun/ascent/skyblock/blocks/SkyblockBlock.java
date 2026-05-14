package fun.ascent.skyblock.blocks;

import fun.ascent.skyblock.player.skill.SkillType;
import fun.ascent.skyblock.world.region.RegionType;
import lombok.Builder;
import net.minestom.server.item.Material;
import net.minestom.server.instance.block.Block;

import java.util.List;

@Builder
public class SkyblockBlock {

    public Material vanillaMaterial;

    public String skyblockItemId;
    public int baseDropAmount;
    public boolean fortuneApplicable;

    public SkillType skillType;
    public double xpAmount;

    public int respawnDelayTicks;
    public Block replacementBlock;

    public List<RegionType> validRegions;

    public boolean appliesTo(RegionType region) {
        return validRegions == null || validRegions.isEmpty() || validRegions.contains(region);
    }
}