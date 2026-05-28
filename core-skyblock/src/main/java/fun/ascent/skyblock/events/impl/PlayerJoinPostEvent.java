package fun.ascent.skyblock.events.impl;

import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.common.redis.RedisManager;
import fun.ascent.common.redis.ServerLookup;
import fun.ascent.common.user.UserManager;
import fun.ascent.common.util.ProxyTransfer;
import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfileManager;
import fun.ascent.skyblock.player.profiles.SkyblockProfile;
import fun.ascent.skyblock.quest.QuestData;
import fun.ascent.skyblock.quest.impl.QuestBreakLog;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.minestom.server.entity.GameMode;

import static fun.ascent.common.StringUtility.*;
import static net.kyori.adventure.text.minimessage.MiniMessage.*;

public class PlayerJoinPostEvent extends SEvent<PlayerSpawnEvent> {

    public static Tag<Boolean> menuTag = Tag.Boolean("skyblock_menu");
    public static ItemStack.Builder menuItem = ItemStackCreator.createNamedItemStack(Material.NETHER_STAR,
            miniMessage().deserialize("<green>Skyblock Menu <gray>(Click)"))
            .lore(
                    miniMessage().deserialize("<gray>View all of your Skyblock progress,"),
                    miniMessage().deserialize("<gray>including your Skills, Collections,"),
                    miniMessage().deserialize("<gray>Recipes, and more!"),
                    Component.empty(),
                    miniMessage().deserialize("<yellow>Click to open!")
            ).set(menuTag,true);

    @Override
    public void onEvent(PlayerSpawnEvent e) {
        e.getPlayer().setGameMode(GameMode.SURVIVAL);
        if(e.isFirstSpawn()){
            e.getPlayer().setDisplayName(UserManager.getDisplayName(e.getPlayer().getUuid()));
            e.getPlayer().sendMessage(text(miniMessage().deserialize("<yellow>Welcome to <green>Ascent SkyBlock</green><yellow>!</yellow>")));
            e.getPlayer().sendMessage(Component.empty());
            
            if (e.getPlayer() instanceof SkyblockPlayer player) {
                String serverType = System.getenv().getOrDefault("ASCENT_SERVER_TYPE", "HUB");

                // 1. Check Redis transfer target to prevent routing loops
                boolean hasTransferTarget = false;
                String transferTarget;
                if (RedisManager.isInitialized()) {
                    transferTarget = RedisManager.get().getTransferTarget(player.getUuid().toString());
                    if (transferTarget != null) {
                        hasTransferTarget = true;
                        if (serverType.equalsIgnoreCase(transferTarget)) {
                            RedisManager.get().clearTransferTarget(player.getUuid().toString());
                        }
                    }
                }

                // 2. Initial connection routing
                if (!hasTransferTarget) {
                    Boolean isFirstTime = player.getTag(SkyblockPlayer.FIRST_TIME_JOIN_TAG);
                    if (isFirstTime != null && isFirstTime) {
                        if (serverType.equalsIgnoreCase("HUB")) {
                            String targetServer = ServerLookup.findAnyByPrefix("island");
                            if (targetServer != null) {
                                if (player.getActiveProfile() != null) {
                                    ProfileManager.saveProfile(player.getActiveProfile().profileID);
                                }
                                if (RedisManager.isInitialized()) {
                                    RedisManager.get().setTransferTarget(player.getUuid().toString(), "island");
                                }
                                ProxyTransfer.send(player, targetServer);
                                return;
                            } else {
                                player.sendMessage(text("<red>Island server is currently offline! Spawning you locally."));
                            }
                        }
                    } else {
                        if (serverType.equalsIgnoreCase("ISLAND")) {
                            String targetServer = ServerLookup.findAnyByPrefix("skyblock");
                            if (targetServer != null) {
                                if (player.getActiveProfile() != null) {
                                    ProfileManager.saveProfile(player.getActiveProfile().profileID);
                                }
                                if (RedisManager.isInitialized()) {
                                    RedisManager.get().setTransferTarget(player.getUuid().toString(), "hub");
                                }
                                ProxyTransfer.send(player, targetServer);
                                return;
                            } else {
                                player.sendMessage(text("<red>SkyBlock Hub is currently offline! Staying on island."));
                            }
                        }
                    }
                }

                // 3. Quest Initialization
                if (player.getActiveProfileData() != null && serverType.equalsIgnoreCase("ISLAND")) {
                    QuestData questData = player.getActiveProfileData().getQuestData();
                    if (questData.getActiveQuests().isEmpty() && questData.getCompletedQuests().isEmpty()) {
                        questData.startQuest(QuestBreakLog.class);
                    }
                }

                // 4. Cancel island-only quests when on Hub
                if (player.getActiveProfileData() != null && !serverType.equalsIgnoreCase("ISLAND")) {
                    QuestData questData = player.getActiveProfileData().getQuestData();
                    questData.cancelIslandOnlyQuests();
                }

                SkyblockProfile activeProfile = player.getActiveProfile();
                if (activeProfile != null) {
                    String profileName = activeProfile.profileName != null ? activeProfile.profileName : "Unknown";
                    String profileId = activeProfile.profileID != null ? activeProfile.profileID.toString() : "Unknown";
                    
                    e.getPlayer().sendMessage(text(miniMessage().deserialize("<green>You are playing on profile: <yellow>" + profileName)));
                    e.getPlayer().sendMessage(text(miniMessage().deserialize("<dark_gray>Profile ID: " + profileId)));
                }
            }
            
            e.getPlayer().getInventory().setItemStack(8,menuItem.build());
        }
    }
}