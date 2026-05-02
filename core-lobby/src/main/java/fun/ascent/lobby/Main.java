package fun.ascent.lobby;

import fun.ascent.common.redis.PingService;
import fun.ascent.common.redis.RedisConfig;
import fun.ascent.common.redis.RedisManager;
import fun.ascent.lobby.command.ServerTransferCommand;
import fun.ascent.lobby.config.LobbyConfig;
import fun.ascent.lobby.npc.LobbyNpcManager;
import fun.ascent.lobby.world.LobbyWorld;
import fun.ascent.lobby.scoreboard.LobbyScoreboardManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;

public final class Main {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    /** Host that Velocity uses to reach this server (Docker sets ASCENT_ADVERTISE_HOST). */
    private static final String ADVERTISE_HOST = System.getenv().getOrDefault("ASCENT_ADVERTISE_HOST", "127.0.0.1");

    private Main() {
    }

    public static void main(String[] args) {
        LobbyConfig config = LobbyConfig.load();

        // ── Redis ───────────────────────────────────────────────────────────
        RedisManager.connect(RedisConfig.fromEnv());
        System.out.println("[Lobby] Connected to Redis");

        // ── Minestom ────────────────────────────────────────────────────────
        MinecraftServer server = MinecraftServer.init(config.auth());

        LobbyWorld world = LobbyWorld.create();
        LobbyNpcManager npcManager = new LobbyNpcManager(world.instance());
        npcManager.spawnDefaults();

        registerEvents(world, npcManager);
        MinecraftServer.getCommandManager().register(new ServerTransferCommand("skyblock", "sb", "island"));

        LobbyScoreboardManager.init();
        System.out.println("[Lobby] Starting the Server on " + config.host() + ":" + config.port());
        server.start(config.host(), config.port());
        System.out.println("[Lobby] Started the Server");

        String serverName = System.getenv("ASCENT_SERVER_NAME");
        if (serverName == null || serverName.isBlank() || serverName.equals("lobby")) {
            String base = (serverName == null || serverName.isBlank()) ? "lobby" : serverName;
            serverName = base + "-" + java.util.UUID.randomUUID().toString().substring(0, 5);
        }

        // ── Ping (must start AFTER server.start()) ─────────────────────
        PingService.start(serverName, ADVERTISE_HOST, config.port());
    }

    private static void registerEvents(LobbyWorld world, LobbyNpcManager npcManager) {
        GlobalEventHandler handler = MinecraftServer.getGlobalEventHandler();

        handler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            event.setSpawningInstance(world.instance());
            event.getPlayer().setRespawnPoint(world.spawn());
        });

        handler.addListener(PlayerSpawnEvent.class, event -> {
            if (!event.isFirstSpawn()) {
                return;
            }

            event.getPlayer().teleport(world.spawn());
            event.getPlayer().sendMessage(MINI_MESSAGE.deserialize("<yellow>Welcome to <gold>Ascent</gold><yellow>! Pick a server to begin.</yellow>"));
        });

        handler.addListener(PlayerMoveEvent.class, event -> {
            if (event.getNewPosition().y() < 0) {
                event.getPlayer().teleport(world.spawn());
            }
        });

        fun.ascent.lobby.item.LobbyItemManager.init(handler);
        npcManager.registerListeners(handler);
    }
}
