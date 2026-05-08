package fun.ascent.skyblock.player.stats;

import lombok.Getter;

public enum Stats {
    /**
     * Stats are organised based off of the skyblock wiki ;)
     * Combat Stats
     */
    HEALTH(false,"Health","<red>❤",StatCategory.COMBAT ,"<red>",100,0),
    DEFENSE(false,"Defense","<green>❈",StatCategory.COMBAT ,"<green>",0,0),
    TRUE_DEFENSE(false,"True Defense","<white>❂",StatCategory.COMBAT ,"<white>",0,0),
    STRENGTH(false,"Strength","<red>❁",StatCategory.COMBAT ,"<red>",0,0),
    CRITICAL_CHANCE(true,"Crit Chance","<blue>☣",StatCategory.COMBAT ,"<blue>",30,0),
    CRITICAL_DAMAGE(true,"Crit Damage","<blue>☠",StatCategory.COMBAT ,"<blue>",50,0),
    ATTACK_SPEED(true,"Attack Speed","<yellow>⚔",StatCategory.COMBAT ,"<yellow>",0,100),
    FEROCITY(false,"Ferocity","<red>⫽",StatCategory.COMBAT ,"<red>",0,500),
    SWING_RANGE(false,"Swing Range","<yellow>Ⓢ",StatCategory.COMBAT ,"<yellow>",3,15),
    INTELLIGENCE(false,"Intelligence","<aqua>✎",StatCategory.COMBAT ,"<aqua>",100,0),
    ABILITY_DAMAGE(true,"Ability Damage","<red>๑",StatCategory.COMBAT ,"<red>",0,0),
    HEALTH_REGEN(false,"Health Regen","<red>❣",StatCategory.COMBAT ,"<red>",100,0),
    VITALITY(false,"Vitality","<dark_red>♨",StatCategory.COMBAT ,"<dark_red>",100,0),
    MENDING(false,"Mending","<green>☄",StatCategory.COMBAT ,"<green>",100,0),
    /**
     * Mining Stats
     */
    BREAKING_POWER(false,"Breaking Power","<dark_green>Ⓟ",StatCategory.MINING ,"<dark_green>",0,0),
    MINING_SPEED(false,"Mining Speed","<gold>⸕",StatCategory.MINING ,"<gold>",0,0),
    MINING_SPREAD(false,"Mining Spread","<yellow>▚",StatCategory.MINING ,"<yellow>",0,10000),
    GEMSTONE_SPREAD(false,"Gemstone Spread","<yellow>▚",StatCategory.MINING ,"<yellow>",0,0),
    PRISTINE(false,"Pristine","<dark_purple>✧",StatCategory.MINING ,"<dark_purple>",0,0),
    MINING_FORTUNE(false,"Mining Fortune","<gold>☘",StatCategory.MINING ,"<gold>",0,0),
    ORE_FORTUNE(false,"Ore Fortune","<gold>☘",StatCategory.MINING ,"<gold>",0,0),
    BLOCK_FORTUNE(false,"Block Fortune","<gold>☘",StatCategory.MINING ,"<gold>",0,0),
    DWARVEN_METAL_FORTUNE(false,"Dwarven Metal Fortune","<gold>☘",StatCategory.MINING ,"<gold>",0,0),
    GEMSTONE_FORTUNE(false,"Gemstone Fortune","<gold>☘",StatCategory.MINING ,"<gold>",0,0),
    /**
     * Farming Stats
     */
    BONUS_PEST_CHANCE(true,"Bonus Pest Chance","<dark_green>ൠ",StatCategory.FARMING ,"<dark_green>",0,0),
    FARMING_FORTUNE(false,"Farming Fortune","<gold>☘",StatCategory.FARMING ,"<gold>",0,0),
    WHEAT_FORTUNE(false,"Wheat Fortune","<gold>☘",StatCategory.FARMING ,"<gold>",0,0),
    CARROT_FORTUNE(false,"Carrot Fortune","<gold>☘",StatCategory.FARMING ,"<gold>",0,0),
    POTATO_FORTUNE(false,"Potato Fortune","<gold>☘",StatCategory.FARMING ,"<gold>",0,0),
    PUMPKIN_FORTUNE(false,"Pumpkin Fortune","<gold>☘",StatCategory.FARMING ,"<gold>",0,0),
    SUGAR_CANE_FORTUNE(false,"Sugar Cane Fortune","<gold>☘",StatCategory.FARMING ,"<gold>",0,0),
    MELON_SLICE_FORTUNE(false,"Melon Slice Fortune","<gold>☘",StatCategory.FARMING ,"<gold>",0,0),
    CACTUS_FORTUNE(false,"Cactus Fortune","<gold>☘",StatCategory.FARMING ,"<gold>",0,0),
    COCOA_BEANS_FORTUNE(false,"Cocoa Beans Fortune","<gold>☘",StatCategory.FARMING ,"<gold>",0,0),
    MUSHROOM_FORTUNE(false,"Mushroom Fortune","<gold>☘",StatCategory.FARMING ,"<gold>",0,0),
    NETHER_WART_FORTUNE(false,"Nether Wart Fortune","<gold>☘",StatCategory.FARMING ,"<gold>",0,0),
    SUNFLOWER_FORTUNE(false,"Sunflower Fortune","<gold>☘",StatCategory.FARMING ,"<gold>",0,0),
    MOONFLOWER_FORTUNE(false,"Moonflower Fortune","<gold>☘",StatCategory.FARMING ,"<gold>",0,0),
    WILD_ROSE_FORTUNE(false,"Wild Rose Fortune","<gold>☘",StatCategory.FARMING ,"<gold>",0,0),
    /**
     * Foraging Stats
     */
    SWEEP(false,"Sweep","<dark_green>∮",StatCategory.FORAGING ,"<dark_green>",0,0),
    FORAGING_FORTUNE(false,"Foraging Fortune","<gold>☘",StatCategory.FORAGING ,"<gold>",0,0),
    FIG_FORTUNE(false,"Fig Fortune","<gold>☘",StatCategory.FORAGING ,"<gold>",0,0),
    MANGROVE_FORTUNE(false,"Mangrove Fortune","<gold>☘",StatCategory.FORAGING ,"<gold>",0,0),
    /**
     * Fishing Stats
     */
    FISHING_SPEED(false,"Fishing Speed","<aqua>☂",StatCategory.FISHING ,"<aqua>",0,300),
    SEA_CREATURE_CHANCE(false,"Sea Creature Chance","<dark_aqua>α",StatCategory.FISHING ,"<dark_aqua>",20,0),
    DOUBLE_HOOK_CHANCE(false,"Double Hook Chance","<blue>⚓",StatCategory.FISHING ,"<blue>",0,0),
    TROPHY_FISH_CHANCE(false,"Trophy Fish Chance","<gold>♔",StatCategory.FISHING ,"<gold>",0,150),
    TREASURE_CHANCE(false,"Treasure Chance","<gold>⛃",StatCategory.FISHING ,"<gold>",0,100),
    /**
     * Hunting Stats
     */
    PULL(false,"Pull","<aqua>ᛷ",StatCategory.HUNTING ,"<aqua>",0,0),
    HUNTER_FORTUNE(false,"Hunter Fortune","<light_purple>☘",StatCategory.HUNTING ,"<light_purple>",20,0),
    /**
     * Misc Stats
     */
    SPEED(false,"Speed","<white>✦",StatCategory.MISC ,"<white>",100,400),
    MAGIC_FIND(false,"Magic Find","<aqua>✯",StatCategory.MISC ,"<aqua>",0,900),
    PET_LUCK(false,"Pet Luck","<light_purple>♣",StatCategory.MISC ,"<light_purple>",0,0),
    HEAT_RESISTANCE(false,"Heat Resistance","<red>♨",StatCategory.MISC ,"<red>",0,0),
    COLD_RESISTANCE(false,"Cold Resistance","<aqua>❄",StatCategory.MISC ,"<aqua>",0,0),
    RESPIRATION(false,"Respiration","<dark_aqua>⚶",StatCategory.MISC ,"<dark_aqua>",30,0),
    PRESSURE_RESISTANCE(false,"Pressure Resistance","<blue>❍",StatCategory.MISC ,"<blue>",0,0),
    FEAR(false,"Fear","<dark_purple>☠",StatCategory.MISC ,"<dark_purple>",0,0),
    TRACKING(false,"Tracking","<light_purple>❃",StatCategory.MISC ,"<light_purple>",0,0),
    /**
     * Wisdom Stats
     */
    COMBAT_WISDOM(false,"Combat Wisdom","<dark_aqua>☯",StatCategory.WISDOM ,"<dark_aqua>",0,0),
    FARMING_WISDOM(false,"Farming Wisdom","<dark_aqua>☯",StatCategory.WISDOM ,"<dark_aqua>",0,0),
    FISHING_WISDOM(false,"Fishing Wisdom","<dark_aqua>☯",StatCategory.WISDOM ,"<dark_aqua>",0,0),
    MINING_WISDOM(false,"Mining Wisdom","<dark_aqua>☯",StatCategory.WISDOM ,"<dark_aqua>",0,0),
    FORAGING_WISDOM(false,"Foraging Wisdom","<dark_aqua>☯",StatCategory.WISDOM ,"<dark_aqua>",0,0),
    ENCHANTING_WISDOM(false,"Enchanting Wisdom","<dark_aqua>☯",StatCategory.WISDOM ,"<dark_aqua>",0,0),
    ALCHEMY_WISDOM(false,"Alchemy Wisdom","<dark_aqua>☯",StatCategory.WISDOM ,"<dark_aqua>",0,0),
    CARPENTRY_WISDOM(false,"Carpentry Wisdom","<dark_aqua>☯",StatCategory.WISDOM ,"<dark_aqua>",0,0),
    RUNECRAFTING_WISDOM(false,"Runecrafting Wisdom","<dark_aqua>☯",StatCategory.WISDOM ,"<dark_aqua>",0,0),
    TAMING_WISDOM(false,"Taming Wisdom","<dark_aqua>☯",StatCategory.WISDOM ,"<dark_aqua>",0,0),
    SOCIAL_WISDOM(false,"Social Wisdom","<dark_aqua>☯",StatCategory.WISDOM ,"<dark_aqua>",0,0),
    HUNTING_WISDOM(false,"Hunting Wisdom","<dark_aqua>☯",StatCategory.WISDOM ,"<dark_aqua>",0,0),
    /**
     * Rift Stats
     */
    RIFT_TIME(false,"Rift Time","<green>ф",StatCategory.RIFT ,"<green>",480,0),
    RIFT_DAMAGE(false,"Rift Damage","<dark_purple>❁",StatCategory.RIFT ,"<dark_purple>",20,0),
    MANA_REGEN(false,"Mana Regen","<aqua>⚡",StatCategory.RIFT ,"<aqua>",0,0),
    HEARTS(false,"Hearts","<green>❤",StatCategory.RIFT ,"<green>",10,0),
    /**
     * Other Stats
     */
    EFFECTIVE_HEALTH(false,"Effective Health","<red>❤",StatCategory.OTHER ,"<red>",0,0),
    MANA(false,"Mana","<aqua>✎",StatCategory.OTHER ,"<aqua>",0,0),
    ABSORPTION(false,"Absorption","<gold>❤",StatCategory.OTHER ,"<gold>",0,0),
    TRUE_DAMAGE(false,"True Damage","<white>✷",StatCategory.OTHER ,"<white>",0,0),
    HEAT(false,"Heat","<red>♨",StatCategory.OTHER ,"<red>",0,0),
    COLD(false,"Cold","<aqua>❄",StatCategory.OTHER ,"<aqua>",0,0),
    /**
     * Backend Stats
     */
    DAMAGE(false,"Damage","<red>❁",StatCategory.BACKEND ,"<red>",1,0),
    MULTIPLICATIVE_DAMAGE(false,"Multiplicative Damage","<red>❁",StatCategory.BACKEND ,"<red>",1,0),
    ADDITIVE_DAMAGE(false,"Additive Damage","<red>❁",StatCategory.BACKEND ,"<red>",1,0),
    BONUS_DAMAGE(false,"Bonus Damage","<red>❁",StatCategory.BACKEND ,"<red>",1,0),
    /**
     * Hidden Stats (Used for things that are technically stats but don't fit into the other categories)
     */
    SOULFLOW(false,"Soulflow","<dark_aqua>⸎",StatCategory.PLAYER ,"<red>",0,0),
    OVERFLOW_MANA(false,"Overflow Mana","<dark_aqua>⸎",StatCategory.PLAYER ,"<red>",0,0),
    ;

    private final boolean isPercentage;
    private final String formattedDisplay;
    private final String symbol;
    private final StatCategory category;
    @Getter
    private final String statColor;
    @Getter
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
    public Double getStatCap()   {
        return statCap;
    }
    public double applyCap(double value) {
        return statCap > 0 ? Math.min(value, statCap) : value;
    }
}
