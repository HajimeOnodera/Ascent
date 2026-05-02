package fun.ascent.common.redis;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Publishes a {@link ServerPing} to Redis every second.
 * Uses Minestom's scheduler so no extra threads are needed.
 *
 * Usage (in Main, after server.start()):
 * <pre>
 *   PingService.start("lobby", "lobby", 25567);
 * </pre>
 */
public final class PingService {

    private static final AtomicBoolean started = new AtomicBoolean(false);

    private PingService() {}

    /**
     * @param serverName  logical name used as the Velocity backend name (e.g. "lobby")
     * @param advertiseHost  hostname/IP that Velocity will use to reach this server
     *                       (Docker service name works; e.g. "lobby")
     * @param port            Minecraft port this server is listening on
     */
    public static void start(String serverName, String advertiseHost, int port) {
        if (!started.compareAndSet(false, true)) {
            throw new IllegalStateException("PingService already started");
        }

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            int online = MinecraftServer.getConnectionManager().getOnlinePlayers().size();
            ServerPing hb = new ServerPing(serverName, advertiseHost, port,
                    online, System.currentTimeMillis());
            try (Jedis jedis = RedisManager.get().getResource()) {
                hb.publish(jedis);
            } catch (JedisException e) {
                System.err.println("[Ping] Failed to publish ping for " + serverName + ": " + e.getMessage());
            }
        }).repeat(TaskSchedule.seconds(1)).schedule();

        System.out.println("[Ping] Started ping for server '" + serverName
                + "' @ " + advertiseHost + ":" + port);
    }
}
