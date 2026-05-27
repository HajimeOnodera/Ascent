package fun.ascent.skyblock.quest.impl;

import fun.ascent.skyblock.events.PlayerCraftItemEvent;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.quest.*;
import fun.ascent.skyblock.world.region.RegionType;
import net.minestom.server.item.Material;

import java.util.*;

public class QuestCraftWorkbench extends Quest {

    @QuestEvent
    public void onCraft(PlayerCraftItemEvent event) {
        SkyblockPlayer sp = event.getPlayer();
        if (sp.getActiveProfileData() == null) return;

        if (event.getCraftedItem().material() == Material.CRAFTING_TABLE) {
            sp.getActiveProfileData().getQuestData().endQuest(QuestCraftWorkbench.class);
        }
    }

    @Override
    public String getID() {
        return "craft_workbench";
    }

    @Override
    public String getName() {
        return "Craft a workbench";
    }

    @Override
    public Map<String, Object> onStart(SkyblockPlayer player, ActiveQuest quest) {
        quest.getNewObjectiveText().forEach(player::sendMessage);
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyblockPlayer player, Map<String, Object> customData, ActiveQuest quest) {
        player.getActiveProfileData().getQuestData().startQuest(QuestCraftWoodenPickaxe.class);
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.PRIVATE_ISLAND);
    }
}
