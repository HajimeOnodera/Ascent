package fun.ascent.skyblock.item;

import fun.ascent.skyblock.item.gemstone.GemstoneSlot;
import fun.ascent.skyblock.item.registries.*;
import fun.ascent.skyblock.player.stats.Stats;
import lombok.Getter;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.component.DataComponent;
import net.minestom.server.component.DataComponents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.color.Color;
import net.minestom.server.item.component.AttributeList;
import net.minestom.server.item.component.CustomData;
import net.minestom.server.item.component.TooltipDisplay;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.ResolvableProfile;
import net.minestom.server.utils.Unit;

import java.util.*;

import static fun.ascent.common.StringUtility.text;
import static fun.ascent.skyblock.player.stats.Stats.*;
import static fun.ascent.skyblock.player.stats.Stats.DAMAGE;
import static net.minestom.server.component.DataComponents.*;

public class SkyblockItem {

    public static final String ITEM_ID_TAG = "item_id";

    private static final Set<DataComponent<?>> HIDDEN_COMPONENTS = Set.of(
            ATTRIBUTE_MODIFIERS,
            DYED_COLOR,
            UNBREAKABLE,
            TRIM,
            POTION_CONTENTS,
            BANNER_PATTERNS,
            FIREWORKS,
            JUKEBOX_PLAYABLE
    );

    private static final TooltipDisplay TOOLTIP_DISPLAY = new TooltipDisplay(false, HIDDEN_COMPONENTS);

    private static final Stats[] LORE_STAT_ORDER = {
            DAMAGE, HEALTH, DEFENSE, TRUE_DEFENSE,
            STRENGTH, INTELLIGENCE, CRITICAL_CHANCE, CRITICAL_DAMAGE,
            ATTACK_SPEED, ABILITY_DAMAGE, FEROCITY, SPEED,
            HEALTH_REGEN, VITALITY, MENDING, SWING_RANGE,
            MAGIC_FIND, PET_LUCK, SEA_CREATURE_CHANCE,
            DOUBLE_HOOK_CHANCE, FISHING_SPEED, TROPHY_FISH_CHANCE,
            TREASURE_CHANCE, MINING_SPEED,
            MINING_FORTUNE, ORE_FORTUNE, BLOCK_FORTUNE,
            GEMSTONE_FORTUNE, DWARVEN_METAL_FORTUNE, PRISTINE,
            MINING_WISDOM,
            FARMING_FORTUNE, BONUS_PEST_CHANCE, FORAGING_FORTUNE,
            COLD_RESISTANCE, HEAT_RESISTANCE, RESPIRATION,
            FEAR,
    };

    private static String getShortbowCooldown(double attackSpeed) {
        if (attackSpeed >= 100) return "0.25s";
        if (attackSpeed >= 67) return "0.3s";
        if (attackSpeed >= 43) return "0.35s";
        if (attackSpeed >= 25) return "0.4s";
        if (attackSpeed >= 12) return "0.45s";
        return "0.5s";
    }

    @Getter
    private final String itemId;
    @Getter
    private final String displayName;
    @Getter
    private final Material material;
    @Getter
    private final ItemType itemType;
    @Getter
    private final Rarity rarity;
    @Getter
    private final Map<Stats, Double> baseStats;
    private final List<String> description;
    private final List<ItemAbility> abilities;
    @Getter
    private final boolean enchantable;
    @Getter
    private final boolean reforgeable;
    private final boolean glowing;
    private final String skinValue;
    private final Color armorColor;
    private final List<GemstoneSlot> gemstoneSlots;
    private final boolean recombobulated;
    private final int hotPotatoCount;
    @Getter
    private final String modifier;
    private final Map<Stats, Double> reforgeStats;
    private final List<String> reforgeLore;

