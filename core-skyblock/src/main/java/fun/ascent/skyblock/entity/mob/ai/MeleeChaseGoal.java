
package fun.ascent.skyblock.entity.mob.ai;

import fun.ascent.skyblock.entity.mob.SkyblockMobEntity;
import fun.ascent.skyblock.world.region.Region;
import fun.ascent.skyblock.world.region.RegionManager;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.pathfinding.Navigator;
import net.minestom.server.utils.time.Cooldown;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class MeleeChaseGoal extends GoalSelector {

    private final Cooldown pathCooldown = new Cooldown(Duration.of(5, TimeUnit.SERVER_TICK));

    private final double reach;
    private final Duration attackRate;

    private long lastAttack = 0;
    private boolean finished = false;
    private Entity cached;

    public MeleeChaseGoal(@NotNull EntityCreature creature, double reach, int attackDelayTicks) {
        super(creature);
        this.reach = reach;
        this.attackRate = Duration.of(attackDelayTicks, TimeUnit.SERVER_TICK);
    }

    @Override
    public boolean shouldStart() {
        cached = findTarget();
        return cached != null;
    }

    @Override
    public void start() {
        if (cached != null) {
            entityCreature.getNavigator().setPathTo(cached.getPosition());
        }
    }

    @Override
    public void tick(long time) {
        Entity target = cached != null ? cached : findTarget();
        cached = null;

        if (target == null || target.isRemoved()) {
            finished = true;
            return;
        }

        // Region boundary validation: Stop chasing if target escapes active spawning zone
        if (entityCreature instanceof SkyblockMobEntity mob && mob.getZoneId() != null) {
            Region playerRegion = RegionManager.getRegion(entityCreature.getInstance(), target.getPosition());
            if (playerRegion == null || !playerRegion.getId().equalsIgnoreCase(mob.getZoneId())) {
                finished = true;
                return;
            }
        }

        if (entityCreature.getDistanceSquared(target) <= reach * reach) {
            entityCreature.lookAt(target);

            if (!Cooldown.hasCooldown(time, lastAttack, attackRate) && !entityCreature.isDead()) {
                entityCreature.attack(target, true);
                lastAttack = time;
            }
            return;
        }

        Navigator nav = entityCreature.getNavigator();
        net.minestom.server.coordinate.Point pathPos = nav.getPathPosition();
        Pos targetPos = target.getPosition();

        // FIXED NULL CHECK: Fixes NullPointerException if pathPos is null
        if (pathPos == null || !pathPos.samePoint(targetPos)) {
            if (pathCooldown.isReady(time)) {
                pathCooldown.refreshLastUpdate(time);
                nav.setPathTo(targetPos);
            }
        }
    }

    @Override
    public boolean shouldEnd() {
        return finished;
    }

    @Override
    public void end() {
        finished = false;
        entityCreature.getNavigator().setPathTo(null);
    }
}
