package fun.ascent.skyblock.player.stats;

import fun.ascent.skyblock.utility.ColorUtils;
import net.kyori.adventure.text.format.NamedTextColor;

public class StatBuilder {

    public static Stat build(Stats base){
        return build(base,-1,-1);
    }
    public static Stat build(Stats base, int curValue, int maxValue) {
        NamedTextColor color = ColorUtils.getNamedTextColor(base.getStatColor());
        String colorName = color.toString();
        String name = "<" + colorName + ">" + base.getStatFormattedDisplay() + "</" + colorName + ">";
        String rawSymbol = base.getStatSymbol().replace(base.getStatColor(), "");
        String symbol = "<" + colorName + ">" + rawSymbol + "</" + colorName + ">";

        String suffix = base.getStatIntType() ? "%" : "";

        Stat stat = new Stat(
                base.name().toLowerCase(),
                name,
                symbol,
                base.getStatCategory(),
                color,
                suffix,
                base.getBaseStat(),
                base.getStatCap()
        );

        if(maxValue > 0) {
            stat.setMaxValue(maxValue);
        }
        if(curValue > 0) {
            stat.setCurValue(curValue);
        }
        return stat;
    }


}