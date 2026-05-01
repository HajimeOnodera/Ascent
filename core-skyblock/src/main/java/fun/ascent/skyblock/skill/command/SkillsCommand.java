package fun.ascent.skyblock.skill.command;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.skill.SkillType;
import fun.ascent.skyblock.skill.gui.SkillDetailMenu;
import fun.ascent.skyblock.skill.gui.SkillOverviewMenu;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.entity.Player;

import java.util.Locale;

public class SkillsCommand extends Command {

    public SkillsCommand() {
        super("skills", "skill");

        setDefaultExecutor((sender, ctx) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;
            SkillOverviewMenu.open(player);
        });

        ArgumentWord skillArg = ArgumentType.Word("skill");

        addSyntax((sender, ctx) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;

            String input = ctx.get(skillArg).toUpperCase(Locale.ROOT);
            SkillType type;
            try {
                type = SkillType.valueOf(input);
            } catch (IllegalArgumentException e) {
                player.sendMessage("§cUnknown skill: §f" + ctx.get(skillArg));
                return;
            }

            SkillDetailMenu.open(player, type, 0);
        }, skillArg);
    }
}