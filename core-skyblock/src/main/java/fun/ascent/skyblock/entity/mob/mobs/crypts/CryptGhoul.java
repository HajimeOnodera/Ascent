package fun.ascent.skyblock.entity.mob.mobs.crypts;

import fun.ascent.skyblock.entity.loot.DropTable;
import fun.ascent.skyblock.entity.loot.MobDrop;
import fun.ascent.skyblock.entity.mob.MobCategory;
import fun.ascent.skyblock.entity.mob.SkyblockMobEntity;
import fun.ascent.skyblock.entity.mob.ai.LastAttackerTarget;
import fun.ascent.skyblock.entity.mob.ai.MeleeChaseGoal;
import fun.ascent.skyblock.entity.mob.ai.NearestPlayerTarget;
import fun.ascent.skyblock.entity.mob.ai.RegionWanderGoal;
import fun.ascent.skyblock.entity.mob.impl.SpotSpawner;
import fun.ascent.skyblock.player.stats.Stats;
import lombok.NonNull;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CryptGhoul extends SkyblockMobEntity implements SpotSpawner {
    private static final String ZONE = "crypts";

    public CryptGhoul() {
        super(EntityType.ZOMBIE);
        setEquipment(EquipmentSlot.CHESTPLATE, ItemStack.of(Material.CHAINMAIL_CHESTPLATE));
        setEquipment(EquipmentSlot.LEGGINGS, ItemStack.of(Material.CHAINMAIL_LEGGINGS));
        setEquipment(EquipmentSlot.BOOTS, ItemStack.of(Material.CHAINMAIL_BOOTS));
        setEquipment(EquipmentSlot.MAIN_HAND, ItemStack.of(Material.IRON_SWORD));
    }

    @Override
    public String displayName() {
        return "Crypt Ghoul";
    }

    @Override
    public int level() {
        return 30;
    }

    @Override
    public float baseStat(Stats stat) {
        return switch (stat) {
            case HEALTH -> 2000f;
            case DAMAGE -> 350f;
            case SPEED -> 75f;
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
        return 36.0;
    }

    @Override
    public double coinsReward() {
        return 13.0;
    }

    @Override
    public int xpOrbReward() {
        return 30;
    }

    @Override
    public @Nullable DropTable dropTable() {
        return new DropTable() {
            @Override
            public List<MobDrop> drops() {
                return List.of(
                        new MobDrop(ItemStack.of(Material.ROTTEN_FLESH), 100.0, 1, 2));
            }
        };
    }

    @Override
    public SpotConfig spotConfig() {
        return SpotConfig.builder()
                .collectionName("crypt_spawn_spots")
                .spawnDelaySeconds(14)
                .maxPerSpot(2)
                .build();
    }
}
