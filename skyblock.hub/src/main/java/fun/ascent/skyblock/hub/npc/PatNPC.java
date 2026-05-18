package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record PatNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_pat"; }
    @Override public String name() { return "<green>Pat"; }
    @Override public NpcSkin skin() { return new NpcSkin("eyJ0aW1lc3RhbXAiOjE1NjAyNjIyNTU5MDksInByb2ZpbGVJZCI6ImEyZjgzNDU5NWM4OTRhMjdhZGQzMDQ5NzE2Y2E5MTBjIiwicHJvZmlsZU5hbWUiOiJiUHVuY2giLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzkyMzk2YjBhZDM1MmIyNTMxMTM5YzlkYzRjZWY5YTMyMzUwYzdjYzE0ZWQ3YmNmYzUxODVjZTE1YTdhN2Q1ZWIifX19", "v0ZBL+K46GWaiXXnd0FnXUfroDo+ELe6S94Q2/RqW5sfQxGNvb2ju5/M4cXiNGT2A6YsBgKA192VzS89K2DHbGlJbTyz0aJ3roWEKJo2vWoDHEmOUeiPMBrUp8obxl6jwzn9N2umYEaRavU6WP09sUutvWcTJik6eRGRaMbWYOFVGw6vGU3Al7m+e7/vJNUKndRWfpOQ8B9QG9Fk4WisDtafJ4b0/hDprgl6wPg4TMt4quxFTurBs56pB+7pQfksiW29jXE1GrukPP400/ws4esBfgnUK5dYJMjxf/9DkWv6GH8i0q7IyctRtRRx8d71JxviS9GhhV12i/Hp4V5WTnHZjaICMrWzRpyVRTgPGVxGHt3gmg5VHy+qVHXAWhVTApWCV1WO27mcGxVjfrYxBjbIvUqYhcluhpIv3smGQxJqpe8cFGL7j26n7w683XrvZZ1i/fYQHA9Nx6J/RLXc6H3CVDAh67tvtDB5Abmij2BbGLHPMw3+GVTqNlx99i/sXuT8V6kmr28JQ3owOQqcqcHtyFP4ENqZeJGLLzMrO/Xlrm+GfKt4QotTh9M5YpExk6P/g5GXe1ULA4mLLQ4/cypXn3A4B6Zk+5RH4QGoLN5O6fWt3MqTN0P26wh/zyQFi6Ss/KSvuVnM0IAJ+AaLmHBDLjtGZzZGmNT7c6ZL4uo="); }
    @Override public Pos position() { return new Pos(-87.5, 73, -94.5, -180, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Hey! I'm Pat.", "I help with pet-related stuff around here.", "Pets can really boost your stats if you level them up!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "How are your pets doing? Keep leveling them up!"); }
}