    // Lore fields
    private final Map<String, Integer> enchantments;
    private final String ultimateEnchant;
    private final int ultimateEnchantLevel;
    private final String runeType;
    private final int runeLevel;
    private final boolean consumable;
    private final boolean dungeon;
    private final boolean soulbound;
    private final boolean coopSoulbound;
    private final String dyeName;
    private final boolean bookOfStats;
    private final int kills;
    private final boolean artOfPeace;

    private SkyblockItem(Builder builder) {
        this.itemId = builder.itemId;
        this.displayName = builder.displayName;
        this.material = builder.material;
        this.itemType = builder.itemType;
        this.rarity = builder.rarity;
        this.baseStats = Collections.unmodifiableMap(new EnumMap<>(builder.baseStats));
        this.description = List.copyOf(builder.description);
        this.abilities = List.copyOf(builder.abilities);
        this.enchantable = builder.enchantable;
        this.reforgeable = builder.reforgeable;
        this.glowing = builder.glowing;
        this.skinValue = builder.skinValue;
        this.armorColor = builder.armorColor;
        this.gemstoneSlots = List.copyOf(builder.gemstoneSlots);
        this.recombobulated = builder.recombobulated;
        this.hotPotatoCount = builder.hotPotatoCount;
        this.modifier = builder.modifier;
        this.reforgeStats = Collections.unmodifiableMap(new EnumMap<>(builder.reforgeStats));
        this.reforgeLore = builder.reforgeLore == null ? List.of() : List.copyOf(builder.reforgeLore);
        this.enchantments = Collections.unmodifiableMap(new LinkedHashMap<>(builder.enchantments));
        this.ultimateEnchant = builder.ultimateEnchant;
        this.ultimateEnchantLevel = builder.ultimateEnchantLevel;
        this.runeType = builder.runeType;
        this.runeLevel = builder.runeLevel;
        this.consumable = builder.consumable;
        this.dungeon = builder.dungeon;
        this.soulbound = builder.soulbound;
        this.coopSoulbound = builder.coopSoulbound;
        this.dyeName = builder.dyeName;
        this.bookOfStats = builder.bookOfStats;
        this.kills = builder.kills;
        this.artOfPeace = builder.artOfPeace;
    }

    public ItemStack buildItemStack() {
        return buildItemStack(null, false);
    }

    public ItemStack buildItemStack(Player player) {
        return buildItemStack(player, false);
    }

    public ItemStack buildItemStack(Player player, boolean preview) {
        Rarity effectiveRarity = recombobulated && rarity.getNextRarity() != null
                ? rarity.getNextRarity() : rarity;

        String namePrefix = (modifier != null && !modifier.isEmpty()) ? modifier + " " : "";
        Component displayComponent = text(effectiveRarity.getColor() + namePrefix + displayName)
                .decoration(TextDecoration.ITALIC, false);

        List<Component> loreComponents = new ArrayList<>();
        for (String line : buildLoreStrings(effectiveRarity, player)) {
            loreComponents.add(text(line).decoration(TextDecoration.ITALIC, false));
        }
        if (preview) {
            loreComponents.add(text("<dark_gray><bold>--------------------").decoration(TextDecoration.ITALIC, false));
            loreComponents.add(text("<green>This is the item you will get.").decoration(TextDecoration.ITALIC, false));
            loreComponents.add(text("<green>Click the <red>ANVIL BELOW<green> to combine.").decoration(TextDecoration.ITALIC, false));
        }

        ItemStack.Builder builder = ItemStack.builder(material)
                .set(CUSTOM_NAME, displayComponent)
                .set(LORE, loreComponents)
                .set(ATTRIBUTE_MODIFIERS, AttributeList.EMPTY)
                .set(DataComponents.TOOLTIP_DISPLAY, TOOLTIP_DISPLAY)
                .set(CUSTOM_DATA, buildCustomData())
                .set(UNBREAKABLE, Unit.INSTANCE);

        if (armorColor != null) {
            builder.set(DYED_COLOR, armorColor);
        }

        if (material == Material.PLAYER_HEAD && skinValue != null && !skinValue.isEmpty()) {
            ResolvableProfile profile = new ResolvableProfile(new ResolvableProfile.Partial(
                    "", UUID.randomUUID(),
                    List.of(new GameProfile.Property("textures", skinValue, ""))));
            builder.set(PROFILE, profile);
        }

        return builder.build();
    }

