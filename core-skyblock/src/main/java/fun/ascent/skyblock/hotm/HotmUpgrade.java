package fun.ascent.skyblock.hotm;

import fun.ascent.common.StringUtility;
import net.kyori.adventure.text.Component;
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
        String nameColor = !unlocked ? "<red>" : maxed ? "<green>" : "<yellow>";

        List<Component> lore = new ArrayList<>();
        if (maxLevel() > 1) {
            lore.add(c("<dark_gray>Level " + (unlocked ? level : 0) + "<dark_gray>/<gray>" + maxLevel()));
            lore.add(Component.empty());
        }
        buildLore(Math.max(level, 1)).forEach(line -> lore.add(c(line)));

        if (!maxed) {
            lore.add(Component.empty());
            if (!unlocked) {
                lore.add(c("<gray>Cost"));
                lore.add(c("<dark_purple>1 Token of the Mountain"));
            } else {
                Powder p = powder(level);
                lore.add(c("<gray>Cost: " + p.colorTag() + fmt(cost(level)) + " " + p.displayName()));
            }
        }

        if (unlocked) {
            lore.add(Component.empty());
            lore.add(tree.isEnabled(this)
                ? c("<gray>Right-click to <red>disable<gray>!")
                : c("<gray>Right-click to <green>enable<gray>!"));
        }

        return ItemStack.builder(mat)
            .customName(c(nameColor + "<bold>" + name()))
            .lore(lore)
            .build();
    }

    protected static Component c(String miniMessage) {
        return StringUtility.text(miniMessage);
    }

    protected static String fmt(int n) {
        return String.format("%,d", n);
    }
}

