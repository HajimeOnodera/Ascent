package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record TiaTheFairyNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_tia_the_fairy"; }
    @Override public String name() { return "<light_purple>Tia the Fairy"; }
    @Override public NpcSkin skin() { return new NpcSkin("ewogICJ0aW1lc3RhbXAiIDogMTY4MzgyNjIyOTU3MywKICAicHJvZmlsZUlkIiA6ICI0YjBhOTI4NDY2NDY0NmRlYjY5NDJiZTIzZTlhMWQyZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJJc2FhY0FsdCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83M2YwNzI5NDBhOTQyYjk1M2EzZjlkMTM5MWZmMzQyYjdkMTkwNzZmN2ExYTNlOTQ0MDQwZDY0MjBjN2Q0OTgwIgogICAgfQogIH0KfQ==", "Jrf7HqW8Dj956V2RFd/8BPaiYICriEJBNJvz4bISRLMWPov15IhtVQlBj8aOF4ka5Q1oeI8Vj+Bm0BU+4JCNJ72Lr/pQR3ozU7uKxn6o+0N4+kyrYHbL9EfjYzyjHNmSJbAZPpNWl91EJvUb7u6LSrgdhfSHpED5uaXavi30TN7HHPdwo3NIx5A8ZdJyoCDB7k1bnO7z0IsrAv0+tLNgD/No47KVfw6kfWHwklz8RkkD7cbdjKojX3nqCvyYbrWqZbMbTNI0BqReQLB1mYBjDTQKBiGy09yJECJsn6jlszEcVY/c2VOmoCgi95aKNlLwUH864NDISnjyLgX/Z+VKgXPdzhf7IseplUr9B+2njvQucjm4rT5IT6GKHlfjTkLEnfaVrwvGI4N1NEyzG9ULSJaRdXnt5qO5UyiNBF9tXAeJ6+yPpmurbr/ZhcPPVErFogda8UKpZ0X9Q+3l+Ij6S+Z9mSNJ1kwROIQsmWDKNy3AhTytibtDzhY3WhsKO9SHseaZaaH2EnVYdwgSGOMG1DcE3u79gMfEzHYT9JZFDZPALrosXmCUC5MpbJz7L44+OSgQx4YHfZl/8iGdxOolXf0n8XmaVEPQ/NVpTm3n2QeYiK4OBOSdfa6BPtO/aZiFbOG5kqvJ+JEg222UZgEDtVdYKdFdc06d6KZ04WC0LRA="); }
    @Override public Pos position() { return new Pos(119.5, 65, 147.5, 145, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Welcome to the Fairy Pond! I am Tia.", "You may have noticed some strange orbs laying around the island.", "They are the souls of my fallen sisters.", "If you find any more during your travels, please bring them back to me!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Have you found any fairy souls?", "Bring them to me and I'll reward you handsomely!"); }
}
