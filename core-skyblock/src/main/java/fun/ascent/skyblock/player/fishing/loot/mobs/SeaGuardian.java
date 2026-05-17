package fun.ascent.skyblock.player.fishing.loot.mobs;

import fun.ascent.skyblock.entity.loot.DropTable;
import fun.ascent.skyblock.entity.loot.MobDrop;
import fun.ascent.skyblock.entity.mob.MobCategory;
import fun.ascent.skyblock.entity.mob.SkyblockMobEntity;
import fun.ascent.skyblock.entity.mob.ai.LastAttackerTarget;
import fun.ascent.skyblock.entity.mob.ai.MeleeChaseGoal;
import fun.ascent.skyblock.entity.mob.ai.NearestPlayerTarget;
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

import static fun.ascent.common.StringUtility.text;

public class SeaGuardian extends SkyblockMobEntity {

    public SeaGuardian() {
        super(EntityType.GUARDIAN);
        setCustomName(text("<aqua>[Lv10] Sea Guardian"));
        setCustomNameVisible(true);
    }

    @Override
    public int level() {
        return 10;
    }

    @Override
    public float baseStat(Stats stat) {
        return switch (stat) {
            case HEALTH -> 5000f;
            case DAMAGE -> 120f;
            case SPEED -> 55f;
            default -> 0f;
        };
    }

    @Override
    public String displayName() {
        return "Sea Guardian";
    }

    @Override
    public @NonNull List<MobCategory> categories() {
        return List.of(MobCategory.AQUATIC);
    }

    @Override
    public @NotNull List<GoalSelector> buildGoals() {
        return List.of(new MeleeChaseGoal(this, 1.5, 20));
    }

    @Override
    public @NotNull List<TargetSelector> buildTargets() {
        return List.of(
                new LastAttackerTarget(this, 10),
                new NearestPlayerTarget(this, 8)
        );
    }

    @Override
    public long attackCooldown() {
        return 800L;
    }

    @Override
    public @Nullable DropTable dropTable() {
        return new DropTable() {
            @Override
            public List<MobDrop> drops() {
                return List.of(
                        new MobDrop(ItemStack.of(Material.PRISMARINE_SHARD), 100.0, 1, 4),
                        new MobDrop(ItemStack.of(Material.PRISMARINE_CRYSTALS), 50.0, 1, 2)
                );
            }
        };
    }
}
