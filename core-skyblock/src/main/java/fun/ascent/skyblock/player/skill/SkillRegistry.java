package fun.ascent.skyblock.player.skill;

import fun.ascent.skyblock.events.EventManager;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.skill.event.SkillXpGainEvent;
import fun.ascent.skyblock.player.skill.impl.*;
import fun.ascent.skyblock.player.skill.unlock.SkillUnlock;

import java.util.EnumMap;
import java.util.Map;

public class SkillRegistry {

    private static final Map<SkillType, SkillDefinition> definitions = new EnumMap<>(SkillType.class);

    public static void init() {
        definitions.clear();

        register(SkillType.COMBAT, new CombatSkill());
        register(SkillType.FARMING, new FarmingSkill());
        register(SkillType.MINING, new MiningSkill());
        register(SkillType.FISHING, new FishingSkill());
        register(SkillType.FORAGING, new ForagingSkill());
        register(SkillType.ENCHANTING, new EnchantingSkill());
        register(SkillType.ALCHEMY, new AlchemySkill());
        register(SkillType.TAMING, new TamingSkill());
        register(SkillType.CARPENTRY, new CarpentrySkill());
        register(SkillType.RUNECRAFTING, new RunecraftingSkill());
        register(SkillType.DUNGEONEERING, new DungeoneeringSkill());
        register(SkillType.HUNTING, new HuntingSkill());
        register(SkillType.SOCIAL, new SocialSkill());

        for (SkillType type : SkillType.values()) {
            if (!definitions.containsKey(type)) {
                throw new IllegalStateException("Missing skill definition for " + type.name());
            }
        }
    }

    private static void register(SkillType type, SkillDefinition definition) {
        definition.validate();

        SkillDefinition previous = definitions.put(type, definition);
        if (previous != null) {
            throw new IllegalStateException("Duplicate skill definition for " + type.name());
        }
    }

    public static SkillDefinition get(SkillType type) {
        SkillDefinition definition = definitions.get(type);
        if (definition == null) {
            throw new IllegalStateException("Skill registry is not initialized for " + type.name());
        }
        return definition;
    }

    public static void grantXp(SkyblockPlayer player, SkillType type, double amount) {
        if (player.getActiveProfileData() == null) return;
        if (amount <= 0) return;

        PlayerSkillData data = player.getActiveProfileData().skillData;
        SkillDefinition def = type.definition();

        double oldXp = data.getRawXp(type);
        int oldLevel = def.levelFor(oldXp);

        data.addXp(type, amount);

        double newXp = data.getRawXp(type);
        int newLevel = def.levelFor(newXp);

        SkillXpGainEvent event = new SkillXpGainEvent(player, type, amount, oldXp, newXp, oldLevel, newLevel);
        EventManager.handler.call(event);

        if (newLevel > oldLevel) {
            for (int lvl = oldLevel + 1; lvl <= newLevel; lvl++) {
                SkillReward reward = def.rewardAt(lvl);
                if (reward == null) continue;
                for (SkillUnlock unlock : reward.unlocks()) {
                    unlock.apply(player);
                }
            }
        }
    }
}
