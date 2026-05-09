package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.menus.command.blockGui.EnchantingTableMenu;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class EnchantingTableInteractEvent extends SEvent<PlayerBlockInteractEvent> {

    @Override
    public void onEvent(PlayerBlockInteractEvent event) {
        if (!(event.getPlayer() instanceof SkyblockPlayer player)) return;
        if (!event.getBlock().name().equals(Block.ENCHANTING_TABLE.name())) return;

        event.setCancelled(true);
        event.setBlockingItemUse(true);

        Instance instance = player.getInstance();
        if (instance == null) return;

        Point blockPos = event.getBlockPosition();
        EnchantingTableMenu.open(player, instance, blockPos);
    }
}
