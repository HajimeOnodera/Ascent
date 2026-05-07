package fun.ascent.skyblock.item;

import fun.ascent.skyblock.item.gemstone.GemstoneSlot;
import fun.ascent.skyblock.item.registries.*;
import fun.ascent.skyblock.player.stats.Stats;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.component.DataComponent;
import net.minestom.server.component.DataComponents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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

public class SkyblockItem {

    public static final String ITEM_ID_TAG = "item_id";

    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacySection();

    private static final Set<DataComponent<?>> HIDDEN_COMPONENTS = Set.of(
            DataComponents.ATTRIBUTE_MODIFIERS,
            DataComponents.DYED_COLOR,
            DataComponents.UNBREAKABLE,
            DataComponents.TRIM,
            DataComponents.POTION_CONTENTS,
            DataComponents.BANNER_PATTERNS,
            DataComponents.FIREWORKS,
            DataComponents.JUKEBOX_PLAYABLE
    );

    private static final TooltipDisplay TOOLTIP_DISPLAY = new TooltipDisplay(false, HIDDEN_COMPONENTS);

    private static final Stats[] LORE_STAT_ORDER = {
            Stats.DAMAGE, Stats.HEALTH, Stats.DEFENSE, Stats.TRUE_DEFENSE,
            Stats.STRENGTH, Stats.INTELLIGENCE, Stats.CRITICAL_CHANCE, Stats.CRITICAL_DAMAGE,
            Stats.ATTACK_SPEED, Stats.ABILITY_DAMAGE, Stats.FEROCITY, Stats.SPEED,
            Stats.HEALTH_REGEN, Stats.VITALITY, Stats.MENDING, Stats.SWING_RANGE,
            Stats.MAGIC_FIND, Stats.PET_LUCK, Stats.SEA_CREATURE_CHANCE,
            Stats.DOUBLE_HOOK_CHANCE, Stats.FISHING_SPEED, Stats.TROPHY_FISH_CHANCE,
            Stats.TREASURE_CHANCE, Stats.MINING_SPEED,
            Stats.MINING_FORTUNE, Stats.ORE_FORTUNE, Stats.BLOCK_FORTUNE,
            Stats.GEMSTONE_FORTUNE, Stats.DWARVEN_METAL_FORTUNE, Stats.PRISTINE,
            Stats.MINING_WISDOM,
            Stats.FARMING_FORTUNE, Stats.BONUS_PEST_CHANCE, Stats.FORAGING_FORTUNE,
            Stats.COLD_RESISTANCE, Stats.HEAT_RESISTANCE, Stats.RESPIRATION,
            Stats.FEAR,
    };

    private static String getShortbowCooldown(double attackSpeed) {
        if (attackSpeed >= 100) return "0.25s";
        if (attackSpeed >= 67) return "0.3s";
        if (attackSpeed >= 43) return "0.35s";
        if (attackSpeed >= 25) return "0.4s";
        if (attackSpeed >= 12) return "0.45s";
        return "0.5s";
    }

    private final String itemId;
    private final String displayName;
    private final Material material;
    private final ItemType itemType;
    private final Rarity rarity;
    private final Map<Stats, Double> baseStats;
    private final List<String> description;
    private final List<ItemAbility> abilities;
    private final boolean enchantable;
    private final boolean reforgeable;
    private final boolean glowing;
    private final String skinValue;
    private final Color armorColor;
    private final List<GemstoneSlot> gemstoneSlots;
    private final boolean recombobulated;
    private final int hotPotatoCount;
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
        Component displayComponent = LEGACY.deserialize(effectiveRarity.getColor() + namePrefix + displayName)
                .decoration(TextDecoration.ITALIC, false);

        List<Component> loreComponents = new ArrayList<>();
        for (String line : buildLoreStrings(effectiveRarity, player)) {
            loreComponents.add(LEGACY.deserialize(line).decoration(TextDecoration.ITALIC, false));
        }
        if (preview) {
            loreComponents.add(LEGACY.deserialize("§8§l--------------------").decoration(TextDecoration.ITALIC, false));
            loreComponents.add(LEGACY.deserialize("§aThis is the item you will get.").decoration(TextDecoration.ITALIC, false));
            loreComponents.add(LEGACY.deserialize("§aClick the §cANVIL BELOW§a to combine.").decoration(TextDecoration.ITALIC, false));
        }

