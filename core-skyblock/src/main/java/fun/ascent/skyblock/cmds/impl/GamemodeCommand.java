package fun.ascent.skyblock.cmds.impl;

import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.GameMode;

import java.util.ArrayList;
import java.util.List;

public class GamemodeCommand extends Command {

    public GamemodeCommand() {
        super("gamemode","gm");

        var gamemode = ArgumentType.String("gamemode");

        setDefaultExecutor((sender,_) -> {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red> Please include the gamemode name"));
        });

        gamemode.setSuggestionCallback((_,_,suggestion) -> {
            String input = suggestion.getInput();
            List<String> mods = new ArrayList<>(List.of("Creative", "Adventure", "Survival", "Spectator"));
            if(!input.isEmpty()) {
                for (String mod : new ArrayList<>(mods)) {
                    if(!mod.toLowerCase().startsWith(input.toLowerCase())) mods.remove(mod);
                }
            }
            mods.forEach(m -> suggestion.addEntry(new SuggestionEntry(m)));
        });
        addSyntax((sender,args) -> {
            if(!(sender instanceof SkyblockPlayer player)) return;
            String mode = args.get(gamemode);
            GameMode modeToSet = null;
            modeToSet = switch (mode.toLowerCase()){
                case "spectator" ,"2", "sp" -> GameMode.SPECTATOR;
                case "survival", "0", "s" -> GameMode.SURVIVAL;
                case "creative", "1", "c" -> GameMode.CREATIVE;
                case "adventure", "3", "ad" -> GameMode.ADVENTURE;
                default -> null;
            };
            if(modeToSet == null){
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Could not find this gamemode."));
                return;
            }
            player.setGameMode(modeToSet);
            player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Set Gamemode to <yellow>" + modeToSet.name().toLowerCase() + "."));
        },gamemode);
    }
}
