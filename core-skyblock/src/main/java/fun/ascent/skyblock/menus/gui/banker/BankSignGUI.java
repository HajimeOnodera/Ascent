package fun.ascent.skyblock.menus.gui.banker;

import net.kyori.adventure.nbt.*;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.client.play.ClientUpdateSignPacket;
import net.minestom.server.network.packet.server.play.BlockChangePacket;
import net.minestom.server.network.packet.server.play.BlockEntityDataPacket;
import net.minestom.server.network.packet.server.play.OpenSignEditorPacket;
import net.minestom.server.timer.TaskSchedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BankSignGUI {

    public record SignGUISession(CompletableFuture<String> future, Pos pos, Block originalBlock) {}

    public static final Map<UUID, SignGUISession> activeSessions = new HashMap<>();
    private static boolean listenerRegistered = false;

    public static void registerPacketListener() {
        if (listenerRegistered) return;
        listenerRegistered = true;

        MinecraftServer.getGlobalEventHandler().addListener(PlayerPacketEvent.class, event -> {
            if (event.getPacket() instanceof ClientUpdateSignPacket signPacket) {
                Player player = event.getPlayer();
                SignGUISession session = activeSessions.remove(player.getUuid());
                if (session != null) {
                    event.setCancelled(true);
                    String textInput = signPacket.lines().isEmpty() ? "" : signPacket.lines().getFirst();
                    session.future().complete(textInput);
                    player.sendPacket(new BlockChangePacket(session.pos(), session.originalBlock()));
                }
            }
        });

        MinecraftServer.getGlobalEventHandler().addListener(PlayerDisconnectEvent.class, event -> {
            Player player = event.getPlayer();
            SignGUISession session = activeSessions.remove(player.getUuid());
            if (session != null) {
                session.future().completeExceptionally(new IllegalStateException("Player disconnected while sign GUI was open"));
            }
        });
    }

    public static CompletableFuture<String> open(Player player, String[] placeholderLines) {
        registerPacketListener();
        Pos pos = player.getPosition().add(0, 6, 0);

        CompoundBinaryTag compound = CompoundBinaryTag.builder()
                .put("is_waxed", ByteBinaryTag.byteBinaryTag((byte) 0))
                .put("back_text", CompoundBinaryTag.builder()
                        .put("has_glowing_text", ByteBinaryTag.byteBinaryTag((byte) 0))
                        .put("color", StringBinaryTag.stringBinaryTag("black"))
                        .put("messages", ListBinaryTag.from(List.of(
                                StringBinaryTag.stringBinaryTag(""),
                                StringBinaryTag.stringBinaryTag(""),
                                StringBinaryTag.stringBinaryTag(""),
                                StringBinaryTag.stringBinaryTag("")
                        )))
                        .build())
                .put("front_text", CompoundBinaryTag.builder()
                        .put("has_glowing_text", ByteBinaryTag.byteBinaryTag((byte) 0))
                        .put("color", StringBinaryTag.stringBinaryTag("black"))
                        .put("messages", ListBinaryTag.from(List.of(
                                StringBinaryTag.stringBinaryTag("^^^^^^^^"),
                                StringBinaryTag.stringBinaryTag(placeholderLines.length > 0 ? placeholderLines[0] : ""),
                                StringBinaryTag.stringBinaryTag(placeholderLines.length > 1 ? placeholderLines[1] : ""),
                                StringBinaryTag.stringBinaryTag(placeholderLines.length > 2 ? placeholderLines[2] : "")
                        )))
                        .build())
                .build();

        player.sendPackets(
                new BlockChangePacket(pos, Block.OAK_SIGN),
                new BlockEntityDataPacket(pos, Block.OAK_SIGN.registry().blockEntityType(), compound)
        );

        MinecraftServer.getSchedulerManager().scheduleTask(() -> player.sendPacket(new OpenSignEditorPacket(pos, true)), TaskSchedule.tick(2), TaskSchedule.stop());

        CompletableFuture<String> future = new CompletableFuture<>();
        activeSessions.put(player.getUuid(), new SignGUISession(future, pos, player.getInstance().getBlock(pos)));
        return future;
    }
}
