package fun.ascent.common.npc;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.entity.metadata.avatar.MannequinMeta;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.network.player.ResolvableProfile;
import net.minestom.server.timer.TaskSchedule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AscentNpc {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final Set<UUID> ALREADY_TALKING = new HashSet<>();
    private static final double HOLOGRAM_DELTA = 0.3;

    private final List<Entity> holograms = new ArrayList<>();
    private final UUID uuid = UUID.randomUUID();
    private final NpcDefinition definition;
    private Entity entity;
    private Pos position;

    public AscentNpc(NpcDefinition definition) {
        this.definition = definition;
        this.position = definition.position();
    }

    public void spawn() {
        if (definition.type() == NpcType.VILLAGER) {
            entity = new Entity(EntityType.VILLAGER);
        } else {
            entity = new EntityCreature(EntityType.MANNEQUIN);
            entity.editEntityMeta(MannequinMeta.class, meta -> meta.setProfile(profile()));
        }

        entity.setNoGravity(true);
        definition.instance().loadChunk(position).thenRun(() -> {
            entity.setInstance(definition.instance(), position);
            spawnHolograms();
        });
    }

    public void interact(Player player) {
        definition.onInteract(player, this);
    }

    public void speak(Player player, String... messagesToSpeak) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (player == null || messagesToSpeak == null || messagesToSpeak.length == 0) {
            future.complete(null);
            return;
        }

        if (ALREADY_TALKING.contains(player.getUuid())) {
            future.complete(null);
            return;
        }

        ALREADY_TALKING.add(player.getUuid());

        int delayMultiplier = 0;
        for (String message : messagesToSpeak) {
            if (message == null || message.isBlank()) {
                continue;
            }

            int delayTicks = Math.max(1, delayMultiplier * 20);
            MinecraftServer.getSchedulerManager().buildTask(() -> {
                sendMessage(player, message);
            }).delay(TaskSchedule.tick(delayTicks)).schedule();

            delayMultiplier++;
        }

        int totalDelay = Math.max(1, delayMultiplier * 20);
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            ALREADY_TALKING.remove(player.getUuid());
            future.complete(null);
        }).delay(TaskSchedule.tick(totalDelay)).schedule();

    }

    public void updateLocation(Pos newPosition) {
        if (newPosition == null) {
            return;
        }

        position = newPosition;
        if (entity != null) {
            entity.teleport(newPosition);
        }

        double yOffset = definition.type() == NpcType.PLAYER ? 2.0 : 2.2;
        for (Entity armorStand : holograms) {
            armorStand.teleport(newPosition.add(0, yOffset, 0));
            yOffset -= HOLOGRAM_DELTA;
        }
    }

    public void remove() {
        if (entity != null) {
            entity.remove();
        }
        removeHolograms();
    }

    public void removeHolograms() {
        for (Entity armorStand : holograms) {
            armorStand.remove();
        }
        holograms.clear();
    }

    public boolean matches(Entity target) {
        return target != null && entity != null && entity.getUuid().equals(target.getUuid());
    }

    public String id() {
        return definition.id();
    }

    public String name() {
        return definition.name();
    }

    public NpcType type() {
        return definition.type();
    }

    public NpcSkin skin() {
        return definition.skin();
    }

    public NpcDefinition definition() {
        return definition;
    }

    public Entity entity() {
        return entity;
    }

    public Pos position() {
        return position;
    }

    public UUID uuid() {
        return uuid;
    }

    private ResolvableProfile profile() {
        NpcSkin skin = definition.skin();
        if (skin != null && skin.texture() != null && skin.signature() != null) {
            return new ResolvableProfile(new PlayerSkin(skin.texture(), skin.signature()));
        }

        String fakeName = uuid.toString().substring(0, 16);
        return new ResolvableProfile(new ResolvableProfile.Partial(fakeName, uuid, List.of()));
    }

    private void spawnHolograms() {
        String[] lines = definition.holograms();
        if (lines == null || lines.length == 0) {
            return;
        }

        double yOffset = definition.type() == NpcType.PLAYER ? 2.0 : 2.2;
        for (String text : lines) {
            if (text == null || text.isBlank()) {
                continue;
            }

            Entity armorStand = new Entity(EntityType.ARMOR_STAND);
            ArmorStandMeta meta = (ArmorStandMeta) armorStand.getEntityMeta();
            meta.setInvisible(true);
            meta.setHasNoBasePlate(true);
            meta.setMarker(true);

            armorStand.setCustomName(MINI_MESSAGE.deserialize(text));
            armorStand.setCustomNameVisible(true);
            armorStand.setNoGravity(true);
            armorStand.setInstance(definition.instance(), position.add(0, yOffset, 0));

            holograms.add(armorStand);
            yOffset -= HOLOGRAM_DELTA;
        }
    }

    private void sendMessage(Player player, String message) {
        String npcName = definition.name() != null ? definition.name() : definition.id();
        player.sendMessage(MINI_MESSAGE.deserialize("<yellow>[NPC] " + npcName + "<white>: " + message));
    }
}
