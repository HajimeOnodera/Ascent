package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record FisherwomanEnidNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_fisherwoman_enid"; }
    @Override public String name() { return "<blue>Fisherwoman Enid"; }
    @Override public NpcSkin skin() { return new NpcSkin("ewogICJ0aW1lc3RhbXAiIDogMTc0MTEwMTMwMzExNSwKICAicHJvZmlsZUlkIiA6ICIzN2JhNjRkYzkxOTg0OGI4YjZhNDdiYTg0ZDgwNDM3MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJTb3lLb3NhIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2IzNWNiMzhiODZjNGViYjYzN2ZjMDJkMzdmNzNkZmJmZWIxN2Y0NjI2N2Q4MTliMzgyOGFkZDQ2OWQ5M2UzOWEiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==", "I+sHx+MZakXIPUh9jbCMzbknhSJScGyDnbjKVxBUqss4/uvOyy+O0lqa3xOLKMPScFtOguogAc61eQyESNZYbtFE5oRHmNmBntPe09FQLJydxeC2Kre/V8s9oxHQ8+aSa4a6UfnhsSEbM5Xe9gEipy+SJZNuEtm1LEK/Nkwcxqn3MzXLPLUcYKGOs8bmH+umqM9c/CILh/paKOBZw0hkzz9bpx3JENPF0NEdRopAqdxE3EzNDg/o90jpbqKjYXSE0NXt1PPmJjC1tn/lqWkU+n+rIAI975X5ViE5cHNOkn+CecweVawZ9eq3hgVMNV9y5jN1utN38nmNUijZcz46rvsB27JjfsCk5tgFDcXjbFB3aMgqmzYwpC8/UALCiAi/GVWPyig1odm+8fvPPPmVD/yEesIcEL4SSRDxWv2CJocm/mQfU4cQ86AcCKPbKaxy5DRJrpm6d9x7ewhVgIwKgrVMR3SGNTlfTdm2UZTtyZZmDhbOAjvGA0b2GNpi47ahdBGqIvHKjpx02CUF21V9MND1zpo2DoAirnNHz5moUp3YpeV4p70vXB0RfwlZq3Caqwe8O7gxvPVYdR4ucg5SG6vHZ6FVMpRD3aqFwTdSa92JufJi7PL8ZDX1Ew9DQFNKL5N1ttp6Z33i7XqcD/lCLvTIBN9xESuTjousXJfD79w="); }
    @Override public Pos position() { return new Pos(41.5, 70, -22.25, 0, 0); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "The fish are biting today! You should try your luck."); }
}
