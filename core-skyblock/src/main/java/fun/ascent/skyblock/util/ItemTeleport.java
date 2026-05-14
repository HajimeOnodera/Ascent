package fun.ascent.skyblock.util;

import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Instance;

import static fun.ascent.common.StringUtility.text;

public final class ItemTeleport {

    private static final double STEP = 0.25;
    private static final double EYE_HEIGHT = 1.62;

    private ItemTeleport() {}

    public static boolean teleport(SkyblockPlayer player, int distance, boolean isEtherwarp) {
        if (isEtherwarp) return etherwarpTeleport(player, distance);
        return normalTeleport(player, distance);
    }

    public static boolean teleport(SkyblockPlayer player, int distance) {
        return teleport(player, distance, false);
    }

    private static boolean normalTeleport(SkyblockPlayer player, int distance) {
        Pos pos = player.getPosition();
        Pos eye = pos.add(0, EYE_HEIGHT, 0);
        double yawRad = Math.toRadians(pos.yaw());
        double pitchRad = Math.toRadians(pos.pitch());

        double dirX = -Math.sin(yawRad) * Math.cos(pitchRad);
        double dirY = -Math.sin(pitchRad);
        double dirZ = Math.cos(yawRad) * Math.cos(pitchRad);

        Instance instance = player.getInstance();
        if (instance == null) return false;

        int steps = (int) (distance / STEP);
        Pos destination = pos;
        boolean moved = false;

        for (int i = 1; i <= steps; i++) {
            double d = STEP * i;
            Pos ray = eye.add(dirX * d, dirY * d, dirZ * d);
            int bx = ray.blockX();
            int bz = ray.blockZ();
            int rayY = ray.blockY();

            if (isSolid(instance, bx, rayY, bz)) break;

            int feetY = rayY;
            while (feetY > rayY - 3 && !isSolid(instance, bx, feetY - 1, bz)) feetY--;

            if (!canFit(instance, bx, feetY, bz)) {
                if (moved) break;
                continue;
            }

            destination = new Pos(bx + 0.5, feetY, bz + 0.5, pos.yaw(), pos.pitch());
            if (bx != pos.blockX() || feetY != pos.blockY() || bz != pos.blockZ()) {
                moved = true;
            }
        }

        if (!moved) {
            player.sendMessage(text("<red>There are blocks in the way!"));
            return false;
        }

        player.teleport(destination);
        player.setVelocity(Vec.ZERO);
        return true;
    }

    private static boolean etherwarpTeleport(SkyblockPlayer player, int distance) {
        Pos pos = player.getPosition();
        Pos eye = pos.add(0, EYE_HEIGHT, 0);
        double yawRad = Math.toRadians(pos.yaw());
        double pitchRad = Math.toRadians(pos.pitch());

        double dirX = -Math.sin(yawRad) * Math.cos(pitchRad);
        double dirY = -Math.sin(pitchRad);
        double dirZ = Math.cos(yawRad) * Math.cos(pitchRad);

        Instance instance = player.getInstance();
        if (instance == null) return false;

        int steps = (int) (distance / STEP);
        Pos destination = pos;

        for (int i = 1; i <= steps; i++) {
            double d = STEP * i;
            Pos ray = eye.add(dirX * d, dirY * d, dirZ * d);
            int bx = ray.blockX();
            int by = ray.blockY();
            int bz = ray.blockZ();

            if (isSolid(instance, bx, by, bz)) {
                int topY = by + 1;
                if (canFit(instance, bx, topY, bz)) {
                    destination = new Pos(bx + 0.5, topY, bz + 0.5, pos.yaw(), pos.pitch());
                }
                break;
            }

            int feetY = (int) Math.floor(ray.y() - EYE_HEIGHT);
            if (isSolid(instance, bx, feetY - 1, bz)) {
                feetY = Math.max(feetY, pos.blockY());
            }
            destination = new Pos(bx + 0.5, feetY, bz + 0.5, pos.yaw(), pos.pitch());
        }

        if (destination.blockX() == pos.blockX() && destination.blockY() == pos.blockY() && destination.blockZ() == pos.blockZ()) {
            player.sendMessage(text("<red>There are blocks in the way!"));
            return false;
        }

        player.teleport(destination);
        player.setVelocity(Vec.ZERO);
        return true;
    }

    private static boolean isSolid(Instance instance, int x, int y, int z) {
        return instance.getBlock(x, y, z).isSolid();
    }

    private static boolean canFit(Instance instance, int x, int y, int z) {
        return !isSolid(instance, x, y, z) && !isSolid(instance, x, y + 1, z);
    }
}
