package fun.ascent.skyblock.entity.mob.ai;

import fun.ascent.skyblock.world.region.Region;
import fun.ascent.skyblock.world.region.RegionManager;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RegionWanderGoal extends GoalSelector {

    private static final long WANDER_INTERVAL_MS = 2500L;

    private final int radius;
    private final String zoneId;
    private final Random rng = new Random();

    private long lastWanderTime = 0;
    private List<Pos> wanderNodes = null;

    public RegionWanderGoal(@NotNull EntityCreature creature, int radius, String zoneId) {
        super(creature);
        this.radius = radius;
        this.zoneId = zoneId;
    }

    @Override
    public boolean shouldStart() {
        return System.currentTimeMillis() - lastWanderTime >= WANDER_INTERVAL_MS;
    }

    @Override
    public void start() {
        if (wanderNodes == null) {
            wanderNodes = buildWanderNodes();
        }

        if (wanderNodes.isEmpty()) {
            end();
            return;
        }

        int attempts = Math.min(10, wanderNodes.size());
        while (attempts-- > 0) {
            Pos target = wanderNodes.get(rng.nextInt(wanderNodes.size()));
            if (entityCreature.getNavigator().setPathTo(target)) {
                break;
            }
        }
    }

    @Override
    public void tick(long time) {}

    @Override
    public boolean shouldEnd() {
        return true;
    }

    @Override
    public void end() {
        lastWanderTime = System.currentTimeMillis();
    }

    private @NotNull List<Pos> buildWanderNodes() {
        List<Pos> nodes = new ArrayList<>();
        Instance instance = entityCreature.getInstance();
        if (instance == null) return nodes;

        Region activeRegion = null;
        for (Region r : RegionManager.getAllRegions()) {
            if (r.getId().equalsIgnoreCase(zoneId)) {
                activeRegion = r;
                break;
            }
        }

        int centerX = entityCreature.getPosition().blockX();
        int centerY = entityCreature.getPosition().blockY();
        int centerZ = entityCreature.getPosition().blockZ();

        // OPTIMIZATION: Instead of scanning 600+ block columns sequentially (which wastes massive CPU),
        // we perform random sampling of 30 target coordinates within the wander radius!
        for (int attempt = 0; attempt < 30; attempt++) {
            int rx = centerX + rng.nextInt(radius * 2 + 1) - radius;
            int rz = centerZ + rng.nextInt(radius * 2 + 1) - radius;

            // Scan elevation offset of +/- 2 blocks to handle stairs, slabs, or minor slopes
            for (int yOffset = -2; yOffset <= 2; yOffset++) {
                int ty = centerY + yOffset;
                Pos candidate = new Pos(rx + 0.5, ty, rz + 0.5);

                // Ensure candidate coordinate is within active zone boundaries
                if (activeRegion != null && !activeRegion.isInside(candidate)) {
                    continue;
                }

                // OPTIMIZATION: Skip unloaded chunks to avoid blocking the main server thread with I/O calls!
                if (!instance.isChunkLoaded(candidate)) {
                    continue;
                }

                Block feetBlock = instance.getBlock(rx, ty, rz);
                Block headBlock = instance.getBlock(rx, ty + 1, rz);
                Block groundBlock = instance.getBlock(rx, ty - 1, rz);

                // Solid surface to walk on, with air above feet and head
                if (feetBlock.isAir() && headBlock.isAir() && !groundBlock.isAir()) {
                    nodes.add(candidate);
                    break;
                }
            }
        }

        return nodes;
    }
}
