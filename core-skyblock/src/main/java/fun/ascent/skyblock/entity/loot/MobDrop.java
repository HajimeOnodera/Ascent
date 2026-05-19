package fun.ascent.skyblock.entity.loot;

import net.minestom.server.item.ItemStack;
import fun.ascent.skyblock.player.SkyblockPlayer;

public record MobDrop(ItemStack item, double chance, int minAmount, int maxAmount) {

    public int rolledAmount() {
        if (minAmount == maxAmount) return minAmount;
        return minAmount + (int) (Math.random() * (maxAmount - minAmount + 1));
    }

    public boolean rolls() {
        return Math.random() * 100.0 < chance;
    }

    public boolean rolls(SkyblockPlayer player) {
        double magicFind = 0;
        if (player.getActiveProfileData() != null) {
            var stat = player.getActiveProfileData().stats.get("magic_find");
            if (stat != null) {
                magicFind = stat.getCurValue();
            }
        }

        double adjustedChance = chance * (1.0 + magicFind / 100.0);
        boolean success = Math.random() * 100.0 < adjustedChance;

        if (success && chance < 5.0 && magicFind > 0) {
            net.kyori.adventure.text.Component name = item.get(net.minestom.server.component.DataComponents.CUSTOM_NAME);
            if (name == null) {
                String matName = item.material().name().replace("minecraft:", "").replace("_", " ").toLowerCase();
                name = net.kyori.adventure.text.Component.text(fun.ascent.common.StringUtility.capitalize(matName));
            }

            player.sendMessage(fun.ascent.common.StringUtility.text(
                    "<yellow>✯ <aqua>MAGIC FIND! <gray>You found "
            ).append(name).append(fun.ascent.common.StringUtility.text(
                    " <gray>(+" + Math.round(magicFind) + "% ✯ Magic Find)"
            )));
            player.playSound(net.kyori.adventure.sound.Sound.sound(
                    net.kyori.adventure.key.Key.key("entity.player.levelup"),
                    net.kyori.adventure.sound.Sound.Source.PLAYER, 1f, 1.2f
            ));
        }

        return success;
    }
}
