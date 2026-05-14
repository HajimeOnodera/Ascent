package fun.ascent.skyblock.cmds.impl;

import fun.ascent.skyblock.crafting.gui.GUIRecipeBook;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;

public class RecipesCommand extends Command {
    public RecipesCommand() {
        super("recipes", "recipebook");

        setDefaultExecutor((sender, _) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;
            GUIRecipeBook.open(player);
        });
    }
}
