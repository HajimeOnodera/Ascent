package fun.ascent.skyblock.entity.mob.impl;

import lombok.Builder;

import java.util.List;

public interface ZoneSpawner {

    List<SpawnZone> spawnZones();

    @Builder
    record SpawnZone(String zoneId, int targetCount) {}
}