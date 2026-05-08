package fun.ascent.skyblock.events.impl;

import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.skyblock.events.SEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;

public class PlayerJoinPostEvent extends SEvent<PlayerSpawnEvent> {

    public static Tag<Boolean> menuTag = Tag.Boolean("skyblock_menu");
    public static ItemStack.Builder menuItem = ItemStackCreator.createNamedItemStack(Material.NETHER_STAR,
            MiniMessage.miniMessage().deserialize("<green>Skyblock Menu <gray>(Click)"))
            .lore(
                    MiniMessage.miniMessage().deserialize("<gray>View all of your Skyblock progress,"),
                    MiniMessage.miniMessage().deserialize("<gray>including your Skills, Collections,"),
                    MiniMessage.miniMessage().deserialize("<gray>Recipes, and more!"),
                    Component.empty(),
                    MiniMessage.miniMessage().deserialize("<yellow>Click to open!")
            ).set(menuTag,true);

    @Override
    public void onEvent(PlayerSpawnEvent e) {
        if(e.isFirstSpawn()){
            e.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<yellow>Welcome to <green>Hypixel SkyBlock</green><yellow>!</yellow>"));
            e.getPlayer().getInventory().setItemStack(8,menuItem.build());
        }
    }
}
