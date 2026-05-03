package fun.ascent.skyblock.world.location;

import fun.ascent.skyblock.player.skill.SkillType;
import fun.ascent.skyblock.world.WorldManager;
import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

public enum SkyblockLocation {
    BARN("<aqua>The Barn", new Point(200, -295), new Point(220, -222), SkillType.FARMING, 1),
    DESERT_SETTLEMENT("<yellow>Desert Settlement", new Point(117, -404), new Point(192, -336), SkillType.FARMING, 5),
    MUSHROOM_DESERT("<yellow>Mushroom Desert", new Point(160, -620), new Point(420, -280), SkillType.FARMING, 5),
    OASIS("<aqua>Oasis", new Point(70, -610), new Point(210, -425), SkillType.FARMING, 5),
    MUSHROM_GORGE("<aqua>Mushroom Gorge", new Point(160, -620, 0), new Point(420, -280, 70)),

    COLOSSEUM("<aqua>Colosseum", new Point(98, -80), new Point(155, -19), "Behold the destruction of a cruel tyrant."),
    ELECTION_ROOM("<aqua>Election Room", new Point(-8, -106, 50), new Point(19, -133, 30), "Vote in the <green>Mayor Election</green>."),
    COMMUNITY_CENTER("<aqua>Community Center", new Point(-7, -114), new Point(16, -96), "Participate in <green>Mayor Elections</green>.", "Contribute to City Projects.", "Unlock upgrades, buffs, and cosmetics in the <aqua>Community Shop</aqua>."),
    BAZAAR("<yellow>Bazaar Allay", new Point(-42, -79), new Point(-25, -73), "Buy and sell materials in bulk at the <gold>Bazaar</gold>."),
    AUCTION_HOUSE("<gold>Auction House", new Point(-57, -100), new Point(-18, -80), "Auction off your special items.", "Bid on players' items."),
    BANK("<gold>Bank", new Point(-31, -70), new Point(-19, -48), "Talk to the Banker.", "Store your coins to keep them safe.", "Earn interest on your coins."),
    FARM("<aqua>Farm", new Point(18, -190), new Point(93, -100), "Talk to the Farmer.", "Gather Wheat.", "Travel to <aqua>The Barn</aqua>."),
    COAL_MINE("<aqua>Coal Mine", new Point(-40, -235, 55), new Point(-5, -140, 90), "Mine Coal.", "Travel to the <gold>Gold Mine</gold>."),

    VILLAGE("<aqua>Village", new Point(-80, -155), new Point(80, 15), "Explore the area to learn more about <aqua>SkyBlock</aqua>.", "Visit the <gold>Auction House</gold>.", "Manage your <gold>coins</gold> in the <gold>Bank</gold>.", "Enchant items in the <aqua>Library</aqua>."),

    GRAVEYARD("<red>Graveyard", new Point(-215, -200), new Point(-66, -80), "Fight Zombies.", "Travel to the <red>Spider's Den</red>.", "Talk to <gray>Pat</gray>.", "Investigate the Catacombs."),

    FOREST("<aqua>Forest", new Point(-225, -80), new Point(-96, 20), "Talk to <green>Lumber Jack</green>.", "Chop down trees.", "Travel to the <green>Birch Park</green>."),
    BIRCH_PARK("<green>Birch Park", new Point(-316, -85, 0), new Point(-269, 50, 104), SkillType.FORAGING, 1, "Talk to Charlie.", "Chop down Birch logs."),
    SPRUCE_WOODS("<green>Spruce Woods", new Point(-430, -31, 87), new Point(-317, 87, 123), SkillType.FORAGING, 2),
    DARK_THICKET("<green>Dark Thicket", new Point(-386, -123, 100), new Point(-311, -6, 124), SkillType.FORAGING, 3),
    SAVANNA_WOODLANDS("<green>Savanna Woodlands", new Point(-462, -53, 106), new Point(-445, -33, 151), SkillType.FORAGING, 4),
    JUNGLE_ISLAND("<green>Jungle Island", new Point(-482, -141, 117), new Point(-360, -30, 162), SkillType.FORAGING, 5),

    MOUNTAIN("<aqua>Mountain", new Point(-60, 0), new Point(40, 70), "Climb to the top!"),
    WILDERNESS("<dark_green>Wilderness", new Point(35, 0), new Point(200, 220), "Fish in the pond.", "Visit <light_purple>Tia the Fairy</light_purple> at the <light_purple>Fairy Pond</light_purple>.", "Discover hidden secrets."),

