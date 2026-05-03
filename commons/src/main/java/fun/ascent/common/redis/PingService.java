package fun.ascent.common.redis;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.util.concurrent.atomic.AtomicBoolean;

public final class PingService {

    private static final AtomicBoolean started = new AtomicBoolean(false);

    private PingService() {}

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
