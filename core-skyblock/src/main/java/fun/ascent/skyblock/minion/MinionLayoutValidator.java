package fun.ascent.skyblock.minion;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;

import java.util.List;

public final class MinionLayoutValidator {
    private MinionLayoutValidator() {
    }

    public static ValidationResult validate(SkyblockMinion minion) {
        MinionType type = minion.getType();
        return switch (type.getActionKind()) {
            case MINING -> validateMining(minion, type);
            case CROP -> validateCrop(minion, type);
            case TREE -> validateTree(minion, type);
            case MOB -> validateMob(minion);
            case FISHING -> validateFishing(minion);
        };
    }

    private static ValidationResult validateMining(SkyblockMinion minion, MinionType type) {
        for (Pos pos : minion.getMiningPositions()) {
            Block block = minion.getInstance().getBlock(pos);
            Block above = minion.getInstance().getBlock(pos.blockX(), pos.blockY() + 1, pos.blockZ());
            if (!block.isAir() && block != type.getGeneratedBlock()) {
                return ValidationResult.failure("This location is not perfect!");
            }
            if (!above.isAir()) {
                return ValidationResult.failure("This location is not perfect!");
            }
        }
        return ValidationResult.success();
    }

    private static ValidationResult validateCrop(SkyblockMinion minion, MinionType type) {
        for (Pos pos : minion.getCropPositions()) {
            Block block = minion.getInstance().getBlock(pos);
            Block below = minion.getInstance().getBlock(pos.blockX(), pos.blockY() - 1, pos.blockZ());
            if (!isValidCropBase(type, minion, pos, below)) {
                return ValidationResult.failure("This location is not perfect!");
            }
            if (!block.isAir() && block != type.getGeneratedBlock()) {
                return ValidationResult.failure("This location is not perfect!");
            }
        }
        return ValidationResult.success();
    }

    private static ValidationResult validateTree(SkyblockMinion minion, MinionType type) {
        for (Pos pos : minion.getTreePositions()) {
            Block base = minion.getInstance().getBlock(pos);
            if (base != Block.DIRT && base != Block.GRASS_BLOCK) {
                return ValidationResult.failure("This location is not perfect!");
            }
            for (int y = 1; y <= 4; y++) {
                Block block = minion.getInstance().getBlock(pos.blockX(), pos.blockY() + y, pos.blockZ());
                if (!block.isAir() && block != type.getGeneratedBlock() && block != type.getSecondaryGeneratedBlock()) {
                    return ValidationResult.failure("This location is not perfect!");
                }
            }
        }
        return ValidationResult.success();
    }

    private static ValidationResult validateMob(SkyblockMinion minion) {
        List<Pos> positions = minion.getCropPositions();
        boolean foundSpace = false;
        for (Pos pos : positions) {
            Block feet = minion.getInstance().getBlock(pos);
            Block head = minion.getInstance().getBlock(pos.blockX(), pos.blockY() + 1, pos.blockZ());
            if (feet.isAir() && head.isAir()) {
                foundSpace = true;
                continue;
            }
            return ValidationResult.failure("This location is not perfect!");
        }
        return foundSpace ? ValidationResult.success() : ValidationResult.failure("I need space to generate mobs");
    }

    private static ValidationResult validateFishing(SkyblockMinion minion) {
        for (Pos pos : minion.getCropPositions()) {
            if (minion.getInstance().getBlock(pos) == Block.WATER) {
                return ValidationResult.success();
            }
        }
        return ValidationResult.failure("This location is not perfect!");
    }

    private static boolean isValidCropBase(MinionType type, SkyblockMinion minion, Pos pos, Block below) {
        if (type == MinionType.SUGARCANE) {
            return below == Block.SAND && hasAdjacentWater(minion, pos.blockX(), pos.blockY() - 1, pos.blockZ());
        }
        return below == type.getIdealLayoutBlock();
    }

    private static boolean hasAdjacentWater(SkyblockMinion minion, int x, int y, int z) {
        return minion.getInstance().getBlock(x + 1, y, z) == Block.WATER
                || minion.getInstance().getBlock(x - 1, y, z) == Block.WATER
                || minion.getInstance().getBlock(x, y, z + 1) == Block.WATER
                || minion.getInstance().getBlock(x, y, z - 1) == Block.WATER;
    }

    public record ValidationResult(boolean valid, String message) {
        public static ValidationResult success() {
            return new ValidationResult(true, null);
        }

        public static ValidationResult failure(String message) {
            return new ValidationResult(false, message);
        }
    }
}
