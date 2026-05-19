package fun.ascent.skyblock.player.combat;

import fun.ascent.skyblock.entity.mob.SkyblockMobEntity;
import fun.ascent.skyblock.events.EventManager;
import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.actionbar.ActionBar;
import fun.ascent.skyblock.player.stats.Stats;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.entity.EntityAttackEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CombatListener {

    private static final Map<UUID, Long> attackCooldowns = new ConcurrentHashMap<>();

    public static void register() {
        EventManager.registerEvent(new SEvent<EntityAttackEvent>() {
            @Override
            public void onEvent(EntityAttackEvent event) {
                if (event.getEntity() instanceof SkyblockPlayer player
                        && event.getTarget() instanceof SkyblockMobEntity mob) {
                    handlePlayerHitsMob(player, mob);
                }
                if (event.getEntity() instanceof SkyblockMobEntity mob
                        && event.getTarget() instanceof SkyblockPlayer player) {
                    handleMobHitsPlayer(mob, player);
                }
            }
        });
    }

    private static void handlePlayerHitsMob(SkyblockPlayer player, SkyblockMobEntity mob) {
        long now = System.currentTimeMillis();
        
        // DYNAMIC ATTACK SPEED COOLDOWN
        double attackSpeed = playerStat(player, Stats.ATTACK_SPEED);
        long attackCooldownMs = (long) (500.0 / (1.0 + attackSpeed / 100.0));
        
        if (now - attackCooldowns.getOrDefault(player.getUuid(), 0L) < attackCooldownMs) return;
        attackCooldowns.put(player.getUuid(), now);

        CombatCalculator.CombatResult result = CombatCalculator.playerHitsMob(player, mob);

        player.playSound(Sound.sound(
                Key.key("entity." + mob.getEntityType().name().toLowerCase().replace("minecraft:", "") + ".hurt"),
                Sound.Source.PLAYER, 1f, 1f
        ), Sound.Emitter.self());

        if (mob.getInstance() != null) {
            DamageIndicator.spawn(mob.getInstance(), mob.getPosition(), result.damage(), result.isCrit());
        }

        mob.damage(new Damage(DamageType.PLAYER_ATTACK, player, player, player.getPosition(), result.damageFloat()));

        // FEROCITY MULTI-HIT SYSTEM
        double ferocity = playerStat(player, Stats.FEROCITY);
        if (ferocity > 0) {
            int extraAttacks = (int) (ferocity / 100);
            double extraAttackChance = (ferocity % 100) / 100.0;

            for (int i = 0; i < extraAttacks; i++) {
                triggerFerocityStrike(player, mob, result);
            }
            if (Math.random() < extraAttackChance) {
                triggerFerocityStrike(player, mob, result);
            }
        }
    }

    private static void triggerFerocityStrike(SkyblockPlayer player, SkyblockMobEntity mob, CombatCalculator.CombatResult result) {
        if (mob.isDead()) return;

        net.minestom.server.coordinate.Pos offsetPos = mob.getPosition().add(
                (Math.random() - 0.5) * 0.5,
                0.2 + Math.random() * 0.3,
                (Math.random() - 0.5) * 0.5
        );

        if (mob.getInstance() != null) {
            DamageIndicator.spawn(mob.getInstance(), offsetPos, result.damage(), result.isCrit());
        }

        player.playSound(Sound.sound(
                Key.key("entity.player.attack.sweep"),
                Sound.Source.PLAYER, 0.85f, 1.4f
        ), Sound.Emitter.self());

        mob.damage(new Damage(DamageType.PLAYER_ATTACK, player, player, player.getPosition(), result.damageFloat()));
    }

    private static void handleMobHitsPlayer(SkyblockMobEntity mob, SkyblockPlayer player) {
        if (!mob.tryAttack()) return;

        double rawDamage = mob.baseStat(Stats.DAMAGE);
        double defense = player.playerStat(Stats.DEFENSE);
        double actualDamage = defense > 0
                ? rawDamage * (1.0 - defense / (defense + 100.0))
                : rawDamage;
        actualDamage = Math.max(1, actualDamage);

        if (player.getInstance() != null) {
            DamageIndicator.spawn(player.getInstance(), player.getPosition(), actualDamage, false);
        }

        player.removeHealth(actualDamage);

        // Visual damage red-flash, tilt, and grunt sound client-side
        player.triggerStatus((byte) 2);

        // Apply physical knockback velocity away from the attacking mob
        double dx = player.getPosition().x() - mob.getPosition().x();
        double dz = player.getPosition().z() - mob.getPosition().z();
        double dist = Math.sqrt(dx * dx + dz * dz);
        if (dist > 0.0) {
            double kx = dx / dist;
            double kz = dz / dist;
            player.setVelocity(new net.minestom.server.coordinate.Vec(kx * 4.5, 3.0, kz * 4.5));
        } else {
            // Fallback direction if perfectly overlapping
            player.setVelocity(new net.minestom.server.coordinate.Vec(0.0, 3.0, 4.5));
        }

        double absorbed = rawDamage - actualDamage;
        if (absorbed > 0) {
            ActionBar.of(player.getUuid()).addReplacement(
                    ActionBar.Section.DEFENSE,
                    "<green>+" + String.format("%,d", Math.round(absorbed)) + "❈",
                    20, 10
            );
        }
    }

    private static double playerStat(SkyblockPlayer player, Stats stat) {
        if (player.getActiveProfileData() == null) return stat.getBaseStat();

        java.util.Map<String, fun.ascent.skyblock.player.stats.Stat> stats = player.getActiveProfileData().stats;
        fun.ascent.skyblock.player.stats.Stat entry = stats.get(stat.name().toLowerCase());
        return entry != null ? entry.getCurValue() : stat.getBaseStat();
    }
}
