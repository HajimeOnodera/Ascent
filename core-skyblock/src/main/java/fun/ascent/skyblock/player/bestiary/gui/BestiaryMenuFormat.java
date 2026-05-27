package fun.ascent.skyblock.player.bestiary.gui;

import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.skyblock.entity.loot.MobDrop;
import fun.ascent.skyblock.entity.mob.MobCategory;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.player.bestiary.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static fun.ascent.common.StringUtility.text;

public final class BestiaryMenuFormat {

    private static final int PROGRESS_BAR_SEGMENTS = 17;
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getIntegerInstance(Locale.US);

    private BestiaryMenuFormat() {
    }

    static void fill(Inventory inventory) {
        ItemStack filler = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
                .customName(Component.text(" "))
                .build();

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            inventory.setItemStack(slot, filler);
        }
    }

    static ItemStack closeButton() {
        return ItemStack.builder(Material.BARRIER)
                .customName(text("<red>Close"))
                .build();
    }

    static ItemStack backButton(String target) {
        return ItemStack.builder(Material.ARROW)
                .customName(text("<green>Go Back"))
                .lore(List.of(text("<gray>To " + target)))
                .build();
    }

    static ItemStack icon(String name, Material material, String texture, List<Component> lore) {
        if (texture != null && !texture.isBlank()) {
            return ItemStackCreator.getStackHead(text(name), texture, 1, lore).build();
        }
        return ItemStackCreator.getStack(text(name), material, 1, lore).build();
    }

    public static List<Component> overallLore(BestiaryProgress progress) {
        int totalFamilies = BestiaryFamily.values().length;
        int foundFamilies = 0;
        int completedFamilies = 0;

        for (BestiaryFamily family : BestiaryFamily.values()) {
            int kills = family.totalKills(progress);
            if (kills > 0) {
                foundFamilies++;
            }
            if (kills >= BestiaryMilestones.maxKills(family)) {
                completedFamilies++;
            }
        }

        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>The Bestiary is a compendium of"));
        lore.add(text("<gray>mobs in SkyBlock. It contains detailed"));
        lore.add(text("<gray>information on loot drops, your mob"));
        lore.add(text("<gray>stats, and more!"));
        lore.add(Component.empty());
        lore.add(text("<gray>Kill mobs within <green>Families <gray>to progress"));
        lore.add(text("<gray>and earn <green>rewards<gray>, including <aqua>✯ Magic"));
        lore.add(text("<aqua>Find <gray>bonuses towards mobs in the"));
        lore.add(text("<gray>Family."));
        lore.add(Component.empty());
        addFamilyProgress(lore, "Families Found", foundFamilies, totalFamilies);
        lore.add(Component.empty());
        addFamilyProgress(lore, "Families Completed", completedFamilies, totalFamilies);
        return lore;
    }

    static List<Component> sectionLore(BestiaryProgress progress, BestiarySection section) {
        List<BestiaryFamily> families = section.families();
        int foundFamilies = 0;
        int completedFamilies = 0;

        for (BestiaryFamily family : families) {
            int kills = family.totalKills(progress);
            if (kills > 0) {
                foundFamilies++;
            }
            if (kills >= BestiaryMilestones.maxKills(family)) {
                completedFamilies++;
            }
        }

        List<Component> lore = new ArrayList<>();
        section.description().forEach(line -> lore.add(text(line)));
        lore.add(Component.empty());
        addFamilyProgress(lore, "Families Found", foundFamilies, families.size());
        lore.add(Component.empty());
        addFamilyProgress(lore, "Families Completed", completedFamilies, families.size());
        return lore;
    }

    static List<Component> familyLore(BestiaryProgress progress, BestiaryFamily family, boolean includeClickHint) {
        int kills = family.totalKills(progress);
        int tier = BestiaryMilestones.currentTier(family, kills);
        int maxTier = family.maxTier();

        List<Component> lore = new ArrayList<>();
        lore.add(text("<gray>" + family.description()));
        lore.add(Component.empty());
        lore.add(text("<gray>Kills: <green>" + formatNumber(kills)));
        lore.add(Component.empty());

        if (tier > 0) {
            lore.add(text("<green>" + family.displayName() + " <green>Bonuses"));
            lore.add(text("<dark_gray>+<aqua>" + BestiaryMilestones.totalMagicFind(tier) + " Magic Find"));
            lore.add(text("<dark_gray>+<red>" + BestiaryMilestones.totalStrength(tier) + " Strength"));
            lore.add(Component.empty());
        }

        if (tier < maxTier) {
            int tierProgress = BestiaryMilestones.killsIntoCurrentTier(family, kills);
            int tierRequirement = BestiaryMilestones.killsForNextTier(family, kills);
            int percent = tierRequirement <= 0 ? 100 : (int) Math.floor((double) tierProgress / tierRequirement * 100);

            lore.add(text("<gray>Progress to Tier " + roman(tier + 1) + ": <aqua>" + percent + "%"));
            addProgress(lore, tierProgress, tierRequirement);
            lore.add(Component.empty());
        }

        int maxKills = BestiaryMilestones.maxKills(family);
        int overallPercent = maxKills <= 0 ? 100 : (int) Math.floor((double) Math.min(kills, maxKills) / maxKills * 100);
        String overallLine = tier < maxTier
                ? "<gray>Overall Progress: <aqua>" + overallPercent + "%"
                : "<gray>Overall Progress: <aqua>" + overallPercent + "% <gray>(<red><bold>MAX!<gray>)";
        lore.add(text(overallLine));
        addProgress(lore, Math.min(kills, maxKills), maxKills);
        if (tier < maxTier) {
            lore.add(text("<dark_gray>Capped at Tier " + roman(maxTier)));
            lore.add(Component.empty());
            lore.add(text("<gray>Tier " + roman(tier + 1) + " Rewards"));
            lore.add(text("  <dark_gray>+<aqua>" + BestiaryMilestones.magicFindGain(tier + 1) + " Magic Find"));
            lore.add(text("  <dark_gray>+<red>" + BestiaryMilestones.strengthGain(tier + 1) + " Strength"));
        }

        if (includeClickHint) {
            lore.add(Component.empty());
            lore.add(text("<yellow>Click to view!"));
        }
        return lore;
    }

    static List<Component> mobLore(BestiaryProgress progress, BestiaryMobType mob) {
        List<Component> lore = new ArrayList<>();
        List<MobCategory> categories = mob.categories();

        if (categories.size() == 1) {
            MobCategory category = categories.getFirst();
            lore.add(text("<gray>Mob Type: " + category.prefix() + " <gray>" + category.getDisplayName()));
            lore.add(Component.empty());
        } else if (!categories.isEmpty()) {
            List<String> display = new ArrayList<>();
            for (MobCategory category : categories) {
                display.add(category.prefix() + " <gray>" + category.getDisplayName());
            }
            lore.add(text("<gray>Mob Types: " + String.join("<gray>, ", display)));
            lore.add(Component.empty());
        }

        lore.add(text("<gray>Mob Stats:"));
        lore.add(text("<gray>Health: <red>" + formatNumber(mob.health()) + "❤"));
        lore.add(text("<gray>Damage: <red>" + formatNumber(mob.damage()) + "❁"));
        lore.add(text("<gray>Kills: <green>" + formatNumber(progress.getKills(mob))));

        List<MobDrop> common = new ArrayList<>();
        List<MobDrop> uncommon = new ArrayList<>();
        List<MobDrop> rare = new ArrayList<>();
        List<MobDrop> legendary = new ArrayList<>();
        List<MobDrop> rngesus = new ArrayList<>();

        for (MobDrop drop : mob.loot()) {
            double chance = drop.chance();
            if (chance <= 0.01) {
                rngesus.add(drop);
            } else if (chance <= 0.1) {
                legendary.add(drop);
            } else if (chance <= 1) {
                rare.add(drop);
            } else if (chance <= 30) {
                uncommon.add(drop);
            } else {
                common.add(drop);
            }
        }

        appendLootGroup(lore, "Common Loot", "<white>", common, false);
        appendLootGroup(lore, "Uncommon Loot", "<green>", uncommon, true);
        appendLootGroup(lore, "Rare Loot", "<blue>", rare, true);
        appendLootGroup(lore, "Legendary Loot", "<gold>", legendary, true);
        appendLootGroup(lore, "RNGesus Loot", "<light_purple>", rngesus, true);

        if (!lore.isEmpty() && Component.empty().equals(lore.getLast())) {
            lore.removeLast();
        }
        return lore;
    }

    private static void addFamilyProgress(List<Component> lore, String label, int current, int total) {
        int percent = total <= 0 ? 100 : (int) Math.floor((double) current / total * 100);
        String color = current >= total ? "<green>" : "<yellow>";
        String line = current >= total
                ? "<gray>" + label + ": " + color + percent + "% <gray>(<red><bold>MAX!<gray>)"
                : "<gray>" + label + ": " + color + percent + "%";
        lore.add(text(line));
        addProgress(lore, current, total);
    }

    private static void addProgress(List<Component> lore, int current, int total) {
        double ratio = total <= 0 ? 1.0 : Math.clamp((double) current / total, 0.0, 1.0);
        int filled = (int) Math.round(ratio * PROGRESS_BAR_SEGMENTS);
        lore.add(text(
                "<dark_aqua><strikethrough>" + "─".repeat(Math.min(filled, PROGRESS_BAR_SEGMENTS))
                        + "<gray><strikethrough>" + "─".repeat(Math.max(PROGRESS_BAR_SEGMENTS - filled, 0))
                        + "<reset> <aqua>" + formatNumber(current)
                        + "<dark_aqua>/<aqua>" + formatRequirement(total)
        ));
    }

    private static void appendLootGroup(List<Component> lore, String title, String color, List<MobDrop> drops, boolean showChance) {
        if (drops.isEmpty()) {
            return;
        }
        lore.add(Component.empty());
        lore.add(text(color + title));
        for (MobDrop drop : drops) {
            String line = " <dark_gray>■ <white>" + displayName(drop);
            if (showChance) {
                line += " <dark_gray>(<green>" + drop.chance() + "%<dark_gray>)";
            }
            lore.add(text(line));
        }
    }

    private static String displayName(MobDrop drop) {
        Component customName = drop.item().get(DataComponents.CUSTOM_NAME);
        if (customName != null) {
            String plain = PlainTextComponentSerializer.plainText().serialize(customName);
            if (!plain.isBlank()) {
                return plain;
            }
        }
        return ItemRegistry.formatName(drop.item().material().name().replace("minecraft:", ""));
    }

    private static String roman(int tier) {
        return fun.ascent.common.StringUtility.getAsRomanNumeral(tier);
    }

    static String formatNumber(double value) {
        return NUMBER_FORMAT.format((long) Math.floor(value));
    }

    private static String formatRequirement(double value) {
        if (value < 1000) {
            return String.valueOf((int) value);
        }
        if (value >= 1_000_000_000) {
            double billions = value / 1_000_000_000.0;
            return (billions == (int) billions ? String.valueOf((int) billions) : String.format(Locale.US, "%.1f", billions)) + "b";
        }
        if (value >= 1_000_000) {
            double millions = value / 1_000_000.0;
            return (millions == (int) millions ? String.valueOf((int) millions) : String.format(Locale.US, "%.1f", millions)) + "m";
        }
        double thousands = value / 1000.0;
        return (thousands == (int) thousands ? String.valueOf((int) thousands) : String.format(Locale.US, "%.1f", thousands)) + "k";
    }
}
