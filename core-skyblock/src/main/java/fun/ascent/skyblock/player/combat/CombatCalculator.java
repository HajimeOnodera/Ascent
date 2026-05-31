package fun.ascent.skyblock.player.combat;

import fun.ascent.skyblock.enchantment.EnchantmentNBT;
import fun.ascent.skyblock.enchantment.EnchantmentRegistry;
import fun.ascent.skyblock.entity.mob.SkyblockMobEntity;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.stats.Stats;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.ItemStack;

public class CombatCalculator {

    public static CombatResult playerHitsMob(SkyblockPlayer player, SkyblockMobEntity mob, ItemStack held, boolean isFirstHit) {
        double weaponDamage = playerStat(player, Stats.DAMAGE);
        double strength = playerStat(player, Stats.STRENGTH);
        double critChance = playerStat(player, Stats.CRITICAL_CHANCE);
        double critDamage = playerStat(player, Stats.CRITICAL_DAMAGE);
        double defense = mob.baseStat(Stats.DEFENSE);

        int criticalLevel = EnchantmentNBT.getEnchantmentLevel(held, EnchantmentRegistry.CRITICAL);
        if (criticalLevel > 0) {
            critDamage += (criticalLevel * 10.0);
        }

        boolean isCrit = Math.random() * 100.0 <= critChance;

        double strengthMultiplier = 1.0 + strength / 100.0;
        double critMultiplier = isCrit ? 1.0 + critDamage / 100.0 : 1.0;

        double damage = weaponDamage * strengthMultiplier * critMultiplier;
        double enchantBoost = 0.0;

        int sharpnessLevel = EnchantmentNBT.getEnchantmentLevel(held, EnchantmentRegistry.SHARPNESS);
        if (sharpnessLevel > 0) {
            enchantBoost += getSharpnessBoost(sharpnessLevel);
        }

        int firstStrikeLevel = EnchantmentNBT.getEnchantmentLevel(held, EnchantmentRegistry.FIRST_STRIKE);
        if (firstStrikeLevel > 0 && isFirstHit) {
            enchantBoost += (firstStrikeLevel * 0.25);
        }

        int tripleStrikeLevel = EnchantmentNBT.getEnchantmentLevel(held, EnchantmentRegistry.TRIPLE_STRIKE);
        if (tripleStrikeLevel > 0) {
            int hits = CombatListener.getHitCount(mob.getUuid());
            if (hits < 3) {
                enchantBoost += (tripleStrikeLevel * 0.10);
            }
        }

        int giantKillerLevel = EnchantmentNBT.getEnchantmentLevel(held, EnchantmentRegistry.GIANT_KILLER);
        if (giantKillerLevel > 0) {
            double playerMax = player.maxStat(Stats.HEALTH);
            double targetMax = mob.baseStat(Stats.HEALTH);
            if (targetMax > playerMax) {
                double extraPct = ((targetMax - playerMax) / playerMax) * 100.0;
                double multiplier = giantKillerMultiplier(giantKillerLevel);
                double maxCap = giantKillerCap(giantKillerLevel);
                enchantBoost += Math.min(extraPct * multiplier, maxCap) / 100.0;
            }
        }

        int executeLevel = EnchantmentNBT.getEnchantmentLevel(held, EnchantmentRegistry.EXECUTE);
        if (executeLevel > 0) {
            double maxH = mob.baseStat(Stats.HEALTH);
            double curH = mob.getHealth();
            if (maxH > 0) {
                double missingPct = ((maxH - curH) / maxH) * 100.0;
                enchantBoost += (missingPct * executeLevel * 0.2) / 100.0;
            }
        }

        int prosecuteLevel = EnchantmentNBT.getEnchantmentLevel(held, EnchantmentRegistry.PROSECUTE);
        if (prosecuteLevel > 0) {
            double maxH = mob.baseStat(Stats.HEALTH);
            double curH = mob.getHealth();
            if (maxH > 0) {
                double healthPct = (curH / maxH) * 100.0;
                enchantBoost += (healthPct * prosecuteLevel * 0.1) / 100.0;
            }
        }

        int smiteLevel = EnchantmentNBT.getEnchantmentLevel(held, EnchantmentRegistry.SMITE);
        if (smiteLevel > 0 && isUndead(mob.getEntityType())) {
            enchantBoost += (smiteLevel * 0.08);
        }

        int enderSlayerLevel = EnchantmentNBT.getEnchantmentLevel(held, EnchantmentRegistry.ENDER_SLAYER);
        if (enderSlayerLevel > 0 && isEndMob(mob.getEntityType())) {
            enchantBoost += (enderSlayerLevel * 0.12);
        }

        int cubismLevel = EnchantmentNBT.getEnchantmentLevel(held, EnchantmentRegistry.CUBISM);
        if (cubismLevel > 0 && isCubismMob(mob.getEntityType())) {
            enchantBoost += (cubismLevel * 0.10);
        }

        damage *= (1.0 + enchantBoost);

        int lethalityLevel = EnchantmentNBT.getEnchantmentLevel(held, EnchantmentRegistry.LETHALITY);
        if (lethalityLevel > 0) {
            int stacks = CombatListener.incrementLethality(mob.getUuid());
            double defenseReduction = stacks * lethalityLevel * 0.03;
            defense *= (1.0 - defenseReduction);
        }

        if (defense > 0) {
            damage *= 1.0 - (defense / (defense + 100.0));
        }

        return new CombatResult(Math.max(1.0, damage), isCrit);
    }

