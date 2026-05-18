package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record GeorgeNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_george"; }
    @Override public String name() { return "<white>George"; }
    @Override public NpcSkin skin() { return new NpcSkin("eyJ0aW1lc3RhbXAiOjE1ODU0NTcyMDE0MzQsInByb2ZpbGVJZCI6IjllOGI4NzQ3YTMxNzQ0ZTY4YTQ4NzEzMzQwM2I0ZDM1IiwicHJvZmlsZU5hbWUiOiJFdmlsRGN0ciIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTliYjEzNzM3OTc5Zjk4YmEyMzZlMzdhZTQ4ZmQ1ZGRkZWRiYTcxOWVjNjIwNjhlOGFjOTYyYWJmN2UwN2Q4MyJ9fX0=", "YT66ecq+c/RYMoGYZ4onxklW28MWnDugvK1xqmgIoNKlD31ZZmUlp7lfLkmmot4+SRRILA+UoeTzMLX164we7lJ40Hl4oTH8D8+ehgVh7sJx2joSUZS5ONACM+nzw4Yu8gEtgxcjhbgIWxIjyHqsgjMhOlM2YIOFU+p3LkVdkeongVLEzjKnCfn48f8qVyfuZ3ckL5kXYzuix07q2Z7l7GGoYWCkwSFfzDkiSLuM3D0ulQNz651vhLyi2FGvYbsQ2i0NB9qoQJ1N6RM0vghjLTuq2RtJsUQXiVayoAxyhZvp1Pa9VoA0sqp7FL0QOoAH2s5IPF4F9NN4cYrcKWxx9KdyHl8rZnhtSbvbNVg+LekErn0g6tb9ukPC+DYfxN7RUi29un9Mv//KZpmWHaJXdeFsWMYC7MhNoqS8iNLFBTbGEOm95OVROnxchlkv7a3fGPk/bIXqnVHmAY6wujIgbIBZGKLnTGMSqYJFmDailamN7NyWVWIzy3fU14Kv+BmkIosu+VPyZENGP4pHdTGgAbQvsoaYJ62IDGnMb1KcRqJbgah3nh48Ddp+a4fENdsikzLxL8gLXbxND7XgllXNxrqWjdP2WANVTQe3O+xEg58fHPJHvtJE1XYsYjKoGHm8JbbGmJLm/tesbyk6YEsartrkM2XS07kQHTj8ICNmpY0="); }
    @Override public Pos position() { return new Pos(21.5, 74, -59.5, 135, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Hello there! I'm George, the pet collector.", "I can help you manage your pets. Click me again to browse!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Looking to browse my pet collection? Click me again!"); }
}
