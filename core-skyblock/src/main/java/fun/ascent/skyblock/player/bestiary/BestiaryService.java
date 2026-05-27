package fun.ascent.skyblock.player.bestiary;

import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.events.PlayerKillMobEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import fun.ascent.skyblock.player.stats.Stats;
import net.kyori.adventure.key.Key;
import fun.ascent.skyblock.player.level.causes.LevelCause;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.GlobalEventHandler;

import static fun.ascent.common.StringUtility.text;

public final class BestiaryService {

    private BestiaryService() {
    }

    public static void register(GlobalEventHandler handler) {
        handler.addListener(PlayerKillMobEvent.class, BestiaryService::handleKill);
    }

    private static void handleKill(PlayerKillMobEvent event) {
        SkyblockPlayer player = event.getPlayer();
        ProfilePlayer profile = player.getActiveProfileData();
        if (profile == null) {
            return;
        }

        BestiaryFamily.ResolvedMob resolvedMob = BestiaryFamily.resolve(event.getKilledMob());
        if (resolvedMob == null) {
            return;
        }

        BestiaryProgress progress = profile.getBestiaryProgress();
        int oldKills = progress.getKills(resolvedMob.mob());
        int newKills = progress.increment(resolvedMob.mob().id(), 1);

        if (newKills == 1) {
            player.sendMessage(text("  <dark_aqua><bold>BESTIARY FAMILY UNLOCKED <aqua>" + resolvedMob.family().displayName()));
            player.playSound(Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.PLAYER, 1f, 0.7f));
            return;
        }

        int oldTier = BestiaryMilestones.currentTier(resolvedMob.family(), oldKills);
        int newTier = BestiaryMilestones.currentTier(resolvedMob.family(), newKills);
        if (newTier <= oldTier) {
            return;
        }

        for (int tier = oldTier + 1; tier <= newTier; tier++) {
            grantTierRewards(profile, tier);
            sendTierMessage(player, resolvedMob.family(), oldTier, tier);
        }
    }

    private static void grantTierRewards(ProfilePlayer profile, int tier) {
        profile.addToStat(Stats.MAGIC_FIND, BestiaryMilestones.magicFindGain(tier));
        profile.addToStat(Stats.STRENGTH, BestiaryMilestones.strengthGain(tier));
        profile.addSkyblockXp(1, LevelCause.BESTIARY_PROGRESSION_CAUSE);
    }

    private static void sendTierMessage(SkyblockPlayer player, BestiaryFamily family, int oldTier, int newTier) {
        String oldRoman = StringUtility.getAsRomanNumeral(oldTier);
        String newRoman = StringUtility.getAsRomanNumeral(newTier);
        String transition = oldTier == 0
                ? "<aqua>" + family.displayName() + " " + newRoman
                : "<aqua>" + family.displayName() + " <dark_gray>" + oldRoman + " -> <aqua>" + newRoman;
        String border = "<dark_aqua><bold>" + "▬".repeat(38);

        player.sendMessage(text(border));
        player.sendMessage(text("  <gold><bold>BESTIARY"));
        player.sendMessage(text("  " + transition));
        player.sendMessage(Component.empty());
        player.sendMessage(text("  <green><bold>REWARDS"));
        player.sendMessage(text("    <dark_gray>+<aqua>" + BestiaryMilestones.magicFindGain(newTier) + " Magic Find"));
        player.sendMessage(text("    <dark_gray>+<red>" + BestiaryMilestones.strengthGain(newTier) + " Strength"));
        player.sendMessage(text("    <dark_gray>+<aqua>1 SkyBlock XP"));
        player.sendMessage(text(border));
        player.playSound(Sound.sound(Key.key("entity.player.levelup"), Sound.Source.PLAYER, 1f, 1f));
    }
}
