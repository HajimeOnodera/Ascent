package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record GrumblefootNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_grumblefoot"; }
    @Override public String name() { return "<white>Grumblefoot"; }
    @Override public NpcSkin skin() { return new NpcSkin("ewogICJ0aW1lc3RhbXAiIDogMTY1NzgwMzgxMTU5NSwKICAicHJvZmlsZUlkIiA6ICIxNzU1N2FjNTEzMWE0YTUzODAwODg3Y2E4ZTQ4YWQyNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJQZW50YXRpbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zMjA2NzNhN2E1NTJmYWMwOTQ2NjFiODM4Yjc1ZmI3YWIxMjAwZTY5MWRlN2JlZWMwYTg0NmE5ZTk3ODEwNThkIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=", "BUms4E/nHM/NSSKYlQrGkzW2VRC9hNGwVbltjvwG679iA62IJk9rGPXSoKMpZNveLz3GnpII24dxIpn2rA91oQMFMQgd2x3gsPVNcSL1BHN1a+8pJPF241q6y1/mgf3+outGXee6vOjVpX9LK4OoGhJQJmKhtqJnnnAsjUKdWdVTHtZmNOrSwuWZ/1OJVow3QVoz1s7Da7FpyrPr+j2drWw15XOvhI/Yg5y0hFv4ViMuFbwaKfbuImSA+mU5RyE3unaQImsQRZ6Iy6Tv6RjT+eczmRzfkzHOxjbAYPwN4v0OH3YGoDzYrUqAQmk9AI/dgA1ALbnBsC5Wh9b6bZi9TMq8aWnKtXUtrOBsNsjjcTO8dCzwfw91fpA0WevYbn3kovJZPCX2i09E80Z58iH88bjy1KtEbPwloYzUgLnt2SF7Yxol14sBRQoAPrBol5UVaikKccEp/z2yKwip2OCgVr3UMvWnDeaNhc9X0PRzrl3lYrAQmx6LnU5q6PrXpBUHuFP5YzTeiwVojIOqHzoUvZJAZFjVTpBTV2F+PRkhBMFL+SRNSqtC5emPqAWvL+n94UBAvLKDkQwCRb2OEv752wTDwVr2LLJ3qiVDFg1zDbkq8Yl+ddB9Ps0kdN/KqmJF81lQg+vhbfDyGMO+1sUWlPZ2CDCWLI1E6wzNeB7ZzYw="); }
    @Override public Pos position() { return new Pos(44, 112.4, 99.594, 149, 30); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Hmph. What do you want?", "I'm just sitting here minding my own business.", "Go bother someone else!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Hmph. Still here, are you?"); }
}
