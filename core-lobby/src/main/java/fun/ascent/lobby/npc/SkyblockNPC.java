package fun.ascent.lobby.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import fun.ascent.lobby.transfer.ProxyTransfer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record SkyblockNPC(Instance instance) implements NpcDefinition {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    @Override
    public String id() {
        return "lobby_skyblock";
    }

    @Override
    public String name() {
        return "<gold>SkyBlock</gold>";
    }

    @Override
    public NpcSkin skin() {
        return new NpcSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTYyMzA0MDQ3NTQ5NCwKICAicHJvZmlsZUlkIiA6ICIzYzdiZDE4ODc2OTU0OWVlODk0ZmIyMjJiZDc5YWVjYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJZZ2dkcmFzaWw4MCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81MmUxNWMyZDdjZmRkMzAwOGRkNzkwYjJmODkxNzVjNzNkNWU3NzYyN2FmNjUzZmMxOGU5ZmNjOGI5NmFkZjg0IgogICAgfQogIH0KfQ==",
                "LVMIfsjQBFjAaMwLvQBIjW3j/V1mbCzEbYN5j4nVOATTDXIOdHDqtOXHRiBE9xcFZLqHsM/LF4j8VZ4/HQPgBivK/7v72U/nvnUMwZq3tChVvcGpwrv6V4NgAf0JYoNe7F3aqSEwx25xbWpsGx39qZ6LO6xGRtVEjXhoXGwL7kpaOn+XF3L+E5/d9aMh0CAfvyuM94hhoB9HCG2J9hzsRPm3V7C66IQ2n+AlPSwBYAzOAj3wQRdz6ipWOvyRpk9siQ/1T5tD+RC8Flwy5o2/0zP9AWLORmbpqe3zSf2r06iMx1LLy6odiMe6LeiC/FTNiu96hatQDzv8OQ2VQAg5mEkv1HyXgGvVbYQcGB5SNiepXkxHdtfyshBewEFR0f0XC8nMlN0sgaJl0Iycr+4+oCFnvGTufKNnnzFxQhU9CknSQnWFyxdFhefUtHUPxtCLnxnexwDIqItL40bafbMEpDQhJSOWSu97vcTbc0yDdTUbLZAVTzO+/wmf4M594i6e1ykFn/WtLiOi58/qdCN2Fwja5QX+e0rKflNl7Yci5RGWgGsKzK471FmzyIGesXfwwQTiy1k/rGCID2rstGa57s1HXFOBuh0g7nwCl9gDo/XokHzYXF5khmKIvxrusC6Bstsg2jy1drW7BgKCyN400BYXb+JxS0Dt0V3WNTslMUQ="
        );
    }

    @Override
    public Pos position() {
        return new Pos(-8.5, 74, 0.5, -90, 0);
    }

    @Override
    public void onInteract(Player player, AscentNpc npc) {
        player.sendMessage(MINI_MESSAGE.deserialize("<yellow>Sending you to <gold>SkyBlock</gold><yellow>...</yellow>"));
        ProxyTransfer.send(player, "skyblock");
    }
}
