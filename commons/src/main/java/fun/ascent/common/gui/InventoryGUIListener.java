package fun.ascent.common.gui;

import fun.ascent.common.item.GUIClickableItem;
import fun.ascent.common.user.Rank;
import fun.ascent.common.user.User;
import fun.ascent.common.user.UserManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.scoreboard.Team;
import net.minestom.server.scoreboard.TeamManager;
import net.minestom.server.timer.TaskSchedule;

public class InventoryGUIListener {

    public static void register(GlobalEventHandler handler) {
        handler.addListener(PlayerSpawnEvent.class, event -> {
            try {
                Player player = event.getPlayer();
                User user = UserManager.getUser(player.getUuid());
                player.setDisplayName(user.getDisplayName());
                MinecraftServer.getSchedulerManager().buildTask(() -> {
                    try {
                        if (!player.isOnline()) return;
                        Rank rank = user.getRank();
                        TeamManager teamManager = MinecraftServer.getTeamManager();
                        String teamName = "rank_" + rank.name().toLowerCase();
                        for (Rank r : Rank.values()) {
                            String otherTeamName = "rank_" + r.name().toLowerCase();
                            Team otherTeam = teamManager.getTeam(otherTeamName);
                            if (otherTeam != null && otherTeam.getMembers().contains(player.getUsername())) {
                                otherTeam.removeMember(player.getUsername());
                            }
                        }
                        Team team = teamManager.getTeam(teamName);
                        if (team == null) {
                            team = teamManager.createTeam(teamName);
                            team.setPrefix(rank.getFormattedPrefix());
                            team.setTeamColor(rank.getTextColor());
                        }
                        team.addMember(player.getUsername());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }).delay(TaskSchedule.tick(10)).schedule();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        handler.addListener(InventoryPreClickEvent.class, event -> {
            Player player = event.getPlayer();
            InventoryGUI gui = InventoryGUI.GUI_MAP.get(player.getUuid());
            
            if (gui != null && event.getInventory() == gui.getInventory()) {
                event.setCancelled(true);
                int slot = event.getSlot();
                GUIItem item = gui.get(slot);
                
                if (item instanceof GUIClickableItem clickable) {
                    clickable.run(event, player);
                }
            }
        });

        handler.addListener(InventoryClickEvent.class, event -> {
            Player player = event.getPlayer();
            InventoryGUI gui = InventoryGUI.GUI_MAP.get(player.getUuid());

            if (gui != null && event.getInventory() == gui.getInventory()) {
                int slot = event.getSlot();
                GUIItem item = gui.get(slot);

                if (item instanceof GUIClickableItem clickable) {
                    clickable.runPost(event, player);
                }
            }
        });

        handler.addListener(InventoryCloseEvent.class, event -> {
            Player player = event.getPlayer();
            InventoryGUI gui = InventoryGUI.GUI_MAP.get(player.getUuid());

            if (gui != null && event.getInventory() == gui.getInventory()) {
                gui.onClose(event, InventoryGUI.CloseReason.PLAYER_EXITED);
                InventoryGUI.GUI_MAP.remove(player.getUuid());
            }
        });
    }
}
