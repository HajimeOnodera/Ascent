package fun.ascent.skyblock.minion;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

import java.util.UUID;

public final class MinionFactory {
    private MinionFactory() {
    }

    public static SkyblockMinion create(UUID ownerUuid, MinionType type, int tier, Instance instance, Pos position) {
        return switch (type) {
            case COAL -> new CoalMinion(ownerUuid, tier, instance, position);
            case COBBLESTONE -> new CobblestoneMinion(ownerUuid, tier, instance, position);
            case DIAMOND -> new DiamondMinion(ownerUuid, tier, instance, position);
            case EMERALD -> new EmeraldMinion(ownerUuid, tier, instance, position);
            case ENDSTONE -> new EndStoneMinion(ownerUuid, tier, instance, position);
            case GLOWSTONE -> new GlowstoneMinion(ownerUuid, tier, instance, position);
            case GOLD -> new GoldMinion(ownerUuid, tier, instance, position);
            case GRAVEL -> new GravelMinion(ownerUuid, tier, instance, position);
            case HARDSTONE -> new HardStoneMinion(ownerUuid, tier, instance, position);
            case ICE -> new IceMinion(ownerUuid, tier, instance, position);
            case IRON -> new IronMinion(ownerUuid, tier, instance, position);
            case LAPIS -> new LapisMinion(ownerUuid, tier, instance, position);
            case OBSIDIAN -> new ObsidianMinion(ownerUuid, tier, instance, position);
            case QUARTZ -> new QuartzMinion(ownerUuid, tier, instance, position);
            case REDSTONE -> new RedstoneMinion(ownerUuid, tier, instance, position);
            case SAND -> new SandMinion(ownerUuid, tier, instance, position);
            case SNOW -> new SnowMinion(ownerUuid, tier, instance, position);
            case CACTUS -> new CactusMinion(ownerUuid, tier, instance, position);
            case CARROT -> new CarrotMinion(ownerUuid, tier, instance, position);
            case COCOA -> new CocoaMinion(ownerUuid, tier, instance, position);
            case MELON -> new MelonMinion(ownerUuid, tier, instance, position);
            case NETHER_WART -> new NetherWartMinion(ownerUuid, tier, instance, position);
            case POTATO -> new PotatoMinion(ownerUuid, tier, instance, position);
            case PUMPKIN -> new PumpkinMinion(ownerUuid, tier, instance, position);
            case SUGARCANE -> new SugarCaneMinion(ownerUuid, tier, instance, position);
            case WHEAT -> new WheatMinion(ownerUuid, tier, instance, position);
            case ACACIA -> new AcaciaMinion(ownerUuid, tier, instance, position);
            case BIRCH -> new BirchMinion(ownerUuid, tier, instance, position);
            case DARK_OAK -> new DarkOakMinion(ownerUuid, tier, instance, position);
            case JUNGLE -> new JungleMinion(ownerUuid, tier, instance, position);
            case OAK -> new OakMinion(ownerUuid, tier, instance, position);
            case SPRUCE -> new SpruceMinion(ownerUuid, tier, instance, position);
            case CHICKEN -> new ChickenMinion(ownerUuid, tier, instance, position);
            case COW -> new CowMinion(ownerUuid, tier, instance, position);
            case CREEPER -> new CreeperMinion(ownerUuid, tier, instance, position);
            case ENDERMAN -> new EndermanMinion(ownerUuid, tier, instance, position);
            case RABBIT -> new RabbitMinion(ownerUuid, tier, instance, position);
            case SHEEP -> new SheepMinion(ownerUuid, tier, instance, position);
            case SKELETON -> new SkeletonMinion(ownerUuid, tier, instance, position);
            case SLIME -> new SlimeMinion(ownerUuid, tier, instance, position);
            case SPIDER -> new SpiderMinion(ownerUuid, tier, instance, position);
            case ZOMBIE -> new ZombieMinion(ownerUuid, tier, instance, position);
            case CAVE_SPIDER -> new CaveSpiderMinion(ownerUuid, tier, instance, position);
            case REVENANT -> new RevenantMinion(ownerUuid, tier, instance, position);
            case TARANTULA -> new TarantulaMinion(ownerUuid, tier, instance, position);
            case FISHING -> new FishingRodMinion(ownerUuid, tier, instance, position);
        };
    }
}