    private CustomData buildCustomData() {
        CompoundBinaryTag.Builder tag = CompoundBinaryTag.builder()
                .putString("id", itemId)
                .putString("uuid", UUID.randomUUID().toString())
                .putLong("timestamp", System.currentTimeMillis());

        if (recombobulated) {
            tag.putByte("rarity_upgrades", (byte) 1);
        }
        if (hotPotatoCount > 0) {
            tag.putByte("hot_potato_count", (byte) hotPotatoCount);
        }
        if (artOfPeace) {
            tag.putByte("art_of_peace", (byte) 1);
        }

        if (modifier != null && !modifier.isEmpty()) {
            tag.putString("modifier", modifier.toLowerCase(Locale.ROOT));
        }

        if (!enchantments.isEmpty() || (ultimateEnchant != null && !ultimateEnchant.isEmpty())) {
            CompoundBinaryTag.Builder enchTag = CompoundBinaryTag.builder();
            enchantments.forEach((name, level) ->
                    enchTag.putInt(name.toLowerCase(Locale.ROOT).replace(' ', '_'), level));
            if (ultimateEnchant != null && !ultimateEnchant.isEmpty()) {
                enchTag.putInt("ultimate_" + ultimateEnchant.toLowerCase(Locale.ROOT).replace(' ', '_'), ultimateEnchantLevel);
            }
            tag.put("enchantments", enchTag.build());
        }

        return new CustomData(tag.build());
    }