    public static CombatResult mobHitsPlayer(SkyblockMobEntity mob, SkyblockPlayer player) {
        double mobDamage = mob.baseStat(Stats.DAMAGE);
        double defense = playerStat(player, Stats.DEFENSE);

        double damage = mobDamage;
        if (defense > 0) {
            damage *= 1 - (defense / (defense + 100));
        }

        return new CombatResult(Math.max(1, damage), false);
    }

    private static double getSharpnessBoost(int level) {
        if (level == 1) return 0.05;
        if (level == 2) return 0.10;
        if (level == 3) return 0.15;
        if (level == 4) return 0.20;
        if (level == 5) return 0.30;
        if (level == 6) return 0.45;
        if (level >= 7) return 0.65;
        return 0.0;
    }

    private static double giantKillerMultiplier(int level) {
        if (level == 1) return 0.1;
        if (level == 2) return 0.2;
        if (level == 3) return 0.3;
        if (level == 4) return 0.4;
        if (level == 5) return 0.6;
        if (level == 6) return 0.9;
        if (level >= 7) return 1.2;
        return 0.0;
    }

    private static double giantKillerCap(int level) {
        if (level == 1) return 5.0;
        if (level == 2) return 10.0;
        if (level == 3) return 15.0;
        if (level == 4) return 20.0;
        if (level == 5) return 30.0;
        if (level == 6) return 45.0;
        if (level >= 7) return 65.0;
        return 0.0;
    }

    private static boolean isUndead(EntityType type) {
        return type == EntityType.ZOMBIE || type == EntityType.SKELETON || type == EntityType.WITHER_SKELETON || type == EntityType.ZOMBIE_VILLAGER || type == EntityType.ZOMBIFIED_PIGLIN || type == EntityType.STRAY;
    }

    private static boolean isEndMob(EntityType type) {
        return type == EntityType.ENDERMAN || type == EntityType.ENDERMITE || type == EntityType.ENDER_DRAGON;
    }

    private static boolean isCubismMob(EntityType type) {
        return type == EntityType.SLIME || type == EntityType.MAGMA_CUBE || type == EntityType.CREEPER;
    }

    private static double playerStat(SkyblockPlayer player, Stats stat) {
        return player.playerStat(stat);
    }

    public record CombatResult(double damage, boolean isCrit) {
        public float damageFloat() {
            return (float) damage;
        }
    }
}
