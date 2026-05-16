package fun.ascent.discord.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import fun.ascent.discord.manager.LinkManager;

import java.util.List;
import java.util.Locale;

import static fun.ascent.common.StringUtility.text;

public final class DiscordCommand implements SimpleCommand {

    private final LinkManager manager;

    public DiscordCommand(LinkManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) return;

        String[] args = invocation.arguments();
        if (args.length == 0) {
            sendRootUsage(player);
            return;
        }

        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "link" -> {
                if (args.length != 2) {
                    sendLinkUsage(player);
                    return;
                }
                manager.attemptLink(player, args[1]);
            }
            case "unlink" -> {
                if (args.length != 1) {
                    sendUnlinkUsage(player);
                    return;
                }
                manager.attemptUnlink(player);
            }
            default -> sendRootUsage(player);
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length <= 1) {
            String prefix = args.length == 0 ? "" : args[0].toLowerCase(Locale.ROOT);
            return List.of("link", "unlink").stream()
                    .filter(option -> option.startsWith(prefix))
                    .toList();
        }
        return List.of();
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source() instanceof Player;
    }

    public static void sendRootUsage(Player player) {
        player.sendMessage(text(
                "<gray>----------------------------------------\n" +
                "<aqua><bold>Discord Commands\n" +
                "<gray>/discord link <white><code><gray> - Link your Discord account\n" +
                "<gray>/discord unlink <gray>- Remove the current link\n" +
                "<gray>Generate your code in Discord with <white>/discord link<gray>.\n" +
                "<gray>----------------------------------------"
        ));
    }

    public static void sendLinkUsage(Player player) {
        player.sendMessage(text(
                "<gray>----------------------------------------\n" +
                "<aqua><bold>Discord Link\n" +
                "<gray>Usage: <white>/discord link <code>\n" +
                "<gray>Example: <white>/discord link ABC123\n" +
                "<gray>Get the code from the Discord bot first.\n" +
                "<gray>----------------------------------------"
        ));
    }

    public static void sendUnlinkUsage(Player player) {
        player.sendMessage(text(
                "<gray>----------------------------------------\n" +
                "<aqua><bold>Discord Unlink\n" +
                "<gray>Usage: <white>/discord unlink\n" +
                "<gray>This removes the current Discord link from your Minecraft account.\n" +
                "<gray>----------------------------------------"
        ));
    }
}