    private List<String> buildLoreStrings(Rarity effectiveRarity, Player player) {
        List<String> lore = new ArrayList<>();

        if (ConsumableRegistry.isConsumable(itemId)) {
            lore.add("<dark_gray>Consumable");
        }

        if (BoosterRegistry.isBooster((itemId))) {
            lore.add("<dark_gray>Booster");

        }

        if (AnvilCombinableRegistry.isAnvilCombinable(itemId)) {
            lore.add("<dark_gray>Combinable in Anvil");
        }

        if (itemType == ItemType.BAIT) {
            lore.add("<dark_gray>Fishing Bait");
            lore.add("<dark_gray>Consumes on Cast");
        }

        if (itemType == ItemType.ARROW_POISON) {
            lore.add("<dark_gray>Consumed on arrow shot");
        }

        double breakingPower = baseStats.getOrDefault(BREAKING_POWER, 0.0);
        if (breakingPower != 0.0) {
            lore.add("<dark_gray>Breaking Power " + (int) breakingPower);
            lore.add(" ");
        }

        Map<Stats, Double> hpbStats = computeHotPotatoStats();
        Map<Stats, Double> aopStats = computeArtOfPeaceStats();
        boolean hasStats = false;
        for (Stats stat : LORE_STAT_ORDER) {
            double base    = baseStats.getOrDefault(stat, 0.0);
            double reforge = reforgeStats.getOrDefault(stat, 0.0);
            double hpb     = hpbStats.getOrDefault(stat, 0.0);
            double aop     = aopStats.getOrDefault(stat, 0.0);
            double total   = base + reforge + hpb + aop;
            if (total != 0.0) {
                hasStats = true;
                String sign = total > 0 ? "+" : "";
                String formatted = stat.getStatIntType()
                        ? sign + (int) total + "%"
                        : sign + (int) total;
                String line = "<gray>" + stat.getStatFormattedDisplay() + ": " + stat.getStatColor() + formatted;
                if (reforge != 0.0) {
                    String rSign = reforge > 0 ? "+" : "";
                    String rFormatted = stat.getStatIntType()
                            ? rSign + (int) reforge + "%"
                            : rSign + (int) reforge;
                    line += " <blue>(" + rFormatted + ")";
                }
                if (hpb != 0.0) {
                    String hSign = hpb > 0 ? "+" : "";
                    String hFormatted = stat.getStatIntType()
                            ? hSign + (int) hpb + "%"
                            : hSign + (int) hpb;
                    line += " <yellow>(" + hFormatted + ")";
                }
                if (aop != 0.0) {
                    String aSign = aop > 0 ? "+" : "";
                    String aFormatted = stat.getStatIntType()
                            ? aSign + (int) aop + "%"
                            : aSign + (int) aop;
                    line += " <red>[" + aFormatted + "]";
                }
                lore.add(line);
            }
        }

        if (!gemstoneSlots.isEmpty()) {
            StringBuilder gemLine = new StringBuilder();
            for (GemstoneSlot slot : gemstoneSlots) {
                String bracketColor;
                String symbolColor;
                String symbol = slot.getType().getSymbol();

                if (!slot.isEmpty()) {
                    bracketColor = slot.getQuality().getRarity().getColor();
                    symbolColor = slot.getGemstone().getColorCode();
                } else {
                    bracketColor = "<dark_gray>";
                    symbolColor = slot.isUnlocked() ? "<gray>" : "<dark_gray>";
                }

                gemLine.append(bracketColor).append(" [")
                       .append(symbolColor).append(symbol)
                       .append(bracketColor).append("]");
            }
            lore.add(gemLine.toString());
            lore.add("");
        } else if (hasStats) {
            lore.add("");
        }

        if (!description.isEmpty()) {
            for (String line : description) {
                lore.add("<gray>" + line);
            }
            lore.add("");
        }

        if (!enchantments.isEmpty()) {
            StringBuilder enchLine = new StringBuilder();
            int count = 0;
            for (Map.Entry<String, Integer> entry : enchantments.entrySet()) {
                if (count > 0 && count % 3 == 0) {
                    lore.add(enchLine.toString().trim());
                    enchLine = new StringBuilder();
                }
                enchLine.append("<blue>").append(entry.getKey()).append(" ").append(entry.getValue()).append("  ");
                count++;
            }
            if (!enchLine.toString().isBlank()) lore.add(enchLine.toString().trim());
        }

        if (ultimateEnchant != null && !ultimateEnchant.isEmpty()) {
            lore.add("<light_purple><bold>" + ultimateEnchant + " " + ultimateEnchantLevel);
        }

        if (!enchantments.isEmpty() || (ultimateEnchant != null && !ultimateEnchant.isEmpty())) {
            lore.add("");
        }

        for (ItemAbility ability : abilities) {
            lore.addAll(ability.buildLore());
            lore.add("");
        }

        if (ShortbowRegistry.isShortbow(itemId)) {
            lore.add(effectiveRarity.getColor() + "Shortbow: Instantly Shoots!");
            lore.add("");
        }

        if (ConsumableRegistry.isConsumable(itemId)) {
            lore.add("<yellow>Right-click to consume!");
            lore.add("");
        }

        if (runeType != null && !runeType.isEmpty()) {
            lore.add("<light_purple>Rune: " + runeType + " " + runeLevel);
            lore.add("");
        }

        if (!reforgeLore.isEmpty()) {
            lore.add("<blue>" + modifier + " Bonus");
            lore.addAll(reforgeLore);
            lore.add("");
        }

        if (soulbound)       lore.add("<dark_gray>Soulbound");
        if (coopSoulbound)   lore.add("<dark_gray>Co-op Soulbound");
        if (dyeName != null && !dyeName.isEmpty()) lore.add("<dark_gray>Dye: " + dyeName);
        if (bookOfStats)     lore.add("<dark_gray>Book of Stats Applied");
        if (kills > 0)       lore.add("<red>Kills: <white>" + kills);

        if (reforgeable && (modifier == null || modifier.isEmpty())
                && (itemType.isArmor() || itemType.isWeapon()
                    || itemType == ItemType.FISHING_ROD || itemType.isEquipment())) {
            lore.add("<dark_gray>This item can be reforged!");
        }

        String typeDisplay = dungeon && itemType != ItemType.NONE
                ? "DUNGEON " + itemType.getDisplay()
                : itemType.getDisplay();
        String rarityLine = effectiveRarity.getDisplay() + (typeDisplay.isEmpty() ? "" : " " + typeDisplay);
        lore.add(recombobulated ? effectiveRarity.getColor() + "<bold><obfuscated>a<reset> " + rarityLine + " <bold><obfuscated>a<reset>" : rarityLine);

        return lore;
    }

