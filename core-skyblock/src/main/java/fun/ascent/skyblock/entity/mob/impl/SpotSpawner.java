package fun.ascent.skyblock.entity.mob.impl;

import lombok.Builder;

public interface SpotSpawner {

    SpotConfig spotConfig();

    @Builder
    record SpotConfig(
            String collectionName,
            int spawnDelaySeconds,
            int maxPerSpot
    ) {}
}
