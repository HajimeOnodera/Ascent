package fun.ascent.skyblock.player.stats;

public enum Stats {
    /**
     * Stats are organised based off of the skyblock wiki ;)
     * Combat Stats
     */
    HEALTH(false,"Health","§c❤",StatCategory.COMBAT ,"§c",100,0),
    DEFENSE(false,"Defense","§a❈",StatCategory.COMBAT ,"§a",0,0),
    TRUE_DEFENSE(false,"True Defense","§f❂",StatCategory.COMBAT ,"§f",0,0),
    STRENGTH(false,"Strength","§c❁",StatCategory.COMBAT ,"§c",0,0),
    CRITICAL_CHANCE(true,"Crit Chance","§9☣",StatCategory.COMBAT ,"§9",30,0),
    CRITICAL_DAMAGE(true,"Crit Damage","§9☠",StatCategory.COMBAT ,"§9",50,0),
    ATTACK_SPEED(true,"Attack Speed","§e⚔",StatCategory.COMBAT ,"§e",0,100),
    FEROCITY(false,"Ferocity","§c⫽",StatCategory.COMBAT ,"§c",0,500),
    SWING_RANGE(false,"Swing Range","§eⓈ",StatCategory.COMBAT ,"§e",3,15),
    INTELLIGENCE(false,"Intelligence","§b✎",StatCategory.COMBAT ,"§b",100,0),
    ABILITY_DAMAGE(true,"Ability Damage","§c๑",StatCategory.COMBAT ,"§c",0,0),
    HEALTH_REGEN(false,"Health Regen","§c❣",StatCategory.COMBAT ,"§c",100,0),
    VITALITY(false,"Vitality","§4♨",StatCategory.COMBAT ,"§4",100,0),
    MENDING(false,"Mending","§a☄",StatCategory.COMBAT ,"§a",100,0),
    /**
     * Mining Stats
     */
    BREAKING_POWER(false,"Breaking Power","§2Ⓟ",StatCategory.MINING ,"§2",0,0),
    MINING_SPEED(false,"Mining Speed","§6⸕",StatCategory.MINING ,"§6",0,0),
    MINING_SPREAD(false,"Mining Spread","§e▚",StatCategory.MINING ,"§e",0,10000),
    GEMSTONE_SPREAD(false,"Gemstone Spread","§e▚",StatCategory.MINING ,"§e",0,0),
    PRISTINE(false,"Pristine","§5✧",StatCategory.MINING ,"§5",0,0),
    MINING_FORTUNE(false,"Mining Fortune","§6☘",StatCategory.MINING ,"§6",0,0),
    ORE_FORTUNE(false,"Ore Fortune","§6☘",StatCategory.MINING ,"§6",0,0),
    BLOCK_FORTUNE(false,"Block Fortune","§6☘",StatCategory.MINING ,"§6",0,0),
    DWARVEN_METAL_FORTUNE(false,"Dwarven Metal Fortune","§6☘",StatCategory.MINING ,"§6",0,0),
    GEMSTONE_FORTUNE(false,"Gemstone Fortune","§6☘",StatCategory.MINING ,"§6",0,0),
    /**
     * Farming Stats
     */
    BONUS_PEST_CHANCE(true,"Bonus Pest Chance","§2ൠ",StatCategory.FARMING ,"§2",0,0),
    FARMING_FORTUNE(false,"Farming Fortune","§6☘",StatCategory.FARMING ,"§6",0,0),
    WHEAT_FORTUNE(false,"Wheat Fortune","§6☘",StatCategory.FARMING ,"§6",0,0),
    CARROT_FORTUNE(false,"Carrot Fortune","§6☘",StatCategory.FARMING ,"§6",0,0),
    POTATO_FORTUNE(false,"Potato Fortune","§6☘",StatCategory.FARMING ,"§6",0,0),
    PUMPKIN_FORTUNE(false,"Pumpkin Fortune","§6☘",StatCategory.FARMING ,"§6",0,0),
    SUGAR_CANE_FORTUNE(false,"Sugar Cane Fortune","§6☘",StatCategory.FARMING ,"§6",0,0),
    MELON_SLICE_FORTUNE(false,"Melon Slice Fortune","§6☘",StatCategory.FARMING ,"§6",0,0),
    CACTUS_FORTUNE(false,"Cactus Fortune","§6☘",StatCategory.FARMING ,"§6",0,0),
    COCOA_BEANS_FORTUNE(false,"Cocoa Beans Fortune","§6☘",StatCategory.FARMING ,"§6",0,0),
    MUSHROOM_FORTUNE(false,"Mushroom Fortune","§6☘",StatCategory.FARMING ,"§6",0,0),
    NETHER_WART_FORTUNE(false,"Nether Wart Fortune","§6☘",StatCategory.FARMING ,"§6",0,0),
    SUNFLOWER_FORTUNE(false,"Sunflower Fortune","§6☘",StatCategory.FARMING ,"§6",0,0),
    MOONFLOWER_FORTUNE(false,"Moonflower Fortune","§6☘",StatCategory.FARMING ,"§6",0,0),
    WILD_ROSE_FORTUNE(false,"Wild Rose Fortune","§6☘",StatCategory.FARMING ,"§6",0,0),
    /**
     * Foraging Stats
     */
    SWEEP(false,"Sweep","§2∮",StatCategory.FORAGING ,"§2",0,0),
    FORAGING_FORTUNE(false,"Foraging Fortune","§6☘",StatCategory.FORAGING ,"§6",0,0),
    FIG_FORTUNE(false,"Fig Fortune","§6☘",StatCategory.FORAGING ,"§6",0,0),
    MANGROVE_FORTUNE(false,"Mangrove Fortune","§6☘",StatCategory.FORAGING ,"§6",0,0),
    /**
     * Fishing Stats
     */
    FISHING_SPEED(false,"Fishing Speed","§b☂",StatCategory.FISHING ,"§b",0,300),
    SEA_CREATURE_CHANCE(false,"Sea Creature Chance","§3α",StatCategory.FISHING ,"§3",20,0),
    DOUBLE_HOOK_CHANCE(false,"Double Hook Chance","§9⚓",StatCategory.FISHING ,"§9",0,0),
    TROPHY_FISH_CHANCE(false,"Trophy Fish Chance","§6♔",StatCategory.FISHING ,"§6",0,150),
    TREASURE_CHANCE(false,"Treasure Chance","§6⛃",StatCategory.FISHING ,"§6",0,100),
    /**
     * Hunting Stats
     */
    PULL(false,"Pull","§bᛷ",StatCategory.HUNTING ,"§b",0,0),
    HUNTER_FORTUNE(false,"Hunter Fortune","§d☘",StatCategory.HUNTING ,"§d",20,0),
    /**
     * Misc Stats
     */
    SPEED(false,"Speed","§f✦",StatCategory.MISC ,"§f",100,400),
    MAGIC_FIND(false,"Magic Find","§b✯",StatCategory.MISC ,"§b",0,900),
    PET_LUCK(false,"Pet Luck","§d♣",StatCategory.MISC ,"§d",0,0),
    HEAT_RESISTANCE(false,"Heat Resistance","§c♨",StatCategory.MISC ,"§c",0,0),
    COLD_RESISTANCE(false,"Cold Resistance","§b❄",StatCategory.MISC ,"§b",0,0),
    RESPIRATION(false,"Respiration","§3⚶",StatCategory.MISC ,"§3",30,0),
    PRESSURE_RESISTANCE(false,"Pressure Resistance","§9❍",StatCategory.MISC ,"§9",0,0),
    FEAR(false,"Fear","§5☠",StatCategory.MISC ,"§5",0,0),
    TRACKING(false,"Tracking","§d❃",StatCategory.MISC ,"§d",0,0),
    /**
     * Wisdom Stats
     */
    COMBAT_WISDOM(false,"Combat Wisdom","§3☯",StatCategory.WISDOM ,"§3",0,0),
    FARMING_WISDOM(false,"Farming Wisdom","§3☯",StatCategory.WISDOM ,"§3",0,0),
    FISHING_WISDOM(false,"Fishing Wisdom","§3☯",StatCategory.WISDOM ,"§3",0,0),
    MINING_WISDOM(false,"Mining Wisdom","§3☯",StatCategory.WISDOM ,"§3",0,0),
    FORAGING_WISDOM(false,"Foraging Wisdom","§3☯",StatCategory.WISDOM ,"§3",0,0),
    ENCHANTING_WISDOM(false,"Enchanting Wisdom","§3☯",StatCategory.WISDOM ,"§3",0,0),
    ALCHEMY_WISDOM(false,"Alchemy Wisdom","§3☯",StatCategory.WISDOM ,"§3",0,0),
    CARPENTRY_WISDOM(false,"Carpentry Wisdom","§3☯",StatCategory.WISDOM ,"§3",0,0),
    RUNECRAFTING_WISDOM(false,"Runecrafting Wisdom","§3☯",StatCategory.WISDOM ,"§3",0,0),
    TAMING_WISDOM(false,"Taming Wisdom","§3☯",StatCategory.WISDOM ,"§3",0,0),
    SOCIAL_WISDOM(false,"Social Wisdom","§3☯",StatCategory.WISDOM ,"§3",0,0),
    HUNTING_WISDOM(false,"Hunting Wisdom","§3☯",StatCategory.WISDOM ,"§3",0,0),
    /**
     * Rift Stats
     */
    RIFT_TIME(false,"Rift Time","§aф",StatCategory.RIFT ,"§a",480,0),
    RIFT_DAMAGE(false,"Rift Damage","§5❁",StatCategory.RIFT ,"§5",20,0),
    MANA_REGEN(false,"Mana Regen","§b⚡",StatCategory.RIFT ,"§b",0,0),
    HEARTS(false,"Hearts","§a❤",StatCategory.RIFT ,"§a",10,0),
    /**
     * Other Stats
     */
    EFFECTIVE_HEALTH(false,"Effective Health","§c❤",StatCategory.OTHER ,"§c",0,0),
    MANA(false,"Mana","§b✎",StatCategory.OTHER ,"§b",0,0),
    ABSORPTION(false,"Absorption","§6❤",StatCategory.OTHER ,"§6",0,0),
    TRUE_DAMAGE(false,"True Damage","§f✷",StatCategory.OTHER ,"§f",0,0),
    HEAT(false,"Heat","§c♨",StatCategory.OTHER ,"§c",0,0),
    COLD(false,"Cold","§b❄",StatCategory.OTHER ,"§b",0,0),
    /**
     * Backend Stats
     */
    DAMAGE(false,"Damage","§c❁",StatCategory.BACKEND ,"§c",1,0),
    MULTIPLICATIVE_DAMAGE(false,"Multiplicative Damage","§c❁",StatCategory.BACKEND ,"§c",1,0),
    ADDITIVE_DAMAGE(false,"Additive Damage","§c❁",StatCategory.BACKEND ,"§c",1,0),
    BONUS_DAMAGE(false,"Bonus Damage","§c❁",StatCategory.BACKEND ,"§c",1,0),
    /**
     * Hidden Stats (Used for things that are technically stats but don't fit into the other categories)
     */
    SOULFLOW(false,"Soulflow","§3⸎",StatCategory.PLAYER ,"§c",0,0),
    OVERFLOW_MANA(false,"Overflow Mana","§3⸎",StatCategory.PLAYER ,"§c",0,0),
    ;

