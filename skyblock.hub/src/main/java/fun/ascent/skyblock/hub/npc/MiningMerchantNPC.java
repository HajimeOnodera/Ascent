package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record MiningMerchantNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_mining_merchant"; }
    @Override public String name() { return "<gray>Mining Merchant"; }
    @Override public NpcSkin skin() { return new NpcSkin("eyJ0aW1lc3RhbXAiOjE1NTA2Nzg1NjA4ODUsInByb2ZpbGVJZCI6ImEyZjgzNDU5NWM4OTRhMjdhZGQzMDQ5NzE2Y2E5MTBjIiwicHJvZmlsZU5hbWUiOiJiUHVuY2giLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzYwODk5MDY1NjQ5MmQ3MjJhOGVhODk2YjgxNmQwMTkxMTM2OGIzODdhMmFjNGJjNzRmNzBhMGFlZGQ3ZWI3ZjgifX19", "M1H74ucEXCmd/ws7LGJSVaek4/4qsXn1Cj5HkuOg0Z7atseANP3NSQGoQF/vjuAOu8pMt/hE6oXfxX2XjsTysVpFxKWho+fVAObgCrkep9xaEZL7BvJ+zfFA+Gb3CAWPl4DtvjC0Jr7iWUo+sdlyWYb4HbipImwSUMVqPE0H0NC/aIlwiC19TlRNhS8WSP9IrlYQ1F50/rRRIgE3VfCyXQ85L7nk/ZjkKnNOmB/ekENSx9PqCbPSJV7nDjYpZAC9aT/f+kU2EuTuSptjomcWAcEgkBee2QZfnHkT79e41ezvEp4ZYfWqjJn2cQOTFNKbvk9Pl3jRUvXeTiDUkL15pWmJWhnhtW9sBzoP3JW7JXvUSQCeqVtNW9eQjeWhm0q2xV28KqTMYmwB3ZPh0lihJv79ae7u2Zretr8GrC2fScD/GJFjpz3JGIIWpiRogp/KY08y2KT07AzatrGjTJVlgTQCyJqIEIy1EmUoMq50i8EYyPR8FA7JvHKiiRheb/97vj0CwqRfA4nFMC5iLpMRJEjNXoeg7t/pau6Y6GA4lXJQcxourKsiTrbq+Mww3yery0Q3HNCDcgvbCOWCMdoLdL9FdC3POI9C+t7Nqh271lxY39NhT8LQ1ZQ+V57dqUq7EBwz/fq8VHpR8xb4JBdhPL+Ksb6IiAt6xmZ1inOJCJs="); }
    @Override public Pos position() { return new Pos(14.5, 63, -114, 90, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"My specialties are ores, stone, and mining equipment.", "I've got everything a miner could need!", "Click me again to open the Miner Shop!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Need mining supplies? I've got picks, ores, and more!"); }
}
