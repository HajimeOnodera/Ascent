package fun.ascent.skyblock.menus.command;

import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;

import static fun.ascent.common.StringUtility.text;

public class GuiCommand extends Command {

    public GuiCommand() {
        super("opengui");

        ArgumentWord guiArg = ArgumentType.Word("guiName");

        addSyntax((sender, ctx) -> {
            if (!(sender instanceof SkyblockPlayer player)) {
                sender.sendMessage("This command is for players only.");
                return;
            }

            String guiName = ctx.get(guiArg);
            String[] packages = {
                "fun.ascent.skyblock.menus.",
                "fun.ascent.skyblock.player.skill.gui.",
                "fun.ascent.skyblock.menus.command.blockGui."
            };

            boolean found = false;
            for (String pkg : packages) {
                try {
                    Class<?> clazz = Class.forName(pkg + guiName);
                    java.lang.reflect.Method openMethod = clazz.getMethod("open", SkyblockPlayer.class);
                    openMethod.invoke(null, player);
                    player.sendMessage(text("<green>Opened GUI: <white>" + guiName));
                    found = true;
                    break;
                } catch (ClassNotFoundException ignored) {
                } catch (NoSuchMethodException e) {
                    player.sendMessage(text("<red>GUI " + guiName + " does not have a static open(SkyblockPlayer) method."));
                    found = true;
                    break;
                } catch (Exception e) {
                    player.sendMessage(text("<red>Error opening GUI: " + e.getMessage()));
                    e.printStackTrace();
                    found = true;
                    break;
                }
            }

            if (!found) {
                player.sendMessage(text("<red>GUI " + guiName + " not found."));
            }
        }, guiArg);
    }
}

