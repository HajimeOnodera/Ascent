package fun.ascent.skyblock.hotm;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public abstract class HotmUpgrade {

    public abstract String id();
    public abstract String name();
    public abstract int maxLevel();
    public abstract int tierRequirement();
    public abstract Powder powder(int level);
    public abstract int cost(int level);
    public abstract List<String> buildLore(int level);

    public List<Class<? extends HotmUpgrade>> prerequisites() {
        return List.of();
    }

    public ItemStack buildItem(HotmTree tree) {
        int level = tree.getLevel(this);
        boolean unlocked = level > 0;
        boolean maxed = level >= maxLevel();

        Material mat = !unlocked ? Material.COAL : maxed ? Material.DIAMOND : Material.EMERALD;
        String nameColor = !unlocked ? "§c" : maxed ? "§a" : "§e";

        List<Component> lore = new ArrayList<>();
        if (maxLevel() > 1) {
            lore.add(c("§8Level " + (unlocked ? level : 0) + "§8/§7" + maxLevel()));
            lore.add(Component.empty());
        }
        buildLore(Math.max(level, 1)).forEach(line -> lore.add(c(line)));

        if (!maxed) {
            lore.add(Component.empty());
            if (!unlocked) {
                lore.add(c("§7Cost"));
                lore.add(c("§51 Token of the Mountain"));
            } else {
                Powder p = powder(level);
                lore.add(c("§7Cost: " + p.color() + fmt(cost(level)) + " " + p.displayName()));
            }
        }

        if (unlocked) {
            lore.add(Component.empty());
            lore.add(tree.isEnabled(this)
                ? c("§7Right-click to §cdisable§7!")
                : c("§7Right-click to §aenable§7!"));
        }

        return ItemStack.builder(mat)
            .customName(c(nameColor + "§l" + name()))
            .lore(lore)
            .build();
    }

    protected static Component c(String legacy) {
        return LegacyComponentSerializer.legacySection().deserialize(legacy);
    }

    protected static String fmt(int n) {
        return String.format("%,d", n);
    }
}
