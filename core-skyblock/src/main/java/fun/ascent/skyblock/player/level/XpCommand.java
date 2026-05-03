package fun.ascent.skyblock.player.level;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfileManager;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;

public class XpCommand extends Command {

    public XpCommand() {
        super("addxp");

        var amount = ArgumentType.Integer("amount");
        addSyntax((sender,context) -> {
            int xp = context.get(amount);
            if(!(sender instanceof SkyblockPlayer player)) {
                System.out.println("Null Player");
                return;
            }
            ProfilePlayer player1 = ProfileManager.getPlayerProfile(player);
            if(player1 == null) {
                System.out.println("Null Profile");
                return;
            }
            player1.addSkyblockXp(xp);

        },amount);
    }
}
