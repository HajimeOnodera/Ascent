package fun.ascent.skyblock;

import fun.ascent.common.redis.PingService;
import fun.ascent.common.redis.RedisConfig;
import fun.ascent.common.redis.RedisManager;
import fun.ascent.common.user.UserManager;
import fun.ascent.skyblock.cmds.CommandHandler;
import fun.ascent.skyblock.item.ItemRegistry;
import fun.ascent.skyblock.item.reforge.Reforge;
import fun.ascent.skyblock.minion.service.MinionManager;
import fun.ascent.skyblock.player.combat.CombatListener;
import fun.ascent.skyblock.entity.mob.EntityRegistry;
import fun.ascent.skyblock.entity.mob.ZonePopulationTicker;
import fun.ascent.skyblock.events.EventManager;
import fun.ascent.skyblock.npc.SkyblockNPCManager;
import fun.ascent.skyblock.calendar.Calendar;
import fun.ascent.skyblock.config.ServerConfig;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.scoreboard.ScoreboardManager;
import fun.ascent.skyblock.player.skill.SkillRegistry;
import fun.ascent.skyblock.player.skill.listener.SkillListeners;
import fun.ascent.skyblock.world.WorldHandler;
import net.minestom.server.MinecraftServer;

public class Main {

    /** Host used by Velocity to reach this container. */
    private static final String ADVERTISE_HOST = System.getenv().getOrDefault("ASCENT_ADVERTISE_HOST", "127.0.0.1");

    static void main(String[] args) {
        ServerConfig config = ServerConfig.load();
        // ── Redis ───────────────────────────────────────────────────────────
        RedisManager.connect(RedisConfig.fromEnv());
        UserManager.init(System.getenv().getOrDefault("MONGODB_URI", "mongodb://127.0.0.1:27017"));
        System.out.println("[Skyblock] Connected to Redis & MongoDB");

        // ── Minestom ────────────────────────────────────────────────────────
        MinecraftServer server = MinecraftServer.init(config.auth());
        MinecraftServer.getConnectionManager().setPlayerProvider(SkyblockPlayer::new);

        ItemRegistry.init();
        Reforge.init();

        WorldHandler.initialise();
        EventManager.initialise();
        SkyblockNPCManager.init();
        Calendar.startTimeUpdates();
        ScoreboardManager.init();
        MinionManager.init();

        SkillRegistry.init();
        SkillListeners.register();
        CommandHandler.initialise();

        EntityRegistry.scanAndRegister("fun.ascent.skyblock.entity.mob.mobs");
        ZonePopulationTicker.start();
        CombatListener.register();
        fun.ascent.skyblock.listeners.SkyblockChatListener.register();

        MinecraftServer.getGlobalEventHandler().addListener(net.minestom.server.event.player.PlayerSpawnEvent.class, event -> {
            net.minestom.server.entity.Player player = event.getPlayer();
            player.setDisplayName(fun.ascent.common.user.UserManager.getDisplayName(player.getUuid()));
        });

        System.out.println("[Skyblock] Starting the Server");
        server.start(config.host(), config.port());
        System.out.println("[Skyblock] Started the Server");

        String serverName = System.getenv("ASCENT_SERVER_NAME");
        if (serverName == null || serverName.isBlank() || serverName.equals("skyblock")) {
            String base = (serverName == null || serverName.isBlank()) ? "skyblock" : serverName;
            serverName = base + "-" + java.util.UUID.randomUUID().toString().substring(0, 5);
        }

        // ── Ping (must start AFTER server.start()) ─────────────────────
        PingService.start(serverName, ADVERTISE_HOST, config.port());
    }
}
