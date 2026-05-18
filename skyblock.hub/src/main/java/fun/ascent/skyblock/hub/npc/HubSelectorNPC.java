package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record HubSelectorNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_hub_selector"; }
    @Override public String name() { return "<white>Hub Selector"; }
    @Override public NpcSkin skin() { return new NpcSkin("ewogICJ0aW1lc3RhbXAiIDogMTY4MTkxMjM0MzUzNywKICAicHJvZmlsZUlkIiA6ICIwZDZjODU0ODA3ZGQ0NWZkYmMxZDEyMzY2OGY1ZWQwZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJXcWxmZnhJcmt0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzUzMTRkMjc4NDRlYWRlYTU1MDM1ZTM1MDU3NWExOWVhMGU0NGMxNTVlMDhiZGNhYTEwOTFlYTExNDNiNmVkODMiCiAgICB9CiAgfQp9", "yGPTZDewV5jFB/WRbL58bu6WjeRJ9cEm6mR0iAr3F3xNO0L+n17+l5jNUYPdEZyuQDAq+7WdcbXwg5BxGv7pwpKSvlkhG7jQnbAzp0vjiB50/d1fMSIuZyYiGrMgnYwelyb/Pf+MbvBXicft53lecOVoP0ba1W4HsxKI1ItVaZvraN+cgUviWSuztDOQQcdkfpL1OOfTdSxoFFXhMpPbSxCyfKx1UovPNCeH1+UZDNrtZbYYUyUJ9mJUhX789V/XTcuCGx0MjQ/xLsxca6nm/xmY33xWjRZiETFLT+dfv8jYzUi8f5FIHHtK0cGJHLQy+ljwnE12T73aNpYR5LBxF6PDYeWG4DM2nssgA1FYwppAkmlJULzzFYI/IVRd8kfjRYDuCGg81Eg1L7Nzbjh/GNlPziHX2YXbD0fOgPaq38TT2/fFjfanIqCakAsiFBtGBqiD5aGQfpxIHSvnrq/J3pgskp0E5IZaJkU2UDsUGFh4mqS+cZoGhZokAOA3ivjU/5veKeG1EUXBlCHOHTPseWEYsSnezfIIqXnHcTJenoub1exiztVxUus/fUMajRpRPTF4qTvTpkG3Ora/kbJI73NZqiinJzFXrcGWoibcsyo07JtGPkGkBsp9z9ZiYyIhZ2Oft7xHlf84flKty3Pkjvxd2MxoYV02cmLUK+bYvYU="); }
    @Override public Pos position() { return new Pos(-5.5, 69, -22.5, -35, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Welcome! I can help you switch between different hub lobbies.", "Click me anytime to select a hub!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Looking to switch hubs? Click me to open the hub selector!"); }
}
