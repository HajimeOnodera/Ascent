package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record SiriusNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_sirius"; }
    @Override public String name() { return "Sirius"; }
    @Override public NpcSkin skin() { return new NpcSkin("ewogICJ0aW1lc3RhbXAiIDogMTYxMjI5MDEyMjIxNSwKICAicHJvZmlsZUlkIiA6ICJkZGVkNTZlMWVmOGI0MGZlOGFkMTYyOTIwZjdhZWNkYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJEaXNjb3JkQXBwIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzY3YWUzZTI1MzlhNDQyNTE4MTMwMzcwZDNiMmQzYjE4MzdhNDkxZWUyM2Q1YWZmMjBmMDE1YWUyODRhMjhjOTAiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==", "wxO8NN0XVV66aolUjL9qhturPPfas7lvoxTGCZ9jkV+ojvREtBS2fYuFTNaF5kB+SZdnZLphOrLBAHlyMIsGS7o+MhBmtlQblJBafdNHN5CsA5LpsbaHwj+WZjqSnzCV2qVVj2gY7w7HLlLV1PEPg97TcRS/SjIZND08257kJOkrCRxJBLGfQ8ZhQWl0p4JF5S4bn+kP754L1fyUx0kYWGstIZGmMcyQkY0aOI251bRapVa3rs+rc0QtOkRyy38OputzTaCREQ3m3fpCZ5lxG0IvR4NScXc3V1EJKwvsEpXUYsy1ElrrbYle+2sZvHdPS4JGYN8RLcVBiOHcncZ2xSSlep2O9rui/jtiTZAIvUGQX6Fn4RSRbeI9qinOr8qhGOnKiPsBXx18BNgCRNcj1rCmwAqmHGbP0eMZ/KYMoVlHZWlXBd1DMN/7duEoy5MV59+GK2sOO2Qkz1C+TeTKRV2gLe2tkTJMukUYS58m4BBZPavdY8RZAnzn0EP6pwWTrb1ms9ZA+CovfFTdGLh4fGuHjrhQBvdldQf00gbbDGcWnOoiiEfsZ/azW1VmB08XpIvRlLDTTgjmI1avwrd0BMe+oRNt9wOLFnUbPZ+weIKPTT/iyrhthG3dialYr+GgTJGJ/+QHKJY9gWkDIcDrl3Iei3Va1tuI2VvEtf9wcy8="); }
    @Override public Pos position() { return new Pos(91, 75, 176, 180, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Are you here for the Auction? Only the richest players can enter.", "You signed up for the Auction!", "You'll be warped once it starts..."}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Are you here for the Auction? Only the richest players can enter."); }
}
