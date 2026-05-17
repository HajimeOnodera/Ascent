package fun.ascent.skyblock.player.fishing.bait;

public record BaitEffect(
        double speedModifier,
        double seaCreatureModifier,
        double treasureModifier,
        int biteTimeReduction
) {}
