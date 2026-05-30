package fun.ascent.lobby.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record AscentStoreNPC(Instance instance) implements NpcDefinition {

    @Override
    public String id() {
        return "lobby_ascent_store";
    }

    @Override
    public String name() {
        return "<aqua>Ascent Store</aqua>";
    }

    @Override
    public NpcSkin skin() {
        return new NpcSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTYxNzY2NzgyNDk5NiwKICAicHJvZmlsZUlkIiA6ICIyYzEwNjRmY2Q5MTc0MjgyODRlM2JmN2ZhYTdlM2UxYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJOYWVtZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80ZmY0ZTA3Zjc5Njg1MjdmMjMyNDMzYTUwYmEwOTNmOTZmZjBkYTVjMmEyMGM3OTNhMjhjMGFiN2Y2YWM0MDA1IgogICAgfQogIH0KfQ==",
                "aHV1ZU8n18dIVYtu921DzS70m+E724f2NddIyqSjLRx+e5CYPQS4SqJnK5/fvyQtsuhErCuvIgUAcqoOzrboakb6iNH0I/2Sr0BAvrh/pydwEVuMWfdPkus6QtHgbk2svTmN0dT4BFRaG/oMelsBrl7v0NxzbUEfRki3I78i0sZGYG7uufstXsFiIM0v3YML4WyY0U5f0Pm7T2b6NNTLDj+V/Qa35UMzFKFTZzQ/9HLbQhELkpz5rtHjvY7H/ldNPFe0PTXFfrhAqIAjpR9XyZZ0l08h5cX+PFtGNEaEp3Mo2OQNdyo2qxTr5TUc0G4rim+H5VdxbOgUsU5OUzllA+JfwZ6Lsrg13TrLURpFntyHB3xNJFBmUT7EED5TA0XibGG3Gt3IT+CFrQwvXyka83y143qqfB+K4vpa97FJNkS8Zef9gI8hqzn1BCiLAn05cDBOSu91EkiBas3pMlZOgnlsNplWmBSxZedBa7Rf2advzSJRpGP1q0/ThVR0Jv0jmpMESWtPJbF2o4rxd9YPfE4CsqwkMkHT2t6eK3WqXFPxMluPXA2gOc/aATBdQ2uOq7//0Zz4njhshrp/LgLEL7ZvXvuJ9thuYFRXRBYgYVR2UCVFU4pD8DzwzCWXr+HHW7LiDc6it9J9bnF1qYlFYqL1El11QXefaVbG+GDRt4s="
        );
    }

    @Override
    public Pos position() {
        return new Pos(-44.5, 92, 25.5, 180, 0);
    }

    @Override
    public String[] holograms() {
        return new String[] {
                "<aqua>Ascent Store</aqua>",
                "<yellow><bold>RIGHT CLICK</bold>"
        };
    }

    @Override
    public void onInteract(Player player, AscentNpc npc) {

    }
}
