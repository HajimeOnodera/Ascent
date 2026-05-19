package fun.ascent.skyblock.quest.impl;

import fun.ascent.skyblock.events.PlayerCraftItemEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.quest.*;
import fun.ascent.skyblock.world.region.RegionType;
import net.minestom.server.item.Material;

import java.util.*;

public class QuestCraftWoodenPickaxe extends Quest {

    @QuestEvent
    public void onCraft(PlayerCraftItemEvent event) {
        SkyblockPlayer sp = event.getPlayer();
        if (sp.getActiveProfileData() == null) return;

        if (event.getCraftedItem().material() == Material.WOODEN_PICKAXE) {
            sp.getActiveProfileData().getQuestData().endQuest(QuestCraftWoodenPickaxe.class);
        }
    }

    @Override
    public String getID() {
        return "craft_wooden_pickaxe";
    }

    @Override
    public String getName() {
        return "Craft a wooden pickaxe";
    }

    @Override
    public Map<String, Object> onStart(SkyblockPlayer player, ActiveQuest quest) {
        quest.getNewObjectiveText().forEach(player::sendMessage);
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyblockPlayer player, Map<String, Object> customData, ActiveQuest quest) {
        ArrayList<String> rewards = new ArrayList<>();
        rewards.add("10 SkyBlock XP");
        quest.getObjectiveCompleteText(rewards).forEach(player::sendMessage);
        player.getActiveProfileData().getQuestData().startQuest(QuestTalkJerry.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.PRIVATE_ISLAND);
    }

    @Override
    public Double getAttachedSkyBlockXP() {
        return 10D;
    }
}
