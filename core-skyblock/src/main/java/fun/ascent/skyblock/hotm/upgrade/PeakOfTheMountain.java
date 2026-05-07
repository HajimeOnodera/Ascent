package fun.ascent.skyblock.hotm.upgrade;

import fun.ascent.skyblock.hotm.HotmUpgrade;
import fun.ascent.skyblock.hotm.HotmTree;
import fun.ascent.skyblock.hotm.Powder;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public final class PeakOfTheMountain extends HotmUpgrade {
    @Override public String id() { return "PEAK_OF_THE_MOUNTAIN"; }
    @Override public String name() { return "Peak of the Mountain"; }
    @Override public int maxLevel() { return 10; }
    @Override public int tierRequirement() { return 5; }

    @Override
    public Powder powder(int l) {
        if (l >= 7) return Powder.GLACIAL;
        if (l >= 5) return Powder.GEMSTONE;
        return Powder.MITHRIL;
    }

    @Override
    public int cost(int l) {
        return switch (l) {
            case 1 -> 50_000;
            case 2 -> 75_000;
            case 3 -> 100_000;
            case 4 -> 125_000;
            case 5 -> 500_000;
            case 6 -> 750_000;
            case 7 -> 1_000_000;
            case 8 -> 1_250_000;
            case 9 -> 1_500_000;
            default -> 0;
        };
    }

    @Override
    public List<String> buildLore(int l) {
        List<String> lore = new ArrayList<>();
        lore.add("§8+ §c1 Pickaxe Ability Level");
        if (l > 1) lore.add("§8+ §a1 Forge Slot");
        if (l > 2) lore.add("§8+ §a1 Commission Slot");
        if (l > 3) lore.add("§8+ §21 Base §2Mithril Powder §7when mining §2Mithril");
        if (l > 4) lore.add("§8+ §51 Token of the Mountain");
        if (l > 5) lore.add("§8+ §d2 Base §dGemstone Powder §7when mining §dGemstones");
        if (l > 6) lore.add("§8+ §51 Token of the Mountain");
        if (l > 7) lore.add("§8+ §b3 Base §bGlacial Powder §7when mining §bGlacite");
        if (l > 8) lore.add("§8+ §a+10% §7chance for §bGlacite Mineshafts §7to spawn");
        if (l > 9) lore.add("§8+ §52 Tokens of the Mountain");
        return lore;
    }

    @Override
    public ItemStack buildItem(HotmTree tree) {
        int level = tree.getLevel(this);
        if (level == 0) level = 1;
        boolean maxed = level >= maxLevel();

        Material mat = maxed ? Material.DIAMOND : Material.EMERALD;
        String nameColor = maxed ? "§a" : "§e";

        List<net.kyori.adventure.text.Component> lore = new ArrayList<>();
        lore.add(c("§8Level " + level + "§8/§7" + maxLevel()));
        lore.add(net.kyori.adventure.text.Component.empty());
        buildLore(level).forEach(s -> lore.add(c(s)));

        if (!maxed) {
            lore.add(net.kyori.adventure.text.Component.empty());
            Powder p = powder(level);
            lore.add(c("§7Cost: " + p.color() + fmt(cost(level)) + " " + p.displayName()));
        }

        return ItemStack.builder(mat)
            .customName(c(nameColor + "§lPeak of the Mountain"))
            .lore(lore)
            .build();
    }
}
