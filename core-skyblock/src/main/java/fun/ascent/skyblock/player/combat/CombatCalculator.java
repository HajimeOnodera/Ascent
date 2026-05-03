package fun.ascent.skyblock.player.combat;

import fun.ascent.skyblock.entity.mob.SkyblockMobEntity;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.stats.Stat;
import fun.ascent.skyblock.player.stats.Stats;

import java.util.Map;

public class CombatCalculator {

    private static final double BASE_WEAPON_DAMAGE = 5.0;

    public static CombatResult playerHitsMob(SkyblockPlayer player, SkyblockMobEntity mob) {
        double strength = playerStat(player, Stats.STRENGTH);
        double critChance = playerStat(player, Stats.CRITICAL_CHANCE);
        double critDamage = playerStat(player, Stats.CRITICAL_DAMAGE);
        double defense = mob.baseStat(Stats.DEFENSE);

        boolean isCrit = Math.random() * 100 <= critChance;

        double damage = BASE_WEAPON_DAMAGE
                * (1 + strength / 100)
                * (isCrit ? 1 + critDamage / 100 : 1);

        if (defense > 0) {
            damage *= 1 - (defense / (defense + 100));
        }

        return new CombatResult(Math.max(1, damage), isCrit);
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

    private static double playerStat(SkyblockPlayer player, Stats stat) {
        if (player.getActiveProfileData() == null) return stat.getBaseStat();

        Map<String, Stat> stats = player.getActiveProfileData().stats;
        Stat entry = stats.get(stat.name().toLowerCase());
        return entry != null ? entry.getCurValue() : stat.getBaseStat();
    }

    public record CombatResult(double damage, boolean isCrit) {
        public float damageFloat() {
            return (float) damage;
        }
    }
}