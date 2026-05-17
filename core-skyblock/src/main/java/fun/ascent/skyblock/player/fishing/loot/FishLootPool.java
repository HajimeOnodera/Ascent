package fun.ascent.skyblock.player.fishing.loot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class FishLootPool {

    protected final List<FishLootEntry> entries = new ArrayList<>();
    private int totalWeight = 0;

    protected void register(FishLootEntry entry) {
        entries.add(entry);
        totalWeight += entry.weight();
    }

    public FishLootEntry roll() {
        if (entries.isEmpty()) return null;
        int roll = ThreadLocalRandom.current().nextInt(totalWeight);
        int cursor = 0;
        for (FishLootEntry entry : entries) {
            cursor += entry.weight();
            if (roll < cursor) {
                return entry;
            }
        }
        return entries.get(entries.size() - 1);
    }
}
