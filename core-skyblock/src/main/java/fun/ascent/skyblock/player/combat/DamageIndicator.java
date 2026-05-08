package fun.ascent.skyblock.player.combat;

import fun.ascent.common.StringUtility;
import fun.ascent.skyblock.entity.display.FloatingTextEntity;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.timer.TaskSchedule;

import java.util.concurrent.ThreadLocalRandom;

public class DamageIndicator {

    private static final String[] CRIT_COLORS = {"<white>", "<white>", "<yellow>", "<gold>", "<red>", "<red>"};
    private static final int LIFETIME_MS = 800;
    private static final int LIFETIME_TICKS = (int) (LIFETIME_MS / 50.0);

    public static void spawn(Instance world, Pos target, double damage, boolean isCrit) {
        int rounded = (int) Math.round(damage);
        String text = isCrit ? rainbow("✧" + rounded + "✧") : "<gray>" + rounded;

        Pos spawnPos = target.add(
                ThreadLocalRandom.current().nextDouble(-0.5, 0.5),
                0.9,
                ThreadLocalRandom.current().nextDouble(-0.5, 0.5)
        );

        FloatingTextEntity indicator = new FloatingTextEntity(StringUtility.text(text), meta -> {});
        indicator.setInstance(world, spawnPos).thenRun(() -> {
            indicator.animateRise(1.2f, LIFETIME_TICKS);
            MinecraftServer.getSchedulerManager().buildTask(indicator::remove)
                    .delay(TaskSchedule.millis(LIFETIME_MS))
                    .schedule();
        });
    }

    private static String rainbow(String input) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (char c : input.toCharArray()) {
            if (i >= CRIT_COLORS.length) i = 0;
            sb.append(CRIT_COLORS[i++]).append(c);
        }
        return sb.toString();
    }
}
