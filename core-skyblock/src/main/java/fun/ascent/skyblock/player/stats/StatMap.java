package fun.ascent.skyblock.player.stats;

import java.util.Map;

public class StatMap {

    private final double[] base;
    private final double[] additive;

    public StatMap(double[] base, double[] additive) {
        this.base = base;
        this.additive = additive;
    }

    public StatMap() {
        this(new double[Stats.SIZE], new double[Stats.SIZE]);
    }

    public double getBase(Stats stat) {
        return base[stat.ordinal()];
    }

    public void setBase(Stats stat, double value) {
        base[stat.ordinal()] = value;
    }

    public void addBase(Stats stat, double value) {
        base[stat.ordinal()] += value;
    }

    public double getAdditive(Stats stat) {
        return additive[stat.ordinal()];
    }

    public void setAdditive(Stats stat, double value) {
        additive[stat.ordinal()] = value;
    }

    public void addAdditive(Stats stat, double value) {
        additive[stat.ordinal()] += value;
    }

    public void clearAdditive() {
        java.util.Arrays.fill(additive, 0);
    }

    public double get(Stats stat) {
        return stat.applyCap(base[stat.ordinal()] + additive[stat.ordinal()]);
    }

    public void applyItemStats(Map<Stats, Double> itemStats) {
        for (Map.Entry<Stats, Double> entry : itemStats.entrySet()) {
            additive[entry.getKey().ordinal()] += entry.getValue();
        }
    }

    public void initDefaults() {
        for (Stats stat : Stats.values()) {
            double baseStat = stat.getBaseStat();
            if (baseStat != 0 && base[stat.ordinal()] == 0) {
                base[stat.ordinal()] = baseStat;
            }
        }
    }
}
