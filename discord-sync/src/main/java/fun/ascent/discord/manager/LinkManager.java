package fun.ascent.discord.manager;

import com.velocitypowered.api.proxy.Player;
import fun.ascent.discord.DiscordSync;
import fun.ascent.discord.data.LinkRepository;
import fun.ascent.discord.data.Outcome;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static fun.ascent.common.StringUtility.text;

public final class LinkManager {

    private final LinkRepository repo;
    private final Map<UUID, Long> cooldowns = new ConcurrentHashMap<>();

    private static final long COOLDOWN_MS = 5_000L;

    public LinkManager(LinkRepository repo) {
        this.repo = repo;
    }

    public void attemptLink(Player player, String raw) {
        UUID uuid = player.getUniqueId();
        String username = player.getUsername();
        long now = System.currentTimeMillis();

        Long prev = cooldowns.get(uuid);
        if (prev != null && now - prev < COOLDOWN_MS) {
            player.sendMessage(text(text("<red>Please wait before trying again.")));
            return;
        }
        cooldowns.put(uuid, now);

        repo.isLinked(username)
                .thenCompose(linked -> {
                    if (linked) {
                        player.sendMessage(text(text(
                                "<gray>----------------------------------------\n" +
                                "<red><bold>Discord Link\n" +
                                "<gray>Your Minecraft account is already linked.\n" +
                                "<gray>Use <white>/unlink <gray>first if you want to switch Discord accounts.\n" +
                                "<gray>----------------------------------------"
                        )));
                        return CompletableFuture.completedFuture(null);
                    }

                    String code = raw.toUpperCase();

                    return repo.fetchCode(code)
                            .thenCompose(outcome -> {
                                if (outcome instanceof Outcome.Fail<?>) {
                                    player.sendMessage(text(text(
                                            "<gray>----------------------------------------\n" +
                                            "<red><bold>Discord Link\n" +
                                            "<gray>That code is invalid or has expired.\n" +
                                            "<gray>Run <white>/discord link <gray>in Discord to get a fresh code.\n" +
                                            "<gray>----------------------------------------"
                                    )));
                                    return CompletableFuture.completedFuture(null);
                                }
                                return repo.consumeAndLink(code, username, uuid.toString())
                                        .thenRun(() -> player.sendMessage(
                                                text(text(
                                                        "<gray>----------------------------------------\n" +
                                                        "<green><bold>Discord Linked\n" +
                                                        "<gray>Minecraft: <white>" + username + "\n" +
                                                        "<gray>Status: <green>Connected to your Discord account\n" +
                                                        "<gray>You can run <white>/unlink <gray>any time.\n" +
                                                        "<gray>----------------------------------------"
                                                ))
                                        ));
                            });
                })
                .exceptionally(ex -> {
                    if (DiscordSync.logger() != null) {
                        DiscordSync.logger().error("Failed to link account for {}", player.getUsername(), ex);
                    }
                    player.sendMessage(text(text("<red>Something went wrong. Please try again later.")));
                    return null;
                });
    }

    public void attemptUnlink(Player player) {
        String username = player.getUsername();

        repo.isLinked(username)
                .thenCompose(linked -> {
                    if (!linked) {
                        player.sendMessage(text(text(
                                "<gray>----------------------------------------\n" +
                                "<red><bold>Discord Link\n" +
                                "<gray>Your Minecraft account is not linked right now.\n" +
                                "<gray>----------------------------------------"
                        )));
                        return CompletableFuture.completedFuture(null);
                    }
                    return repo.unlink(username)
                            .thenAccept(removed -> {
                                if (removed) {
                                    player.sendMessage(text(text(
                                            "<gray>----------------------------------------\n" +
                                            "<yellow><bold>Discord Unlinked\n" +
                                            "<gray>Minecraft: <white>" + username + "\n" +
                                            "<gray>Status: <yellow>Connection removed\n" +
                                            "<gray>Run <white>/link <code> <gray>to connect again.\n" +
                                            "<gray>----------------------------------------"
                                    )));
                                } else {
                                    player.sendMessage(text(text(
                                            "<gray>----------------------------------------\n" +
                                            "<red><bold>Discord Link\n" +
                                            "<gray>Unlink failed. Try again in a moment.\n" +
                                            "<gray>----------------------------------------"
                                    )));
                                }
                            });
                })
                .exceptionally(ex -> {
                    if (DiscordSync.logger() != null) {
                        DiscordSync.logger().error("Failed to unlink account for {}", player.getUsername(), ex);
                    }
                    player.sendMessage(text(text("<red>Something went wrong. Please try again later.")));
                    return null;
                });
    }
}
