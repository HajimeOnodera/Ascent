package fun.ascent.skyblock.cmds.impl;

import fun.ascent.common.StringUtility;
import fun.ascent.database.PlayerRepository;
import fun.ascent.database.SkyblockRepository;
import fun.ascent.skyblock.island.IslandManager;
import fun.ascent.skyblock.island.data.IslandDatabase;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfileManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.TaskSchedule;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class WipeSbCommand extends Command {

    public WipeSbCommand() {
        super("wipesb");

        var usernameArg = ArgumentType.String("username");

        setDefaultExecutor((sender, _) -> sender.sendMessage(StringUtility.text("<red>Usage: /wipesb <username>")));

        addSyntax((sender, args) -> {
            String targetUsername = args.get(usernameArg);
            sender.sendMessage(StringUtility.text("<yellow>Initiating Skyblock data wipe for player <gold>" + targetUsername + "<yellow>..."));

            // Run database queries and processing asynchronously to avoid blocking the main server tick thread
            CompletableFuture.runAsync(() -> {
                try {
                    UUID targetUUID;
                    Player onlinePlayer = MinecraftServer.getConnectionManager().getOnlinePlayerByUsername(targetUsername);
                    if (onlinePlayer != null) {
                        targetUUID = onlinePlayer.getUuid();
                    } else {
                        targetUUID = PlayerRepository.getUUIDByName(targetUsername);
                    }

                    if (targetUUID == null) {
                        sender.sendMessage(StringUtility.text("<red>Could not find player <yellow>" + targetUsername + "<red> in the database."));
                        return;
                    }

                    final UUID finalTargetUUID = targetUUID;

                    if (onlinePlayer != null) {
                        MinecraftServer.getSchedulerManager().submitTask(() -> {
                            onlinePlayer.kick("Your Skyblock data is being wiped.");
                            return TaskSchedule.stop();
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ignored) {}
                    }

                    List<Document> profileDocs = SkyblockRepository.getProfilesForPlayer(finalTargetUUID);
                    List<UUID> profileIDs = new ArrayList<>();
                    for (Document doc : profileDocs) {
                        try {
                            profileIDs.add(UUID.fromString(doc.getString("_id")));
                        } catch (Exception ignored) {}
                    }

                    if (!profileIDs.isEmpty()) {
                        MinecraftServer.getSchedulerManager().submitTask(() -> {
                            for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                                if (p instanceof SkyblockPlayer sbp) {
                                    if (sbp.getActiveProfile() != null && profileIDs.contains(sbp.getActiveProfile().profileID)) {
                                        p.kick("The Skyblock profile you were on is being wiped.");
                                    }
                                }
                            }
                            return TaskSchedule.stop();
                        });
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ignored) {}
                    }

                    for (UUID profileID : profileIDs) {
                        MinecraftServer.getSchedulerManager().submitTask(() -> {
                            IslandManager.unloadIsland(profileID);
                            ProfileManager.profiles.remove(profileID);
                            return TaskSchedule.stop();
                        });

                        IslandDatabase.deleteIsland(profileID);
                        SkyblockRepository.deleteProfile(profileID);
                    }

                    PlayerRepository.removeField(finalTargetUUID, "skyblock");

                    sender.sendMessage(StringUtility.text("<green>Successfully wiped all Skyblock profiles, islands, and player data for <yellow>" + targetUsername + "<green>!"));
                } catch (Exception e) {
                    sender.sendMessage(StringUtility.text("<red>An error occurred while wiping player data: " + e.getMessage()));
                    e.printStackTrace();
                }
            });
        }, usernameArg);
    }
}
