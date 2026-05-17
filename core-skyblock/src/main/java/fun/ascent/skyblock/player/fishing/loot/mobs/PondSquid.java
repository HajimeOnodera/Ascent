package fun.ascent.skyblock.player.fishing.loot.mobs;

import fun.ascent.skyblock.entity.loot.DropTable;
import fun.ascent.skyblock.entity.loot.MobDrop;
import fun.ascent.skyblock.entity.mob.MobCategory;
import fun.ascent.skyblock.entity.mob.SkyblockMobEntity;
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

public class PondSquid extends SkyblockMobEntity {

    public PondSquid() {
        super(EntityType.SQUID);
        setCustomName(text("<aqua>[Lv1] Pond Squid"));
        setCustomNameVisible(true);
    }

    @Override
    public int level() {
        return 1;
    }

    @Override
    public float baseStat(Stats stat) {
        return switch (stat) {
            case HEALTH -> 120f;
            case DAMAGE -> 0f;
            case SPEED -> 45f;
            default -> 0f;
        };
    }

    @Override
    public String displayName() {
        return "Pond Squid";
    }

    @Override
    public @NonNull List<MobCategory> categories() {
        return List.of(MobCategory.AQUATIC);
    }

    @Override
    public @NotNull List<GoalSelector> buildGoals() {
        return List.of();
    }

    @Override
    public @NotNull List<TargetSelector> buildTargets() {
        return List.of();
    }

    @Override
    public long attackCooldown() {
        return 1000L;
    }

    @Override
    public @Nullable DropTable dropTable() {
        return new DropTable() {
            @Override
            public List<MobDrop> drops() {
                return List.of(new MobDrop(ItemStack.of(Material.INK_SAC), 100.0, 1, 3));
            }
        };
    }
}
