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
        // Here we could load from MongoDB, but for now we'll register the core hub regions
        // based on the previous SkyblockLocation hardcoded values.
        
        register(new Region("village", RegionType.VILLAGE, new Pos(-80, 0, -155), new Pos(80, 255, 15)));
        register(new Region("forest", RegionType.FOREST, new Pos(-225, 0, -80), new Pos(-96, 255, 20)));
        register(new Region("farm", RegionType.FARM, new Pos(18, 0, -190), new Pos(93, 255, -100)));
        register(new Region("coal_mine", RegionType.COAL_MINE, new Pos(-40, 55, -235), new Pos(-5, 90, -140)));
        register(new Region("mountain", RegionType.MOUNTAIN, new Pos(-60, 0, 0), new Pos(40, 255, 70)));
        register(new Region("wilderness", RegionType.WILDERNESS, new Pos(35, 0, 0), new Pos(200, 255, 220)));
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
