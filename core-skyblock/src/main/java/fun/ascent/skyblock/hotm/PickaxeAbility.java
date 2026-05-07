package fun.ascent.skyblock.hotm;

import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public abstract class PickaxeAbility extends HotmUpgrade {

    public abstract int cooldownSeconds();
    public abstract void activate(SkyblockPlayer player);

    @Override public final int maxLevel() { return 1; }
    @Override public final Powder powder(int level) { return Powder.MITHRIL; }
    @Override public final int cost(int level) { return 0; }

    @Override
    public ItemStack buildItem(HotmTree tree) {
        int level = tree.getLevel(this);
        boolean selected = id().equals(tree.getActiveAbilityId());

        Material mat = level == 0 ? Material.COAL_BLOCK
            : selected ? Material.DIAMOND_BLOCK
            : Material.EMERALD_BLOCK;
        String nameColor = level == 0 ? "§c" : selected ? "§a" : "§e";

        List<Component> lore = new ArrayList<>();
        buildLore(1).forEach(line -> lore.add(c(line)));
        lore.add(Component.empty());

        if (level > 0) {
            lore.add(selected ? c("§2§lSELECTED") : c("§eClick to select!"));
        } else {
            lore.add(c("§7Cost"));
            lore.add(c("§51 Token of the Mountain"));
        }

        return ItemStack.builder(mat)
            .customName(c(nameColor + "§l" + name()))
            .lore(lore)
            .build();
    }
}
