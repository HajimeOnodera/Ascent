package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record JacobusNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_jacobus"; }
    @Override public String name() { return "<white>Jacobus"; }
    @Override public NpcSkin skin() { return new NpcSkin("ewogICJ0aW1lc3RhbXAiIDogMTYyNTg2MjUzNTA5OSwKICAicHJvZmlsZUlkIiA6ICIyYzEwNjRmY2Q5MTc0MjgyODRlM2JmN2ZhYTdlM2UxYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJOYWVtZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81YjFhNjg0ZTI3NGY2MWJkMjUwZDhkZGNlNTcxYzgxNjY2YzMwNTZjY2YyZjNkZjVkMGEzZDYwMjc0YmQzZThjIgogICAgfQogIH0KfQ==", "UyawAzr+K7HV66yViJLuJXuCAUBBnoxEtlfszgLIe1waFxfCL/YBnQsdXSKIaM7W5QTu+nD9H6Mp0C3bCWsC4zDcWXEq+UW38AT15llzr8Y9e48DJ+LtNXGWdkgK3k2FsbCMziz6+KH/3JmA2Ux3UcqLXCNdD47/T+SORJSW8cQIvxTvUrH1dOoVcJVPB2H30i8GC22xdYgffX2X79hlUIermyCg+y02asJWW1iwt2aTmBYQecyS1yyLvLpS1IUmGWOR+4E8gqm8Q7C/gUAk+H174mTkCobVvZYmu1OnJql4hvm0FaTgUKlR/yIyTWitF6hjl94s8//ETmtTqy4gig8SeHBrNWmZ/4CLyg/2Qmqyy24JtJpCmi4bVacZFkIMVaF06kUbLQZy+Y0Ybnc4iqFklf4nBW7BFCJc0BrA2e4pmK5bOO6DScw8PWzLh0bYbibbhezk4G+SwVNHK9hEtpXsHz4LCSSaRF8HVX3uKJS5kBki0MQYoWASPWY24GgPUikVXd/hYOhArEQfWcGt7C9m6irrDRjzJq6GPco1AOWDpT1uj4kHYhF5enA49Dilqs9hVfsFuh3XvyC0U73FAjm1pTI6iSagd+lnGbzISofgXn4YTtl9EHDtX8JTZuFMGL/tfd2UAqHOxFxY+godkXii+MlramNilHXCGr+zyx0="); }
    @Override public Pos position() { return new Pos(-49.5, 70, -60.5, 90, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Greetings, traveler. I am Jacobus.", "I deal in exotic goods from distant lands.", "Come back when my shop is ready!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "My shop is not quite ready yet. Check back later!"); }
}
