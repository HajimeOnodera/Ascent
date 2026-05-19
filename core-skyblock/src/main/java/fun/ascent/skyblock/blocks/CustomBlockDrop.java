package fun.ascent.skyblock.blocks;

import fun.ascent.skyblock.world.region.RegionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public record CustomBlockDrop(List<DropRule> rules) {

    private static final Random RANDOM = new Random();

    public List<DropResult> simulateDrop(String brokenBlockId, String toolId,
                                         boolean hasSilkTouch, RegionType regionType,
                                         boolean isOnIsland) {
        List<DropResult> drops = new ArrayList<>();

        // Find the first matching rule
        for (DropRule rule : rules) {
            if (rule.matches(toolId, hasSilkTouch, regionType, isOnIsland)) {
                if (rule.drops.isEmpty()) {
                    return drops; // No drops for this rule
                }

                for (Drop drop : rule.drops) {
                    if (RANDOM.nextDouble() <= drop.chance) {
                        int amount = calculateAmount(drop.minAmount, drop.maxAmount);
                        if (amount > 0) {
                            drops.add(new DropResult(drop.skyblockItemId, amount));
                        }
                    }
                }
                return drops;
            }
        }

        if (brokenBlockId != null) {
            drops.add(new DropResult(brokenBlockId, 1));
        }
        return drops;
    }

    private int calculateAmount(int min, int max) {
        if (min == max) return min;
        return RANDOM.nextInt(max - min + 1) + min;
    }

    // ─── Records ─────────────────────────────────────────────────────

    public record DropRule(
            DropConditions conditions,
            List<Drop> drops
    ) {
        public boolean matches(String toolId, boolean hasSilkTouch,
                               RegionType regionType, boolean isOnIsland) {
            if (conditions == null) return true;
            return conditions.matches(toolId, hasSilkTouch, regionType, isOnIsland);
        }
    }

    public record DropConditions(
            Boolean silkTouch,
            String requiredToolId,
            String excludedToolId,
            Boolean islandOnly,
            RegionType requiredRegion,
            RegionType excludedRegion
    ) {
        public boolean matches(String toolId, boolean hasSilkTouch,
                               RegionType regionType, boolean isOnIsland) {
            // Silk touch condition
            if (silkTouch != null && silkTouch != hasSilkTouch) {
                return false;
            }

            // Required tool condition
            if (requiredToolId != null) {
                if (toolId == null || !toolId.equals(requiredToolId)) {
                    return false;
                }
            }

            // Excluded tool condition
            if (toolId != null && toolId.equals(excludedToolId)) {
                return false;
            }

            // Island-only condition
            if (islandOnly != null && islandOnly != isOnIsland) {
                return false;
            }

            // Required region condition
            if (requiredRegion != null && regionType != requiredRegion) {
                return false;
            }

            // Excluded region condition
            return excludedRegion == null || regionType != excludedRegion;
        }
    }

    public record Drop(
            String skyblockItemId,
            double chance,
            int minAmount,
            int maxAmount
    ) {
    }

    public record DropResult(
            String skyblockItemId,
            int amount
    ) {
    }

    // ─── Builder ─────────────────────────────────────────────────────

    public static class DropBuilder {
        private final List<DropRule> rules = new ArrayList<>();

        public DropBuilder addRule(DropConditions conditions, List<Drop> drops) {
            rules.add(new DropRule(conditions, drops));
            return this;
        }

        public DropBuilder addSimpleRule(String dropItemId, double chance, int amount) {
            return addSimpleRule(dropItemId, chance, amount, amount);
        }

        public DropBuilder addSimpleRule(String dropItemId, double chance, int minAmount, int maxAmount) {
            DropConditions conditions = noConditions();
            List<Drop> drops = List.of(new Drop(dropItemId, chance, minAmount, maxAmount));
            return addRule(conditions, drops);
        }

        public CustomBlockDrop build() {
            return new CustomBlockDrop(rules);
        }
    }

    // ─── Static Factory Methods ──────────────────────────────────────

    public static DropBuilder builder() {
        return new DropBuilder();
    }

    public static DropConditions noConditions() {
        return new DropConditions(null, null, null, null, null, null);
    }

    public static DropConditions silkTouch(boolean required) {
        return new DropConditions(required, null, null, null, null, null);
    }

    public static DropConditions requiredTool(String toolId) {
        return new DropConditions(null, toolId, null, null, null, null);
    }

    public static DropConditions islandOnly() {
        return new DropConditions(null, null, null, true, null, null);
    }

    public static DropConditions region(RegionType region) {
        return new DropConditions(null, null, null, null, region, null);
    }

    public static DropConditions notRegion(RegionType excludedRegion) {
        return new DropConditions(null, null, null, null, null, excludedRegion);
    }

    public static Drop drop(String itemId, double chance, int amount) {
        return new Drop(itemId, chance, amount, amount);
    }

    public static Drop drop(String itemId, double chance, int minAmount, int maxAmount) {
        return new Drop(itemId, chance, minAmount, maxAmount);
    }
}
