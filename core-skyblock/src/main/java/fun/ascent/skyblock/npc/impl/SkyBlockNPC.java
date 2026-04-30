package fun.ascent.skyblock.npc.impl;

import fun.ascent.skyblock.npc.impl.enums.NPCType;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.entity.metadata.avatar.MannequinMeta;
import net.minestom.server.network.player.ResolvableProfile;
import net.minestom.server.timer.TaskSchedule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
public class SkyBlockNPC {
    private static final Set<UUID> ALREADY_TALKING = new HashSet<>();
    private static final double HOLOGRAM_DELTA = 0.3;

    private final List<Entity> holograms = new ArrayList<>();

    private final String[] messages;
    private final UUID uuid;
    private final String id;
    private final NPCType type;
    private final NPCSkin skin;
    private final NPCParameters parameters;

    private Entity entity;

    @Setter
    private Pos position;

    public SkyBlockNPC(NPCParameters npcParameters) {
        this.uuid = UUID.randomUUID();
        this.id = npcParameters.id();
        this.type = npcParameters.type();
        this.messages = npcParameters.messages();
        this.position = npcParameters.position();
        this.skin = npcParameters.skin();
        this.parameters = npcParameters;

        SkyblockNPCManager.registerNPC(this);
    }

    public void spawn() {
        if (type == NPCType.VILLAGER) {
            this.entity = new Entity(EntityType.VILLAGER);
            this.entity.setNoGravity(true);
            parameters.instance().loadChunk(position).thenRun(() -> {
                this.entity.setInstance(parameters.instance(), position);
                spawnHolograms();
            });
        } else {
            this.entity = new EntityCreature(EntityType.MANNEQUIN);
            this.entity.setNoGravity(true);
            this.entity.editEntityMeta(MannequinMeta.class, meta -> {
                ResolvableProfile profile;
                if (skin != null && skin.getTexture() != null && skin.getSignature() != null) {
                    PlayerSkin minestomSkin = new PlayerSkin(skin.getTexture(), skin.getSignature());
                    profile = new ResolvableProfile(minestomSkin);
                } else {
                    String fakeName = uuid.toString().substring(0, 16);
                    profile = new ResolvableProfile(new ResolvableProfile.Partial(fakeName, uuid, List.of()));
                }
                meta.setProfile(profile);
            });
            parameters.instance().loadChunk(position).thenRun(() -> {
                this.entity.setInstance(parameters.instance(), position);
                spawnHolograms();
            });
        }
    }

    public void spawnHolograms() {
        if (parameters.holograms() == null || parameters.holograms().length == 0)
            return;

        double yOffset = type == NPCType.PLAYER ? 2.0 : 2.2;

        for (String text : parameters.holograms()) {
            if (text == null || text.isBlank())
                continue;

            Entity armorStand = new Entity(EntityType.ARMOR_STAND);
            ArmorStandMeta meta = (ArmorStandMeta) armorStand.getEntityMeta();
            meta.setInvisible(true);
            meta.setHasNoBasePlate(true);
            meta.setMarker(true);

            armorStand.setCustomName(MiniMessage.miniMessage().deserialize(text));
            armorStand.setCustomNameVisible(true);
            armorStand.setNoGravity(true);

            Pos holoPos = position.add(0, yOffset, 0);
            armorStand.setInstance(parameters.instance(), holoPos);

            holograms.add(armorStand);
            yOffset -= HOLOGRAM_DELTA;
        }
    }

    public void removeHolograms() {
        for (Entity armorStand : holograms) {
            if (armorStand != null) {
                armorStand.remove();
            }
        }
        holograms.clear();
    }

    // update holograms

    public void updateLocation(Pos newPosition) {
        if (newPosition == null)
            return;
        this.position = newPosition;

        if (entity != null) {
            entity.teleport(newPosition);
        }

        double yOffset = type == NPCType.PLAYER ? 2.0 : 2.2;
        for (Entity armorStand : holograms) {
            if (armorStand != null) {
                armorStand.teleport(newPosition.add(0, yOffset, 0));
            }
            yOffset -= HOLOGRAM_DELTA;
        }
    }

    // messagws

    public CompletableFuture<Void> speak(Player player, String... messagesToSpeak) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (player == null || messagesToSpeak == null || messagesToSpeak.length == 0) {
            future.complete(null);
            return future;
        }

        if (ALREADY_TALKING.contains(player.getUuid())) {
            future.complete(null);
            return future;
        }

        ALREADY_TALKING.add(player.getUuid());

        int delayMultiplier = 0;
        for (String message : messagesToSpeak) {
            if (message == null || message.isBlank())
                continue;

            final int delayTicks = Math.max(1, delayMultiplier * 20);
            MinecraftServer.getSchedulerManager().buildTask(() -> {
                sendMessage(player, message);
                player.playSound(Sound.sound(Key.key("entity.villager.yes"), Sound.Source.NEUTRAL, 1f, 1f));
            }).delay(TaskSchedule.tick(delayTicks)).schedule();

            delayMultiplier++;
        }

        final int totalDelay = Math.max(1, delayMultiplier * 20);
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            ALREADY_TALKING.remove(player.getUuid());
            future.complete(null);
        }).delay(TaskSchedule.tick(totalDelay)).schedule();

        return future;
    }

    private void sendMessage(Player player, String message) {
        if (player == null || message == null || parameters == null)
            return;
        String npcName = parameters.name() != null ? parameters.name() : id;
        player.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>[NPC] " + npcName + "<white>: " + message));
    }
}
