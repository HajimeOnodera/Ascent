package fun.ascent.skyblock.blocks;

import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.skill.SkillRegistry;
import fun.ascent.skyblock.player.skill.SkillType;
import fun.ascent.skyblock.world.region.Region;
import fun.ascent.skyblock.world.region.RegionManager;
import fun.ascent.skyblock.world.region.RegionType;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class BlockManager {

    public static final Map<Material, List<SkyblockBlock>> REGISTRY = new HashMap<>();

    public static void initialize() {
        REGISTRY.clear();

        // FARMING
        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.WHEAT)
                .skyblockItemId("WHEAT")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(SkillType.FARMING)
                .xpAmount(4.0)
                .respawnDelayTicks(60)
                .replacementBlock(Block.DIRT)
                .validRegions(List.of(RegionType.FARM, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.SUGAR_CANE)
                .skyblockItemId("SUGAR_CANE")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(SkillType.FARMING)
                .xpAmount(2.0)
                .validRegions(List.of(RegionType.FARM, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.CACTUS)
                .skyblockItemId("CACTUS")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(SkillType.FARMING)
                .xpAmount(2.0)
                .validRegions(List.of(RegionType.FARM, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.CARROT)
                .skyblockItemId("CARROT_ITEM")
                .baseDropAmount(3)
                .fortuneApplicable(true)
                .skillType(SkillType.FARMING)
                .xpAmount(4.0)
                .respawnDelayTicks(60)
                .replacementBlock(Block.FARMLAND)
                .validRegions(List.of(RegionType.FARM, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.POTATO)
                .skyblockItemId("POTATO_ITEM")
                .baseDropAmount(3)
                .fortuneApplicable(true)
                .skillType(SkillType.FARMING)
                .xpAmount(4.0)
                .respawnDelayTicks(60)
                .replacementBlock(Block.FARMLAND)
                .validRegions(List.of(RegionType.FARM, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.PUMPKIN)
                .skyblockItemId("PUMPKIN")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(SkillType.FARMING)
                .xpAmount(4.5)
                .validRegions(List.of(RegionType.FARM, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.MELON)
                .skyblockItemId("MELON")
                .baseDropAmount(5)
                .fortuneApplicable(true)
                .skillType(SkillType.FARMING)
                .xpAmount(4.5)
                .validRegions(List.of(RegionType.FARM, RegionType.PRIVATE_ISLAND))
                .build());

        // MINING
        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.COBBLESTONE)
                .skyblockItemId("COBBLESTONE")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(SkillType.MINING)
                .xpAmount(1.0)
                .validRegions(List.of(RegionType.COAL_MINE, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.COAL_ORE)
                .skyblockItemId("COAL")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(SkillType.MINING)
                .xpAmount(5.0)
                .respawnDelayTicks(100)
                .replacementBlock(Block.BEDROCK)
                .validRegions(List.of(RegionType.COAL_MINE, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.IRON_ORE)
                .skyblockItemId("IRON_INGOT")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(SkillType.MINING)
                .xpAmount(7.0)
                .respawnDelayTicks(150)
                .replacementBlock(Block.BEDROCK)
                .validRegions(List.of(RegionType.GOLD_MINE, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.GOLD_ORE)
                .skyblockItemId("GOLD_INGOT")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(SkillType.MINING)
                .xpAmount(9.0)
                .respawnDelayTicks(180)
                .replacementBlock(Block.BEDROCK)
                .validRegions(List.of(RegionType.GOLD_MINE, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.DIAMOND_ORE)
                .skyblockItemId("DIAMOND")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(SkillType.MINING)
                .xpAmount(15.0)
                .respawnDelayTicks(300)
                .replacementBlock(Block.BEDROCK)
                .validRegions(List.of(RegionType.DEEP_CAVERNS, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.LAPIS_ORE)
                .skyblockItemId("LAPIS_LAZULI")
                .baseDropAmount(6)
                .fortuneApplicable(true)
                .skillType(SkillType.MINING)
                .xpAmount(12.0)
                .respawnDelayTicks(200)
                .replacementBlock(Block.BEDROCK)
                .validRegions(List.of(RegionType.DEEP_CAVERNS, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.REDSTONE_ORE)
                .skyblockItemId("REDSTONE")
                .baseDropAmount(6)
                .fortuneApplicable(true)
                .skillType(SkillType.MINING)
                .xpAmount(10.0)
                .respawnDelayTicks(200)
                .replacementBlock(Block.BEDROCK)
                .validRegions(List.of(RegionType.DEEP_CAVERNS, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.EMERALD_ORE)
                .skyblockItemId("EMERALD")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(SkillType.MINING)
                .xpAmount(20.0)
                .respawnDelayTicks(400)
                .replacementBlock(Block.BEDROCK)
                .validRegions(List.of(RegionType.DEEP_CAVERNS, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.OBSIDIAN)
                .skyblockItemId("OBSIDIAN")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(SkillType.MINING)
                .xpAmount(25.0)
                .respawnDelayTicks(600)
                .replacementBlock(Block.BEDROCK)
                .validRegions(List.of(RegionType.DEEP_CAVERNS, RegionType.PRIVATE_ISLAND))
                .build());

        // FORAGING
        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.OAK_LOG)
                .skyblockItemId("OAK_LOG")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(SkillType.FORAGING)
                .xpAmount(6.0)
                .validRegions(List.of(RegionType.FOREST, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.BIRCH_LOG)
                .skyblockItemId("BIRCH_LOG")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(SkillType.FORAGING)
                .xpAmount(6.0)
                .validRegions(List.of(RegionType.FOREST, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.SPRUCE_LOG)
                .skyblockItemId("SPRUCE_LOG")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(SkillType.FORAGING)
                .xpAmount(6.0)
                .validRegions(List.of(RegionType.FOREST, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.DARK_OAK_LOG)
                .skyblockItemId("DARK_OAK_LOG")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(SkillType.FORAGING)
                .xpAmount(6.0)
                .validRegions(List.of(RegionType.FOREST, RegionType.PRIVATE_ISLAND))
                .build());
    }

    public static void register(SkyblockBlock block) {
        REGISTRY.computeIfAbsent(block.vanillaMaterial, _ -> new ArrayList<>()).add(block);
    }

    public static SkyblockBlock getBlock(Material material, RegionType regionType) {
        List<SkyblockBlock> possibleBlocks = REGISTRY.get(material);
        if (possibleBlocks == null) return null;

        for (SkyblockBlock sb : possibleBlocks) {
            if (sb.appliesTo(regionType)) {
                return sb;
            }
        }
        return null;
    }

    public static void handleBlockBreak(SkyblockPlayer player, Instance instance, Pos pos, Block brokenBlock) {
        Region region = RegionManager.getRegion(instance, pos);
        RegionType regionType = region != null ? region.getType() : RegionType.HUB;
        SkyblockBlock sbBlock = getBlock(Material.fromKey(brokenBlock.key()), regionType);

        if (sbBlock == null) {
            // Even if not a SkyblockBlock, we must remove it on the private island
            if (regionType == RegionType.PRIVATE_ISLAND) {
                instance.setBlock(pos, Block.AIR);
            }
            return;
        }

        int dropAmount = sbBlock.baseDropAmount;
        if (sbBlock.fortuneApplicable && player.getActiveProfileData() != null) {
            double fortune = 0;
            switch (sbBlock.skillType) {
                case MINING -> fortune = player.getActiveProfileData().stats.get("mining_fortune").getCurValue();
                case FARMING -> fortune = player.getActiveProfileData().stats.get("farming_fortune").getCurValue();
                case FORAGING -> fortune = player.getActiveProfileData().stats.get("foraging_fortune").getCurValue();
            }

            double multiplier = 1.0 + (fortune / 100.0);
            dropAmount = (int) Math.floor(sbBlock.baseDropAmount * multiplier);

            double chanceForExtra = (sbBlock.baseDropAmount * multiplier) - dropAmount;
            if (ThreadLocalRandom.current().nextDouble() < chanceForExtra) {
                dropAmount++;
            }
        }

        if (dropAmount > 0) {
            SkyblockItem item = ItemRegistry.getItem(sbBlock.skyblockItemId);
            if (item != null) {
                if (player.getActiveProfileData().level.curLevel >= 6){
                    player.getInventory().addItemStack(item.buildItemStack(player).withAmount(dropAmount));
                }else{
                    ItemEntity entity = new ItemEntity(item.buildItemStack(player).withAmount(dropAmount));
                    entity.setPickupDelay(Duration.ofMillis(200));
                    entity.setInstance(player.getInstance(),pos);
                    entity.setVelocity(new Vec(3));
                }
            }
        }

        if (sbBlock.skillType != null && sbBlock.xpAmount > 0) {
            SkillRegistry.grantXp(player, sbBlock.skillType, sbBlock.xpAmount);
        }

        if (player.getActiveProfile() != null && sbBlock.skyblockItemId != null) {
            player.getActiveProfile().updateCollection(sbBlock.skyblockItemId, dropAmount);
        }

        if (sbBlock.respawnDelayTicks > 0 && sbBlock.replacementBlock != null) {
            instance.setBlock(pos, sbBlock.replacementBlock);

            MinecraftServer.getSchedulerManager().buildTask(() -> instance.setBlock(pos, brokenBlock)).delay(TaskSchedule.tick(sbBlock.respawnDelayTicks)).schedule();
        } else {
            instance.setBlock(pos, Block.AIR);
        }
    }
}