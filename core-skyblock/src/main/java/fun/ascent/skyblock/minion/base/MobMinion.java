package fun.ascent.skyblock.minion.base;

import fun.ascent.skyblock.minion.model.MinionTask;
import fun.ascent.skyblock.minion.model.MinionType;
import fun.ascent.skyblock.minion.visual.MinionAnimation;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;

import java.util.List;
import java.util.UUID;

public abstract class MobMinion extends SkyblockMinion {
    protected MobMinion(UUID ownerUuid, MinionType type, int tier, Instance instance, Pos position) {
        super(ownerUuid, type, tier, instance, position);
    }

    @Override
    protected void performAction() {
        pruneSpawnedMobs();
        Pos spawnPos = findMobSpawnPosition();
        if (spawnPos != null && getProfile().mobEntityType() != null) {
            setCurrentTask(MinionTask.SPAWN);
            MinionAnimation.animatePlace(this, spawnPos, () -> {
                LivingEntity spawnedMob = new LivingEntity(getProfile().mobEntityType());
                spawnedMob.setInstance(getInstance(), spawnPos.add(0.5, 0, 0.5));
                addSpawnedMob(spawnedMob);
            });
            return;
        }
        LivingEntity targetMob = findMobToKill();
        if (targetMob == null) {
            return;
        }
        setCurrentTask(MinionTask.SLAY);
        List<ItemStack> drops = getProfile().createHarvestDrops();
        if (!canStoreDrops(drops)) {
            return;
        }
        Pos target = targetMob.getPosition();
        MinionAnimation.animateKill(this, target, () -> {
            targetMob.remove();
            removeSpawnedMob(targetMob);
            storeDrops(drops);
        });
    }
}
