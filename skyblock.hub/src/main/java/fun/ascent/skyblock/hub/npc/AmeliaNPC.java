package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record AmeliaNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_amelia"; }
    @Override public String name() { return "<gold>Amelia"; }
    @Override public NpcSkin skin() {
        return new NpcSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTYwMTU1MTcxMzA0NSwKICAicHJvZmlsZUlkIiA6ICJkZTE0MGFmM2NmMjM0ZmM0OTJiZTE3M2Y2NjA3MzViYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTUlRlYW0iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmQ1OTU1Y2M1YWY2ZmEzMzkwZGYzNmM5YTIzYTMyNjRjODQyNjk5MWU3NTMzYWQyZDA4MWY0MWVhMTMwYzYzYiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
                "QMZr7iKG5E74dUHwnnPYC36dM5pbiOx2rpjfbH3nzA+3yTntqWY873eHU/JHFXFfYwSKOufprcLqH2KOGjAfAhn7ez24bq4KKRfDWH1HJ0KYmo07gAjgZrYKTUrFf4dEN3mPwL8A2w8QeHLu/l95gEWx/eLYS8al+vhiomFyXJjmSjKLzrrIiQVrMa2Kaf9Ev9x/0UWWmNji2Jvhaf0NPRUXshWrLONXGUcgTZv3B8T1IPRGDPdw04L1kSuL92N5SZa2l216bJRzl4op2PQmSsGch/UP5g6jzMWx/GensNuw8X0ZmiFCi/UAvV6Ry3hH9Ce0S0FU6FMjbfeuq/r+6zp0UC1br2Nf9inLDUt0Fc4CHzLjD3Q5EkC/crj4EbaPvBMQ+AaN6n80LdlbxRESc/XlQ2U+anYSB9PPGn3cqOPOlLxkhWVLQ6IRxCehR73LQdoblIaKRc/VfWU7xjxhybSsO3muLcl2U8AAbRsqtuFlf3s6764lLdHluDZl992nWZYJMBysddpU9tVs/PGFscdJw1KC69Q3/UmNZO2HtW7B13sJw8JIFPvk2mqgCQvMfnkFJ/lHWfgITphpk6mWqe1IokA5ZCnqhmkWwUAxQaP9q7at2LbPO1gb5R/5XKhmakQZ7lShw8/blENqRe6qYpm3qWgBEtjdfQnZd9ZCo3k="); }
    @Override public Pos position() { return new Pos(-15.5, 74, -68.5, -90, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Hey there! I'm Amelia.", "I help adventurers find their way around the hub.", "Come talk to me anytime you need directions!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Need directions? I'm always happy to help!"); }
}
