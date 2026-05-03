package fun.ascent.skyblock.entity.mob;

import fun.ascent.skyblock.entity.display.DroppedItemEntity;
import fun.ascent.skyblock.entity.display.FloatingTextEntity;
import fun.ascent.skyblock.entity.loot.DropTable;
import fun.ascent.skyblock.entity.loot.MobDrop;
import fun.ascent.skyblock.entity.mob.impl.SkinOverride;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.stats.Stats;
import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.metadata.avatar.MannequinMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.player.ResolvableProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public abstract class SkyblockMobEntity extends EntityCreature {

    @Getter
    private static final List<SkyblockMobEntity> activeMobs = new ArrayList<>();

    private FloatingTextEntity nameplate;
    private Component currentNameplate;
    private final AtomicLong lastAttackMillis = new AtomicLong(0);

    protected SkyblockMobEntity(EntityType type) {
        super(type);
        setupBaseAttributes();
        addAIGroup(buildGoals(), buildTargets());

        if (this instanceof SkinOverride skin) {
            applySkin(skin);
        }

        currentNameplate = buildNameplateComponent(baseStat(Stats.HEALTH), baseStat(Stats.HEALTH));
        nameplate = new FloatingTextEntity(currentNameplate, meta -> {});

        onSetup();
    }

    private void setupBaseAttributes() {
        getAttribute(Attribute.MAX_HEALTH).setBaseValue(baseStat(Stats.HEALTH));
        getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue((baseStat(Stats.SPEED) / 1000f) * 2.5f);
        setHealth(baseStat(Stats.HEALTH));
    }

    private void applySkin(SkinOverride skin) {
        if (skin.skinTexture() == null || skin.skinSignature() == null) return;
        editEntityMeta(MannequinMeta.class, meta -> {
            PlayerSkin ps = new PlayerSkin(skin.skinTexture(), skin.skinSignature());
            meta.setProfile(new ResolvableProfile(ps));
        });
    }

    @Override
    public void spawn() {
        super.spawn();
        activeMobs.add(this);
        nameplate.setInstance(getInstance(), nameplatePos());
        onSpawn();
    }

    @Override
    public void updateNewViewer(@NotNull Player player) {
        super.updateNewViewer(player);
        if (nameplate != null) {
            nameplate.updateNewViewer(player);
        }
    }

    @Override
    public boolean damage(@NotNull Damage source) {
        boolean applied = super.damage(source);
        if (!applied) return false;

        if (source.getSource() != null) {
            takeKnockback(
                    0.4f,
                    Math.sin(source.getSource().getPosition().yaw() * Math.PI / 180),
                    -Math.cos(source.getSource().getPosition().yaw() * Math.PI / 180)
            );
        }

        currentNameplate = buildNameplateComponent(getHealth(), baseStat(Stats.HEALTH));
        if (nameplate != null) {
            nameplate.editEntityMeta(TextDisplayMeta.class, meta -> meta.setText(currentNameplate));
        }

        return true;
    }

    @Override
    public void kill() {
        if (nameplate != null) {
            nameplate.remove();
            nameplate = null;
        }

        activeMobs.remove(this);
        super.kill();

        assert getLastDamageSource() != null;
        if (!(getLastDamageSource().getAttacker() instanceof SkyblockPlayer killer)) return;

        onKilledBy(killer);

        DropTable table = dropTable();
        if (table == null || getInstance() == null) return;

        Pos dropPos = getPosition().add(0, 0.5, 0);
        for (MobDrop drop : table.roll()) {
            DroppedItemEntity dropped = new DroppedItemEntity(
                    drop.item().withAmount(drop.rolledAmount()), killer);
            dropped.setInstance(getInstance(), dropPos);
        }
    }

    @Override
    public void tick(long time) {
        Instance world = getInstance();
        if (world == null) return;

        Pos pos = getPosition();
        if (!world.isChunkLoaded(pos)) {
            world.loadChunk(pos).join();
        }

        if (nameplate != null && nameplate.getInstance() != null) {
            nameplate.teleport(nameplatePos());
        }

        pushOverlappingMobs();

        try {
            super.tick(time);
        } catch (Exception ignored) {
        }
    }

    private Pos nameplatePos() {
        return getPosition().add(0, getBoundingBox().height() + nameplateOffset(), 0);
    }

    private void pushOverlappingMobs() {
        for (SkyblockMobEntity other : activeMobs) {
            if (other == this) continue;
            if (other.getInstance() != getInstance()) continue;

            double dx = getPosition().x() - other.getPosition().x();
            double dz = getPosition().z() - other.getPosition().z();
            double distSq = dx * dx + dz * dz;

            if (distSq < 0.0001) continue;

            double combined = (getBoundingBox().width() + other.getBoundingBox().width()) / 2.0 * 1.5;
            if (distSq >= combined * combined) continue;

            double dist = Math.sqrt(distSq);
            double overlap = combined - dist;
            double nx = dx / dist;
            double nz = dz / dist;

            double base = overlap * 1.2;
            double motionMult = 1.0 + Math.max(getVelocity().length(), other.getVelocity().length()) * 2.0;
            double push = Math.min(base * motionMult, 0.8);

            setVelocity(getVelocity().add(nx * push, 0, nz * push));
            other.setVelocity(other.getVelocity().add(-nx * push, 0, -nz * push));
        }
    }

    private Component buildNameplateComponent(float hp, float max) {
        MobCategory primary = categories().getFirst();
        return Component.text(
                "§8[§7Lv" + level() + "§8] "
                        + primary.prefix() + " §c"
                        + displayName()
                        + " §a" + Math.round(hp)
                        + "§f/§a" + Math.round(max)
        );
    }

    public boolean tryAttack() {
        long now = System.currentTimeMillis();
        return lastAttackMillis.getAndUpdate(last ->
                now - last >= attackCooldown() ? now : last
        ) + attackCooldown() <= now;
    }

    public float nameplateOffset() {
        return 0.3f;
    }

    public void onSetup() {}

    public void onSpawn() {}

    public void onKilledBy(SkyblockPlayer killer) {}

    public abstract String displayName();

    public abstract int level();

    public abstract float baseStat(Stats stat);

    public abstract @NonNull List<MobCategory> categories();

    public abstract @NotNull List<GoalSelector> buildGoals();

    public abstract @NotNull List<TargetSelector> buildTargets();

    public abstract long attackCooldown();

    public abstract @Nullable DropTable dropTable();

}