    private Map<Stats, Double> computeArtOfPeaceStats() {
        if (!artOfPeace || !itemType.isArmor()) return Collections.emptyMap();
        Map<Stats, Double> result = new EnumMap<>(Stats.class);
        result.put(HEALTH, 40.0);
        return result;
    }

    private Map<Stats, Double> computeHotPotatoStats() {
        if (hotPotatoCount == 0) return Collections.emptyMap();
        Map<Stats, Double> result = new EnumMap<>(Stats.class);
        if (itemType.isArmor()) {
            result.put(DEFENSE, hotPotatoCount * 2.0);
            result.put(HEALTH,  hotPotatoCount * 4.0);
        } else if (itemType.isWeapon() || itemType == ItemType.FISHING_ROD) {
            result.put(DAMAGE,   hotPotatoCount * 2.0);
            result.put(STRENGTH, hotPotatoCount * 2.0);
        }
        return result;
    }

    public boolean hasArtOfPeace() { return artOfPeace; }

    public Map<Stats, Double> getTotalStats() {
        Map<Stats, Double> total = new EnumMap<>(Stats.class);
        Map<Stats, Double> hpb = computeHotPotatoStats();
        Map<Stats, Double> aop = computeArtOfPeaceStats();
        for (Stats stat : Stats.values()) {
            double base = baseStats.getOrDefault(stat, 0.0);
            double reforge = reforgeStats.getOrDefault(stat, 0.0);
            double h = hpb.getOrDefault(stat, 0.0);
            double a = aop.getOrDefault(stat, 0.0);
            double sum = base + reforge + h + a;
            if (sum != 0.0) total.put(stat, sum);
        }
        return total;
    }

    public Builder toBuilder() {
        Builder b = new Builder(itemId, material, rarity);
        b.displayName = displayName;
        b.itemType = itemType;
        b.baseStats.putAll(baseStats);
        b.description.addAll(description);
        b.abilities.addAll(abilities);
        b.enchantable = enchantable;
        b.reforgeable = reforgeable;
        b.glowing = glowing;
        b.skinValue = skinValue;
        b.armorColor = armorColor;
        b.gemstoneSlots.addAll(gemstoneSlots);
        b.recombobulated = recombobulated;
        b.hotPotatoCount = hotPotatoCount;
        b.modifier = modifier;
        b.reforgeStats.putAll(reforgeStats);
        b.reforgeLore = reforgeLore.isEmpty() ? null : new ArrayList<>(reforgeLore);
        b.enchantments.putAll(enchantments);
        b.ultimateEnchant = ultimateEnchant;
        b.ultimateEnchantLevel = ultimateEnchantLevel;
        b.runeType = runeType;
        b.runeLevel = runeLevel;
        b.consumable = consumable;
        b.dungeon = dungeon;
        b.soulbound = soulbound;
        b.coopSoulbound = coopSoulbound;
        b.dyeName = dyeName;
        b.bookOfStats = bookOfStats;
        b.kills = kills;
        b.artOfPeace = artOfPeace;
        return b;
    }

    public static Builder builder(String itemId, Material material, Rarity rarity) {
        return new Builder(itemId, material, rarity);
    }

