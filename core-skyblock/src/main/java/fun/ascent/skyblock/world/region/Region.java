package fun.ascent.skyblock.world.region;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;

@Getter
@Setter
public class Region {
    private final String id;
    private final RegionType type;
    private final Pos firstLocation;
    private final Pos secondLocation;

    public Region(String id, RegionType type, Pos firstLocation, Pos secondLocation) {
        this.id = id.toLowerCase();
        this.type = type;
        this.firstLocation = firstLocation;
        this.secondLocation = secondLocation;
    }

    public boolean isInside(Pos location) {
        List<Integer> bounds = getBounds();
        double x = location.x();
        double y = location.y();
        double z = location.z();
        
        return x >= (double) bounds.get(0) && x <= (double) bounds.get(1) &&
               y >= (double) bounds.get(2) && y <= (double) bounds.get(3) &&
               z >= (double) bounds.get(4) && z <= (double) bounds.get(5);
    }

    public boolean isInside(Point point) {
        return isInside(Pos.fromPoint(point));
    }

    public List<Integer> getBounds() {
        int sx = Math.min(firstLocation.blockX(), secondLocation.blockX()),
            ex = Math.max(firstLocation.blockX(), secondLocation.blockX()),
            sy = Math.min(firstLocation.blockY(), secondLocation.blockY()),
            ey = Math.max(firstLocation.blockY(), secondLocation.blockY()),
            sz = Math.min(firstLocation.blockZ(), secondLocation.blockZ()),
            ez = Math.max(firstLocation.blockZ(), secondLocation.blockZ());
        return Arrays.asList(sx, ex, sy, ey, sz, ez);
    }

    public long getVolume() {
        List<Integer> bounds = getBounds();
        long x = (long) bounds.get(1) - bounds.get(0) + 1;
        long y = (long) bounds.get(3) - bounds.get(2) + 1;
        long z = (long) bounds.get(5) - bounds.get(4) + 1;
        return x * y * z;
    }
}