    public static final int SIZE = values().length;

    private static final Stats[] BY_ORDINAL = values();

    private final boolean isPercentage;
    private final String formattedDisplay;
    private final String symbol;
    private final StatCategory category;
    private final String statColor;
    private final double baseStat;
    private final double statCap;

    Stats(boolean isPercentage, String formattedDisplay, String symbol, StatCategory category, String statColor, double baseStat, double statCap) {
        this.isPercentage = isPercentage;
        this.formattedDisplay = formattedDisplay;
        this.symbol = symbol;
        this.category = category;
        this.statColor = statColor;
        this.baseStat = baseStat;
        this.statCap = statCap;
    }

    public static Stats byOrdinal(int ordinal) {
        return BY_ORDINAL[ordinal];
    }

    public boolean getStatIntType() {
        return isPercentage;
    }
    public String getStatFormattedDisplay() {
        return formattedDisplay;
    }
    public String getStatSymbol() {
        return symbol;
    }
    public StatCategory getStatCategory() {
        return category;
    }
    public String getStatColor() {
        return statColor;
    }
    public double getBaseStat()  {
        return baseStat;
    }
    public Double getStatCap()   {
        return statCap;
    }
    public double applyCap(double value) {
        return statCap > 0 ? Math.min(value, statCap) : value;
    }
}