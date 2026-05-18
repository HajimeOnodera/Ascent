package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record OzanneNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_ozanne"; }
    @Override public String name() { return "<gold>Ozanne"; }
    @Override public NpcSkin skin() { return new NpcSkin("ewogICJ0aW1lc3RhbXAiIDogMTYxNzczNjQ4NDc0OCwKICAicHJvZmlsZUlkIiA6ICI0NWY3YTJlNjE3ODE0YjJjODAwODM5MmRmN2IzNWY0ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJfSnVzdERvSXQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmY5YjUxNTYwODNmNWE3Nzk5Nzc2M2JhZWU5YjRhM2VhY2U5MmFhNDIzOWM2YjUzODM3MTE0NWExMjAyOTg5NCIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9", "ydKwHzLhzyVnZDtJlsJyWCtueMAGT878Lb2KN9L0QkGo/uqUSOaZBurWERVg9SEIVhDNe4UULMkax6Lb8rpAv/X+0mtS+UH2XPyn/78vEJBK+cojJvDRUVUrJHAaK5G0TT2ea8sFXs1R1QNLrwSymQQcrnMJAs+Re9cwtwUSLK9kMZaTnsMiYXpJZ/caF6dZI7iZGU4jlyfGXp/Xbxbva8NxDfRc/69ox9vwBP1M/3E48aetjW6SLphoVw4Gjgo4n8QGOWFoYNXD7AVBqPrBVPjKjqBtQ3dgGyQCRBShoxN1uqfbLuMfMX46LvsuPwdUiX/NAlOZzkpJAXvH2DddzZTmDTwrsOoqiBtDyhZuI4wfhbzPrztdeXJ0UbuMiZT794sQv+DeLX7DMmUvWJYwmB2kx74WXDENQD+6zDVnnXTGB7SOc2txvQ0T0Su+CdEv6ekVgQN4zhRzqtGUOBJgSaVEyzin9ZjQ8v7SkmNSNHDXF9QZ6YrNj6NAMili29rMgZ6dod2W5iKUJRX0t9aDbOj7AXYD4GzzW8fyuX7veLzfBxiaV1QnALjJFCwcoYKMRMrBGOEHvIIoNUfM0JsPAZPADRg3mzT6sIk2aySKrPdTKCzVHyXqHCV817WijG4LeqjEYuppSG/rx73IUxd6GpyQPbh+045XSYl+HMpv18s="); }
    @Override public Pos position() { return new Pos(-54.5, 70, -65.5, 0, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Hello! I'm Ozanne.", "I sell dyes and cosmetic items for your gear.", "Fashion is just as important as function!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Looking to add some color to your armor?"); }
}
