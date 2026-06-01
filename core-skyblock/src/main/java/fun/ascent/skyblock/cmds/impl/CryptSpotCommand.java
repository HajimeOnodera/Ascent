package fun.ascent.skyblock.cmds.impl;

import fun.ascent.skyblock.entity.mob.ZonePopulationTicker;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.coordinate.Pos;

import java.util.List;

import static fun.ascent.common.StringUtility.text;

public class CryptSpotCommand extends Command {

    public CryptSpotCommand() {
        super("cryptspot");

        var action = ArgumentType.Word("action").from("add", "list", "clear");

        setDefaultExecutor((sender, ctx) ->
                sender.sendMessage(text("&cUsage: /cryptspot <add/list/clear>")));

        addSyntax((sender, ctx) -> {
            if (!(sender instanceof SkyblockPlayer player)) {
                sender.sendMessage(text("&cOnly players can use this command."));
                return;
            }

            String act = ctx.get(action).toLowerCase();
            switch (act) {
                case "add": {
                    Pos pos = player.getPosition();
                    ZonePopulationTicker.addCryptSpawnSpot(pos);
                    player.sendMessage(text("&aAdded Crypt spawn spot at &f" 
                            + String.format("%.1f, %.1f, %.1f", pos.x(), pos.y(), pos.z())));
                    break;
                }
                case "list": {
                    List<ZonePopulationTicker.MobSpawnSpot> spots = ZonePopulationTicker.getCryptSpawnSpots();
                    if (spots.isEmpty()) {
                        player.sendMessage(text("&7No Crypt spawn spots exist."));
                        return;
                    }
                    player.sendMessage(text("&6Active Crypt Spawn Spots:"));
                    int index = 1;
                    for (ZonePopulationTicker.MobSpawnSpot spot : spots) {
                        Pos pos = spot.getPosition();
                        player.sendMessage(text(String.format(" &e#%d&7: &a%.1f, %.1f, %.1f &7(Active: %d)",
                                index++, pos.x(), pos.y(), pos.z(), spot.getActiveEntities().size())));
                    }
                    break;
                }
                case "clear": {
                    ZonePopulationTicker.clearCryptSpawnSpots();
                    player.sendMessage(text("&aSuccessfully cleared all Crypt spawn spots."));
                    break;
                }
            }
        }, action);
    }
}
