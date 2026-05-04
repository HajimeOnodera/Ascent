package fun.ascent.skyblock.item.reforge;

import fun.ascent.skyblock.item.Rarity;
import fun.ascent.skyblock.item.reforge.types.ArmorReforge;
import fun.ascent.skyblock.item.reforge.types.BowReforge;

public enum ReforgeStoneRegistry {
    PRECURSOR_GEAR(ArmorReforge.Ancient, new RarityStat(500, 750, 1000, 2000, 3500, 5000)),
    OPTICAL_LENS(BowReforge.Precise,    new RarityStat(300, 500, 750, 1500, 2500, 4000));

    private final Reforge reforge;
    private final RarityStat cost;

    ReforgeStoneRegistry(Reforge reforge, RarityStat cost) {
        this.reforge = reforge;
        this.cost    = cost;
    }

    public Reforge getReforge()           { return reforge; }
    public int     getCost(Rarity rarity) { return (int) cost.fromRarity(rarity); }

    public static Reforge getReforgeForStone(String itemId) {
        for (ReforgeStoneRegistry entry : values()) {
            if (entry.name().equals(itemId)) return entry.reforge;
        }
        return null;
    }
}
