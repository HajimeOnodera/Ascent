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

public class SeaWitch extends SkyblockMobEntity {

    public SeaWitch() {
        super(EntityType.WITCH);
        setCustomName(text("<aqua>[Lv15] Sea Witch"));
        setCustomNameVisible(true);
    }

    @Override
    public int level() {
        return 15;
    }

    @Override
    public float baseStat(Stats stat) {
        return switch (stat) {
            case HEALTH -> 8000f;
            case DAMAGE -> 180f;
            case SPEED -> 60f;
            default -> 0f;
        };
    }

    @Override
    public String displayName() {
        return "Sea Witch";
    }

    @Override
    public @NonNull List<MobCategory> categories() {
        return List.of(MobCategory.AQUATIC, MobCategory.ARCANE);
    }

    @Override
    public @NotNull List<GoalSelector> buildGoals() {
        return List.of(new MeleeChaseGoal(this, 1.6, 20));
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
        return 700L;
    }

    @Override
    public @Nullable DropTable dropTable() {
        return new DropTable() {
            @Override
            public List<MobDrop> drops() {
                return List.of(
                        new MobDrop(ItemStack.of(Material.SPIDER_EYE), 80.0, 1, 2),
                        new MobDrop(ItemStack.of(Material.GLASS_BOTTLE), 50.0, 1, 1)
                );
            }
        };
    }
}
