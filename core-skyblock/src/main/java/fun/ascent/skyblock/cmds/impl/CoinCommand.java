package fun.ascent.skyblock.cmds.impl;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;

public class CoinCommand extends Command {

    public CoinCommand() {
        super("coins");

        var amount = ArgumentType.Integer("amount");

        addSyntax((sender, args) -> {
            int am = args.get(amount);
            if(!(sender instanceof SkyblockPlayer pl)) return;
            ProfilePlayer profile = pl.getActiveProfileData();
            profile.playerCoins += am;

        },amount);
    }
}
