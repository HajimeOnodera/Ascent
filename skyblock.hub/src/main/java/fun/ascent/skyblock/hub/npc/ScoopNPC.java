package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record ScoopNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_scoop"; }
    @Override public String name() { return "Scoop"; }
    @Override public NpcSkin skin() { return new NpcSkin("ewogICJ0aW1lc3RhbXAiIDogMTYxMzQwOTIyMzExMywKICAicHJvZmlsZUlkIiA6ICIyYzEwNjRmY2Q5MTc0MjgyODRlM2JmN2ZhYTdlM2UxYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJOYWVtZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hODk4YWQ1OGQzNjY0Y2ZhY2YxYmYxMWFhZDlmNTEzZDI1YThiOWJiOTFhNzczODJiMzJjMTBiN2QwYTRiYzliIgogICAgfQogIH0KfQ==", "PS+Pk0FRc+9Klz9VLvuDKpzgA6MK6JCKPTLQKtRKPtZGxkUR8IU0HMhzLt9hRjvIrPsgFtNPz4pezviDBdTFyNm934B8UyslZWzsQJjeqqNYsDeSbDFQxQ1KSVIYurKbZpvMF7hS69uscQLlRG5ee3dvVva3XhgCryTllXTJjhaqLjLC8w+ujHzs1PU0WBrtFiArLmtMnwOIhr4CIK2T4jnmb9PAkzsCcI/TIYY39R0PvANnmN5tTl7BohHVXQgZsDZLOqDvam8kLg9iXh0HY6dOx35q7YaKudN8uk/FwusMlJLnvDaEJK1e47oNmSWykJ3TCGpO5U1gCX45e/3gO4VTPSKKZUHNJwO7T3K9lwM4HG8yfS2lKFk5z0y5tJtQBHQ1HirJS0OpQbqFu4ccvXgGeqlWUCByDbgiwMAm0K40daQNM1kzLaHMffNqR3Q+k16lbZnmICGHDJqBzkf/nlKscGjeQ5vB+3O/82k+3CYEoNLeKpBQwP9T5lhwZqCk685TjG7fqyFtbMG154nubl5FKH05d1JL91Ir+Zsn101DypoUY7f5KKkaWgjf8B66aKA9Mkh6yiSDZmaczuj3hlWUM/fUC6LNAAXaYqlDoboyACJrswV+Teiis6h9vuq2ZefU5kmVlSnifrvkCIeYFrLkv1LEvex7LOClbJ3V8YY="); }
    @Override public Pos position() { return new Pos(4.5, 180, 56.5, 69, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Hey there! I'm Scoop, the news reporter!", "I've got all the latest scoops on what's happening around here!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Got any hot tips for me?"); }
}
