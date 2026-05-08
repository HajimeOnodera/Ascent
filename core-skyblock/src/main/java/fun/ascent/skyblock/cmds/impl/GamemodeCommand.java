package fun.ascent.skyblock.cmds.impl;

import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.GameMode;

import java.util.ArrayList;
import java.util.List;

import static fun.ascent.common.StringUtility.*;
import static net.minestom.server.entity.GameMode.*;

public class GamemodeCommand extends Command {

    public GamemodeCommand() {
        super("gamemode","gm");

        var gamemode = ArgumentType.String("gamemode");

        setDefaultExecutor((sender,_) -> sender.sendMessage(text(MiniMessage.miniMessage().deserialize("<red> Please include the gamemode name"))));

        gamemode.setSuggestionCallback((_,_,suggestion) -> {
            String input = suggestion.getInput();
            List<String> mods = new ArrayList<>(List.of("Creative", "Adventure", "Survival", "Spectator"));
            if(!input.isEmpty()) {
                mods.removeIf(mod -> !mod.toLowerCase().startsWith(input.toLowerCase()));
            }
            mods.forEach(m -> suggestion.addEntry(new SuggestionEntry(m)));
        });
        addSyntax((sender,args) -> {
            if(!(sender instanceof SkyblockPlayer player)) return;
            String mode = args.get(gamemode);
            GameMode modeToSet;
            modeToSet = switch (mode.toLowerCase()){
                case "spectator" ,"2", "sp" -> SPECTATOR;
                case "survival", "0", "s" -> SURVIVAL;
                case "creative", "1", "c" -> CREATIVE;
                case "adventure", "3", "ad" -> ADVENTURE;
                default -> null;
            };
            if(modeToSet == null){
                player.sendMessage(text(MiniMessage.miniMessage().deserialize("<red>Could not find this gamemode.")));
                return;
            }
            player.setGameMode(modeToSet);
            player.sendMessage(text(MiniMessage.miniMessage().deserialize("<green>Set Gamemode to <yellow>" + modeToSet.name().toLowerCase() + ".")));
        },gamemode);
    }
}
