package fun.ascent.skyblock.blocks;

import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.skill.SkillRegistry;
import fun.ascent.skyblock.world.location.SkyblockLocation;
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

        // Example: Hub Farm Wheat
        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.WHEAT)
                .skyblockItemId("WHEAT")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(fun.ascent.skyblock.player.skill.SkillType.FARMING)
                .xpAmount(4.0)
                .respawnDelayTicks(60)
                .replacementBlock(Block.DIRT)
                .validLocations(List.of(SkyblockLocation.FARM))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.LAPIS_ORE)
                .skyblockItemId("LAPIS_LAZULI")
                .baseDropAmount(3)
                .fortuneApplicable(true)
                .skillType(fun.ascent.skyblock.player.skill.SkillType.MINING)
                .xpAmount(12.0)
                .respawnDelayTicks(200)
                .replacementBlock(Block.BEDROCK)
                .validLocations(List.of(SkyblockLocation.LAPIS))
                .build());
    }

    public static void register(SkyblockBlock block) {
        REGISTRY.computeIfAbsent(block.vanillaMaterial, k -> new ArrayList<>()).add(block);
    }

    public static SkyblockBlock getBlock(Material material, SkyblockLocation location) {
        List<SkyblockBlock> possibleBlocks = REGISTRY.get(material);
        if (possibleBlocks == null) return null;

        for (SkyblockBlock sb : possibleBlocks) {
            if (sb.appliesTo(location)) {
                return sb;
            }
        }
        return null;
    }

    public static void handleBlockBreak(SkyblockPlayer player, Instance instance, Pos pos, Block brokenBlock) {
        SkyblockLocation location = SkyblockLocation.getLocation(instance, pos);
        SkyblockBlock sbBlock = getBlock(Material.fromKey(brokenBlock.key()), location);

        if (sbBlock == null) {
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

        if (sbBlock.respawnDelayTicks > 0 && sbBlock.replacementBlock != null) {
            instance.setBlock(pos, sbBlock.replacementBlock);

            MinecraftServer.getSchedulerManager().buildTask(() -> instance.setBlock(pos, brokenBlock)).delay(TaskSchedule.tick(sbBlock.respawnDelayTicks)).schedule();
        } else {
            instance.setBlock(pos, Block.AIR);
        }
    }
}