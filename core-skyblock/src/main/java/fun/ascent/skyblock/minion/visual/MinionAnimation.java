package fun.ascent.skyblock.minion.visual;

import fun.ascent.skyblock.minion.base.SkyblockMinion;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.BlockBreakAnimationPacket;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;

public final class MinionAnimation {
    private MinionAnimation() {
    }

    public static void animatePlace(SkyblockMinion minion, Pos target, Runnable action) {
        minion.setBusy(true);
        minion.rotateToward(target);
        final Task[] taskRef = new Task[1];
        final int[] step = {0};
        int[] poses = {-10, -30, -60, -100};
        taskRef[0] = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (step[0] < poses.length) {
                minion.setRightArmRotation(new Vec(poses[step[0]], 0, 15));
                minion.setLeftArmRotation(new Vec(poses[step[0]], 0, -15));
                if (step[0] == poses.length - 1) {
                    action.run();
                }
                step[0]++;
                return;
            }
            reset(minion);
            taskRef[0].cancel();
        }).repeat(TaskSchedule.tick(2)).schedule();
    }

    public static void animateBreak(SkyblockMinion minion, Pos target, Block block, Runnable action) {
        minion.setBusy(true);
        minion.rotateToward(target);
        final Task[] taskRef = new Task[1];
        final int[] stage = {0};
        taskRef[0] = MinecraftServer.getSchedulerManager().buildTask(() -> {
            byte destroyStage = (byte) stage[0];
            minion.broadcastPacket(new BlockBreakAnimationPacket(minion.getEntity().getEntityId(), target, destroyStage));
            minion.setRightArmRotation(new Vec(-100 + stage[0] * 10, -8, 15));
            if (stage[0] >= 9) {
                action.run();
                minion.broadcastPacket(new BlockBreakAnimationPacket(minion.getEntity().getEntityId(), target, (byte) -1));
                minion.broadcastPacket(new ParticlePacket(Particle.BLOCK.withBlock(block), target.add(0.5, 0.5, 0.5), Vec.ZERO, 0.2f, 16));
                reset(minion);
                taskRef[0].cancel();
                return;
            }
            stage[0]++;
        }).repeat(TaskSchedule.tick(2)).schedule();
    }

    public static void animateKill(SkyblockMinion minion, Pos target, Runnable action) {
        minion.setBusy(true);
        minion.rotateToward(target);
        final Task[] taskRef = new Task[1];
        final int[] step = {0};
        taskRef[0] = MinecraftServer.getSchedulerManager().buildTask(() -> {
            minion.setRightArmRotation(new Vec(-90 + step[0] * 15, -8, 15));
            if (step[0] >= 6) {
                action.run();
                minion.broadcastPacket(new ParticlePacket(Particle.CRIT, target.add(0, 0.8, 0), Vec.ZERO, 0.15f, 10));
                reset(minion);
                taskRef[0].cancel();
                return;
            }
            step[0]++;
        }).repeat(TaskSchedule.tick(2)).schedule();
    }

    public static void animateFishing(SkyblockMinion minion, Pos target, Runnable action) {
        minion.setBusy(true);
        minion.rotateToward(target);
        final Task[] taskRef = new Task[1];
        final int[] step = {0};
        taskRef[0] = MinecraftServer.getSchedulerManager().buildTask(() -> {
            minion.setRightArmRotation(new Vec(-35 + step[0] * 12, 0, 15));
            if (step[0] >= 5) {
                action.run();
                minion.broadcastPacket(new ParticlePacket(Particle.SPLASH, target.add(0.5, 0.7, 0.5), Vec.ZERO, 0.15f, 12));
                reset(minion);
                taskRef[0].cancel();
                return;
            }
            step[0]++;
        }).repeat(TaskSchedule.tick(2)).schedule();
    }

    private static void reset(SkyblockMinion minion) {
        minion.setHeadRotation(Vec.ZERO);
        minion.setRightArmRotation(new Vec(0, 0, 15));
        minion.setLeftArmRotation(new Vec(0, 0, -15));
        minion.setBusy(false);
    }
}
