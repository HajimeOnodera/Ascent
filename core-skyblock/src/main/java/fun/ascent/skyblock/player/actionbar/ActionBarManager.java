package fun.ascent.skyblock.player.actionbar;

import fun.ascent.skyblock.events.EventManager;
import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;

import static fun.ascent.common.StringUtility.text;
import static fun.ascent.skyblock.player.stats.Stats.*;

public final class ActionBarManager {

    private ActionBarManager() {}

    public static void init() {
        EventManager.registerEvent(new SEvent<PlayerDisconnectEvent>() {
            @Override public void onEvent(PlayerDisconnectEvent e) {
                ActionBar.remove(e.getPlayer().getUuid());
            }
        });

        barLoop();
        healthLoop();
        manaLoop();
    }

    private static void barLoop() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();
        scheduler.submitTask(() -> {
            for (Player online : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                if (!(online instanceof SkyblockPlayer player)) continue;
                if (player.getActiveProfileData() == null) continue;

                ActionBar bar = ActionBar.of(player.getUuid());

                double hp = player.getCurrentHealth();
                double maxHp = player.maxStat(HEALTH);
                double def = player.playerStat(DEFENSE);
                double mana = player.getCurrentMana();
                double maxMana = player.maxStat(INTELLIGENCE);

                bar.setDefault(ActionBar.Section.HEALTH,
                        "<red>" + Math.round(hp) + "/" + Math.round(maxHp) + "❤");
                bar.setDefault(ActionBar.Section.DEFENSE,
                        def == 0 ? "" : "<green>" + Math.round(def) + "❈ Defense");
                bar.setDefault(ActionBar.Section.MANA,
                        "<aqua>" + Math.round(mana) + "/" + Math.round(maxMana) + "✎ Mana");

                player.sendActionBar(text(bar.build()));
            }
            return TaskSchedule.tick(4);
        }, ExecutionType.TICK_END);
    }

    private static void healthLoop() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();
        scheduler.submitTask(() -> {
            for (Player online : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                if (!(online instanceof SkyblockPlayer player)) continue;
                if (player.getActiveProfileData() == null) continue;

                double max = player.maxStat(HEALTH);
                if (player.getCurrentHealth() >= max) continue;

                player.addHealth(1.5 + (max * 0.01));
            }
            return TaskSchedule.tick(30);
        }, ExecutionType.TICK_END);
    }

    private static void manaLoop() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();
        scheduler.submitTask(() -> {
            for (Player online : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                if (!(online instanceof SkyblockPlayer player)) continue;
                if (player.getActiveProfileData() == null) continue;

                double max = player.maxStat(INTELLIGENCE);
                if (player.getCurrentMana() >= max) continue;

                player.addMana(max / 50.0);
            }
            return TaskSchedule.seconds(1);
        }, ExecutionType.TICK_END);
    }
}
