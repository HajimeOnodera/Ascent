package fun.ascent.skyblock.bazaar.vars;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@AllArgsConstructor
public enum BazaarCategory {

    FARMING("<gray>Bazaar ➜ Farming",null),
    MINING("<gray>Bazaar ➜ Mining",null),
    COMBAT("<gray>Bazaar ➜ Combat",null),
    FISHING_WOOD("<gray>Bazaar ➜ Woods & Fishes",null),
    ODDITIES("<gray>Bazaar ➜ Oddities",null),
    ;
    public final String titleMiniMessage;
    public final BazaarCategory parent;

    public Component getTitle(){
        return MiniMessage.miniMessage().deserialize(titleMiniMessage);
    }
}
