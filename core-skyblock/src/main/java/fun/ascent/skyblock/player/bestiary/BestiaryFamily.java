package fun.ascent.skyblock.player.bestiary;

import fun.ascent.skyblock.entity.mob.SkyblockMobEntity;
import fun.ascent.skyblock.entity.mob.mobs.graveyard.GraveyardZombie;
import fun.ascent.skyblock.player.fishing.loot.mobs.DeepRider;
import fun.ascent.skyblock.player.fishing.loot.mobs.PondSquid;
import fun.ascent.skyblock.player.fishing.loot.mobs.SeaGuardian;
import fun.ascent.skyblock.player.fishing.loot.mobs.SeaWitch;
import net.minestom.server.item.Material;

import java.util.Arrays;
import java.util.List;

public enum BestiaryFamily {
    PRIVATE_ISLAND_ZOMBIE(
            "private_island_zombie",
            BestiarySection.YOUR_ISLAND,
            "Zombie",
            "Brains.",
            1,
            5,
            Material.ZOMBIE_HEAD,
            null,
            List.of()
    ),
    GRAVEYARD_ZOMBIE(
            "graveyard_zombie",
            BestiarySection.HUB,
            "Graveyard Zombie",
            "Brains.",
            1,
            5,
            Material.ZOMBIE_HEAD,
            null,
            List.of(new BestiaryMobType("graveyard_zombie", GraveyardZombie.class, GraveyardZombie::new, Material.ZOMBIE_HEAD, null))
    ),
    OLD_WOLF(
            "old_wolf",
            BestiarySection.HUB,
            "Old Wolf",
            "Wolves older than the island itself.",
            4,
            15,
            Material.PLAYER_HEAD,
            "d359537c15534f61c1cd886bc118774ed22280e7cdab6613870160aad4ca39",
            List.of()
    ),
    WOLF(
            "wolf",
            BestiarySection.HUB,
            "Wolf",
            "Roaming the remains of a Castle far from its best days.",
            4,
            15,
            Material.PLAYER_HEAD,
            "f4cb7a6bf6c32c49f2589147e6f0f888e9e35875dd1ea2a8af379ca710589e6b",
            List.of()
    ),
    ZOMBIE_VILLAGER(
            "zombie_villager",
            BestiarySection.HUB,
            "Zombie Villager",
            "The real enemy isn't the dead - it's the living.",
            1,
            5,
            Material.PLAYER_HEAD,
            "69198f410a10f99314aa0fbe9a3db10697bbc1c011f019507d96673c64217f5a",
            List.of()
    ),
    EMERALD_SLIME(
            "emerald_slime",
            BestiarySection.DEEP_CAVERNS,
            "Emerald Slime",
            "It is said that these slimes absorb emeralds to grow larger.",
            5,
            15,
            Material.PLAYER_HEAD,
            "895aeec6b842ada8669f846d65bc49762597824ab944f22f45bf3bbb941abe6c",
            List.of()
    ),
    LAPIS_ZOMBIE(
            "lapis_zombie",
            BestiarySection.DEEP_CAVERNS,
            "Lapis Zombie",
            "These zombies adapted to their environment, using the lapis around them as a defense mechanism.",
            4,
            15,
            Material.PLAYER_HEAD,
            "e9f7979b25001087969d58c06e14d00b8dab57dab060b4c8b483c1b7f869940",
            List.of()
    ),
    MINER_SKELETON(
            "miner_skeleton",
            BestiarySection.DEEP_CAVERNS,
            "Miner Skeleton",
            "These skeletons have crafted gear from the diamonds around them - resulting in a look both fashionable and protective.",
            4,
            15,
            Material.PLAYER_HEAD,
            "8de8bbd7f6d77a1614865ef6a1d31f53f797550d14ee21d107a8415c14b48ca6",
            List.of()
    ),
    MINER_ZOMBIE(
            "miner_zombie",
            BestiarySection.DEEP_CAVERNS,
            "Miner Zombie",
            "Like their skeleton counterparts, these zombies have bedazzled themselves throughout the years.",
            4,
            15,
            Material.PLAYER_HEAD,
            "1b8a707e8a58d2ffe297474d18daee86951b21994566358dc0b5d7dcc9e2ed9b",
            List.of()
    ),
    REDSTONE_PIGMAN(
            "redstone_pigman",
            BestiarySection.DEEP_CAVERNS,
            "Redstone Pigman",
            "These pigmen will defend their redstone to the death.",
            4,
            15,
            Material.PLAYER_HEAD,
            "74e9c6e98582ffd8ff8feb3322cd1849c43fb16b158abb11ca7b42eda7743eb",
            List.of()
    ),
    SNEAKY_CREEPER(
            "sneaky_creeper",
            BestiarySection.DEEP_CAVERNS,
            "Sneaky Creeper",
            "They be creepin'.",
            4,
            15,
            Material.PLAYER_HEAD,
            "74e9c6e98582ffd8ff8feb3322cd1849c43fb16b158abb11ca7b42eda7743eb",
            List.of()
    ),
    POND_SQUID(
            "pond_squid",
            BestiarySection.FISHING,
            "Pond Squid",
            "Just a lowly squid.",
            2,
            15,
            Material.SQUID_SPAWN_EGG,
            null,
            List.of(new BestiaryMobType("pond_squid", PondSquid.class, PondSquid::new, Material.SQUID_SPAWN_EGG, null))
    ),
    SEA_GUARDIAN(
            "sea_guardian",
            BestiarySection.FISHING,
            "Sea Guardian",
            "Guards the sea.",
            3,
            15,
            Material.GUARDIAN_SPAWN_EGG,
            null,
            List.of(new BestiaryMobType("sea_guardian", SeaGuardian.class, SeaGuardian::new, Material.GUARDIAN_SPAWN_EGG, null))
    ),
    SEA_WITCH(
            "sea_witch",
            BestiarySection.FISHING,
            "Sea Witch",
            "She'll turn you into a newt!",
            3,
            15,
            Material.WITCH_SPAWN_EGG,
            null,
            List.of(new BestiaryMobType("sea_witch", SeaWitch.class, SeaWitch::new, Material.WITCH_SPAWN_EGG, null))
    ),
    DEEP_RIDER(
            "deep_rider",
            BestiarySection.FISHING,
            "Deep Rider",
            "The chicken is the one in charge.",
            3,
            15,
            Material.STRAY_SPAWN_EGG,
            null,
            List.of(new BestiaryMobType("deep_rider", DeepRider.class, DeepRider::new, Material.STRAY_SPAWN_EGG, null))
    );

