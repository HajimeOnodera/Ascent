package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.events.definitions.InventoryItemAddEvent;
import fun.ascent.skyblock.player.collections.CollectionRegistry;
import fun.ascent.skyblock.player.profiles.SkyblockProfile;
import net.minestom.server.item.Material;

public class CollectionAddEvent extends SEvent<InventoryItemAddEvent> {

    @Override
    public void onEvent(InventoryItemAddEvent event) {
        if (event.getAmount() < 0) return;
        SkyblockProfile profile = event.getSkyblockPlayer().getActiveProfile();
        if (profile == null) return;

        String rawId = event.getSkyblockItem() != null ? event.getSkyblockItem().getItemId() : null;
        String collectionId = getCollectionId(event.getMaterial(), rawId);

        if (collectionId != null && CollectionRegistry.get(collectionId) != null) {
            profile.updateCollection(collectionId, event.getAmount());
        }
    }

    public static String getCollectionId(Material material, String itemId) {
        if (itemId != null) {
            String upperId = itemId.toUpperCase();
            if (CollectionRegistry.get(upperId) != null) {
                return upperId;
            }
            if (CollectionRegistry.get(itemId) != null) {
                return itemId;
            }
            if (CollectionRegistry.get(itemId.toLowerCase()) != null) {
                return itemId.toLowerCase();
            }
        }

        if (material == null || material == Material.AIR) return null;

        String matName = material.name().toUpperCase().replace("MINECRAFT:", "");
        return switch (matName) {
            // Farming
            case "WHEAT" -> "WHEAT";
            case "WHEAT_SEEDS" -> "SEEDS";
            case "CARROT", "CARROTS", "CARROT_ITEM" -> "CARROT";
            case "POTATO", "POTATOES", "POTATO_ITEM" -> "POTATO";
            case "PUMPKIN" -> "PUMPKIN";
            case "MELON", "MELON_SLICE" -> "MELON_SLICE";
            case "CACTUS" -> "CACTUS";
            case "COCOA", "COCOA_BEANS" -> "COCOA_BEANS";
            case "SUGAR_CANE" -> "SUGAR_CANE";
            case "FEATHER" -> "FEATHER";
            case "LEATHER" -> "LEATHER";
            case "MUTTON" -> "MUTTON";
            case "PORKCHOP" -> "PORKCHOP";
            case "RABBIT" -> "RABBIT";
            case "CHICKEN" -> "CHICKEN";
            case "RED_MUSHROOM", "BROWN_MUSHROOM" -> "RED_MUSHROOM";
            case "NETHER_WART" -> "NETHER_WART";

            // Mining
            case "COBBLESTONE" -> "COBBLESTONE";
            case "COAL" -> "COAL";
            case "GOLD_INGOT" -> "GOLD_INGOT";
            case "LAPIS_LAZULI" -> "LAPIS_LAZULI";
            case "GRAVEL", "FLINT" -> "GRAVEL";
            case "DIAMOND" -> "DIAMOND";
            case "EMERALD" -> "EMERALD";
            case "END_STONE" -> "END_STONE";
            case "GLOWSTONE", "GLOWSTONE_DUST" -> "GLOWSTONE_DUST";
            case "STONE" -> "HARD_STONE";
            case "ICE" -> "ICE";
            case "IRON_INGOT" -> "IRON_INGOT";
            case "NETHERRACK" -> "NETHERRACK";
            case "OBSIDIAN" -> "OBSIDIAN";
            case "REDSTONE" -> "REDSTONE";
            case "SAND" -> "SAND";

            // Combat
            case "BLAZE_ROD" -> "BLAZE_ROD";
            case "STRING" -> "STRING";
            case "ENDER_PEARL" -> "ENDER_PEARL";
            case "BONE" -> "BONE";
            case "GHAST_TEAR" -> "GHAST_TEAR";
            case "GUNPOWDER" -> "GUNPOWDER";
            case "MAGMA_CREAM" -> "MAGMA_CREAM";
            case "ROTTEN_FLESH" -> "ROTTEN_FLESH";
            case "SLIME_BALL" -> "SLIME_BALL";
            case "SPIDER_EYE" -> "SPIDER_EYE";

            // Foraging
            case "OAK_LOG" -> "OAK_LOG";
            case "SPRUCE_LOG" -> "SPRUCE_LOG";
            case "BIRCH_LOG" -> "BIRCH_LOG";
            case "JUNGLE_LOG" -> "JUNGLE_LOG";
            case "ACACIA_LOG" -> "ACACIA_LOG";
            case "DARK_OAK_LOG" -> "DARK_OAK_LOG";

            // Fishing
            case "CLAY", "CLAY_BALL" -> "CLAY_BALL";
            case "COD", "RAW_FISH" -> "RAW_FISH";
            case "SALMON" -> "RAW_SALMON";
            case "PUFFERFISH" -> "PUFFERFISH";
            case "TROPICAL_FISH" -> "TROPICAL_FISH";
            case "INK_SAC" -> "INK_SAC";
            case "LILY_PAD" -> "LILY_PAD";
            case "PRISMARINE_CRYSTALS" -> "PRISMARINE_CRYSTALS";
            case "PRISMARINE_SHARD" -> "PRISMARINE_SHARD";
            case "SPONGE" -> "SPONGE";

            default -> {
                if (CollectionRegistry.get(matName) != null) {
                    yield matName;
                }
                yield null;
            }
        };
    }
}

