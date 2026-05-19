package fun.ascent.skyblock.cmds.impl;

import fun.ascent.common.redis.ServerLookup;
import fun.ascent.common.util.ProxyTransfer;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfileManager;
import fun.ascent.skyblock.player.profiles.SkyblockProfile;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;

import java.util.LinkedHashMap;
import java.util.Map;

import static fun.ascent.common.StringUtility.text;

public class WarpCommand extends Command {

    private record WarpDestination(String displayName, String serverPrefix, String serverType) {}

    private static final Map<String, WarpDestination> WARPS = new LinkedHashMap<>();

    static {
        WARPS.put("hub", new WarpDestination("SkyBlock Hub", "skyblock", "HUB"));
        WARPS.put("island", new WarpDestination("Your Island", "island", "ISLAND"));
        WARPS.put("dungeon_hub", new WarpDestination("Dungeon Hub", "dungeon-hub", "DUNGEON_HUB"));
    }

    public WarpCommand() {
        super("warp");

        var warpArg = ArgumentType.Word("destination").from(WARPS.keySet().toArray(String[]::new));

        setDefaultExecutor((sender, _) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;
            player.sendMessage(text("<yellow>Available warps: <white>" + String.join(", ", WARPS.keySet())));
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;

            String warpName = context.get(warpArg);
            WarpDestination dest = WARPS.get(warpName);
            if (dest == null) {
                player.sendMessage(text("<red>Unknown warp! Available: <white>" + String.join(", ", WARPS.keySet())));
                return;
            }

            String currentType = System.getenv().getOrDefault("ASCENT_SERVER_TYPE", "HUB");

            // Already on the target server type
            if (currentType.equalsIgnoreCase(dest.serverType)) {
                player.sendMessage(text("<red>You are already here!"));
                return;
            }

            String targetServer = ServerLookup.findAnyByPrefix(dest.serverPrefix);
            if (targetServer == null) {
                player.sendMessage(text("<red>" + dest.displayName + " is currently offline!"));
                return;
            }

            SkyblockProfile profile = player.getActiveProfile();
            if (profile != null) {
                ProfileManager.saveProfile(profile.profileID);
            }

            player.sendMessage(text("<green>Warping to " + dest.displayName + "..."));
            ProxyTransfer.send(player, targetServer);
        }, warpArg);
    }
}
