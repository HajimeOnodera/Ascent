package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.crafting.gui.CraftingMenu;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.instance.block.Block;

public class CraftingTableInteractEvent extends SEvent<PlayerBlockInteractEvent> {

    @Override
    public void onEvent(PlayerBlockInteractEvent event) {
        if (event.getBlock().compare(Block.CRAFTING_TABLE)) {
            if (event.getPlayer() instanceof SkyblockPlayer player) {
                event.setCancelled(true);
                CraftingMenu.open(player);
            }
        }
    }
}
