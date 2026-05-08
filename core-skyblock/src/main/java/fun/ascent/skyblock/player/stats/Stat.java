package fun.ascent.skyblock.player.stats;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import static fun.ascent.common.StringUtility.*;

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
        this.parsedName = text(name);
        this.parsedSymbol = text(symbol);
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

    public void setMaxValue(double newVal){
        this.maxValue = applyCap(newVal);
    }

    public Stat addCurValue(double amount){
        return setCurValue(getCurValue() + amount);
    }
    public Component getText(int type){
        Component name = parsedName;
        Component symbol = parsedSymbol;
        String color = getStatColor().asHexString();
        Component joiner = text("<white>: ");
        Component stat = text("<" + color + ">" + this.curValue);
        if(!isSingleValue){
            stat = stat.append(text("<white>/").append(
                    text("<" + color + ">" + this.maxValue)));
        }
        Component suffix =  text("<" + color + ">" + this.suffix);
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
