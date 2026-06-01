package fun.ascent.skyblock.entity.mob.mobs.graveyard;

import fun.ascent.skyblock.entity.loot.DropTable;
import fun.ascent.skyblock.entity.loot.MobDrop;
import fun.ascent.skyblock.entity.mob.MobCategory;
import fun.ascent.skyblock.entity.mob.SkyblockMobEntity;
import fun.ascent.skyblock.entity.mob.ai.LastAttackerTarget;
import fun.ascent.skyblock.entity.mob.ai.MeleeChaseGoal;
import fun.ascent.skyblock.entity.mob.ai.NearestPlayerTarget;
import fun.ascent.skyblock.entity.mob.ai.RegionWanderGoal;
import fun.ascent.skyblock.player.stats.Stats;
import lombok.NonNull;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GraveyardZombie extends SkyblockMobEntity {

    private static final String ZONE = "graveyard";

    public GraveyardZombie() {
        super(EntityType.ZOMBIE);
    }

    @Override
    public String displayName() {
        return "Graveyard Zombie";
    }

    @Override
    public int level() {
        return 1;
    }

    @Override
    public float baseStat(Stats stat) {
        return switch (stat) {
            case HEALTH -> 100f;
            case DAMAGE -> 20f;
            case SPEED -> 55f;
            default -> 0f;
        };
    }

    @Override
    public @NonNull List<MobCategory> categories() {
        return List.of(MobCategory.UNDEAD);
    }

    @Override
    public @NotNull List<GoalSelector> buildGoals() {
        return List.of(
                new MeleeChaseGoal(this, 1.8, 20),
                new RegionWanderGoal(this, 12, ZONE)
        );
    }

    @Override
    public @NotNull List<TargetSelector> buildTargets() {
        return List.of(
                new LastAttackerTarget(this, 10),
                new NearestPlayerTarget(this, 6)
        );
    }

    @Override
    public long attackCooldown() {
        return 500L;
    }

    @Override
    public double combatXpReward() {
        return 6.0;
    }

    @Override
    public double coinsReward() {
        return 1.0;
    }

    @Override
    public int xpOrbReward() {
        return 1;
    }

    @Override
    public @Nullable DropTable dropTable() {
        return new DropTable() {
            @Override
            public List<MobDrop> drops() {
                return List.of(
                        new MobDrop(ItemStack.of(Material.ROTTEN_FLESH), 100.0, 1, 1),
                        new MobDrop(ItemStack.of(Material.POISONOUS_POTATO), 2.0, 1, 1),
                        new MobDrop(ItemStack.of(Material.CARROT), 1.0, 1, 1),
                        new MobDrop(ItemStack.of(Material.POTATO), 1.0, 1, 1)
                );
            }
        };
    }
}
