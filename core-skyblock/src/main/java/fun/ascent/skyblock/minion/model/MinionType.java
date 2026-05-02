package fun.ascent.skyblock.minion;

import net.minestom.server.color.Color;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;

public enum MinionType {
    COAL("coal", "Coal Minion", Material.COAL, Material.COAL, MinionCategory.MINING, MinionActionKind.MINING, Block.COAL_BLOCK, Block.COAL_ORE, null, null, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MTkyNTA4MjIsInByb2ZpbGVJZCI6IjU2Njc1YjIyMzJmMDRlZTA4OTE3OWU5YzkyMDZjZmU4IiwicHJvZmlsZU5hbWUiOiJUaGVJbmRyYSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDI1YjhkMmVhOTY1Yzc4MDY1MmQyOWMyNmIxNTcyNjg2ZmQ3NGY2ZmU2NDAzYjVhMzgwMDk1OWZlYjJhZDkzNSJ9fX0==", new Color(90, 90, 90), "generating and mining Coal!", "Requires an open area to place ore.", "Open 5x5 ore area", 1),
    COBBLESTONE("cobblestone", "Cobblestone Minion", Material.COBBLESTONE, Material.COBBLESTONE, MinionCategory.MINING, MinionActionKind.MINING, Block.COBBLESTONE, Block.COBBLESTONE, null, null, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MTkyNzc1NzMsInByb2ZpbGVJZCI6IjkxZjA0ZmU5MGYzNjQzYjU4ZjIwZTMzNzVmODZkMzllIiwicHJvZmlsZU5hbWUiOiJTdG9ybVN0b3JteSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmY5MzI4OWE4MmJkMmEwNmNiYmU2MWI3MzNjZmRjMWYxYmQ5M2M0MzQwZjdhOTBhYmQ5YmRkYTc3NDEwOTA3MSJ9fX0=", new Color(120, 124, 132), "generating and mining cobblestone!", "Requires an open area to place cobblestone.", "Open 5x5 ore area", 1),
    DIAMOND("diamond", "Diamond Minion", Material.DIAMOND, Material.DIAMOND, MinionCategory.MINING, MinionActionKind.MINING, Block.DIAMOND_BLOCK, Block.DIAMOND_ORE, null, null, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MTk0NzI5ODEsInByb2ZpbGVJZCI6IjU3MGIwNWJhMjZmMzRhOGViZmRiODBlY2JjZDdlNjIwIiwicHJvZmlsZU5hbWUiOiJMb3JkU29ubnkiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzIzNTRiYmU2MDRkZmU1OGJmOTJlNzcyOTczMGQwYzhlMzc4NDRlODMxZWUzODE2ZDdlODQyN2MyN2ExODI0YTIifX19", new Color(77, 231, 244), "generating and mining Diamonds!", "Requires an open area to place ore.", "Open 5x5 ore area", 1),
    EMERALD("emerald", "Emerald Minion", Material.EMERALD, Material.EMERALD, MinionCategory.MINING, MinionActionKind.MINING, Block.EMERALD_BLOCK, Block.EMERALD_ORE, null, null, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MTk1MjU2MTUsInByb2ZpbGVJZCI6ImRkZWQ1NmUxZWY4YjQwZmU4YWQxNjI5MjBmN2FlY2RhIiwicHJvZmlsZU5hbWUiOiJEaXNjb3JkQXBwIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85YmY1N2YzNDAxYjEzMGM2YjUzODA4ZjJiMWUxMTljYzdiOTg0NjIyZGFjNzA3N2JiZDUzNDU0ZTFmNjViYmYwIn19fQ==", new Color(80, 215, 120), "generating and mining Emeralds!", "Requires an open area to place ore.", "Open 5x5 ore area", 1),
    ENDSTONE("endstone", "End Stone Minion", Material.END_STONE, Material.END_STONE, MinionCategory.MINING, MinionActionKind.MINING, Block.END_STONE, Block.END_STONE, null, null, null, "eyJ0aW1lc3RhbXAiOjE1NjM5ODE2MjkxODksInByb2ZpbGVJZCI6IjgyYzYwNmM1YzY1MjRiNzk4YjkxYTEyZDNhNjE2OTc3IiwicHJvZmlsZU5hbWUiOiJOb3ROb3RvcmlvdXNOZW1vIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83OTk0YmUzZGNmYmI0ZWQwYTVhNzQ5NWI3MzM1YWYxYTNjZWQwYjU4ODhiNTAwNzI4NmE3OTA3NjdjM2I1N2U2In19fQ==", new Color(221, 221, 146), "generating and mining End Stone!", "Requires an open area to place blocks.", "Open 5x5 ore area", 1),
    GLOWSTONE("glowstone", "Glowstone Minion", Material.GLOWSTONE, Material.GLOWSTONE_DUST, MinionCategory.MINING, MinionActionKind.MINING, Block.GLOWSTONE, Block.GLOWSTONE, null, null, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MTk2NTk3MTUsInByb2ZpbGVJZCI6IjNmYzdmZGY5Mzk2MzRjNDE5MTE5OWJhM2Y3Y2MzZmVkIiwicHJvZmlsZU5hbWUiOiJZZWxlaGEiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzIwZjRkN2MyNmIwMzEwOTkwYTdkM2EzYjQ1OTQ4Yjk1ZGQ0YWI0MDdhMTZhNGI2ZDNiN2NiNGZiYTAzMWFlZWQifX19", new Color(244, 195, 73), "generating and mining Glowstone!", "Requires an open area to place blocks.", "Open 5x5 ore area", 1),
    GOLD("gold", "Gold Minion", Material.GOLD_INGOT, Material.GOLD_INGOT, MinionCategory.MINING, MinionActionKind.MINING, Block.GOLD_BLOCK, Block.GOLD_ORE, null, null, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MTk3MTIzMDIsInByb2ZpbGVJZCI6IjkxZjA0ZmU5MGYzNjQzYjU4ZjIwZTMzNzVmODZkMzllIiwicHJvZmlsZU5hbWUiOiJTdG9ybVN0b3JteSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjZkYTA0ZWQ4YzgxMGJlMjliYmE1M2M2MmU3MTJkNjVjZmIyNTIzODExN2I5NGQ3ZTg1YTQ2MTU3NzViZjE0ZiJ9fX0=", new Color(232, 189, 45), "generating and mining Gold!", "Requires an open area to place ore.", "Open 5x5 ore area", 1),
    GRAVEL("gravel", "Gravel Minion", Material.GRAVEL, Material.GRAVEL, MinionCategory.MINING, MinionActionKind.MINING, Block.GRAVEL, Block.GRAVEL, null, null, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MTk3NDgyNjUsInByb2ZpbGVJZCI6ImIwZDRiMjhiYzFkNzQ4ODlhZjBlODY2MWNlZTk2YWFiIiwicHJvZmlsZU5hbWUiOiJ4RmFpaUxlUiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzQ1ODUwN2VkMzFjZjlhMzg5ODZhYzg3OTUxNzNjNjA5NjM3ZjAzZGE2NTNmMzA0ODNhNzIxZDNmYmU2MDJkIn19fQ==", new Color(108, 108, 108), "generating and mining Gravel!", "Requires an open area to place gravel.", "Open 5x5 ore area", 2),
    HARDSTONE("hardstone", "Hard Stone Minion", Material.STONE, Material.COBBLESTONE, MinionCategory.MINING, MinionActionKind.MINING, Block.STONE, Block.STONE, null, null, null, "ewogICJ0aW1lc3RhbXAiIDogMTYxOTExMjc4NDE1MywKICAicHJvZmlsZUlkIiA6ICJiNzVjZDRmMThkZjg0MmNlYjJhY2MxNTU5MTNiMjA0YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJLcmlzdGlqb25hczEzIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzFlOGJhYjk0OTM3MDhiZWRhMzQyNTU2MDZkNTg4M2I4NzYyNzQ2YmNiZTZjOTRlOGNhNzhhNzdhNDA4YzhiYTgiCiAgICB9CiAgfQp9", new Color(143, 143, 143), "generating and mining Hard Stone!", "Requires an open area to place stone.", "Open 5x5 ore area", 1),
    ICE("ice", "Ice Minion", Material.ICE, Material.ICE, MinionCategory.MINING, MinionActionKind.MINING, Block.ICE, Block.ICE, null, null, null, "eyJ0aW1lc3RhbXAiOjE1NTk2NTkzMjk0MjcsInByb2ZpbGVJZCI6ImIwZDczMmZlMDBmNzQwN2U5ZTdmNzQ2MzAxY2Q5OGNhIiwicHJvZmlsZU5hbWUiOiJPUHBscyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTUwMDA2NDMyMWIxMjk3MmY4ZTU3NTA3OTNlYzFjODIzZGE0NjI3NTM1ZTlkMTJmZWFlZTc4Mzk0Yjg2ZGFiZSJ9fX0=", new Color(168, 219, 255), "generating and mining Ice!", "Requires an open area to place ice.", "Open 5x5 ore area", 1),
    IRON("iron", "Iron Minion", Material.IRON_INGOT, Material.IRON_INGOT, MinionCategory.MINING, MinionActionKind.MINING, Block.IRON_BLOCK, Block.IRON_ORE, null, null, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MTk3OTUzNTAsInByb2ZpbGVJZCI6IjJjMTA2NGZjZDkxNzQyODI4NGUzYmY3ZmFhN2UzZTFhIiwicHJvZmlsZU5hbWUiOiJOYWVtZSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWY0MzUwMjJjYjM4MDlhNjhkYjBmY2NmYTg5OTNmYzE5NTRkYzY5N2E3MTgxNDk0OTA1YjAzZmRkYTAzNWU0YSJ9fX0=", new Color(187, 187, 187), "generating and mining Iron!", "Requires an open area to place ore.", "Open 5x5 ore area", 1),
    LAPIS("lapis", "Lapis Minion", Material.LAPIS_LAZULI, Material.LAPIS_LAZULI, MinionCategory.MINING, MinionActionKind.MINING, Block.LAPIS_BLOCK, Block.LAPIS_ORE, null, null, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MTc2MDMwNDMsInByb2ZpbGVJZCI6IjkxZjA0ZmU5MGYzNjQzYjU4ZjIwZTMzNzVmODZkMzllIiwicHJvZmlsZU5hbWUiOiJTdG9ybVN0b3JteSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjRmZDk3YjkzNDZjMTIwOGMxZGIzOTU3NTMwY2RmYzU3ODllM2U2NTk0Mzc4NmIwMDcxY2YyYjI5MDRhNmI1YyJ9fX0=", new Color(77, 97, 212), "generating and mining Lapis!", "Requires an open area to place ore.", "Open 5x5 ore area", 1),
    OBSIDIAN("obsidian", "Obsidian Minion", Material.OBSIDIAN, Material.OBSIDIAN, MinionCategory.MINING, MinionActionKind.MINING, Block.OBSIDIAN, Block.OBSIDIAN, null, null, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MTk5NDcyMTUsInByb2ZpbGVJZCI6IjNmYzdmZGY5Mzk2MzRjNDE5MTE5OWJhM2Y3Y2MzZmVkIiwicHJvZmlsZU5hbWUiOiJZZWxlaGEiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzMyMGMyOWFiOTY2NjM3Y2I5YWVjYzM0ZWU3NmQ1YTAxMzA0NjFlMGM0ZmRiMDhjZGFmODA5MzlmYTEyMDkxMDIifX19", new Color(45, 25, 61), "generating and mining Obsidian!", "Requires an open area to place blocks.", "Open 5x5 ore area", 1),
    QUARTZ("quartz", "Quartz Minion", Material.QUARTZ, Material.QUARTZ, MinionCategory.MINING, MinionActionKind.MINING, Block.QUARTZ_BLOCK, Block.NETHER_QUARTZ_ORE, null, null, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MjAwNDc1MjYsInByb2ZpbGVJZCI6IjNmYzdmZGY5Mzk2MzRjNDE5MTE5OWJhM2Y3Y2MzZmVkIiwicHJvZmlsZU5hbWUiOiJZZWxlaGEiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2QyNzAwOTNiZTYyZGZkMzAxOWY5MDgwNDNkYjU3MGI1ZGZkMzY2ZmQ1MzQ1ZmNjZjlkYTM0MGU3NWM3MDFhNjAifX19", new Color(245, 242, 233), "generating and mining Quartz!", "Requires an open area to place ore.", "Open 5x5 ore area", 1),
    REDSTONE("redstone", "Redstone Minion", Material.REDSTONE, Material.REDSTONE, MinionCategory.MINING, MinionActionKind.MINING, Block.REDSTONE_BLOCK, Block.REDSTONE_ORE, null, null, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MjAxMDg2NDUsInByb2ZpbGVJZCI6Ijc1MTQ0NDgxOTFlNjQ1NDY4Yzk3MzlhNmUzOTU3YmViIiwicHJvZmlsZU5hbWUiOiJUaGFua3NNb2phbmciLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzFlZGVmY2YxYTg5ZDY4N2EwYTRlY2YxNTg5OTc3YWYxZTUyMGZjNjczYzQ4YTA0MzRiZTQyNjYxMmU4ZmFhNjcifX19", new Color(175, 35, 35), "generating and mining Redstone!", "Requires an open area to place ore.", "Open 5x5 ore area", 1),
    SAND("sand", "Sand Minion", Material.SAND, Material.SAND, MinionCategory.MINING, MinionActionKind.MINING, Block.SAND, Block.SAND, null, null, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MjAxNDY2NzAsInByb2ZpbGVJZCI6IjgyYzYwNmM1YzY1MjRiNzk4YjkxYTEyZDNhNjE2OTc3IiwicHJvZmlsZU5hbWUiOiJOb3ROb3RvcmlvdXNOZW1vIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84MWY4ZTJhZDAyMWVlZmQxMjE3ZTY1MGU4NDhiNTc2MjIxNDRkMmJmOGEzOWZiZDUwZGFiOTM3YTdlYWMxMGRlIn19fQ==", new Color(218, 203, 128), "generating and mining Sand!", "Requires an open area to place sand.", "Open 5x5 ore area", 1),
    SNOW("snow", "Snow Minion", Material.SNOW_BLOCK, Material.SNOWBALL, MinionCategory.MINING, MinionActionKind.MINING, Block.SNOW_BLOCK, Block.SNOW_BLOCK, null, null, null, "eyJ0aW1lc3RhbXAiOjE1NzY1MTMxOTQ4MDUsInByb2ZpbGVJZCI6ImRlNTcxYTEwMmNiODQ4ODA4ZmU3YzlmNDQ5NmVjZGFkIiwicHJvZmlsZU5hbWUiOiJNSEZfTWluZXNraW4iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2Y2ZDE4MDY4NGMzNTIxYzlmYzg5NDc4YmE0NDA1YWU5Y2U0OTdkYTgxMjRmYTBkYTVhMDEyNjQzMWM0Yjc4YzMifX19", new Color(247, 247, 247), "generating and mining Snow!", "Requires an open area to place snow.", "Open 5x5 ore area", 1),

    CACTUS("cactus", "Cactus Minion", Material.CACTUS, Material.CACTUS, MinionCategory.FARMING, MinionActionKind.CROP, Block.SANDSTONE, Block.CACTUS, Block.SAND, null, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MTgxNDU1MzMsInByb2ZpbGVJZCI6IjZlZDg3NmUzZjg4MTRmYzhhMTNlMDU0MDU1ODFjNDZiIiwicHJvZmlsZU5hbWUiOiJBenVyZUJsdWUiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2VmOTNlYzZlNjdhNmNkMjcyYzlhOTY4NGI2N2RmNjI1ODRjYjA4NGEyNjVlZWUzY2RlMTQxZDIwZTcwZDdkNzIifX19", new Color(40, 175, 75), "generating and harvesting Cactus!", "Requires sand nearby so cactus can be planted.", "Sand nearby", 1),
    CARROT("carrot", "Carrot Minion", Material.CARROT, Material.CARROT, MinionCategory.FARMING, MinionActionKind.CROP, Block.HAY_BLOCK, Block.CARROTS, Block.FARMLAND, null, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MTkwOTgyNTEsInByb2ZpbGVJZCI6IjVkMjRiYTBiMjg4YzQyOTM4YmExMGVjOTkwNjRkMjU5IiwicHJvZmlsZU5hbWUiOiIxbnYzbnQxdjN0NGwzbnQiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzRiYWVhOTkwYjQ1ZDMzMDk5OGNiMGMxZjg1MTVjMjdiMjRmOTNiZmYxZGYwZGIwNTZlNjQ3ZjgyMDBkMDNiOWQifX19", new Color(235, 126, 32), "generating and harvesting Carrots!", "Requires farmland nearby so carrots can be planted.", "Farmland nearby", 1),
    COCOA("cocoa", "Cocoa Beans Minion", Material.COCOA_BEANS, Material.COCOA_BEANS, MinionCategory.FARMING, MinionActionKind.CROP, Block.JUNGLE_LOG, Block.COCOA, Block.JUNGLE_LOG, null, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MTkzMTQ3MDQsInByb2ZpbGVJZCI6ImIwZDRiMjhiYzFkNzQ4ODlhZjBlODY2MWNlZTk2YWFiIiwicHJvZmlsZU5hbWUiOiJ4RmFpaUxlUiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWNiNjgwZTk2ZjYxNzdjZDhmZmFmMjdlOTYyNWQ4YjU0NGQ3MjBhZmM1MDczODgwMTgxOGQwZTc0NWMwZTVmNyJ9fX0=", new Color(125, 77, 40), "generating and harvesting Cocoa Beans!", "Requires jungle logs nearby so cocoa can be planted.", "Jungle logs nearby", 1),
    MELON("melon", "Melon Minion", Material.MELON, Material.MELON_SLICE, MinionCategory.FARMING, MinionActionKind.CROP, Block.HAY_BLOCK, Block.MELON, Block.FARMLAND, null, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MjA1Mjg4MzQsInByb2ZpbGVJZCI6ImRkZWQ1NmUxZWY4YjQwZmU4YWQxNjI5MjBmN2FlY2RhIiwicHJvZmlsZU5hbWUiOiJEaXNjb3JkQXBwIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85NWQ1NDUzOWFjOGQzZmJhOTY5NmM5MWY0ZGNjN2YxNWMzMjBhYjg2MDI5ZDVjOTJmMTIzNTlhYmQ0ZGY4MTFlIn19fQ==", new Color(87, 178, 72), "generating and harvesting Melons!", "Requires farmland nearby so melons can grow.", "Farmland nearby", 1),
    NETHER_WART("nether_wart", "Nether Wart Minion", Material.NETHER_WART, Material.NETHER_WART, MinionCategory.FARMING, MinionActionKind.CROP, Block.NETHERRACK, Block.NETHER_WART, Block.SOUL_SAND, null, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MTk5MDAxMjIsInByb2ZpbGVJZCI6IjkxZjA0ZmU5MGYzNjQzYjU4ZjIwZTMzNzVmODZkMzllIiwicHJvZmlsZU5hbWUiOiJTdG9ybVN0b3JteSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFhNDYyMGJiMzQ1OWMxYzJmYTc0YjIxMGIxYzA3YjRhMDIyNTQzNTFmNzUxNzNlNjQzYTBlMDA5YTYzZjU1OCJ9fX0=", new Color(160, 25, 25), "generating and harvesting Nether Wart!", "Requires soul sand nearby so nether wart can be planted.", "Soul Sand nearby", 1),
    POTATO("potato", "Potato Minion", Material.POTATO, Material.POTATO, MinionCategory.FARMING, MinionActionKind.CROP, Block.HAY_BLOCK, Block.POTATOES, Block.FARMLAND, null, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MTk5OTkyNjksInByb2ZpbGVJZCI6IjkxZjA0ZmU5MGYzNjQzYjU4ZjIwZTMzNzVmODZkMzllIiwicHJvZmlsZU5hbWUiOiJTdG9ybVN0b3JteSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2RkYTM1YTA0NGNiMDM3NGI1MTYwMTVkOTkxYTBmNjViZjdkMGZiNjU2NmUzNTA0OTY2NDJjZjIwNTlmZjFkOSJ9fX0=", new Color(198, 154, 75), "generating and harvesting Potatoes!", "Requires farmland nearby so potatoes can be planted.", "Farmland nearby", 1),
    PUMPKIN("pumpkin", "Pumpkin Minion", Material.PUMPKIN, Material.PUMPKIN, MinionCategory.FARMING, MinionActionKind.CROP, Block.HAY_BLOCK, Block.PUMPKIN, Block.FARMLAND, null, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MjAwMjQ5NTQsInByb2ZpbGVJZCI6ImIwZDRiMjhiYzFkNzQ4ODlhZjBlODY2MWNlZTk2YWFiIiwicHJvZmlsZU5hbWUiOiJ4RmFpaUxlUiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjNmYjY2M2U4NDNhN2RhNzg3ZTI5MGYyM2M4YWYyZjk3ZjdiNmY1NzJmYTU5YTBkNGQwMjE4NmRiNmVhYWJiNyJ9fX0=", new Color(232, 124, 32), "generating and harvesting Pumpkins!", "Requires farmland nearby so pumpkins can grow.", "Farmland nearby", 1),
    SUGARCANE("sugarcane", "Sugar Cane Minion", Material.SUGAR_CANE, Material.SUGAR_CANE, MinionCategory.FARMING, MinionActionKind.CROP, Block.SANDSTONE, Block.SUGAR_CANE, Block.SAND, null, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MjA0OTcwNDksInByb2ZpbGVJZCI6ImIwZDRiMjhiYzFkNzQ4ODlhZjBlODY2MWNlZTk2YWFiIiwicHJvZmlsZU5hbWUiOiJ4RmFpaUxlUiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmZjZWQwZTgwZjBkN2E1ZDFmNDVhMWE3MjE3ZTZhOTllYTk3MjAxNTZjNjNmNmVmYzg0OTE2ZDQ4MzdmYWJkZSJ9fX0=", new Color(116, 210, 102), "generating and harvesting Sugar Cane!", "Requires sand and adjacent water so sugar cane can be planted.", "Sand and water nearby", 1),
    WHEAT("wheat", "Wheat Minion", Material.WHEAT, Material.WHEAT, MinionCategory.FARMING, MinionActionKind.CROP, Block.HAY_BLOCK, Block.WHEAT, Block.FARMLAND, null, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MjA1NzU1MzAsInByb2ZpbGVJZCI6IjNmYzdmZGY5Mzk2MzRjNDE5MTE5OWJhM2Y3Y2MzZmVkIiwicHJvZmlsZU5hbWUiOiJZZWxlaGEiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2JiYzU3MWM1NTI3MzM2MzUyZTJmZWUyYjQwYTllZGZhMmU4MDlmNjQyMzA3NzlhYTAxMjUzYzZhYTUzNTg4MWIifX19", new Color(226, 207, 88), "generating and harvesting Wheat!", "Requires farmland nearby so wheat can be planted.", "Farmland nearby", 2),

    ACACIA("acacia", "Acacia Minion", Material.ACACIA_LOG, Material.ACACIA_LOG, MinionCategory.FORAGING, MinionActionKind.TREE, Block.ACACIA_LOG, Block.ACACIA_LOG, null, Block.ACACIA_LEAVES, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MTcyMjA2NjAsInByb2ZpbGVJZCI6IjZlZDg3NmUzZjg4MTRmYzhhMTNlMDU0MDU1ODFjNDZiIiwicHJvZmlsZU5hbWUiOiJBenVyZUJsdWUiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzQyMTgzZWFmNWIxMzNiODM4ZGIxM2QxNDUyNDdlMzg5YWI0YjRmMzNjNjc4NDYzNjM3OTJkYzNkODJiNTI0YzAifX19", new Color(191, 130, 82), "generating and chopping Acacia Logs!", "Requires an open area to place trees.", "Open 5x5 tree area", 1),
    BIRCH("birch", "Birch Minion", Material.BIRCH_LOG, Material.BIRCH_LOG, MinionCategory.FORAGING, MinionActionKind.TREE, Block.BIRCH_LOG, Block.BIRCH_LOG, null, Block.BIRCH_LEAVES, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MTg5MDY1NTQsInByb2ZpbGVJZCI6ImU3NTMyNjk3ZTgwZjQ1NmU5ZjNhZjZiODIzNWU5YTgxIiwicHJvZmlsZU5hbWUiOiJNaW5lQWx0c19NU2tpbl8xIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lYjc0MTA5ZGJiODgxNzhhZmI3YTk4NzRhZmM2ODI5MDRjZWRiM2RmNzU5NzhhNTFmN2JlZWIyOGY5MjQyNTEifX19", new Color(234, 222, 195), "generating and chopping Birch Logs!", "Requires an open area to place trees.", "Open 5x5 tree area", 1),
    DARK_OAK("dark_oak", "Dark Oak Minion", Material.DARK_OAK_LOG, Material.DARK_OAK_LOG, MinionCategory.FORAGING, MinionActionKind.TREE, Block.DARK_OAK_LOG, Block.DARK_OAK_LOG, null, Block.DARK_OAK_LEAVES, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MTk0NDEzODMsInByb2ZpbGVJZCI6IjkxZjA0ZmU5MGYzNjQzYjU4ZjIwZTMzNzVmODZkMzllIiwicHJvZmlsZU5hbWUiOiJTdG9ybVN0b3JteSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWVjZGM4ZDZiMmI3ZTA4MWVkOWMzNjYwOTA1MmM5MTg3OWI4OTczMGI5OTUzYWRiYzk4N2UyNWJmMTZjNTU4MSJ9fX0=", new Color(80, 58, 38), "generating and chopping Dark Oak Logs!", "Requires an open area to place trees.", "Open 5x5 tree area", 1),
    JUNGLE("jungle", "Jungle Minion", Material.JUNGLE_LOG, Material.JUNGLE_LOG, MinionCategory.FORAGING, MinionActionKind.TREE, Block.JUNGLE_LOG, Block.JUNGLE_LOG, null, Block.JUNGLE_LEAVES, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MTk4MTgzOTYsInByb2ZpbGVJZCI6IjVkMjRiYTBiMjg4YzQyOTM4YmExMGVjOTkwNjRkMjU5IiwicHJvZmlsZU5hbWUiOiIxbnYzbnQxdjN0NGwzbnQiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzJmZTczZDk4MTY5MGMxYmUzNDZhMTYzMzE4MTljNGU4ODAwODU5ZmNkYzNlNTE1MzcxOGM2YWQ0NTg2MTkyNGMifX19", new Color(144, 107, 61), "generating and chopping Jungle Logs!", "Requires an open area to place trees.", "Open 5x5 tree area", 1),
    OAK("oak", "Oak Minion", Material.OAK_LOG, Material.OAK_LOG, MinionCategory.FORAGING, MinionActionKind.TREE, Block.OAK_LOG, Block.OAK_LOG, null, Block.OAK_LEAVES, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MTk5MjMyMDcsInByb2ZpbGVJZCI6ImIwZDRiMjhiYzFkNzQ4ODlhZjBlODY2MWNlZTk2YWFiIiwicHJvZmlsZU5hbWUiOiJ4RmFpaUxlUiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTdlNGEzMGYzNjEyMDRlYTljZGVkM2ZiZmY4NTAxNjA3MzFhMDA4MWNjNDUyY2ZlMjZhZWQ0OGU5N2Y2MzY0YiJ9fX0=", new Color(160, 118, 70), "generating and chopping Oak Logs!", "Requires an open area to place trees.", "Open 5x5 tree area", 1),
    SPRUCE("spruce", "Spruce Minion", Material.SPRUCE_LOG, Material.SPRUCE_LOG, MinionCategory.FORAGING, MinionActionKind.TREE, Block.SPRUCE_LOG, Block.SPRUCE_LOG, null, Block.SPRUCE_LEAVES, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MjA0NDk3MDAsInByb2ZpbGVJZCI6IjkxZjA0ZmU5MGYzNjQzYjU4ZjIwZTMzNzVmODZkMzllIiwicHJvZmlsZU5hbWUiOiJTdG9ybVN0b3JteSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2JhMDRiZmU1MTY5NTVmZDQzOTMyZGNiMzNiZDVlYWMyMGIzOGEyMzFkOWZhODQxNWIzZmIzMDFmNjBmNzM2MyJ9fX0=", new Color(82, 58, 35), "generating and chopping Spruce Logs!", "Requires an open area to place trees.", "Open 5x5 tree area", 1),

    CHICKEN("chicken", "Chicken Minion", Material.CHICKEN, Material.CHICKEN, MinionCategory.COMBAT, MinionActionKind.MOB, Block.HAY_BLOCK, null, null, null, EntityType.CHICKEN, "eyJ0aW1lc3RhbXAiOjE1NTc5MTkxNTgwNDEsInByb2ZpbGVJZCI6IjNmYzdmZGY5Mzk2MzRjNDE5MTE5OWJhM2Y3Y2MzZmVkIiwicHJvZmlsZU5hbWUiOiJZZWxlaGEiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2EwNGI3ZGExM2IwYTk3ODM5ODQ2YWE1NjQ4ZjVhYzY3MzZiYTBjYTlmYmYzOGNkMzY2OTE2ZTQxNzE1M2ZkN2YifX19", new Color(255, 255, 255), "generating and slaying Chickens!", "Requires open mob space nearby.", "Open mob space", 1),
    COW("cow", "Cow Minion", Material.BEEF, Material.BEEF, MinionCategory.COMBAT, MinionActionKind.MOB, Block.HAY_BLOCK, null, null, null, EntityType.COW, "eyJ0aW1lc3RhbXAiOjE1NTc5MTkzNDg2MTYsInByb2ZpbGVJZCI6IjgyYzYwNmM1YzY1MjRiNzk4YjkxYTEyZDNhNjE2OTc3IiwicHJvZmlsZU5hbWUiOiJOb3ROb3RvcmlvdXNOZW1vIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jMmZkODk3NmUxYjY0YWViZmQzOGFmYmU2MmFhMTQyOTkxNDI1M2RmMzQxN2FjZTFmNTg5ZTVjZjQ1ZmJkNzE3In19fQ==", new Color(110, 82, 58), "generating and slaying Cows!", "Requires open mob space nearby.", "Open mob space", 1),
    CREEPER("creeper", "Creeper Minion", Material.GUNPOWDER, Material.GUNPOWDER, MinionCategory.COMBAT, MinionActionKind.MOB, Block.MOSS_BLOCK, null, null, null, EntityType.CREEPER, "eyJ0aW1lc3RhbXAiOjE1NTc5MTk0MDIzMjgsInByb2ZpbGVJZCI6IjkxZmUxOTY4N2M5MDQ2NTZhYTFmYzA1OTg2ZGQzZmU3IiwicHJvZmlsZU5hbWUiOiJoaGphYnJpcyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTRhOTJjMmY4YzFiMzc3NGU4MDQ5MjIwMGQwYjIyMThkN2IwMTkzMTRhNzNjOWNiNWI5ZjA0Y2ZjYWNlYzQ3MSJ9fX0=", new Color(74, 162, 63), "generating and slaying Creepers!", "Requires open mob space nearby.", "Open mob space", 1),
    ENDERMAN("enderman", "Enderman Minion", Material.ENDER_PEARL, Material.ENDER_PEARL, MinionCategory.COMBAT, MinionActionKind.MOB, Block.END_STONE, null, null, null, EntityType.ENDERMAN, "eyJ0aW1lc3RhbXAiOjE1NTc5MTk1NDkxODMsInByb2ZpbGVJZCI6ImIwZDRiMjhiYzFkNzQ4ODlhZjBlODY2MWNlZTk2YWFiIiwicHJvZmlsZU5hbWUiOiJ4RmFpaUxlUiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTQ2MGQyMGJhMWU5Y2QxZDRjZmQ2ZDVmYjAxNzlmZjQxNTk3YWM2ZDI0NjFiZDdjY2RiNThiMjAyOTFlYzQ2ZSJ9fX0=", new Color(55, 45, 70), "generating and slaying Endermen!", "Requires open mob space nearby.", "Open mob space", 1),
    RABBIT("rabbit", "Rabbit Minion", Material.RABBIT, Material.RABBIT, MinionCategory.COMBAT, MinionActionKind.MOB, Block.GRASS_BLOCK, null, null, null, EntityType.RABBIT, "eyJ0aW1lc3RhbXAiOjE1NTc5MjAwNzAyMTIsInByb2ZpbGVJZCI6IjU2Njc1YjIyMzJmMDRlZTA4OTE3OWU5YzkyMDZjZmU4IiwicHJvZmlsZU5hbWUiOiJUaGVJbmRyYSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWY1OWMwNTJkMzM5YmI2MzA1Y2FkMzcwZmQ4YzUyZjU4MjY5YTk1N2RmYWY0MzNhMjU1NTk3ZDk1ZTY4YTM3MyJ9fX0=", new Color(213, 198, 177), "generating and slaying Rabbits!", "Requires open mob space nearby.", "Open mob space", 1),
    SHEEP("sheep", "Sheep Minion", Material.MUTTON, Material.MUTTON, MinionCategory.COMBAT, MinionActionKind.MOB, Block.WHITE_WOOL, null, null, null, EntityType.SHEEP, "eyJ0aW1lc3RhbXAiOjE1NTc5MjAyNTMwNzQsInByb2ZpbGVJZCI6IjkxZjA0ZmU5MGYzNjQzYjU4ZjIwZTMzNzVmODZkMzllIiwicHJvZmlsZU5hbWUiOiJTdG9ybVN0b3JteSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmQxNWQ0YjhiY2U3MDhmNzdmOTYzZjFiNGU4N2IxYjk2OWZlZjE3NjZhM2U5YjY3YjI0OWM1OWQ1ZTgwZThjNSJ9fX0=", new Color(240, 240, 240), "generating and slaying Sheep!", "Requires open mob space nearby.", "Open mob space", 1),
    SKELETON("skeleton", "Skeleton Minion", Material.BONE, Material.BONE, MinionCategory.COMBAT, MinionActionKind.MOB, Block.BONE_BLOCK, null, null, null, EntityType.SKELETON, "eyJ0aW1lc3RhbXAiOjE1NTc5MjAzMTgzOTIsInByb2ZpbGVJZCI6ImIwZDczMmZlMDBmNzQwN2U5ZTdmNzQ2MzAxY2Q5OGNhIiwicHJvZmlsZU5hbWUiOiJPUHBscyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmZlMDA5YzVjZmE0NGMwNWM4OGU1ZGYwNzBhZTI1MzNiZDY4MmE3MjhlMGIzM2JmYzkzZmQ5MmE2ZTVmM2Y2NCJ9fX0=", new Color(215, 215, 215), "generating and slaying Skeletons!", "Requires open mob space nearby.", "Open mob space", 1),
    SLIME("slime", "Slime Minion", Material.SLIME_BALL, Material.SLIME_BALL, MinionCategory.COMBAT, MinionActionKind.MOB, Block.SLIME_BLOCK, null, null, null, EntityType.SLIME, "eyJ0aW1lc3RhbXAiOjE1NTc5MjAzNDQ0NzcsInByb2ZpbGVJZCI6IjkxZmUxOTY4N2M5MDQ2NTZhYTFmYzA1OTg2ZGQzZmU3IiwicHJvZmlsZU5hbWUiOiJoaGphYnJpcyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzk1ZWNlZDg1ZGI2MmM5MjI3MjRlZmNhODA0ZWEwMDYwYzRhODdmY2RlZGYyZmQ1YzRmOWFjMTEzMGE2ZWIyNiJ9fX0=", new Color(81, 201, 101), "generating and slaying Slimes!", "Requires open mob space nearby.", "Open mob space", 1),
    SPIDER("spider", "Spider Minion", Material.STRING, Material.STRING, MinionCategory.COMBAT, MinionActionKind.MOB, Block.COBWEB, null, null, null, EntityType.SPIDER, "eyJ0aW1lc3RhbXAiOjE1NTc5MTkxMjk4MzgsInByb2ZpbGVJZCI6IjJkYzc3YWU3OTQ2MzQ4MDI5NDI4MGM4NDIyNzRiNTY3IiwicHJvZmlsZU5hbWUiOiJzYWR5MDYxMCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWQ4MTVkZjk3M2JjZDAxZWU4ZGZkYjNiZDc0ZjBiN2NiOGZlZjJhNzA1NTllNGZhYTU5MDUxMjdiYmI0YTQzNSJ9fX0=", new Color(55, 33, 33), "generating and slaying Spiders!", "Requires open mob space nearby.", "Open mob space", 1),
    ZOMBIE("zombie", "Zombie Minion", Material.ROTTEN_FLESH, Material.ROTTEN_FLESH, MinionCategory.COMBAT, MinionActionKind.MOB, Block.COBBLESTONE, null, null, null, EntityType.ZOMBIE, "eyJ0aW1lc3RhbXAiOjE1NTc5MjA1OTkxMzMsInByb2ZpbGVJZCI6IjkxZjA0ZmU5MGYzNjQzYjU4ZjIwZTMzNzVmODZkMzllIiwicHJvZmlsZU5hbWUiOiJTdG9ybVN0b3JteSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTk2MDYzYTg4NGQzOTAxYzQxZjM1YjY5YThjOWY0MDFjNjFhYzlmNjMzMGY5NjRmODBjMzUzNTJjM2U4YmZiMCJ9fX0=", new Color(119, 170, 88), "generating and slaying Zombies!", "Requires open mob space nearby.", "Open mob space", 1),
    CAVE_SPIDER("cavespider", "Cave Spider Minion", Material.SPIDER_EYE, Material.SPIDER_EYE, MinionCategory.COMBAT, MinionActionKind.MOB, Block.COBWEB, null, null, null, EntityType.CAVE_SPIDER, "eyJ0aW1lc3RhbXAiOjE1NTc5MTkxMjk4MzgsInByb2ZpbGVJZCI6IjJkYzc3YWU3OTQ2MzQ4MDI5NDI4MGM4NDIyNzRiNTY3IiwicHJvZmlsZU5hbWUiOiJzYWR5MDYxMCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWQ4MTVkZjk3M2JjZDAxZWU4ZGZkYjNiZDc0ZjBiN2NiOGZlZjJhNzA1NTllNGZhYTU5MDUxMjdiYmI0YTQzNSJ9fX0=", new Color(44, 84, 88), "generating and slaying Cave Spiders!", "Requires open mob space nearby.", "Open mob space", 1),
    REVENANT("revenant", "Revenant Minion", Material.ROTTEN_FLESH, Material.ROTTEN_FLESH, MinionCategory.COMBAT, MinionActionKind.MOB, Block.COBBLESTONE, null, null, null, EntityType.ZOMBIE, "eyJ0aW1lc3RhbXAiOjE1Njg1MzgxNzcxMTMsInByb2ZpbGVJZCI6IjQxZDNhYmMyZDc0OTQwMGM5MDkwZDU0MzRkMDM4MzFiIiwicHJvZmlsZU5hbWUiOiJNZWdha2xvb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2EzZGNlODU1NTkyMzU1OGQ4ZDc0YzJhMmIyNjFiMmIyZDYzMDU1OWRiNTRlZjk3ZWQzZjljMzBlOWEyMGFiYSJ9fX0=", new Color(116, 173, 88), "generating and slaying Revenants!", "Requires open mob space nearby.", "Open mob space", 1),
    TARANTULA("tarantula", "Tarantula Minion", Material.STRING, Material.STRING, MinionCategory.COMBAT, MinionActionKind.MOB, Block.COBWEB, null, null, null, EntityType.SPIDER, "eyJ0aW1lc3RhbXAiOjE1NjkyNDU3NDI1ODAsInByb2ZpbGVJZCI6IjQxZDNhYmMyZDc0OTQwMGM5MDkwZDU0MzRkMDM4MzFiIiwicHJvZmlsZU5hbWUiOiJNZWdha2xvb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzk3ZTg2MDA3MDY0YzljZTI2ZWI0YmFkOGFjOWFhMzBhYWMzMDllNzBhOWUwYjYxNTkzNjMxOGRlYTQwYTcyMSJ9fX0=", new Color(80, 28, 40), "generating and slaying Tarantulas!", "Requires open mob space nearby.", "Open mob space", 1),

    FISHING("fishing", "Fishing Minion", Material.FISHING_ROD, Material.COD, MinionCategory.FISHING, MinionActionKind.FISHING, Block.OAK_PLANKS, null, Block.WATER, null, null, "eyJ0aW1lc3RhbXAiOjE1NTc5MTk1Njk3MzYsInByb2ZpbGVJZCI6IjNmYzdmZGY5Mzk2MzRjNDE5MTE5OWJhM2Y3Y2MzZmVkIiwicHJvZmlsZU5hbWUiOiJZZWxlaGEiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzUzZWEwZmQ4OTUyNGRiM2Q3YTM1NDQ5MDQ5MzM4MzBiNGZjODg5OWVmNjBjMTEzZDk0OGJiM2M0ZmU3YWFiYjEifX19", new Color(70, 146, 201), "fishing up items for you!", "Works best next to water.", "Water nearby", 2);

    private static final int[] UPGRADE_COST_BY_TIER = {0, 80, 160, 320, 512, 640, 768, 896, 1024, 1280, 1536, 2048};

    private final String id;
    private final String displayName;
    private final Material icon;
    private final Material outputMaterial;
    private final MinionCategory category;
    private final MinionActionKind actionKind;
    private final Block baseBlock;
    private final Block generatedBlock;
    private final Block idealLayoutBlock;
    private final Block secondaryGeneratedBlock;
    private final EntityType mobEntityType;
    private final String texture;
    private final Color armorColor;
    private final String placementDescription;
    private final String layoutHint;
    private final String idealLayoutText;
    private final int tierOneStorageSlots;

    MinionType(String id, String displayName, Material icon, Material outputMaterial, MinionCategory category,
               MinionActionKind actionKind, Block baseBlock, Block generatedBlock, Block idealLayoutBlock,
               Block secondaryGeneratedBlock, EntityType mobEntityType, String texture, Color armorColor,
               String placementDescription, String layoutHint, String idealLayoutText, int tierOneStorageSlots) {
        this.id = id;
        this.displayName = displayName;
        this.icon = icon;
        this.outputMaterial = outputMaterial;
        this.category = category;
        this.actionKind = actionKind;
        this.baseBlock = baseBlock;
        this.generatedBlock = generatedBlock;
        this.idealLayoutBlock = idealLayoutBlock;
        this.secondaryGeneratedBlock = secondaryGeneratedBlock;
        this.mobEntityType = mobEntityType;
        this.texture = texture;
        this.armorColor = armorColor;
        this.placementDescription = placementDescription;
        this.layoutHint = layoutHint;
        this.idealLayoutText = idealLayoutText;
        this.tierOneStorageSlots = tierOneStorageSlots;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getIcon() {
        return icon;
    }

    public Material getOutputMaterial() {
        return outputMaterial;
    }

    public MinionCategory getCategory() {
        return category;
    }

    public MinionActionKind getActionKind() {
        return actionKind;
    }

    public Block getBaseBlock() {
        return baseBlock;
    }

    public Block getGeneratedBlock() {
        return generatedBlock;
    }

    public Block getIdealLayoutBlock() {
        return idealLayoutBlock;
    }

    public Block getSecondaryGeneratedBlock() {
        return secondaryGeneratedBlock;
    }

    public EntityType getMobEntityType() {
        return mobEntityType;
    }

    public String getTexture() {
        return texture;
    }

    public Color getArmorColor() {
        return armorColor;
    }

    public String getPlacementDescription() {
        return placementDescription;
    }

    public String getLayoutHint() {
        return layoutHint;
    }

    public String getIdealLayout() {
        return idealLayoutText;
    }

    public MinionTier getTierData(int tier) {
        int normalized = Math.max(1, Math.min(12, tier));
        int storageSlots = normalized == 1 ? tierOneStorageSlots : category.getStorageSlots(normalized);
        return new MinionTier(normalized, category.getDelaySeconds(normalized), storageSlots);
    }

    public int getActionDelaySeconds(int tier) {
        return getTierData(tier).actionDelaySeconds();
    }

    public int getStorageSlots(int tier) {
        return getTierData(tier).storageSlots();
    }

    public int getMaxStorage(int tier) {
        return getTierData(tier).maxStorage();
    }

    public boolean hasNextTier(int tier) {
        return tier < 12;
    }

    public int getUpgradeCost(int currentTier) {
        if (currentTier >= 12) {
            return -1;
        }
        return UPGRADE_COST_BY_TIER[Math.max(1, Math.min(UPGRADE_COST_BY_TIER.length - 1, currentTier))];
    }

    public List<ItemStack> createHarvestDrops() {
        return switch (this) {
            case WHEAT -> List.of(ItemStack.of(Material.WHEAT, 2), ItemStack.of(Material.WHEAT_SEEDS, 1));
            case MELON -> List.of(ItemStack.of(Material.MELON_SLICE, 3));
            case PUMPKIN -> List.of(ItemStack.of(Material.PUMPKIN, 1));
            case CARROT -> List.of(ItemStack.of(Material.CARROT, 2));
            case POTATO -> List.of(ItemStack.of(Material.POTATO, 2));
            case SUGARCANE -> List.of(ItemStack.of(Material.SUGAR_CANE, 2));
            case CACTUS -> List.of(ItemStack.of(Material.CACTUS, 1));
            case COCOA -> List.of(ItemStack.of(Material.COCOA_BEANS, 3));
            default -> List.of(ItemStack.of(outputMaterial, 1));
        };
    }

    public Material getHeldTool(int tier) {
        return switch (actionKind) {
            case MINING -> {
                if (tier >= 11) {
                    yield Material.DIAMOND_PICKAXE;
                }
                if (tier >= 9) {
                    yield Material.GOLDEN_PICKAXE;
                }
                if (tier >= 6) {
                    yield Material.IRON_PICKAXE;
                }
                if (tier >= 3) {
                    yield Material.STONE_PICKAXE;
                }
                yield Material.WOODEN_PICKAXE;
            }
            case CROP -> {
                if (tier >= 11) {
                    yield Material.DIAMOND_HOE;
                }
                if (tier >= 9) {
                    yield Material.GOLDEN_HOE;
                }
                if (tier >= 6) {
                    yield Material.IRON_HOE;
                }
                if (tier >= 3) {
                    yield Material.STONE_HOE;
                }
                yield Material.WOODEN_HOE;
            }
            case TREE -> {
                if (tier >= 11) {
                    yield Material.DIAMOND_AXE;
                }
                if (tier >= 9) {
                    yield Material.GOLDEN_AXE;
                }
                if (tier >= 6) {
                    yield Material.IRON_AXE;
                }
                if (tier >= 3) {
                    yield Material.STONE_AXE;
                }
                yield Material.WOODEN_AXE;
            }
            case MOB -> {
                if (tier >= 11) {
                    yield Material.DIAMOND_SWORD;
                }
                if (tier >= 9) {
                    yield Material.GOLDEN_SWORD;
                }
                if (tier >= 6) {
                    yield Material.IRON_SWORD;
                }
                if (tier >= 3) {
                    yield Material.STONE_SWORD;
                }
                yield Material.WOODEN_SWORD;
            }
            case FISHING -> Material.FISHING_ROD;
        };
    }

    public static MinionType fromId(String id) {
        for (MinionType type : values()) {
            if (type.id.equalsIgnoreCase(id)) {
                return type;
            }
        }
        return null;
    }
}
