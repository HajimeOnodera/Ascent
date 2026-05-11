package fun.ascent.common.command;

import fun.ascent.common.user.User;
import fun.ascent.common.user.UserManager;
import fun.ascent.database.PlayerRepository;
import lombok.Setter;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

import java.util.function.Consumer;

import static fun.ascent.common.StringUtility.text;

public class AchievementCommand extends Command {

    @Setter
    private static Consumer<Player> guiOpener;

    public AchievementCommand() {
        super("achievements", "achievement", "ach");

        setDefaultExecutor((sender, _) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(text("<red>This command is for players only!"));
                return;
            }

            if (guiOpener != null) {
                guiOpener.accept(player);
            } else {
                player.sendMessage(text("<red>Achievement menu is not available on this server!"));
            }
        });

        addSyntax((sender, _) -> {
            if (!(sender instanceof Player player)) return;
            
            // Wipe achievements
            PlayerRepository.setSection(player.getUuid(), "achievements", new org.bson.Document());
            
            // Reset cached points
            User user = UserManager.getUser(player.getUuid());
            if (user != null) user.setAchievementPoints(0);
            
            player.sendMessage(text("<green>Successfully wiped your achievements for testing!"));
        }, ArgumentType.Literal("wipe"));
    }
}
