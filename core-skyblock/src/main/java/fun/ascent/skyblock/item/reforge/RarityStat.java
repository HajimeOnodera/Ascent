package fun.ascent.skyblock.item.reforge;

import fun.ascent.skyblock.item.Rarity;

public class RarityStat {

    private final double common;
    private final double uncommon;
    private final double rare;
    private final double epic;
    private final double legendary;
    private final double mythic;

    public RarityStat(double common, double uncommon, double rare, double epic, double legendary, double mythic) {
        this.common = common;
        this.uncommon = uncommon;
        this.rare = rare;
        this.epic = epic;
        this.legendary = legendary;
        this.mythic = mythic;
    }

    public RarityStat(double allRarities) {
        this(allRarities, allRarities, allRarities, allRarities, allRarities, allRarities);
    }

    public double fromRarity(Rarity rarity) {
        return switch (rarity) {
            case COMMON -> common;
            case UNCOMMON -> uncommon;
            case RARE -> rare;
            case EPIC -> epic;
            case LEGENDARY -> legendary;
            case MYTHIC, DIVINE, SPECIAL, VERY_SPECIAL, ULTIMATE, ADMIN -> mythic;
        };
    }

    public double getCommon() { return common; }
    public double getUncommon() { return uncommon; }
    public double getRare() { return rare; }
    public double getEpic() { return epic; }
    public double getLegendary() { return legendary; }
    public double getMythic() { return mythic; }
}
