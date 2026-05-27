package fun.ascent.skyblock.player.level.command;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.profiles.ProfileManager;
import fun.ascent.skyblock.player.profiles.ProfilePlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;

import static fun.ascent.common.StringUtility.text;

public class SetSbLevelCommand extends Command {

    public SetSbLevelCommand() {
        super("setsblevel", "setsblvl");

        var levelArg = ArgumentType.Integer("level");
        addSyntax((sender, context) -> {
            int targetLevel = context.get(levelArg);
            if (!(sender instanceof SkyblockPlayer player)) {
                sender.sendMessage(text("<red>This command can only be executed by players."));
                return;
            }

            if (targetLevel < 0) {
                player.sendMessage(text("<red>The level must be a non-negative number."));
                return;
            }

            ProfilePlayer profile = ProfileManager.getPlayerProfile(player);
            if (profile == null) {
                player.sendMessage(text("<red>Failed to load profile."));
                return;
            }

            int previousLevel = profile.level.curLevel;
            profile.level.setLevel(targetLevel);

            if (targetLevel > previousLevel) {
                profile.sendLevelUpMessage(previousLevel, targetLevel);
            } else {
                player.sendMessage(text("<green>Your SkyBlock level has been set to <yellow>" + targetLevel + "<green>."));
            }

        }, levelArg);
    }
}
