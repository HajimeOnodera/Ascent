package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import fun.ascent.skyblock.menus.gui.banker.GUIBanker;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.quest.ActiveQuest;
import fun.ascent.skyblock.quest.QuestData;
import fun.ascent.skyblock.quest.impl.QuestTalkToBanker;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.timer.TaskSchedule;

import java.util.Map;

public record BankerNPC(Instance instance) implements NpcDefinition {

    @Override
    public String id() {
        return "hub_banker";
    }

    @Override
    public String name() {
        return "<gold>Banker</gold>";
    }

    @Override
    public NpcSkin skin() {
        return new NpcSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTYxMjE1ODIzNTU2OSwKICAicHJvZmlsZUlkIiA6ICIwNDkzZDQxYTE2NzQ0OTQwYTYzOGEzNmZjZWQxZDIxMCIsCiAgInByb2ZpbGVOYW1lIiA6ICIwMDBkYW50ZTAwMCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xYjMwZDA4OGEyNmQyMDc5OGExZWY5MmM2NWM3MWNlNDVhNjMzZjAwYWEyYWQ2ZDljOGZlMjRhMTM1ZWE5NThlIgogICAgfQogIH0KfQ==",
                "LuKR2hNX5PCQWlk6ct9ZLNrWr9ZZ8Rq1+NKXseqC53NmsRVNh5AqiRioMrORUQaVbZX1kz8LCuNQIkYuvgSZFoiB+cOqhXIQf5BMQVPGO8G9jxcSVo9QYubE4x4KXhRyD6wOoLQI8HdUpzIpDFMI0NIW1g3/UKf1r6v3YbAOuYE41LYyxGJWlrakUawd47Y8ecZiUkfyAqlN+XTdv5479fTOwFy/p3f/qR/3d5rltAAbJGXSx+jSUhJUAmCLUwSCzMc4phrbDGU3IigkqsiQcQur8r/OTYHEwOPU2tnflVyirPkzG90mHg4MvNAOchAeABoR8A3qVS4oZfRN7t0Ny1aYnQy6lri9gMJ05qL1+DCcmWWTzQHFDVW6wVaXwIEzaeP08GiMHnc8DcY5i2t5WDDQ2qB/BcL0ULq4YurZszJ9vZgzDQWYla3bjekrWDbD6fJfjWXW52veziOAUDCpcz+hYFO6rQv8r//QwAb+2BQskrY2Tf2YGG7dXhbt1wqPunXv3vlN/Fr1NQcJ2ZmGwwBVdFWfceYFfiONRBRGHeaPD8mBiB2eJivHSfFCkjpg96B0B4z2O8D/a5sExlyPiNs09ZF2dYQ+AUCL7AA50LJ0w6KYD8Nq/gThOk/cjzYpVr/BRwXUdQeLnWqirxysYq+Q+dYZVKjquDs/d4NDmAM="
        );
    }

    @Override
    public Pos position() {
        return new Pos(-29.5, 72, -38.5,-90,0);
    }

    @Override
    public void onInteract(Player player, AscentNpc npc) {
        if (player instanceof SkyblockPlayer sp && sp.getActiveProfileData() != null) {
            QuestData questData = sp.getActiveProfileData().getQuestData();
            Map.Entry<ActiveQuest, Boolean> entry = questData.getQuest(QuestTalkToBanker.class);
            if (entry != null && !entry.getValue()) {
                sp.sendMessage("§e[NPC] Banker§f: Hello there!");

                MinecraftServer.getSchedulerManager().buildTask(() -> {
                    sp.sendMessage("§e[NPC] Banker§f: You may want to store your §6Coins §fin a safe place while you are off adventuring.");
                }).delay(TaskSchedule.tick(30)).schedule();

                MinecraftServer.getSchedulerManager().buildTask(() -> {
                    sp.sendMessage("§e[NPC] Banker§f: Storing them in your §6Bank §fkeeps them safe and allows you to earn interest at the start of every season!");
                    questData.endQuest(QuestTalkToBanker.class);
                    new GUIBanker().open(sp);
                }).delay(TaskSchedule.tick(60)).schedule();

                return;
            }
        }
        new GUIBanker().open(player);
    }
}
