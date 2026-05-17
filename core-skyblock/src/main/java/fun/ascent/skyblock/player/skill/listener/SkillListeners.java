package fun.ascent.skyblock.player.skill.listener;

import fun.ascent.skyblock.events.EventManager;
import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import fun.ascent.skyblock.player.skill.SkillReward;
import fun.ascent.skyblock.player.skill.SkillType;
import fun.ascent.skyblock.player.skill.event.SkillXpGainEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

import java.util.Locale;

import static fun.ascent.common.StringUtility.text;

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
    }

    private static void handleXpDisplay(SkillXpGainEvent event) {
        SkyblockPlayer player = event.player();
        ProfilePlayer profileData = player.getActiveProfileData();
        if (profileData == null) return;

        SkillType type = event.skillType();
        double gained = event.xpGained();
        double xpInLevel = profileData.skillData.getXpIntoCurrentLevel(type);
        Integer nextLevel = profileData.skillData.getNextLevel(type);

        String xpStr = String.format("%.0f", gained);
        String progressStr = nextLevel != null
                ? String.format("%.0f/%.0f", xpInLevel, (double) type.definition().rewardAt(nextLevel).xpRequired())
                : "MAX";

        player.sendActionBar(text(
                "<dark_aqua>+" + xpStr + " " + type.getDisplayName() + " XP <dark_gray>(" + progressStr + ")"
        ));
    }

    private static void handleLevelUp(SkillXpGainEvent event) {
        SkyblockPlayer player = event.player();
        SkillType type = event.skillType();
        int newLevel = event.newLevel();

        String border = "<dark_aqua><bold>" + "▬".repeat(38);
        String oldRoman = SkillReward.toRoman(event.oldLevel());
        String newRoman = SkillReward.toRoman(newLevel);

        String arrow = oldRoman.isEmpty() ? "<yellow>" + newRoman : oldRoman + "<dark_gray>➜<yellow>" + newRoman;

        player.sendMessage(border);

        Component levelMsg = text("  <aqua><bold>SKILL LEVEL UP <dark_aqua>" + type.getDisplayName() + " <dark_gray>" + arrow)
                .hoverEvent(HoverEvent.showText(text("<yellow>Click to view your " + type.getDisplayName() + " progress")))
                .clickEvent(ClickEvent.runCommand("/skills " + type.name().toLowerCase(Locale.ROOT)));

        player.sendMessage(levelMsg);

        SkillReward reward = type.definition().rewardAt(newLevel);
        if (reward != null && !reward.unlocks().isEmpty()) {
            player.sendMessage(" ");
            player.sendMessage(text("  <green><bold>REWARDS"));
            reward.unlocks().forEach(unlock -> player.sendMessage(text("    " + unlock.display())));
        }

        player.sendMessage(border);
    }
}

