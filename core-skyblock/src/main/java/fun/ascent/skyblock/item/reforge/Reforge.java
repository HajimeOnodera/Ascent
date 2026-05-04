package fun.ascent.skyblock.item.reforge;

import fun.ascent.skyblock.item.ItemType;
import fun.ascent.skyblock.item.Rarity;
import fun.ascent.skyblock.item.reforge.types.ArmorReforge;
import fun.ascent.skyblock.item.reforge.types.BowReforge;
import fun.ascent.skyblock.item.reforge.types.SwordReforge;
import fun.ascent.skyblock.player.stats.Stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Reforge {

    String getName();
    String getId();
    List<String> getLoreText();

    double getStat(Stats stat, Rarity rarity);
    Map<Stats, RarityStat> getStats();
    ItemType[] getAllowedTypes();
    boolean isReforgeStone();
    boolean isItemSpecific();

    default String getDuplicatePrefix() { return null; }

    default boolean canApplyTo(ItemType type) {
        for (ItemType allowed : getAllowedTypes()) {
            if (allowed == type) return true;
        }
        return false;
    }

    static int getReforgeCost(Rarity rarity) {
        return switch (rarity) {
            case COMMON -> 250;
            case UNCOMMON -> 500;
            case RARE -> 1000;
            case EPIC -> 2500;
            case LEGENDARY -> 5000;
            case MYTHIC, DIVINE -> 10000;
            case SPECIAL -> 25000;
            case VERY_SPECIAL -> 50000;
            case ULTIMATE, ADMIN -> 100000;
        };
    }

    Map<String, List<Reforge>> REFORGES = new HashMap<>();

    static void init() {
        for (Reforge r : SwordReforge.values()) register(r);
        for (Reforge r : BowReforge.values()) register(r);
        for (Reforge r : ArmorReforge.values()) register(r);
    }

    private static void register(Reforge r) {
        REFORGES.computeIfAbsent(r.getId(), _ -> new ArrayList<>()).add(r);
    }

    static Reforge getById(String id, ItemType type) {
        List<Reforge> list = REFORGES.get(id);
        if (list == null) return null;
        for (Reforge r : list) {
            if (r.canApplyTo(type)) return r;
        }
        return list.getFirst();
    }

    static Reforge getById(String id) {
        List<Reforge> list = REFORGES.get(id);
        return (list == null || list.isEmpty()) ? null : list.getFirst();
    }

    RarityStat LINEAR_ONE = new RarityStat(1, 2, 3, 4, 5, 6);
    RarityStat LINEAR_TWO = new RarityStat(2, 4, 6, 8, 10, 12);
}
