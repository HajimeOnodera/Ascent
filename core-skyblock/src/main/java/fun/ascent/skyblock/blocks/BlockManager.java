package fun.ascent.skyblock.blocks;

import fun.ascent.skyblock.events.impl.CollectionAddEvent;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.SkyblockItem;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.skill.SkillRegistry;
import fun.ascent.skyblock.player.skill.SkillType;
import fun.ascent.skyblock.player.stats.Stats;
import fun.ascent.skyblock.world.region.Region;
import fun.ascent.skyblock.world.region.RegionManager;
import fun.ascent.skyblock.world.region.RegionType;
import net.minestom.server.MinecraftServer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;

import fun.ascent.skyblock.entity.display.DroppedItemEntity;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.tag.Tag;

import net.minestom.server.timer.TaskSchedule;


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
                .vanillaMaterial(Material.STONE)
                .skyblockItemId("COBBLESTONE")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(SkillType.MINING)
                .xpAmount(1.0)
                .validRegions(List.of(RegionType.COAL_MINE, RegionType.GOLD_MINE, RegionType.PRIVATE_ISLAND))
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

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.JUNGLE_LOG)
                .skyblockItemId("JUNGLE_LOG")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(SkillType.FORAGING)
                .xpAmount(6.0)
                .validRegions(List.of(RegionType.FOREST, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.ACACIA_LOG)
                .skyblockItemId("ACACIA_LOG")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(SkillType.FORAGING)
                .xpAmount(6.0)
                .validRegions(List.of(RegionType.FOREST, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.OAK_WOOD)
                .skyblockItemId("OAK_LOG")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(SkillType.FORAGING)
                .xpAmount(6.0)
                .validRegions(List.of(RegionType.FOREST, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.BIRCH_WOOD)
                .skyblockItemId("BIRCH_LOG")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(SkillType.FORAGING)
                .xpAmount(6.0)
                .validRegions(List.of(RegionType.FOREST, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.SPRUCE_WOOD)
                .skyblockItemId("SPRUCE_LOG")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(SkillType.FORAGING)
                .xpAmount(6.0)
                .validRegions(List.of(RegionType.FOREST, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.DARK_OAK_WOOD)
                .skyblockItemId("DARK_OAK_LOG")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(SkillType.FORAGING)
                .xpAmount(6.0)
                .validRegions(List.of(RegionType.FOREST, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.JUNGLE_WOOD)
                .skyblockItemId("JUNGLE_LOG")
                .baseDropAmount(1)
                .fortuneApplicable(true)
                .skillType(SkillType.FORAGING)
                .xpAmount(6.0)
                .validRegions(List.of(RegionType.FOREST, RegionType.PRIVATE_ISLAND))
                .build());

        register(SkyblockBlock.builder()
                .vanillaMaterial(Material.ACACIA_WOOD)
                .skyblockItemId("ACACIA_LOG")
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
        boolean hasSilkTouch = false;
        ItemStack handItem = player.getItemInMainHand();
        var enchants = handItem.get(DataComponents.ENCHANTMENTS);
        if (enchants != null && enchants.enchantments().containsKey(Enchantment.SILK_TOUCH)) {
            hasSilkTouch = true;
        }

        SkyblockBlock sbBlock = getBlock(Material.fromKey(brokenBlock.key()), regionType);

        if (sbBlock == null) {
            if (regionType == RegionType.PRIVATE_ISLAND) {
                try {
                    Material material = Material.fromKey(brokenBlock.key());
                    if (material != null && material != Material.AIR) {
                        Material dropMaterial = material;
                        boolean shouldDrop = true;
                        boolean isShearable = material == Material.SHORT_GRASS || material == Material.TALL_GRASS || material == Material.FERN || material == Material.LARGE_FERN || material.name().toUpperCase().endsWith("_LEAVES") || material == Material.DEAD_BUSH;

                        boolean hasShears = handItem.material() == Material.SHEARS;

                        if (isShearable) {
                            if (hasShears || hasSilkTouch) {
                                dropMaterial = material;
                            } else {
                                shouldDrop = false;

                                // Dead bush drops 0-2 sticks
                                if (material == Material.DEAD_BUSH) {
                                    int sticksCount = ThreadLocalRandom.current().nextInt(3);
                                    if (sticksCount > 0) {
                                        player.getInventory().addItemStack(ItemRegistry.createSkyblockOrVanillaStack(Material.STICK, sticksCount));
                                    }
                                }
                                // Leaves drop saplings (5% chance) or apples (0.5% chance for oak leaves)
                                else if (material.name().toUpperCase().endsWith("_LEAVES")) {
                                    double rand = ThreadLocalRandom.current().nextDouble();
                                    if (rand < 0.05) {
                                        String saplingName = material.name().toUpperCase().replace("_LEAVES", "_SAPLING");
                                        Material saplingMaterial = Material.fromKey(saplingName.toLowerCase());
                                        if (saplingMaterial != null) {
                                            player.getInventory().addItemStack(ItemRegistry.createSkyblockOrVanillaStack(saplingMaterial, 1));
                                        }
                                    } else if (material == Material.OAK_LEAVES && rand < 0.055) {
                                        player.getInventory().addItemStack(ItemRegistry.createSkyblockOrVanillaStack(Material.APPLE, 1));
                                    }
                                }
                                // Short grass, tall grass, ferns drop wheat seeds sometimes (10% chance)
                                else if (material == Material.SHORT_GRASS || material == Material.TALL_GRASS || material == Material.FERN || material == Material.LARGE_FERN) {
                                    if (ThreadLocalRandom.current().nextDouble() < 0.10) {
                                        player.getInventory().addItemStack(ItemRegistry.createSkyblockOrVanillaStack(Material.WHEAT_SEEDS, 1));
                                    }
                                }
                            }
                        } else if (!hasSilkTouch) {
                            if (material == Material.STONE) dropMaterial = Material.COBBLESTONE;
                            else if (material == Material.GRASS_BLOCK) dropMaterial = Material.DIRT;
                            else if (material == Material.COAL_ORE) dropMaterial = Material.COAL;
                            else if (material == Material.DIAMOND_ORE) dropMaterial = Material.DIAMOND;
                            else if (material == Material.EMERALD_ORE) dropMaterial = Material.EMERALD;
                            else if (material == Material.LAPIS_ORE) dropMaterial = Material.LAPIS_LAZULI;
                            else if (material == Material.REDSTONE_ORE) dropMaterial = Material.REDSTONE;
                            else if (material == Material.NETHER_QUARTZ_ORE) dropMaterial = Material.QUARTZ;
                        }

                        if (shouldDrop) {
                            player.getInventory().addItemStack(ItemRegistry.createSkyblockOrVanillaStack(dropMaterial, 1));
                        }
                    }
                    instance.setBlock(pos, Block.AIR);
                } catch (Exception e) {
                    System.err.println("[BlockManager] Error breaking block " + brokenBlock.key() + " at " + pos);
                    instance.setBlock(pos, Block.AIR);
                }
            }
            return;
        }

        // Calculate drop amount with fortune
        int dropAmount = sbBlock.baseDropAmount;
        if (sbBlock.fortuneApplicable && player.getActiveProfileData() != null) {
            double fortune = 0;
            try {
                switch (sbBlock.skillType) {
                    case MINING -> fortune = player.playerStat(Stats.MINING_FORTUNE);
                    case FARMING -> fortune = player.playerStat(Stats.FARMING_FORTUNE);
                    case FORAGING -> fortune = player.playerStat(Stats.FORAGING_FORTUNE);
                    default -> throw new IllegalArgumentException("Unexpected value: " + sbBlock.skillType);
                }
            } catch (NullPointerException ignored) {
                // Keep base fortune if stat lookup fails
            }

            double multiplier = 1.0 + (fortune / 100.0);
            dropAmount = (int) Math.floor(sbBlock.baseDropAmount * multiplier);

            double chanceForExtra = (sbBlock.baseDropAmount * multiplier) - dropAmount;
            if (ThreadLocalRandom.current().nextDouble() < chanceForExtra) {
                dropAmount++;
            }
        }

        // Check if we should override with silk touch
        boolean isSilkTouchable = (sbBlock.vanillaMaterial == Material.COAL_ORE ||
                                 sbBlock.vanillaMaterial == Material.IRON_ORE ||
                                 sbBlock.vanillaMaterial == Material.GOLD_ORE ||
                                 sbBlock.vanillaMaterial == Material.DIAMOND_ORE ||
                                 sbBlock.vanillaMaterial == Material.EMERALD_ORE ||
                                 sbBlock.vanillaMaterial == Material.LAPIS_ORE ||
                                 sbBlock.vanillaMaterial == Material.REDSTONE_ORE ||
                                 sbBlock.vanillaMaterial == Material.NETHER_QUARTZ_ORE ||
                                 sbBlock.vanillaMaterial == Material.STONE ||
                                 sbBlock.vanillaMaterial == Material.GRASS_BLOCK);

        boolean usedSilkTouchOnOre = hasSilkTouch && isSilkTouchable;

        ItemStack dropStack;
        if (usedSilkTouchOnOre) {
            dropStack = ItemRegistry.createSkyblockOrVanillaStack(sbBlock.vanillaMaterial, 1);
        } else {
            SkyblockItem skyItem = sbBlock.skyblockItemId != null ?
                ItemRegistry.getItem(sbBlock.skyblockItemId) : null;

            if (skyItem != null) {
                dropStack = skyItem.buildItemStack(player).withAmount(dropAmount);
            } else {
                dropStack = ItemRegistry.createSkyblockOrVanillaStack(sbBlock.vanillaMaterial, dropAmount);
            }
        }

        if (dropAmount > 0 || usedSilkTouchOnOre) {
            var profileData = player.getActiveProfileData();
            boolean teleportsToInventory = profileData != null && profileData.level != null && profileData.level.curLevel >= 6;

            if (teleportsToInventory) {
                player.getInventory().addItemStack(dropStack);
            } else {
                Pos playerPos = player.getPosition();

                Pos[] offsets = {
                        new Pos(1, 0, 0), new Pos(-1, 0, 0),
                        new Pos(0, 1, 0), new Pos(0, -1, 0),
                        new Pos(0, 0, 1), new Pos(0, 0, -1)
                };

                Pos nearestAirBlock = null;
                double closestDistanceSquared = Double.MAX_VALUE;

                for (Pos offset : offsets) {
                    Pos adjacentPos = pos.add(offset);
                    Block adjacentBlock = instance.getBlock(adjacentPos);

                    if (adjacentBlock.isAir()) {
                        double distanceSquared = adjacentPos.distanceSquared(playerPos);
                        if (distanceSquared < closestDistanceSquared) {
                            closestDistanceSquared = distanceSquared;
                            nearestAirBlock = adjacentPos;
                        }
                    }
                }

                // Use nearest air block or fallback to above-block position
                Pos dropPos = (nearestAirBlock != null)
                        ? nearestAirBlock.add(0.5, 0.5, 0.5)
                        : pos.add(0.5, 1.5, 0.5);

                // Spawn per-player visible dropped item entity
                DroppedItemEntity droppedItem = new DroppedItemEntity(dropStack, player);
                Instance inst = player.getInstance() != null ? player.getInstance() : instance;
                if (inst != null) {
                    droppedItem.setInstance(inst, dropPos);
                }
            }
        }

        boolean playerPlaced = brokenBlock.hasTag(Tag.Boolean("player_placed")) && brokenBlock.getTag(Tag.Boolean("player_placed"));

        // Grant skill XP (not granted if placed by player OR if silk touch was used on an ore)
        if (!playerPlaced && !usedSilkTouchOnOre && sbBlock.skillType != null && sbBlock.xpAmount > 0) {
            SkillRegistry.grantXp(player, sbBlock.skillType, sbBlock.xpAmount);
        }

        // Update collection data
        if (!playerPlaced && !usedSilkTouchOnOre && player.getActiveProfile() != null) {
            String collectionId = CollectionAddEvent.getCollectionId(sbBlock.vanillaMaterial, sbBlock.skyblockItemId);
            if (collectionId != null) {
                player.getActiveProfile().updateCollection(collectionId, dropAmount);
            }
        }

        // Handle block respawn or removal
        if (sbBlock.respawnDelayTicks > 0 && sbBlock.replacementBlock != null) {
            instance.setBlock(pos, sbBlock.replacementBlock);

            MinecraftServer.getSchedulerManager().buildTask(() -> instance.setBlock(pos, brokenBlock)).delay(TaskSchedule.tick(sbBlock.respawnDelayTicks)).schedule();
        } else {
            instance.setBlock(pos, Block.AIR);
        }
    }
}

