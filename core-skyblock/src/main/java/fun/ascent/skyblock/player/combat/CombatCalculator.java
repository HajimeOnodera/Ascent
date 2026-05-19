package fun.ascent.skyblock.player.combat;

import fun.ascent.skyblock.entity.mob.SkyblockMobEntity;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.stats.Stat;
import fun.ascent.skyblock.player.stats.Stats;

import java.util.Map;

public class CombatCalculator {

    public static CombatResult playerHitsMob(SkyblockPlayer player, SkyblockMobEntity mob) {
        double weaponDamage = playerStat(player, Stats.DAMAGE);
        double strength = playerStat(player, Stats.STRENGTH);
        double critChance = playerStat(player, Stats.CRITICAL_CHANCE);
        double critDamage = playerStat(player, Stats.CRITICAL_DAMAGE);
        double defense = mob.baseStat(Stats.DEFENSE);

        boolean isCrit = Math.random() * 100.0 <= critChance;

        double damage = weaponDamage
                * (1.0 + strength / 100.0)
                * (isCrit ? 1.0 + critDamage / 100.0 : 1.0);

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
