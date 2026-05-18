package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record KatNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_kat"; }
    @Override public String name() { return "<aqua>Kat"; }
    @Override public NpcSkin skin() { return new NpcSkin("ewogICJ0aW1lc3RhbXAiIDogMTU4ODcyMTU5ODM4OCwKICAicHJvZmlsZUlkIiA6ICI5ZThiODc0N2EzMTc0NGU2OGE0ODcxMzM0MDNiNGQzNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJFdmlsRGN0ciIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lYWFlODEyNzc1NzA2YjU2NTk2NjgzNDg2YWJlYWY4NTdlYTFjMDY4Y2IzNGE4YmMxZGU5YTU3YzYzOGNmNDExIgogICAgfQogIH0KfQ==", "vPok0YXlbGxwdgMYI8WExOL1dfYlF47ISzPRMUr5g9nvtiHdTktB45gT7hTB45VwsAScDIgKOHl2fo/jlLCW01xtsHlczuvjurLvtyrQj8ipjfFr/RZTWUF1ZdXM2C5fRWa0etz4UQeQwcmKOnD+NUOKux3GJfhN7el7xYI2fp6nRDCqIcyAW/MIL7UhqdoRCUDIFgymYBQ04AVDIZ59nZjaqdBS+5oUQ8AkJkcZSThx0CpCdj24/xeyNlTgx9I0jwvIrf9AP+cc8hxrEIVhy9sMH9lHmIhbsdiGYlgMBFjfvdp9KOrFyWSjUyeqHstNXZ8C5v2ieWDy4u+FYtG485vylOQPo5CRitRdNBBbiMsL5rroVumLKroh0gyZGLlYHYAq0CmWST6wbizPpcE3tAAvYdiPCEMHpTYWiPAuKckBeSWaVL3t1MZuQoRM5aV8QeDGoe6QhYBsXVxrPDxRX05nZ5sDII8Uht6r8u0uk21ejvswnSGXH9Rpjoy85YKTDLtSzXYmzk/YmpfPUWB9YnXeARCJYO+kNN6QciDFTt+e5XtSKz4o9ejbvWu3Y74CXnWYcEs6Vqu/zztYfq47roCdTP/TVGD311pRxZvjS5PaQdEbrkLCFblOhGOcxg0IFbs/zlz8bemBdQZyJIzR3IwgIH1tcv4GEMs20rTrP9k="); }
    @Override public Pos position() { return new Pos(10.5, 72, -53.5, 45, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"Hello! I'm Kat, the pet sitter.", "I can upgrade your pets to the next rarity for a fee.", "Just bring me your pet and I'll take good care of it!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "Need a pet upgraded? Bring it to me and I'll work my magic!"); }
}
