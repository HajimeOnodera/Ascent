package fun.ascent.skyblock.player.combat;

import fun.ascent.skyblock.entity.mob.SkyblockMobEntity;
import fun.ascent.skyblock.events.EventManager;
import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.entity.EntityAttackEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CombatListener {

    private static final long PLAYER_ATTACK_COOLDOWN_MS = 500;
    private static final Map<UUID, Long> playerAttackCooldowns = new ConcurrentHashMap<>();

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
        long last = playerAttackCooldowns.getOrDefault(player.getUuid(), 0L);
        if (now - last < PLAYER_ATTACK_COOLDOWN_MS) return;
        playerAttackCooldowns.put(player.getUuid(), now);

        CombatCalculator.CombatResult result = CombatCalculator.playerHitsMob(player, mob);

        player.playSound(Sound.sound(
                Key.key("entity." + mob.getEntityType().name().toLowerCase().replace("minecraft:", "") + ".hurt"),
                Sound.Source.PLAYER, 1f, 1f
        ), Sound.Emitter.self());

        if (mob.getInstance() != null) {
            DamageIndicator.spawn(mob.getInstance(), mob.getPosition(), result.damage(), result.isCrit());
        }

        mob.damage(new Damage(DamageType.PLAYER_ATTACK, player, player, player.getPosition(), result.damageFloat()));
    }

    private static void handleMobHitsPlayer(SkyblockMobEntity mob, SkyblockPlayer player) {
        if (!mob.tryAttack()) return;

        CombatCalculator.CombatResult result = CombatCalculator.mobHitsPlayer(mob, player);

        if (player.getInstance() != null) {
            DamageIndicator.spawn(player.getInstance(), player.getPosition(), result.damage(), false);
        }

        player.damage(new Damage(DamageType.MOB_ATTACK, mob, mob, mob.getPosition(), result.damageFloat()));
    }
}