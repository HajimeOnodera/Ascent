package fun.ascent.skyblock.entity.loot;

import fun.ascent.skyblock.player.stats.Stats;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import fun.ascent.skyblock.player.SkyblockPlayer;

import static fun.ascent.common.StringUtility.*;

public record MobDrop(ItemStack item, double chance, int minAmount, int maxAmount) {

    public int rolledAmount() {
        if (minAmount == maxAmount) return minAmount;
        return minAmount + (int) (Math.random() * (maxAmount - minAmount + 1));
    }

    public boolean rolls() {
        return Math.random() * 100.0 < chance;
    }

    public boolean rolls(SkyblockPlayer player) {
        double magicFind = player.playerStat(Stats.MAGIC_FIND);

        double adjustedChance = chance * (1.0 + magicFind / 100.0);
        boolean success = Math.random() * 100.0 < adjustedChance;

        if (success && chance < 5.0 && magicFind > 0) {
            Component name = item.get(DataComponents.CUSTOM_NAME);
            if (name == null) {
                String matName = item.material().name().replace("minecraft:", "").replace("_", " ").toLowerCase();
                name = Component.text(capitalize(matName));
            }

            player.sendMessage(text(
                    "<yellow>✯ <aqua>MAGIC FIND! <gray>You found "
            ).append(name).append(text(
                    " <gray>(+" + Math.round(magicFind) + "% ✯ Magic Find)"
            )));
            player.playSound(Sound.sound(Key.key("entity.player.levelup"), Sound.Source.PLAYER, 1f, 1.2f));
        }

        return success;
    }
}
