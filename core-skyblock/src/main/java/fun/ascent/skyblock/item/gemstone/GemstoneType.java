package fun.ascent.skyblock.item.gemstone;


import fun.ascent.skyblock.player.stats.Stats;

public enum GemstoneType {
    RUBY(Stats.HEALTH, "§c", "❤"),
    AMBER(Stats.MINING_SPEED, "§6", "⸕"),
    SAPPHIRE(Stats.INTELLIGENCE, "§b", "✎"),
    JADE(Stats.MINING_FORTUNE, "§a", "☘"),
    AMETHYST(Stats.DEFENSE, "§5", "❈"),
    TOPAZ(Stats.PRISTINE, "§e", "✧"),
    JASPER(Stats.STRENGTH, "§d", "❁"),
    OPAL(Stats.TRUE_DEFENSE, "§f", "❂"),
    AQUAMARINE(Stats.SEA_CREATURE_CHANCE, "§3", "α"),
    CITRINE(Stats.FORAGING_FORTUNE, "§4", "☘"),
    ONYX(Stats.CRITICAL_DAMAGE, "§9", "☠"),
    PERIDOT(Stats.FARMING_FORTUNE, "§2", "☘");

    private final Stats stat;
    private final String colorCode;
    private final String symbol;

    GemstoneType(Stats stat, String colorCode, String symbol) {
        this.stat = stat;
        this.colorCode = colorCode;
        this.symbol = symbol;
    }

    public Stats getStat() { return stat; }
    public String getColorCode() { return colorCode; }
    public String getSymbol() { return symbol; }
}