    SPIDER_DEN("<aqua>Spider's Den", new Point(-400, -372), new Point(-126, -125), SkillType.COMBAT, 1),
    NETHER("<red>Nether Fortress", new Point(-443, -760), new Point(-175, -372), SkillType.COMBAT, 5),
    END("<light_purple>The End", new Point(-810, -450), new Point(-430, -100), SkillType.COMBAT, 12),
    DRAGON_NEST("<light_purple>Dragon Nest", new Point(-740, -330, 0), new Point(-611, -222, 100)),

    DEEP_CAVERNS("<aqua>Deep Caverns", new Point(-108, -650), new Point(85, -476), SkillType.MINING, 5),
    GOLD_MINE("<gold>Gold Mine", new Point(-95, -410), new Point(50, -260), SkillType.MINING, 1),
    GUNPOWDER("<aqua>Gunpowder Mines", new Point(-108, -650, 136), new Point(85, -476, 178)),
    LAPIS("<aqua>Lapis Quarry", new Point(-108, -650, 112), new Point(85, -476, 136)),
    PIGMEN("<aqua>Pigmen's Den", new Point(-108, -650, 78), new Point(85, -476, 112)),
    SLIMEHILL("<aqua>Slimehill", new Point(-108, -650, 46), new Point(85, -476, 78)),
    DIAMOND("<aqua>Diamond Reserve", new Point(-108, -650, 19), new Point(85, -476, 46)),
    OBSIDIAN("<aqua>Obsidian Sanctuary", new Point(-108, -650, 0), new Point(85, -476, 19)),

    CASTLE("<aqua>Ruins", new Point(-281, 27, 50), new Point(-190, 131, 255)),
    
    GARDEN("<green>The Garden", new Point(0, 0, 0), new Point(0, 0, 0), "Talk to <aqua>Sam</aqua>."),

    VOID_SEPULTURE("<light_purple>Void Sepulture", new Point(-574, -320, 7), new Point(-563, -315, 9)),

    NONE("<gray>None", new Point(0, 0), new Point(0, 0));

    @Getter private final String name;
    private final Point min;
    private final Point max;
    @Getter private final String[] features;
    @Getter private final SkillType skill;
    @Getter private final int lvl;

    SkyblockLocation(String name, Point min, Point max, String... features) {
        this(name, min, max, null, 0, features);
    }

    SkyblockLocation(String name, Point min, Point max, SkillType skill, int lvl, String... features) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.skill = skill;
        this.lvl = lvl;
        this.features = features;
    }

    public boolean isInside(int x, int y, int z) {
        if (this == NONE) return false;
        
        boolean insideXZ = x >= Math.min(min.x, max.x) && x <= Math.max(min.x, max.x)
                        && z >= Math.min(min.z, max.z) && z <= Math.max(min.z, max.z);

        if (!insideXZ) return false;

        if (min.y == null || max.y == null) {
            return true;
        }

        return y >= Math.min(min.y, max.y) && y <= Math.max(min.y, max.y);
    }

    public boolean canGo(fun.ascent.skyblock.player.SkyblockPlayer player) {
        if (skill == null || lvl <= 0) return true;
        fun.ascent.skyblock.player.skill.PlayerSkillData skillData = player.getSkillData();
        if (skillData == null) return false;
        return skillData.getLevel(skill) >= lvl;
    }

    public String getRequirementMessage() {
        if (skill == null || lvl <= 0) return null;
        return "<red>You need " + skill.getDisplayName() + " level " + lvl + " to enter " + name + "<red>!";
    }

    public static SkyblockLocation getLocation(Instance instance, Pos pos) {
        if (instance == null || pos == null) return NONE;

        String worldName = WorldManager.getWorldName(instance);
        if (worldName == null) worldName = "";
        worldName = worldName.toLowerCase();

        if (worldName.contains("garden")) {
            return GARDEN;
        }
        
        int x = pos.blockX();
        int y = pos.blockY();
        int z = pos.blockZ();

        for (SkyblockLocation zone : values()) {
            if (zone != NONE && zone != GARDEN && zone.isInside(x, y, z)) {
                return zone;
            }
        }
        
        return NONE;
    }

    public static class Point {
        final int x, z;
        final Integer y;

        public Point(int x, int z) {
            this(x, z, null);
        }

        public Point(int x, int z, Integer y) {
            this.x = x;
            this.z = z;
            this.y = y;
        }
    }
}
