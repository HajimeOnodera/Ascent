package fun.ascent.skyblock.skill.listener;

import fun.ascent.skyblock.events.EventManager;
import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import fun.ascent.skyblock.skill.SkillReward;
import fun.ascent.skyblock.skill.SkillType;
import fun.ascent.skyblock.skill.event.SkillXpGainEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;

import java.util.Locale;

public class SkillListeners {

    public static void register() {
        EventManager.registerEvent(new SEvent<SkillXpGainEvent>() {
            @Override
            public void onEvent(SkillXpGainEvent event) {
                handleXpDisplay(event);
            }
        });

        EventManager.registerEvent(new SEvent<SkillXpGainEvent>() {
            @Override
            public void onEvent(SkillXpGainEvent event) {
                if (!event.leveledUp()) return;
                handleLevelUp(event);
            }
        });

        EventManager.registerEvent(new SEvent<PlayerBlockBreakEvent>() {
            @Override
            public void onEvent(PlayerBlockBreakEvent event) {
                if (!(event.getPlayer() instanceof SkyblockPlayer player)) return;
                // Mining XP - full block tracking will live here once
                // the item system is wired up. This shows the hook point.
                // SkillRegistry.grantXp(player, SkillType.MINING, xpValue);
            }
        });
    }

    private static void handleXpDisplay(SkillXpGainEvent event) {
        SkyblockPlayer player = event.getPlayer();
        ProfilePlayer profileData = player.getActiveProfileData();
        if (profileData == null) return;

        SkillType type = event.getSkillType();
        double gained = event.getXpGained();
        double xpInLevel = profileData.skillData.getXpIntoCurrentLevel(type);
        Integer nextLevel = profileData.skillData.getNextLevel(type);

        String xpStr = String.format("%.0f", gained);
        String progressStr = nextLevel != null
                ? String.format("%.0f/%.0f", xpInLevel, (double) type.definition().rewardAt(nextLevel).xpRequired())
                : "MAX";

        player.sendActionBar(Component.text(
                "§3+" + xpStr + " " + type.getDisplayName() + " XP §8(" + progressStr + ")"
        ));
    }

    private static void handleLevelUp(SkillXpGainEvent event) {
        SkyblockPlayer player = event.getPlayer();
        SkillType type = event.getSkillType();
        int newLevel = event.getNewLevel();

        String border = "§3§l" + "▬".repeat(38);
        String oldRoman = SkillReward.toRoman(event.getOldLevel());
        String newRoman = SkillReward.toRoman(newLevel);

        String arrow = oldRoman.isEmpty() ? "§e" + newRoman : oldRoman + "§8➜§e" + newRoman;

        player.sendMessage(border);

        Component levelMsg = Component.text("  §b§lSKILL LEVEL UP §3" + type.getDisplayName() + " §8" + arrow)
                .hoverEvent(HoverEvent.showText(Component.text("§eClick to view your " + type.getDisplayName() + " progress")))
                .clickEvent(ClickEvent.runCommand("/skills " + type.name().toLowerCase(Locale.ROOT)));

        player.sendMessage(levelMsg);

        SkillReward reward = type.definition().rewardAt(newLevel);
        if (reward != null && !reward.unlocks().isEmpty()) {
            player.sendMessage(" ");
            player.sendMessage("  §a§lREWARDS");
            reward.unlocks().forEach(unlock -> player.sendMessage("    " + unlock.display()));
        }

        player.sendMessage(border);
    }
}
