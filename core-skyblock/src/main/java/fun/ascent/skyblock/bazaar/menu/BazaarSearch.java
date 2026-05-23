package fun.ascent.skyblock.bazaar.menu;

import fun.ascent.skyblock.bazaar.BazaarItem;
import fun.ascent.skyblock.bazaar.vars.BazaarCategory;
import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.coordinate.Point;
import net.minestom.server.event.player.PlayerEditSignEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.BlockChangePacket;
import net.minestom.server.network.packet.server.play.OpenSignEditorPacket;
import net.minestom.server.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BazaarSearch extends SEvent<PlayerEditSignEvent> {

    private static final Map<Player, Point> pendingSearches = new ConcurrentHashMap<>();

    public static void startSearch(SkyblockPlayer player) {
        player.closeInventory();
        
        Point pos = player.getPosition().add(0, 5, 0).withPitch(0).withYaw(0); // Above player
        pendingSearches.put(player, pos);
        
        player.sendPacket(new BlockChangePacket(pos, Block.OAK_SIGN));
        player.sendPacket(new OpenSignEditorPacket(pos, true));
        
        player.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>Enter your search query on the sign and click Done."));
    }

    @Override
    public void onEvent(PlayerEditSignEvent event) {
        Player player = event.getPlayer();
        if (!pendingSearches.containsKey(player)) return;
        
        Point pos = pendingSearches.remove(player);
        
        // Clean up block change
        player.sendPacket(new BlockChangePacket(pos, Block.AIR));
        
        StringBuilder queryBuilder = new StringBuilder();
        for (String line : event.getLines()) {
            if (line != null && !line.isEmpty()) {
                queryBuilder.append(line).append(" ");
            }
        }
        
        String query = queryBuilder.toString().trim().toLowerCase();
        
        if (query.isEmpty()) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Search cancelled."));
            if (player instanceof SkyblockPlayer sp) {
                BazaarMenu.open(sp);
            }
            return;
        }
        
        List<BazaarItem> results = new ArrayList<>();
        for (Map.Entry<BazaarCategory, List<fun.ascent.skyblock.bazaar.BazaarItemFamily>> entry : fun.ascent.skyblock.bazaar.BazaarRegistry.familyList.entrySet()) {
            for (fun.ascent.skyblock.bazaar.BazaarItemFamily family : entry.getValue()) {
                for (String productId : family.productIds) {
                    if (productId.toLowerCase().contains(query) || 
                        productId.replace("_", " ").toLowerCase().contains(query)) {
                        results.add(new BazaarItem(productId, false, entry.getKey()));
                    }
                }
            }
        }
        
        if (player instanceof SkyblockPlayer sp) {
            if (results.isEmpty()) {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>No items found for '" + query + "'!"));
                BazaarMenu.open(sp);
            } else {
                BazaarSearchMenu.open(sp, query, results, 1);
            }
        }
    }
}
