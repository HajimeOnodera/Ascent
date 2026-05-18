package fun.ascent.skyblock.hub.npc;

import fun.ascent.common.npc.AscentNpc;
import fun.ascent.common.npc.NpcDefinition;
import fun.ascent.common.npc.NpcSkin;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public record GavinNPC(Instance instance) implements NpcDefinition {
    @Override public String id() { return "hub_gavin"; }
    @Override public String name() { return "<aqua>Gavin"; }
    @Override public NpcSkin skin() { return new NpcSkin("ewogICJ0aW1lc3RhbXAiIDogMTc0MDA3MTUzNTE3NCwKICAicHJvZmlsZUlkIiA6ICI1ODc5MjNlNDkxMzM0ZDMzYWE4ZjQ3ZWJkZTljOTc3MiIsCiAgInByb2ZpbGVOYW1lIiA6ICJFbGV2ZW5mb3VyMTAiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDZlMzc1NmQ4Y2ZhYzU0MDU1YmNkNTUyNWQyYTNhYmVmZTZjZWY3NzkyNGY4YTk5ODQ2YmVhMmZlZmY3NjExNCIKICAgIH0KICB9Cn0=", "qwNSHj1b7UwXoeP6/Vs/1EGb0pyfO+DGOVUQ9DJ7DY0ZPn6VwQv1Ej1W39wANDGJI1p8eQKRqMLKH0Xj4WkSwnMnj0e7DZU2VROO9xi3th5IhJg/7SzpMt2vMvYUN3u6HI1EzhYHuLL1oHF1eGK/5lZZp6xvb2X4ZuOvX8oASvrgQFPcxR2WMn4nwS4bKRp5CDFGg+fCHCMuHHQAjZpJAHqbfxD2DWXq7CbuyOQJlIybhGDY30syWVKH1aZsp5Nmm8fOeSysqyZL+F49zBbxPUBaX/gmxsBR8cRyU37gBzT39aTeybFsZrQSOz3raFX7H4pRC8xf9dKQzTYxCvi14ljjp8q+IH1AWYSZJSZkxA6k03gxVH6Oxbs8XjHWxYQIu4uPhTV3LRShkSRz1WTLYHqu+I/fhmAsAN7YJQHYNFJGkLSBVYRCkSPCQ3efj7TDX455KlojZ23waaqEvH0d9gUMEScRc3Qpq1Tf3X63CNvd5BBO6apiN9Vfq3TZ3YKdhGLQLXBsu3QewCH2qcf6jbU37in6FltyncRsrd84pFsl2ryquOYhwj2slnVNIyhG7zAguRM9p7zvZOPYlgSplcMgW8sQ3Isv25PBGZ+B6Qhwdl4yYpUnc/n7EyxMuthGyID/nhwTBq4f51L3+RNDYnFz9ED26E3IcW/dQdgxPNA="); }
    @Override public Pos position() { return new Pos(147.5, 69.9, -59.5, -90, 0); }
    @Override public String[] firstInteractionMessages() { return new String[]{"At the end of each year I bake cakes for everyone in town to celebrate the year.", "I made one especially for you, here you go.", "I've recently added a New Year Cake Bag to my inventory. Sadly, it's not free! Click me again to open my shop!"}; }
    @Override public void onFirstInteract(Player player, AscentNpc npc) { npc.speak(player, firstInteractionMessages()); }
    @Override public void onInteract(Player player, AscentNpc npc) { npc.speak(player, "I'd rather be out there fishing for Treasure. But mum insists I finish my studies first."); }
}
