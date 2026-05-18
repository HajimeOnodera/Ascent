package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record BeaNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_bea"; }
    @Override public String name() { return "<yellow>Bea"; }
    @Override public NpcSkin skin() {
        return new NpcSkin(
                "eyJ0aW1lc3RhbXAiOjE1ODIzMDAzNTg3ODYsInByb2ZpbGVJZCI6IjQxZDNhYmMyZDc0OTQwMGM5MDkwZDU0MzRkMDM4MzFiIiwicHJvZmlsZU5hbWUiOiJNZWdha2xvb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2UzYzM0YjllNTFmMzUyZDZjNjljODIxYTFmYzI5ZjUwMDUwOTgwM2ExZWVjMDI0YzM4NzAxNWI1YzQzYzc3M2EiLCJtZXRhZGF0YSI6eyJtb2RlbCI6InNsaW0ifX19fQ==",
                "KKbZH4W3Qmx57D3vaF30MdHzxB2hryzgSNffNssfSo2lo1VBjLWM6bpyY6BYGbKM5Yi+6C0RA33IKAHGNeARhIr/tF52pUEzMc4Jp93CGxY7tUd9yQyW5EKw87aWzoriIJFVlcN8DuC4ZOujKFlPSVAPGXQE74PchoYBhi5VvBuO2GvGc0JZt4Z9aEVJIpG5yMv/ZhcxKKBVai8w4sZnj60lVblXcyznJ7lQvRgavyCesp8pcUtMMwqzblcdN3tJKN2VMMxqOI4+F/JxBKP5LmqF7/XgqGjf6avjQGxYsUvfMa2NjDz7lg/TqV91GOPBPDsdCHt6v+ybfdveohYrmAkdgKKeZOtFD8SZsAtiSjeWMdCTvw9ix9aCk/MTI1uNqhLM/cydXEVVOO3HrfweL//A+agXe2/tYDBxBB2VvyRIN5j+s7HZ36+UIdzl+WgzqrgUmOVf8AyGRUDYDt8kFZRQeLlm+49UJgs12DG5PhNEa+x9iwfODHG/RBcys/0NP8q7KyEAFH0bv+xmSpgcGp0HxIKeRujSkHvBQpJ+kI/HT1l3vlEce6fIE0XIF1UOwJV/0cuaBqDlso7PXq8LJyOg99TosoalUg4/IGh1fqQgkURfbmtzUFtWJqzwQIypVYLPlhsU8rxa0GBZrk7NrXT+JqrbGrXrRif7UPHjJiE="); }
    @Override public Pos position() { return new Pos(11.5, 72, -58.5, -76, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Hello! Do you have a pet?", "Pets are little companions for your adventures in SkyBlock!", "Personally, I prefer the bee pet!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Bzzz! Bees are the best pets, don't you think?"); }
}