    public static class Builder {
        private final String itemId;
        private String displayName = "";
        private final Material material;
        private ItemType itemType = ItemType.NONE;
        private final Rarity rarity;
        private final Map<Stats, Double> baseStats = new EnumMap<>(Stats.class);
        private final List<String> description = new ArrayList<>();
        private final List<ItemAbility> abilities = new ArrayList<>();
        private boolean enchantable = true;
        private boolean reforgeable = true;
        private boolean glowing = false;
        private String skinValue = null;
        private Color armorColor = null;
        private final List<GemstoneSlot> gemstoneSlots = new ArrayList<>();
        private boolean recombobulated = false;
        private int hotPotatoCount = 0;
        private String modifier = null;
        private final Map<Stats, Double> reforgeStats = new EnumMap<>(Stats.class);
        private List<String> reforgeLore = null;
        private final Map<String, Integer> enchantments = new LinkedHashMap<>();
        private String ultimateEnchant = null;
        private int ultimateEnchantLevel = 0;
        private String runeType = null;
        private int runeLevel = 0;
        private boolean consumable = false;
        private boolean dungeon = false;
        private boolean soulbound = false;
        private boolean coopSoulbound = false;
        private String dyeName = null;
        private boolean bookOfStats = false;
        private int kills = 0;
        private boolean artOfPeace = false;

        private Builder(String itemId, Material material, Rarity rarity) {
            this.itemId = itemId;
            this.material = material;
            this.rarity = rarity;
        }

        public Builder displayName(String displayName) { this.displayName = displayName; return this; }
        public Builder itemType(ItemType itemType) { this.itemType = itemType; return this; }
        public Builder stat(Stats stat, double value) { this.baseStats.put(stat, value); return this; }
        public Builder description(String... lines) { this.description.addAll(List.of(lines)); return this; }
        public Builder ability(ItemAbility ability) { this.abilities.add(ability); return this; }
        public Builder enchantable(boolean enchantable) { this.enchantable = enchantable; return this; }
        public Builder reforgeable(boolean reforgeable) { this.reforgeable = reforgeable; return this; }
        public Builder glowing(boolean glowing) { this.glowing = glowing; return this; }
        public void skinValue(String skinValue) { this.skinValue = skinValue;}
        public void armorColor(Color color) { this.armorColor = color;}
        public void gemstoneSlot(GemstoneSlot slot) { this.gemstoneSlots.add(slot);}
        public Builder recombobulated(boolean recombobulated) { this.recombobulated = recombobulated; return this; }
        public Builder hotPotatoCount(int n) { this.hotPotatoCount = n; return this; }
        public Builder modifier(String modifier) { this.modifier = modifier; return this; }
        public void reforgeStat(Stats stat, double value) { this.reforgeStats.put(stat, value);}
        public Builder reforgeLore(List<String> lore) { this.reforgeLore = lore; return this; }
        public Builder enchantment(String name, int level) { this.enchantments.put(name, level); return this; }
        public Builder ultimateEnchant(String name, int level) { this.ultimateEnchant = name; this.ultimateEnchantLevel = level; return this; }
        public Builder rune(String type, int level) { this.runeType = type; this.runeLevel = level; return this; }
        public Builder consumable(boolean consumable) { this.consumable = consumable; return this; }
        public void dungeon(boolean dungeon) { this.dungeon = dungeon;}
        public void soulbound(boolean soulbound) { this.soulbound = soulbound;}
        public void coopSoulbound(boolean coopSoulbound) { this.coopSoulbound = coopSoulbound;}
        public Builder dyeName(String dyeName) { this.dyeName = dyeName; return this; }
        public Builder bookOfStats(boolean bookOfStats) { this.bookOfStats = bookOfStats; return this; }
        public Builder kills(int kills) { this.kills = kills; return this; }
        public Builder artOfPeace(boolean artOfPeace) { this.artOfPeace = artOfPeace; return this; }

        public SkyblockItem build() { return new SkyblockItem(this); }
    }
}

