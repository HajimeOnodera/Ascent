package fun.ascent.skyblock.player.bestiary;

import fun.ascent.skyblock.entity.loot.DropTable;
import fun.ascent.skyblock.entity.loot.MobDrop;
import fun.ascent.skyblock.entity.mob.MobCategory;
import fun.ascent.skyblock.entity.mob.SkyblockMobEntity;
import fun.ascent.skyblock.player.stats.Stats;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.function.Supplier;

public record BestiaryMobType(
        String id,
        Class<? extends SkyblockMobEntity> mobClass,
        Supplier<? extends SkyblockMobEntity> sampleFactory,
        Material iconMaterial,
        String iconTexture
) {

    public boolean matches(SkyblockMobEntity mob) {
        return mobClass.isInstance(mob);
    }

    public SkyblockMobEntity sample() {
        return sampleFactory.get();
    }

    public String displayName() {
        return sample().displayName();
    }

    public int level() {
        return sample().level();
    }

    public int health() {
        return Math.round(sample().baseStat(Stats.HEALTH));
    }

    public int damage() {
        return Math.round(sample().baseStat(Stats.DAMAGE));
    }

    public List<MobCategory> categories() {
        return sample().categories();
    }

    public List<MobDrop> loot() {
        DropTable dropTable = sample().dropTable();
        return dropTable == null ? List.of() : dropTable.drops();
    }
}
