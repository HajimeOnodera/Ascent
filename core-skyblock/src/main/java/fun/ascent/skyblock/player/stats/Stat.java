package fun.ascent.skyblock.player.stats;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Stat {

    public final String id;
    private final String suffix;
    private final Component parsedName;
    private final Component parsedSymbol;
    @Getter private final StatCategory category;
    private final String statColor;
    @Getter private final double baseStat;
    @Getter private final double statCap;
    @Getter private double curValue;
    @Getter private double maxValue;
    private final boolean isSingleValue;

    public Stat(String id,String name,String symbol,StatCategory category,TextColor color,String suffix,double baseStat,double statCap){
        this.id = id;
        this.suffix = suffix;
        this.parsedName = MiniMessage.miniMessage().deserialize(name);
        this.parsedSymbol = MiniMessage.miniMessage().deserialize(symbol);
        this.category = category;
        this.statColor = color.asHexString();
        this.baseStat = baseStat;
        this.statCap = statCap;
        this.curValue = baseStat;
        this.maxValue = baseStat;
        this.isSingleValue = true;
    }

    public Stat setCurValue(double newVal){
        this.curValue = Math.min(newVal,maxValue);
        return this;
    }

    public Stat setMaxValue(double newVal){
        this.maxValue = applyCap(newVal);
        return this;
    }

    public Stat addCurValue(double amount){
        return setCurValue(getCurValue() + amount);
    }
    public Component getText(int type){
        Component name = parsedName;
        Component symbol = parsedSymbol;
        Component joiner = Component.text(": ").color(NamedTextColor.WHITE);
        Component stat = Component.text(this.curValue).color(getStatColor());
        if(!isSingleValue){
            stat = stat.append(Component.text("/").color(NamedTextColor.WHITE).append(
                    Component.text(this.maxValue).color(getStatColor())));
        }
        Component suffix =  Component.text(this.suffix).color(getStatColor());
        return switch (type){
            case 0 -> symbol.append(name).append(joiner).append(stat).append(suffix);
            case 1 -> symbol.append(name);
            case 2 -> name.append(joiner).append(stat).append(suffix);
            case 3 -> symbol.append(joiner).append(stat).append(suffix);
            case 4 -> stat.append(suffix);
            default -> symbol.append(name);
        };
    }

    public TextColor getStatColor() {
        return TextColor.fromHexString(statColor);
    }

    public double applyCap(double value) {
        return statCap > 0 ? Math.min(value, statCap) : value;
    }
}
