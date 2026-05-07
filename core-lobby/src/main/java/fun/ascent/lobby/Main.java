package fun.ascent.lobby;

import fun.ascent.common.Ascent;
import fun.ascent.common.gui.InventoryGUIListener;
import fun.ascent.common.redis.PingService;
import fun.ascent.common.user.UserManager;
import fun.ascent.lobby.command.ServerTransferCommand;
import fun.ascent.lobby.config.LobbyConfig;
import fun.ascent.lobby.item.LobbyItemManager;
import fun.ascent.lobby.listener.LobbyChatListener;
import fun.ascent.lobby.listener.LobbyProtectionListener;
import fun.ascent.lobby.npc.LobbyNpcManager;
import fun.ascent.lobby.world.LobbyWorld;
import fun.ascent.lobby.scoreboard.LobbyScoreboardManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;

import static fun.ascent.common.StringUtility.color;

public final class Main {
    private static String currentServerName;
    private static final String ADVERTISE_HOST = System.getenv().getOrDefault("ASCENT_ADVERTISE_HOST", "127.0.0.1");
    public static String getServerName() {
        return currentServerName;
    }

    private Main() {}

    static void main() {
        LobbyConfig config = LobbyConfig.load();
        Ascent.initialize();

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

        currentServerName = serverName;

        // ── Ping (must start AFTER server.start()) ─────────────────────
        PingService.start(serverName, ADVERTISE_HOST, config.port());
    }

    private static void registerEvents(LobbyWorld world, LobbyNpcManager npcManager) {
        GlobalEventHandler handler = MinecraftServer.getGlobalEventHandler();

        InventoryGUIListener.register(handler);

        handler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            event.setSpawningInstance(world.instance());
            event.getPlayer().setRespawnPoint(world.spawn());
        });

        handler.addListener(PlayerSpawnEvent.class, event -> {
            Player player = event.getPlayer();
            player.setDisplayName(UserManager.getDisplayName(player.getUuid()));
            
            if (!event.isFirstSpawn()) {
                return;
            }

            player.teleport(world.spawn());
            player.sendMessage(color("&eWelcome to &6Ascent&e! Pick a server to begin."));
        });

        handler.addListener(PlayerMoveEvent.class, event -> {
            if (event.getNewPosition().y() < 0) {
                event.getPlayer().teleport(world.spawn());
            }
        });

        LobbyItemManager.init(handler);
        LobbyProtectionListener.register(handler);
        LobbyChatListener.register(handler);
        npcManager.registerListeners(handler);
    }
}
