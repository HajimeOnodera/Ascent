package fun.ascent.skyblock.cmds.impl;

import fun.ascent.skyblock.menus.shop.ShopMenu;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;

public class ShopCommand extends Command {

    public ShopCommand() {
        super("shop");

        var shopID = ArgumentType.String("shop");
        addSyntax((sender, args) -> {

            String shop = args.get(shopID);
            if(!(sender instanceof SkyblockPlayer pl)) return;
            System.out.println("[SHOP] Opening Shop " + shop);
            ShopMenu.open(pl,shop);

        },shopID);
    }
}
