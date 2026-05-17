package fun.ascent.skyblock.player.fishing.loot;

import fun.ascent.skyblock.entity.mob.SkyblockMobEntity;
import fun.ascent.skyblock.player.fishing.loot.mobs.DeepRider;
import fun.ascent.skyblock.player.fishing.loot.mobs.PondSquid;
import fun.ascent.skyblock.player.fishing.loot.mobs.SeaGuardian;
import fun.ascent.skyblock.player.fishing.loot.mobs.SeaWitch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class SeaCreaturePool {

    private static final List<CreatureEntry> MOB_REGISTRY = new ArrayList<>();

    static {
        MOB_REGISTRY.add(new CreatureEntry(PondSquid.class, 1, 100));
        MOB_REGISTRY.add(new CreatureEntry(SeaGuardian.class, 10, 50));
        MOB_REGISTRY.add(new CreatureEntry(SeaWitch.class, 15, 25));
        MOB_REGISTRY.add(new CreatureEntry(DeepRider.class, 20, 10));
    }

    public static Class<? extends SkyblockMobEntity> roll(int fishingLevel) {
        List<CreatureEntry> matches = new ArrayList<>();
        int weightSum = 0;
        for (CreatureEntry entry : MOB_REGISTRY) {
            if (fishingLevel >= entry.requiredLevel()) {
                matches.add(entry);
                weightSum += entry.weight();
            }
        }
        if (matches.isEmpty()) {
            return PondSquid.class;
        }
        int roll = ThreadLocalRandom.current().nextInt(weightSum);
        int cursor = 0;
        for (CreatureEntry entry : matches) {
            cursor += entry.weight();
            if (roll < cursor) {
                return entry.mobClass();
            }
        }
        return matches.get(matches.size() - 1).mobClass();
    }

    private record CreatureEntry(
            Class<? extends SkyblockMobEntity> mobClass,
            int requiredLevel,
            int weight
    ) {}

    private SeaCreaturePool() {}
}
