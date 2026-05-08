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

    private static final long ATTACK_COOLDOWN_MS = 500;
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
        if (now - attackCooldowns.getOrDefault(player.getUuid(), 0L) < ATTACK_COOLDOWN_MS) return;
        attackCooldowns.put(player.getUuid(), now);

        CombatCalculator.CombatResult result = CombatCalculator.playerHitsMob(player, mob);

        player.playSound(Sound.sound(
                Key.key("entity." + mob.getEntityType().name().toLowerCase().replace("minecraft:", "") + ".hurt"),
                Sound.Source.PLAYER, 1f, 1f
        ), Sound.Emitter.self());

        if (mob.getInstance() != null)
            DamageIndicator.spawn(mob.getInstance(), mob.getPosition(), result.damage(), result.isCrit());

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

        if (player.getInstance() != null)
            DamageIndicator.spawn(player.getInstance(), player.getPosition(), actualDamage, false);

        player.removeHealth(actualDamage);

        double absorbed = rawDamage - actualDamage;
        if (absorbed > 0) {
            ActionBar.of(player.getUuid()).addReplacement(
                    ActionBar.Section.DEFENSE,
                    "<green>+" + String.format("%,d", Math.round(absorbed)) + "❈",
                    20, 10
            );
        }
    }
}
