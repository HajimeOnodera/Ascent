package fun.ascent.skyblock.entity.mob.ai;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RegionWanderGoal extends GoalSelector {

    private static final long WANDER_INTERVAL = 2500L;

    private final String zoneId;
    private final int radius;
    private final Random rng = new Random();

    private long lastWander = 0;
    private List<Vec> reachable = null;

    public RegionWanderGoal(@NotNull EntityCreature creature, int radius, String zoneId) {
        super(creature);
        this.zoneId = zoneId;
        this.radius = radius;
    }

    @Override
    public boolean shouldStart() {
        return System.currentTimeMillis() - lastWander >= WANDER_INTERVAL;
    }

    @Override
    public void start() {
        if (reachable == null) {
            reachable = buildReachableOffsets();
        }

        if (reachable.isEmpty()) {
            end();
            return;
        }

        int attempts = reachable.size();
        while (attempts-- > 0) {
            Vec offset = reachable.get(rng.nextInt(reachable.size()));
            Pos target = entityCreature.getPosition().add(offset);
            if (entityCreature.getNavigator().setPathTo(target)) break;
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
        lastWander = System.currentTimeMillis();
    }

    private @NotNull List<Vec> buildReachableOffsets() {
        List<Vec> result = new ArrayList<>();

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                int bx = entityCreature.getPosition().blockX() + x;
                int by = entityCreature.getPosition().blockY();
                int bz = entityCreature.getPosition().blockZ() + z;

                Pos candidate = new Pos(bx, by, bz);

                if (entityCreature.getInstance() != null) {
                    if (!entityCreature.getInstance().isChunkLoaded(candidate)) {
                        entityCreature.getInstance().loadChunk(candidate).join();
                    }
                    Block block = entityCreature.getInstance().getBlock(bx, by, bz);
                    if (!block.isAir()) continue;
                }

                result.add(new Vec(x, by, z));
            }
        }

        return result;
    }
}