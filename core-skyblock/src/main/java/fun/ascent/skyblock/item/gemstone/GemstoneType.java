package fun.ascent.skyblock.item.gemstone;

import fun.ascent.skyblock.player.stats.Stats;
import lombok.Getter;

@Getter
public enum GemstoneType {
    RUBY(Stats.HEALTH, "<red>", "❤"),
    AMBER(Stats.MINING_SPEED, "<gold>", "⸕"),
    SAPPHIRE(Stats.INTELLIGENCE, "<aqua>", "✎"),
    JADE(Stats.MINING_FORTUNE, "<green>", "☘"),
    AMETHYST(Stats.DEFENSE, "<dark_purple>", "❈"),
    TOPAZ(Stats.PRISTINE, "<yellow>", "✧"),
    JASPER(Stats.STRENGTH, "<light_purple>", "❁"),
    OPAL(Stats.TRUE_DEFENSE, "<white>", "❂"),
    AQUAMARINE(Stats.SEA_CREATURE_CHANCE, "<dark_aqua>", "α"),
    CITRINE(Stats.FORAGING_FORTUNE, "<dark_red>", "☘"),
    ONYX(Stats.CRITICAL_DAMAGE, "<blue>", "☠"),
    PERIDOT(Stats.FARMING_FORTUNE, "<dark_green>", "☘");

    private final Stats stat;
    private final String colorCode;
    private final String symbol;

    GemstoneType(Stats stat, String colorCode, String symbol) {
        this.stat = stat;
        this.colorCode = colorCode;
        this.symbol = symbol;
    }

}