    private final String id;
    private final BestiarySection section;
    private final String displayName;
    private final String description;
    private final int bracket;
    private final int maxTier;
    private final Material iconMaterial;
    private final String iconTexture;
    private final List<BestiaryMobType> mobs;

    BestiaryFamily(String id, BestiarySection section, String displayName, String description, int bracket, int maxTier, Material iconMaterial, String iconTexture, List<BestiaryMobType> mobs) {
        this.id = id;
        this.section = section;
        this.displayName = displayName;
        this.description = description;
        this.bracket = bracket;
        this.maxTier = maxTier;
        this.iconMaterial = iconMaterial;
        this.iconTexture = iconTexture;
        this.mobs = mobs;
    }

    public String id() {
        return id;
    }

    public BestiarySection section() {
        return section;
    }

    public String displayName() {
        return displayName;
    }

    public String description() {
        return description;
    }

    public int bracket() {
        return bracket;
    }

    public int maxTier() {
        return maxTier;
    }

    public List<BestiaryMobType> mobs() {
        return mobs;
    }

    public Material iconMaterial() {
        return iconMaterial;
    }

    public String iconTexture() {
        return iconTexture;
    }

    public BestiaryMobType primaryMob() {
        return mobs.isEmpty() ? null : mobs.getFirst();
    }

    public int totalKills(BestiaryProgress progress) {
        return progress.getKills(this);
    }

    public boolean unlocked(BestiaryProgress progress) {
        return totalKills(progress) > 0;
    }

    public static ResolvedMob resolve(SkyblockMobEntity mob) {
        return Arrays.stream(values())
                .flatMap(family -> family.mobs.stream().map(entry -> new ResolvedMob(family, entry)))
                .filter(match -> match.mob().matches(mob))
                .findFirst()
                .orElse(null);
    }

    public boolean hasMobBindings() {
        return !mobs.isEmpty();
    }

    public record ResolvedMob(BestiaryFamily family, BestiaryMobType mob) {
    }
}
