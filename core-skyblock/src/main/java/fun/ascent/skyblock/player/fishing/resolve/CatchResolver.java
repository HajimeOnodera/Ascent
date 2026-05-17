package fun.ascent.skyblock.player.fishing.resolve;

import fun.ascent.skyblock.entity.mob.SkyblockMobEntity;
import fun.ascent.skyblock.events.EventManager;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.skill.SkillRegistry;
import fun.ascent.skyblock.player.skill.SkillType;
import fun.ascent.skyblock.player.stats.Stats;
import fun.ascent.skyblock.player.fishing.WaterHookEntity;
import fun.ascent.skyblock.player.fishing.bait.BaitDefinitions;
import fun.ascent.skyblock.player.fishing.bait.BaitEffect;
import fun.ascent.skyblock.player.fishing.event.FishCaughtEvent;
import fun.ascent.skyblock.player.fishing.loot.*;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.sound.SoundEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

import static fun.ascent.common.StringUtility.text;

public final class CatchResolver {

    private CatchResolver() {}

    public static CatchResult resolve(@NotNull WaterHookEntity hook) {
        SkyblockPlayer player = hook.getOwner();
        Instance instance = hook.getInstance();
        if (instance == null) return null;

        String activeBait = player.getTag(fun.ascent.skyblock.events.impl.RodInteractionHandler.ACTIVE_BAIT_TAG);
        BaitEffect bait = BaitDefinitions.get(activeBait);

        double seaCreatureChance = player.playerStat(Stats.SEA_CREATURE_CHANCE) + (bait.seaCreatureModifier() * 100.0);
        double treasureChance = player.playerStat(Stats.TREASURE_CHANCE) + (bait.treasureModifier() * 100.0);

        int fishingLevel = player.getActiveProfileData() != null 
                ? player.getActiveProfileData().skillData.getLevel(SkillType.FISHING) 
                : 1;

        double roll = ThreadLocalRandom.current().nextDouble(100.0);
        CatchResult result;

        if (roll < seaCreatureChance) {
            result = triggerSeaCreature(player, instance, hook.getPosition(), fishingLevel);
        } else if (roll < (seaCreatureChance + treasureChance)) {
            result = triggerTreasure(player, instance, hook.getPosition());
        } else {
            result = triggerNormalCatch(player, instance, hook.getPosition());
        }

        if (result != null) {
            SkillRegistry.grantXp(player, SkillType.FISHING, result.fishingXp());

            if (result.category() == CatchCategory.NORMAL_CATCH || result.category() == CatchCategory.TREASURE) {
                if (result.rewardStack() != null && !result.rewardStack().isAir()) {
                    String materialName = result.rewardStack().material().name();
                    String collectionId = materialName;
                    if (materialName.equals("COD")) {
                        collectionId = "RAW_FISH";
                    } else if (materialName.equals("SALMON")) {
                        collectionId = "RAW_SALMON";
                    }

                    if (player.getActiveProfile() != null) {
                        player.getActiveProfile().updateCollection(collectionId, result.rewardStack().amount());
                    }
                }
            }

            EventManager.handler.call(new FishCaughtEvent(player, result));
        }

        return result;
    }

    private static CatchResult triggerSeaCreature(SkyblockPlayer player, Instance instance, Pos pos, int fishingLevel) {
        Class<? extends SkyblockMobEntity> mobClass = SeaCreaturePool.roll(fishingLevel);
        try {
            SkyblockMobEntity mob = mobClass.getDeclaredConstructor().newInstance();
            mob.setInstance(instance, pos.add(0, 0.5, 0));
            
            player.sendMessage(text("<red>A Sea Creature emerged: " + mob.displayName() + "!"));
            player.playSound(Sound.sound(SoundEvent.ENTITY_WITHER_SPAWN, Sound.Source.MASTER, 0.8f, 1.2f));

            double xp = mob.level() * 15.0;
            return new CatchResult(CatchCategory.SEA_CREATURE, ItemStack.AIR, xp, mob.displayName());
        } catch (Exception e) {
            return triggerNormalCatch(player, instance, pos);
        }
    }

    private static CatchResult triggerTreasure(SkyblockPlayer player, Instance instance, Pos pos) {
        FishLootEntry entry = TreasureLootPool.get().roll();
        int amount = entry.rollAmount();
        ItemStack stack = ItemStack.of(entry.material()).withAmount(amount);

        ItemEntity entity = new ItemEntity(stack);
        entity.setInstance(instance, pos.add(0, 0.5, 0));
        
        Pos playerPos = player.getPosition();
        double dx = playerPos.x() - pos.x();
        double dy = playerPos.y() - pos.y();
        double dz = playerPos.z() - pos.z();
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        
        double speedScale = 0.075;
        double vertScale = 0.065;
        
        double vx = dx * speedScale * 20.0;
        double vy = (dy * speedScale + Math.sqrt(distance) * vertScale) * 20.0;
        double vz = dz * speedScale * 20.0;
        
        Vec vel = new Vec(vx, vy, vz);
        entity.setVelocity(vel);

        player.sendMessage(text("<gold>A treasure was pulled up: " + entry.displayName() + "!"));
        player.playSound(Sound.sound(SoundEvent.ENTITY_PLAYER_LEVELUP, Sound.Source.MASTER, 0.9f, 1.1f));

        return new CatchResult(CatchCategory.TREASURE, stack, entry.xpReward(), entry.displayName());
    }

    private static CatchResult triggerNormalCatch(SkyblockPlayer player, Instance instance, Pos pos) {
        FishLootEntry entry = StandardFishPool.get().roll();
        int amount = entry.rollAmount();
        ItemStack stack = ItemStack.of(entry.material()).withAmount(amount);

        ItemEntity entity = new ItemEntity(stack);
        entity.setInstance(instance, pos.add(0, 0.5, 0));
        
        Pos playerPos = player.getPosition();
        double dx = playerPos.x() - pos.x();
        double dy = playerPos.y() - pos.y();
        double dz = playerPos.z() - pos.z();
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        
        double speedScale = 0.075;
        double vertScale = 0.065;
        
        double vx = dx * speedScale * 20.0;
        double vy = (dy * speedScale + Math.sqrt(distance) * vertScale) * 20.0;
        double vz = dz * speedScale * 20.0;
        
        Vec vel = new Vec(vx, vy, vz);
        entity.setVelocity(vel);

        player.playSound(Sound.sound(SoundEvent.ENTITY_ITEM_PICKUP, Sound.Source.MASTER, 1.0f, 1.5f));

        return new CatchResult(CatchCategory.NORMAL_CATCH, stack, entry.xpReward(), entry.displayName());
    }
}
