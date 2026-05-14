package fun.ascent.skyblock.player.stats.command;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.stats.Stat;
import fun.ascent.skyblock.player.stats.StatBuilder;
import fun.ascent.skyblock.player.stats.Stats;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;

import static fun.ascent.common.StringUtility.text;

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
                sender.sendMessage(text("<red>Usage: /stat <set/show> [stat] [value]")));

        addSyntax((sender, ctx) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;
            String act = ctx.get(action);
            if (!act.equals("show")) {
                sender.sendMessage(text("<red>Usage: /stat set <stat> <value> or /stat show"));
                return;
            }
            showStats(player);
        }, action);

        addSyntax((sender, ctx) -> {
            if (!(sender instanceof SkyblockPlayer player)) return;
            String act = ctx.get(action);
            if (!act.equals("set")) {
                sender.sendMessage(text("<red>Usage: /stat set <stat> <value>"));
                return;
            }
            String statName = ctx.get(statArg);
            double value = ctx.get(valueArg);

            Stats stat;
            try {
                stat = Stats.valueOf(statName.toUpperCase());
            } catch (IllegalArgumentException e) {
                sender.sendMessage(text("<red>Unknown stat: " + statName));
                return;
            }

            if (player.getActiveProfileData() == null) {
                sender.sendMessage(text("<red>No active profile."));
                return;
            }

            Stat built = StatBuilder.build(stat);
            built.setMaxValue(value);
            built.setCurValue(value);
            player.getActiveProfileData().stats.put(built.id, built);
            player.updatePlayer();
            sender.sendMessage(text("<green>Set " + stat.getStatColor() + stat.getStatFormattedDisplay()
                    + " <green>to <white>" + (int) value));
        }, action, statArg, valueArg);
    }

    private void showStats(SkyblockPlayer player) {
        player.sendMessage(text("<gold><bold>     Player Stats"));
        player.sendMessage("");
        for (Stats stat : Stats.values()) {
            double value = player.playerStat(stat);
            String suffix = stat.getStatIntType() ? "%" : "";
            String symbol = stat.getStatSymbol();
            player.sendMessage(text(" " + symbol + " <gray>" + stat.getStatFormattedDisplay()
                    + ": " + stat.getStatColor() + (int) value + suffix));
        }
    }
}

