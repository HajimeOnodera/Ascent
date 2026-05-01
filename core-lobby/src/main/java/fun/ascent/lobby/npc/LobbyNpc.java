package fun.ascent.lobby.npc;

import fun.ascent.lobby.transfer.ProxyTransfer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.instance.Instance;

import java.util.ArrayList;
import java.util.List;

final class LobbyNpc {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final double HOLOGRAM_DELTA = 0.3;

    private final String id;
    private final String displayName;
    private final String targetServer;
    private final Pos position;
    private final Instance instance;
    private final List<Entity> holograms = new ArrayList<>();
    private Entity entity;

    LobbyNpc(String id, String displayName, String targetServer, Pos position, Instance instance) {
        this.id = id;
        this.displayName = displayName;
        this.targetServer = targetServer;
        this.position = position;
        this.instance = instance;
    }

    void spawn() {
        entity = new Entity(EntityType.VILLAGER);
        entity.setNoGravity(true);

        instance.loadChunk(position).thenRun(() -> {
            entity.setInstance(instance, position);
            spawnHolograms();
        });
    }

    void interact(Player player) {
        player.sendMessage(MINI_MESSAGE.deserialize("<yellow>Sending you to <gold>" + displayName + "</gold><yellow>...</yellow>"));
        ProxyTransfer.send(player, targetServer);
    }

    boolean is(Entity target) {
        return target != null && entity != null && entity.getUuid().equals(target.getUuid());
    }

    private void spawnHolograms() {
        double yOffset = 2.2;
        for (String line : List.of("<gold>" + displayName + "</gold>", "<yellow><bold>CLICK</bold>")) {
            Entity armorStand = new Entity(EntityType.ARMOR_STAND);
            ArmorStandMeta meta = (ArmorStandMeta) armorStand.getEntityMeta();
            meta.setInvisible(true);
            meta.setHasNoBasePlate(true);
            meta.setMarker(true);

            armorStand.setCustomName(MINI_MESSAGE.deserialize(line));
            armorStand.setCustomNameVisible(true);
            armorStand.setNoGravity(true);
            armorStand.setInstance(instance, position.add(0, yOffset, 0));

            holograms.add(armorStand);
            yOffset -= HOLOGRAM_DELTA;
        }
    }

    String id() {
        return id;
    }
}
