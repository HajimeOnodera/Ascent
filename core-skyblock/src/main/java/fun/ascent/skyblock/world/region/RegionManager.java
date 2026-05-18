package fun.ascent.skyblock.world.region;

import fun.ascent.skyblock.world.WorldHandler;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionManager {
    private static final Map<String, Region> regions = new HashMap<>();

    public static void initialize() {
        regions.clear();

        register(new Region("village", RegionType.VILLAGE, new Pos(-276, 3, -194), new Pos(229, 182, 201)));
        register(new Region("auction_house", RegionType.AUCTION_HOUSE, new Pos(-44, 71, -20), new Pos(-26, 82, -6)));
        register(new Region("shens_auction", RegionType.SHENS_AUCTION, new Pos(-37, 52, -22), new Pos(-2, 63, -1)));
        register(new Region("bazaar", RegionType.BAZAAR, new Pos(-40, 71, -35), new Pos(-30, 79, -21)));
        register(new Region("bank", RegionType.BANK, new Pos(-33, 72, -45), new Pos(-25, 80, -35)));
        register(new Region("farm", RegionType.FARM, new Pos(28, 66, -153), new Pos(107, 106, -74)));
        register(new Region("coal_mine", RegionType.COAL_MINE, new Pos(-31, 61, -199), new Pos(24, 112, -146)));
        register(new Region("forest", RegionType.FOREST, new Pos(-204, 62, -72), new Pos(-66, 104, -6)));
        register(new Region("graveyard", RegionType.GRAVEYARD, new Pos(-195, 70, -221), new Pos(-73, 121, -101)));
        register(new Region("crypts", RegionType.CRYPTS, new Pos(-176, 40, -149), new Pos(-80, 71, -77)));
        register(new Region("mountain", RegionType.MOUNTAIN, new Pos(-51, 85, 1), new Pos(-38, 98, 16)));
        register(new Region("wilderness", RegionType.WILDERNESS, new Pos(76, 76, 64), new Pos(175, 146, 201)));
        register(new Region("combat_settlement", RegionType.COMBAT_SETTLEMENT, new Pos(-70, 63, -98), new Pos(-32, 88, -61)));
        register(new Region("fishing_outpost", RegionType.FISHING_OUTPOST, new Pos(92, 69, -79), new Pos(166, 95, -21)));
        register(new Region("mining_district", RegionType.MINING_DISTRICT, new Pos(-25, 55, -142), new Pos(23, 92, -93)));
        register(new Region("foraging_camp", RegionType.FORAGING_CAMP, new Pos(-151, 72, -50), new Pos(-101, 87, -15)));
        register(new Region("community_center", RegionType.COMMUNITY_CENTER, new Pos(-13, 71, 6), new Pos(15, 88, 24)));
        register(new Region("library", RegionType.LIBRARY, new Pos(-75, 69, -85), new Pos(-63, 78, -74)));
        register(new Region("thaumaturgist", RegionType.THAUMATURGIST, new Pos(-74, 69, -72), new Pos(-48, 77, -57)));
        register(new Region("trade_center", RegionType.TRADE_CENTER, new Pos(-38, 68, -83), new Pos(-29, 73, -73)));
        register(new Region("election_room", RegionType.ELECTION_ROOM, new Pos(-16, 47, 16), new Pos(16, 63, 54)));
        register(new Region("ruins", RegionType.RUINS, new Pos(-299, 40, 28), new Pos(-189, 148, 158)));
        register(new Region("museum", RegionType.MUSEUM, new Pos(13, 65, -3), new Pos(45, 84, 53)));
        register(new Region("archery_range", RegionType.ARCHERY_RANGE, new Pos(-60, 63, -55), new Pos(-45, 75, -45)));
    }

    public static void register(Region region) {
        regions.put(region.getId(), region);
    }

    public static Region getRegion(Instance instance, Point point) {
        if (instance == null) return null;

        // Check for Private Island first
        String worldId = instance.getTag(WorldHandler.worldID);
        if (worldId != null && worldId.length() > 10) {
            return new Region("private_island", RegionType.PRIVATE_ISLAND, new Pos(0,0,0), new Pos(0,0,0));
        }

        Pos pos = Pos.fromPoint(point);
        Region smallest = null;
        long smallestVolume = Long.MAX_VALUE;

        for (Region region : regions.values()) {
            if (region.isInside(pos)) {
                long volume = region.getVolume();
                if (volume < smallestVolume) {
                    smallestVolume = volume;
                    smallest = region;
                }
            }
        }

        return smallest;
    }

    public static List<Region> getAllRegions() {
        return new ArrayList<>(regions.values());
    }
}
