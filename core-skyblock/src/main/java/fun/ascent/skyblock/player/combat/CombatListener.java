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
    
    public static final java.util.Set<UUID> damagedMobs = java.util.concurrent.ConcurrentHashMap.newKeySet();
    private static final Map<UUID, Integer> lethalityStacks = new ConcurrentHashMap<>();
    private static final Map<UUID, Integer> mobHitCounts = new ConcurrentHashMap<>();

    public static int incrementLethality(UUID mobUuid) {
        return lethalityStacks.merge(mobUuid, 1, (old, val) -> Math.min(4, old + 1));
    }

    public static int getHitCount(UUID mobUuid) {
        return mobHitCounts.getOrDefault(mobUuid, 0);
    }

    public static void incrementHitCount(UUID mobUuid) {
        mobHitCounts.merge(mobUuid, 1, (old, val) -> old + 1);
    }

    public static void cleanMobData(UUID mobUuid) {
        damagedMobs.remove(mobUuid);
        lethalityStacks.remove(mobUuid);
        mobHitCounts.remove(mobUuid);
    }

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
        if (mob.isDead()) return;

        long now = System.currentTimeMillis();
        
        double attackSpeed = playerStat(player, Stats.ATTACK_SPEED);
        long attackCooldownMs = (long) (500.0 / (1.0 + attackSpeed / 100.0));
        
        if (now - attackCooldowns.getOrDefault(player.getUuid(), 0L) < attackCooldownMs) return;
        attackCooldowns.put(player.getUuid(), now);

        net.minestom.server.item.ItemStack held = player.getEquipment(net.minestom.server.entity.EquipmentSlot.MAIN_HAND);
        boolean isFirstHit = damagedMobs.add(mob.getUuid());

        CombatCalculator.CombatResult result = CombatCalculator.playerHitsMob(player, mob, held, isFirstHit);

        player.playSound(Sound.sound(
                Key.key("entity." + mob.getEntityType().name().toLowerCase().replace("minecraft:", "") + ".hurt"),
                Sound.Source.PLAYER, 1f, 1f
        ), Sound.Emitter.self());

        if (mob.getInstance() != null) {
            DamageIndicator.spawn(mob.getInstance(), mob.getPosition(), result.damage(), result.isCrit());
        }

        mob.damage(new Damage(DamageType.PLAYER_ATTACK, player, player, player.getPosition(), result.damageFloat()));

        int lifeStealLevel = fun.ascent.skyblock.enchantment.EnchantmentNBT.getEnchantmentLevel(held, fun.ascent.skyblock.enchantment.EnchantmentRegistry.LIFE_STEAL);
        if (lifeStealLevel > 0) {
            double maxHealth = player.maxStat(Stats.HEALTH);
            double healAmount = maxHealth * (0.005 * lifeStealLevel);
            if (healAmount > 0) {
                player.addHealth(healAmount);
                player.playSound(Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.PLAYER, 0.4f, 1.4f), Sound.Emitter.self());
                ActionBar.of(player.getUuid()).addReplacement(
                        ActionBar.Section.HEALTH,
                        "<green>+" + Math.round(healAmount) + "❤ (Life Steal)",
                        20, 10
                );
            }
        }

        if (result.isCrit()) {
            int syphonLevel = fun.ascent.skyblock.enchantment.EnchantmentNBT.getEnchantmentLevel(held, fun.ascent.skyblock.enchantment.EnchantmentRegistry.SYPHON);
            if (syphonLevel > 0) {
                double critDamage = playerStat(player, Stats.CRITICAL_DAMAGE);
                double multiplier = critDamage / 10.0;
                double maxHealth = player.maxStat(Stats.HEALTH);
                double healAmount = maxHealth * (0.001 * syphonLevel) * multiplier;
                if (healAmount > 0) {
                    player.addHealth(healAmount);
                    player.playSound(Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.PLAYER, 0.4f, 1.4f), Sound.Emitter.self());
                    ActionBar.of(player.getUuid()).addReplacement(
                            ActionBar.Section.HEALTH,
                            "<green>+" + Math.round(healAmount) + "❤ (Syphon)",
                            20, 10
                    );
                }
            }
        }

        int thunderlordLevel = fun.ascent.skyblock.enchantment.EnchantmentNBT.getEnchantmentLevel(held, fun.ascent.skyblock.enchantment.EnchantmentRegistry.THUNDERLORD);
        if (thunderlordLevel > 0) {
            int currentHits = getHitCount(mob.getUuid()) + 1;
            if (currentHits % 3 == 0) {
                double magicDmg = thunderlordLevel * 100.0;
                mob.damage(new Damage(DamageType.MAGIC, player, player, player.getPosition(), (float) magicDmg));
                if (mob.getInstance() != null) {
                    net.minestom.server.entity.Entity lightning = new net.minestom.server.entity.Entity(net.minestom.server.entity.EntityType.LIGHTNING_BOLT);
                    lightning.setInstance(mob.getInstance(), mob.getPosition());
                }
                player.playSound(Sound.sound(Key.key("entity.lightning_bolt.thunder"), Sound.Source.PLAYER, 0.4f, 1.2f), Sound.Emitter.self());
            }
        }

        int cleaveLevel = fun.ascent.skyblock.enchantment.EnchantmentNBT.getEnchantmentLevel(held, fun.ascent.skyblock.enchantment.EnchantmentRegistry.CLEAVE);
        if (cleaveLevel > 0 && mob.getInstance() != null) {
            double cleaveDamage = result.damage() * (cleaveLevel * 0.03);
            if (cleaveDamage > 0) {
                mob.getInstance().getNearbyEntities(mob.getPosition(), 3.0).stream()
                        .filter(e -> e instanceof SkyblockMobEntity && !e.equals(mob))
                        .forEach(e -> {
                            SkyblockMobEntity targetMob = (SkyblockMobEntity) e;
                            targetMob.damage(new Damage(DamageType.PLAYER_ATTACK, player, player, player.getPosition(), (float) cleaveDamage));
                            DamageIndicator.spawn(targetMob.getInstance(), targetMob.getPosition(), cleaveDamage, false);
                        });
            }
        }

        if (mob.isDead()) {
            net.minestom.server.MinecraftServer.getGlobalEventHandler().call(new fun.ascent.skyblock.events.PlayerKillMobEvent(player, mob));
            int scavengerLevel = fun.ascent.skyblock.enchantment.EnchantmentNBT.getEnchantmentLevel(held, fun.ascent.skyblock.enchantment.EnchantmentRegistry.SCAVENGER);
            if (scavengerLevel > 0) {
                double extraCoins = scavengerLevel * 1.5;
                player.addCoins(extraCoins);
                player.sendMessage(fun.ascent.common.StringUtility.text("<gold>+ " + extraCoins + " Coins (Scavenger)"));
            }

            int vampirismLevel = fun.ascent.skyblock.enchantment.EnchantmentNBT.getEnchantmentLevel(held, fun.ascent.skyblock.enchantment.EnchantmentRegistry.VAMPIRISM);
            if (vampirismLevel > 0) {
                double maxHealth = player.maxStat(Stats.HEALTH);
                double healAmount = (maxHealth - player.getCurrentHealth()) * (vampirismLevel * 0.01);
                if (healAmount > 0) {
                    player.addHealth(healAmount);
                    player.playSound(Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.PLAYER, 0.4f, 1.4f), Sound.Emitter.self());
                    ActionBar.of(player.getUuid()).addReplacement(
                            ActionBar.Section.HEALTH,
                            "<green>+" + Math.round(healAmount) + "❤ (Vampirism)",
                            20, 10
                    );
                }
            }
        }

        incrementHitCount(mob.getUuid());

        double ferocity = playerStat(player, Stats.FEROCITY);
        if (ferocity > 0) {
            int extraAttacks = (int) (ferocity / 100);
            double extraAttackChance = (ferocity % 100) / 100.0;

            for (int i = 0; i < extraAttacks; i++) {
                triggerFerocityStrike(player, mob, result, held);
            }
            if (Math.random() < extraAttackChance) {
                triggerFerocityStrike(player, mob, result, held);
            }
        }
    }

    private static void triggerFerocityStrike(SkyblockPlayer player, SkyblockMobEntity mob, CombatCalculator.CombatResult result, net.minestom.server.item.ItemStack held) {
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

        if (mob.isDead()) {
            net.minestom.server.MinecraftServer.getGlobalEventHandler().call(new fun.ascent.skyblock.events.PlayerKillMobEvent(player, mob));
            int scavengerLevel = fun.ascent.skyblock.enchantment.EnchantmentNBT.getEnchantmentLevel(held, fun.ascent.skyblock.enchantment.EnchantmentRegistry.SCAVENGER);
            if (scavengerLevel > 0) {
                double extraCoins = scavengerLevel * 1.5;
                player.addCoins(extraCoins);
                player.sendMessage(fun.ascent.common.StringUtility.text("<gold>+ " + extraCoins + " Coins (Scavenger)"));
            }

            int vampirismLevel = fun.ascent.skyblock.enchantment.EnchantmentNBT.getEnchantmentLevel(held, fun.ascent.skyblock.enchantment.EnchantmentRegistry.VAMPIRISM);
            if (vampirismLevel > 0) {
                double maxHealth = player.maxStat(Stats.HEALTH);
                double healAmount = (maxHealth - player.getCurrentHealth()) * (vampirismLevel * 0.01);
                if (healAmount > 0) {
                    player.addHealth(healAmount);
                    player.playSound(Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.PLAYER, 0.4f, 1.4f), Sound.Emitter.self());
                    ActionBar.of(player.getUuid()).addReplacement(
                            ActionBar.Section.HEALTH,
                            "<green>+" + Math.round(healAmount) + "❤ (Vampirism)",
                            20, 10
                    );
                }
            }
        }
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
        player.triggerStatus((byte) 2);

        double dx = player.getPosition().x() - mob.getPosition().x();
        double dz = player.getPosition().z() - mob.getPosition().z();
        double dist = Math.sqrt(dx * dx + dz * dz);
        if (dist > 0.0) {
            double kx = dx / dist;
            double kz = dz / dist;
            player.setVelocity(new net.minestom.server.coordinate.Vec(kx * 4.5, 3.0, kz * 4.5));
        } else {
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