        ItemStack.Builder builder = ItemStack.builder(material)
                .set(DataComponents.CUSTOM_NAME, displayComponent)
                .set(DataComponents.LORE, loreComponents)
                .set(DataComponents.ATTRIBUTE_MODIFIERS, AttributeList.EMPTY)
                .set(DataComponents.TOOLTIP_DISPLAY, TOOLTIP_DISPLAY)
                .set(DataComponents.CUSTOM_DATA, buildCustomData())
                .set(DataComponents.UNBREAKABLE, Unit.INSTANCE);

        if (armorColor != null) {
            builder.set(DataComponents.DYED_COLOR, armorColor);
        }

        if (material == Material.PLAYER_HEAD && skinValue != null && !skinValue.isEmpty()) {
            ResolvableProfile profile = new ResolvableProfile(new ResolvableProfile.Partial(
                    "", UUID.randomUUID(),
                    List.of(new GameProfile.Property("textures", skinValue, ""))));
            builder.set(DataComponents.PROFILE, profile);
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
            lore.add("§8Consumable");
        }

        if (BoosterRegistry.isBooster((itemId))) {
            lore.add("§8Booster");

        }

        if (AnvilCombinableRegistry.isAnvilCombinable(itemId)) {
            lore.add("§8Combinable in Anvil");
        }

        if (itemType == ItemType.BAIT) {
            lore.add("§8Fishing Bait");
            lore.add("§8Consumes on Cast");
        }

        if (itemType == ItemType.ARROW_POISON) {
            lore.add("§8Consumed on arrow shot");
        }

        double breakingPower = baseStats.getOrDefault(Stats.BREAKING_POWER, 0.0);
        if (breakingPower != 0.0) {
            lore.add("§8Breaking Power " + (int) breakingPower);
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
                String line = "§7" + stat.getStatFormattedDisplay() + ": " + stat.getStatColor() + formatted;
                if (reforge != 0.0) {
                    String rSign = reforge > 0 ? "+" : "";
                    String rFormatted = stat.getStatIntType()
                            ? rSign + (int) reforge + "%"
                            : rSign + (int) reforge;
                    line += " §9(" + rFormatted + ")";
                }
                if (hpb != 0.0) {
                    String hSign = hpb > 0 ? "+" : "";
                    String hFormatted = stat.getStatIntType()
                            ? hSign + (int) hpb + "%"
                            : hSign + (int) hpb;
                    line += " §e(" + hFormatted + ")";
                }
                if (aop != 0.0) {
                    String aSign = aop > 0 ? "+" : "";
                    String aFormatted = stat.getStatIntType()
                            ? aSign + (int) aop + "%"
                            : aSign + (int) aop;
                    line += " §c[" + aFormatted + "]";
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
                    bracketColor = "§8";
                    symbolColor = slot.isUnlocked() ? "§7" : "§8";
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
                lore.add("§7" + line);
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
                enchLine.append("§9").append(entry.getKey()).append(" ").append(entry.getValue()).append("  ");
                count++;
            }
            if (!enchLine.toString().isBlank()) lore.add(enchLine.toString().trim());
        }

        if (ultimateEnchant != null && !ultimateEnchant.isEmpty()) {
            lore.add("§d§l" + ultimateEnchant + " " + ultimateEnchantLevel);
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
            lore.add("§eRight-click to consume!");
            lore.add("");
        }

        if (runeType != null && !runeType.isEmpty()) {
            lore.add("§dRune: " + runeType + " " + runeLevel);
            lore.add("");
        }

        if (!reforgeLore.isEmpty()) {
            lore.add("§9" + modifier + " Bonus");
            for (String line : reforgeLore) {
                lore.add(line);
            }
            lore.add("");
        }

        if (soulbound)       lore.add("§8Soulbound");
        if (coopSoulbound)   lore.add("§8Co-op Soulbound");
        if (dyeName != null && !dyeName.isEmpty()) lore.add("§8Dye: " + dyeName);
        if (bookOfStats)     lore.add("§8Book of Stats Applied");
        if (kills > 0)       lore.add("§cKills: §f" + kills);

