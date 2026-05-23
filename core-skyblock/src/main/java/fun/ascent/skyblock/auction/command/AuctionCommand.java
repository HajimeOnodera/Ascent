package fun.ascent.skyblock.auction.command;

import fun.ascent.skyblock.auction.menu.AuctionMenu;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

import static fun.ascent.common.StringUtility.text;

public class AuctionCommand extends Command {

    public AuctionCommand() {
        super("auction", "ah");

        setDefaultExecutor((sender, ctx) -> {
            if (sender instanceof Player player) {
                if (player instanceof SkyblockPlayer sp) {
                    AuctionMenu.open(sp);
                } else {
                    player.sendMessage(text("<red>You must be a SkyblockPlayer to open the Auction House."));
                }
            } else {
                sender.sendMessage(text("<red>Only players can use this command."));
            }
        });
    }
}
