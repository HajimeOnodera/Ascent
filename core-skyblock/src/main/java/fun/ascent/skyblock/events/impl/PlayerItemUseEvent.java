package fun.ascent.skyblock.events.impl;

import fun.ascent.skyblock.events.SEvent;
import fun.ascent.skyblock.item.ItemAbility;
import fun.ascent.skyblock.item.ItemNBT;
import fun.ascent.skyblock.item.items.ItemDefinition;
import fun.ascent.skyblock.item.items.ItemDefinitions;
import fun.ascent.skyblock.menus.SkyblockMenu;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.actionbar.ActionBar;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;

import static fun.ascent.common.StringUtility.text;

public class PlayerItemUseEvent extends SEvent<PlayerUseItemEvent> {

    @Override
    public void onEvent(PlayerUseItemEvent event) {
        ItemStack item = event.getItemStack();

        var value = item.getTag(PlayerJoinPostEvent.menuTag);
        if (value != null && value) {
            if (event.getPlayer() instanceof SkyblockPlayer pl) {
                if (pl.getActiveProfile() != null && pl.getActiveProfileData() != null) {
                    SkyblockMenu.open(pl);
                }
            }
            return;
        }

        if (!(event.getPlayer() instanceof SkyblockPlayer player)) return;
        if (!ItemNBT.isSkyblockItem(item)) return;

        String itemId = ItemNBT.getItemId(item);
        ItemDefinition definition = ItemDefinitions.get(itemId);
        if (definition == null) return;

        ItemAbility ability = definition.ability();
        if (ability == null || ability.abilityType() != ItemAbility.AbilityType.RIGHT_CLICK) return;

        if (ability.cooldownSeconds() > 0 && !player.startCooldown(itemId, ability.cooldownSeconds())) {
            int remaining = player.getCooldownRemaining(itemId);
            player.sendMessage(text("<red>This ability is on cooldown for " + remaining + "s."));
            return;
        }

        if (ability.manaCost() > 0 && !player.consumeMana(ability.manaCost())) {
            if (ability.cooldownSeconds() > 0) player.startCooldown(itemId, 0);
            return;
        }

        if (ability.manaCost() > 0) {
            ActionBar.of(player.getUuid()).addReplacement(
                    ActionBar.Section.DEFENSE,
                    "<aqua>-" + ability.manaCost() + " (<gold>" + ability.abilityName() + "<aqua>)",
                    40, 10);
        }

        definition.onRightClick(player);
        event.setCancelled(true);
    }
}
