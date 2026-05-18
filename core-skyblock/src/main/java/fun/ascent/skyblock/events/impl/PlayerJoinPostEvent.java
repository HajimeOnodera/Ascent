package fun.ascent.skyblock.events.impl;

import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.common.user.UserManager;
import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.SkyblockProfile;
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
