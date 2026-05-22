package fun.ascent.skyblock.quest.impl;

import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.quest.*;
import fun.ascent.skyblock.world.region.RegionType;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.item.Material;

import java.util.*;

public class QuestBreakLog extends Quest {

    @QuestEvent
    public void onBreak(PlayerBlockBreakEvent event) {
        if (!(event.getPlayer() instanceof SkyblockPlayer sp)) return;
        if (sp.getActiveProfileData() == null) return;

        QuestData data = sp.getActiveProfileData().getQuestData();
        Material material = Material.fromKey(event.getBlock().key());
        if (material == Material.OAK_LOG || material == Material.BIRCH_LOG || 
            material == Material.SPRUCE_LOG || material == Material.DARK_OAK_LOG || 
            material == Material.ACACIA_LOG || material == Material.JUNGLE_LOG ||
            material == Material.OAK_WOOD || material == Material.BIRCH_WOOD || 
            material == Material.SPRUCE_WOOD || material == Material.DARK_OAK_WOOD || 
            material == Material.ACACIA_WOOD || material == Material.JUNGLE_WOOD ||
            (material != null && (material.name().contains("LOG") || material.name().endsWith("_WOOD")))) {
            
            data.endQuest(QuestBreakLog.class);
        }
    }

    @QuestEvent
    public void onSpawn(PlayerSpawnEvent event) {
        if (!(event.getPlayer() instanceof SkyblockPlayer sp)) return;
        if (sp.getActiveProfileData() == null) return;

        QuestData data = sp.getActiveProfileData().getQuestData();
        if (!data.isCurrentlyActive(QuestBreakLog.class) && !data.hasCompleted(QuestBreakLog.class)) {
            data.startQuest(QuestBreakLog.class);
        }
    }

    @Override
    public String getID() {
        return "break_log";
    }

    @Override
    public String getName() {
        return "Break a log";
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
        player.getActiveProfileData().getQuestData().startQuest(QuestCraftWorkbench.class);
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
