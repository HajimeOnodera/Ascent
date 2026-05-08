package fun.ascent.skyblock.player.stats.command;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.stats.Stats;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;

public class StatCommand extends Command {

    public StatCommand() {
        super("stat");

        var action = ArgumentType.Word("action").from("set", "show");
        var statArg = ArgumentType.Word("stat");
        var valueArg = ArgumentType.Double("value");

        statArg.setSuggestionCallback((sender, context, suggestion) -> {
            String input = suggestion.getInput().toLowerCase();
            for (Stats stat : Stats.values()) {
                String name = stat.name().toLowerCase();
                if (input.isEmpty() || name.startsWith(input)) {
                    suggestion.addEntry(new SuggestionEntry(stat.name()));
                }
            }
        });

        setDefaultExecutor((sender, ctx) ->
                sender.sendMessage("§cUsage: /stat <set/show> [stat] [value]"));

        addSyntax((sender, ctx) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;
            String act = ctx.get(action);
            if (!act.equals("show")) {
                sender.sendMessage("§cUsage: /stat set <stat> <value> or /stat show");
                return;
            }
            showStats(player);
        }, action);

        addSyntax((sender, ctx) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;
            String act = ctx.get(action);
            if (!act.equals("set")) {
                sender.sendMessage("§cUsage: /stat set <stat> <value>");
                return;
            }
            String statName = ctx.get(statArg);
            double value = ctx.get(valueArg);

            Stats stat;
            try {
                stat = Stats.valueOf(statName.toUpperCase());
            } catch (IllegalArgumentException e) {
                sender.sendMessage("§cUnknown stat: " + statName);
                return;
            }

            if (player.getActiveProfileData() == null) {
                sender.sendMessage("§cNo active profile.");
                return;
            }

            player.getActiveProfileData().stats.setBase(stat, value);
            player.updatePlayer();
            sender.sendMessage("§aSet " + stat.getStatColor() + stat.getStatFormattedDisplay()
                    + " §ato §f" + (int) value);
        }, action, statArg, valueArg);
    }

    private void showStats(SkyblockPlayer player) {
        player.sendMessage("§6§l     Player Stats");
        player.sendMessage("");
        for (Stats stat : Stats.values()) {
            double value = player.stat(stat);
            if (value == 0) continue;
            String suffix = stat.getStatIntType() ? "%" : "";
            String symbol = stat.getStatSymbol();
            player.sendMessage(" " + symbol + " §7" + stat.getStatFormattedDisplay()
                    + ": " + stat.getStatColor() + (int) value + suffix);
        }
    }
}
