package fun.ascent.skyblock.minion.command;

import fun.ascent.skyblock.minion.MinionManager;
import fun.ascent.skyblock.minion.MinionItems;
import fun.ascent.skyblock.minion.MinionType;
import fun.ascent.skyblock.minion.SkyblockMinion;
import fun.ascent.skyblock.minion.gui.MinionMenu;
import fun.ascent.skyblock.player.SkyblockPlayer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;

public final class MinionCommand extends Command {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public MinionCommand() {
        super("minion");

        ArgumentWord action = ArgumentType.Word("action");
        ArgumentWord type = ArgumentType.Word("type");
        var tier = ArgumentType.Integer("tier").min(1).max(12);

        addSyntax((sender, context) -> {
            if (!(sender instanceof SkyblockPlayer player)) {
                sender.sendMessage("This command is for players only.");
                return;
            }

            String actionName = context.get(action);
            if (actionName.equalsIgnoreCase("give") || actionName.equalsIgnoreCase("get")) {
                String typeName = context.get(type);
                MinionType minionType = MinionType.fromId(typeName);
                if (minionType == null) {
                    player.sendMessage(MINI_MESSAGE.deserialize("<red>Unknown minion type.</red>"));
                    return;
                }
                int targetTier = context.get(tier);
                player.getInventory().addItemStack(MinionItems.createPlacementItem(minionType, targetTier));
                player.sendMessage(MINI_MESSAGE.deserialize("<green>Given " + minionType.getDisplayName() + " " + targetTier + ".</green>"));
                return;
            }
            if (actionName.equalsIgnoreCase("place")) {
                String typeName = context.get(type);
                MinionType minionType = MinionType.fromId(typeName);
                if (minionType == null) {
                    player.sendMessage(MINI_MESSAGE.deserialize("<red>Unknown minion type.</red>"));
                    return;
                }
                MinionManager.placeMinion(player, minionType);
                return;
            }

            if (actionName.equalsIgnoreCase("menu")) {
                SkyblockMinion minion = MinionManager.getNearestOwnedMinion(player, 4);
                if (minion == null) {
                    player.sendMessage(MINI_MESSAGE.deserialize("<red>No nearby minion found.</red>"));
                    return;
                }
                MinionMenu.open(player, minion);
                return;
            }

            if (actionName.equalsIgnoreCase("list")) {
                int count = MinionManager.getOwnedMinions(player).size();
                player.sendMessage(MINI_MESSAGE.deserialize("<yellow>You have <white>" + count + "</white> minions placed.</yellow>"));
                return;
            }

            if (actionName.equalsIgnoreCase("types")) {
                StringBuilder builder = new StringBuilder("<yellow>Available minions:</yellow> <white>");
                MinionType[] values = MinionType.values();
                for (int i = 0; i < values.length; i++) {
                    if (i > 0) {
                        builder.append("<gray>, </gray>");
                    }
                    builder.append(values[i].getId());
                }
                builder.append("</white>");
                player.sendMessage(MINI_MESSAGE.deserialize(builder.toString()));
                return;
            }

            player.sendMessage(MINI_MESSAGE.deserialize("<red>Usage: /minion give|get <type> <tier>, /minion place <type>, /minion menu, /minion list, /minion types</red>"));
        }, action, type, tier);

        addSyntax((sender, context) -> {
            if (!(sender instanceof SkyblockPlayer player)) {
                sender.sendMessage("This command is for players only.");
                return;
            }

            String actionName = context.get(action);
            if (actionName.equalsIgnoreCase("place")) {
                String typeName = context.get(type);
                MinionType minionType = MinionType.fromId(typeName);
                if (minionType == null) {
                    player.sendMessage(MINI_MESSAGE.deserialize("<red>Unknown minion type.</red>"));
                    return;
                }
                MinionManager.placeMinion(player, minionType);
                return;
            }

            if (actionName.equalsIgnoreCase("give") || actionName.equalsIgnoreCase("get")) {
                String typeName = context.get(type);
                MinionType minionType = MinionType.fromId(typeName);
                if (minionType == null) {
                    player.sendMessage(MINI_MESSAGE.deserialize("<red>Unknown minion type.</red>"));
                    return;
                }
                player.getInventory().addItemStack(MinionItems.createPlacementItem(minionType, 1));
                player.sendMessage(MINI_MESSAGE.deserialize("<green>Given " + minionType.getDisplayName() + " I.</green>"));
                return;
            }

            player.sendMessage(MINI_MESSAGE.deserialize("<red>Usage: /minion give|get <type> <tier>, /minion place <type>, /minion menu, /minion list, /minion types</red>"));
        }, action, type);

        addSyntax((sender, context) -> {
            if (!(sender instanceof SkyblockPlayer player)) {
                sender.sendMessage("This command is for players only.");
                return;
            }

            String actionName = context.get(action);
            if (actionName.equalsIgnoreCase("menu")) {
                SkyblockMinion minion = MinionManager.getNearestOwnedMinion(player, 4);
                if (minion == null) {
                    player.sendMessage(MINI_MESSAGE.deserialize("<red>No nearby minion found.</red>"));
                    return;
                }
                MinionMenu.open(player, minion);
                return;
            }

            if (actionName.equalsIgnoreCase("list")) {
                int count = MinionManager.getOwnedMinions(player).size();
                player.sendMessage(MINI_MESSAGE.deserialize("<yellow>You have <white>" + count + "</white> minions placed.</yellow>"));
                return;
            }

            if (actionName.equalsIgnoreCase("types")) {
                StringBuilder builder = new StringBuilder("<yellow>Available minions:</yellow> <white>");
                MinionType[] values = MinionType.values();
                for (int i = 0; i < values.length; i++) {
                    if (i > 0) {
                        builder.append("<gray>, </gray>");
                    }
                    builder.append(values[i].getId());
                }
                builder.append("</white>");
                player.sendMessage(MINI_MESSAGE.deserialize(builder.toString()));
                return;
            }

            player.sendMessage(MINI_MESSAGE.deserialize("<red>Usage: /minion give|get <type> <tier>, /minion place <type>, /minion menu, /minion list, /minion types</red>"));
        }, action);

        setDefaultExecutor((sender, context) ->
                sender.sendMessage("Usage: /minion give|get <type> <tier>, /minion place <type>, /minion menu, /minion list, /minion types"));
    }
}
