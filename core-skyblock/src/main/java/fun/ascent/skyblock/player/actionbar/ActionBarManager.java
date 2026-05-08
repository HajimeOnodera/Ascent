package fun.ascent.skyblock.player.actionbar;

import fun.ascent.skyblock.events.EventManager;
import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.stats.Stats;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;

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

                double hp      = player.getCurrentHealth();
                double maxHp   = player.stat(Stats.HEALTH);
                double def     = player.stat(Stats.DEFENSE);
                double mana    = player.getCurrentMana();
                double maxMana = player.stat(Stats.INTELLIGENCE);

                bar.setDefault(ActionBar.Section.HEALTH,
                        "§c" + Math.round(hp) + "/" + Math.round(maxHp) + "❤");
                bar.setDefault(ActionBar.Section.DEFENSE,
                        def == 0 ? "" : "§a" + Math.round(def) + "❈ Defense");
                bar.setDefault(ActionBar.Section.MANA,
                        "§b" + Math.round(mana) + "/" + Math.round(maxMana) + "✎ Mana");

                player.sendActionBar(Component.text(bar.build()));
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

                double max = player.stat(Stats.HEALTH);
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

                double max = player.stat(Stats.INTELLIGENCE);
                if (player.getCurrentMana() >= max) continue;

                player.addMana(max / 50.0);
            }
            return TaskSchedule.seconds(1);
        }, ExecutionType.TICK_END);
    }
}