        if (reforgeable && (modifier == null || modifier.isEmpty())
                && (itemType.isArmor() || itemType.isWeapon()
                    || itemType == ItemType.FISHING_ROD || itemType.isEquipment())) {
            lore.add("§8This item can be reforged!");
        }

        String typeDisplay = dungeon && itemType != ItemType.NONE
                ? "DUNGEON " + itemType.getDisplay()
                : itemType.getDisplay();
        String rarityLine = effectiveRarity.getDisplay() + (typeDisplay.isEmpty() ? "" : " " + typeDisplay);
        lore.add(recombobulated ? effectiveRarity.getColor() + "§l§ka§r " + rarityLine + " §l§ka§r" : rarityLine);

        return lore;
    }

    private Map<Stats, Double> computeArtOfPeaceStats() {
        if (!artOfPeace || !itemType.isArmor()) return Collections.emptyMap();
        Map<Stats, Double> result = new EnumMap<>(Stats.class);
        result.put(Stats.HEALTH, 40.0);
        return result;
    }

    private Map<Stats, Double> computeHotPotatoStats() {
        if (hotPotatoCount == 0) return Collections.emptyMap();
        Map<Stats, Double> result = new EnumMap<>(Stats.class);
        if (itemType.isArmor()) {
            result.put(Stats.DEFENSE, hotPotatoCount * 2.0);
            result.put(Stats.HEALTH,  hotPotatoCount * 4.0);
        } else if (itemType.isWeapon() || itemType == ItemType.FISHING_ROD) {
            result.put(Stats.DAMAGE,   hotPotatoCount * 2.0);
            result.put(Stats.STRENGTH, hotPotatoCount * 2.0);
        }
        return result;
    }

    public String getItemId() { return itemId; }
    public String getDisplayName() { return displayName; }
    public Material getMaterial() { return material; }
    public ItemType getItemType() { return itemType; }
    public Rarity getRarity() { return rarity; }
    public Map<Stats, Double> getBaseStats() { return baseStats; }
    public boolean isReforgeable() { return reforgeable; }
    public boolean isEnchantable() { return enchantable; }
    public String getModifier() { return modifier; }
    public boolean hasArtOfPeace() { return artOfPeace; }

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
        public Builder skinValue(String skinValue) { this.skinValue = skinValue; return this; }
        public Builder armorColor(Color color) { this.armorColor = color; return this; }
        public Builder gemstoneSlot(GemstoneSlot slot) { this.gemstoneSlots.add(slot); return this; }
        public Builder recombobulated(boolean recombobulated) { this.recombobulated = recombobulated; return this; }
        public Builder hotPotatoCount(int n) { this.hotPotatoCount = n; return this; }
        public Builder modifier(String modifier) { this.modifier = modifier; return this; }
        public Builder reforgeStat(Stats stat, double value) { this.reforgeStats.put(stat, value); return this; }
        public Builder reforgeLore(List<String> lore) { this.reforgeLore = lore; return this; }
        public Builder enchantment(String name, int level) { this.enchantments.put(name, level); return this; }
        public Builder ultimateEnchant(String name, int level) { this.ultimateEnchant = name; this.ultimateEnchantLevel = level; return this; }
        public Builder rune(String type, int level) { this.runeType = type; this.runeLevel = level; return this; }
        public Builder consumable(boolean consumable) { this.consumable = consumable; return this; }
        public Builder dungeon(boolean dungeon) { this.dungeon = dungeon; return this; }
        public Builder soulbound(boolean soulbound) { this.soulbound = soulbound; return this; }
        public Builder coopSoulbound(boolean coopSoulbound) { this.coopSoulbound = coopSoulbound; return this; }
        public Builder dyeName(String dyeName) { this.dyeName = dyeName; return this; }
        public Builder bookOfStats(boolean bookOfStats) { this.bookOfStats = bookOfStats; return this; }
        public Builder kills(int kills) { this.kills = kills; return this; }
        public Builder artOfPeace(boolean artOfPeace) { this.artOfPeace = artOfPeace; return this; }

        public SkyblockItem build() { return new SkyblockItem(this); }
    }
}
