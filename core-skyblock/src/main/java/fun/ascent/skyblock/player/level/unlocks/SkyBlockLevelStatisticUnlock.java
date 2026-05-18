package fun.ascent.skyblock.player.level.unlocks;

import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.skyblock.player.stats.Stats;
import fun.ascent.skyblock.player.level.SkyBlockLevelUnlock;
import fun.ascent.skyblock.player.SkyblockPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class SkyBlockLevelStatisticUnlock extends SkyBlockLevelUnlock {
    private final Map<Stats, Double> statistics;

    public SkyBlockLevelStatisticUnlock(Map<Stats, Double> statistics) {
        this.statistics = statistics != null ? statistics : new HashMap<>();
    }

    @Override
    public UnlockType type() {
        return UnlockType.STATISTIC;
    }

    @Override
    public ItemStack.Builder getItemDisplay(SkyblockPlayer player, int level) {
        List<String> statisticsDisplay = new ArrayList<>();
        statistics.forEach((key, value) -> {
            if (value > 0) {
                statisticsDisplay.add("§8 +§a" + value.intValue() + " " + key.getStatSymbol() + " " + key.getStatFormattedDisplay());
            }
        });

        if (statisticsDisplay.isEmpty()) {
            statisticsDisplay.add("§8No statistics unlocked");
        }

        List<String> lore = new ArrayList<>();
        for (int i = 1; i < statisticsDisplay.size(); i++) {
            lore.add(statisticsDisplay.get(i));
        }
        lore.add("§8Level " + level);

        return ItemStackCreator.getStack(statisticsDisplay.getFirst(), Material.APPLE, 1, lore);
    }

    @Override
    public List<String> getDisplay(SkyblockPlayer player, int level) {
        ArrayList<String> lore = new ArrayList<>();
        statistics.forEach((key, value) -> {
            if (value > 0) {
                lore.add("§8 +§a" + value.intValue() + " " + key.getStatSymbol() + " " + key.getStatFormattedDisplay());
            }
        });
        return lore;
    }
}
