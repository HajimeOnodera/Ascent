package fun.ascent.skyblock.player.level;

import fun.ascent.skyblock.utility.ItemBuilder;
import fun.ascent.skyblock.utility.Progress;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.LinkedHashMap;
import java.util.Map;

public class SkyblockLevel {

    public int curLevel = 0;
    public Progress progress = new Progress(0,100);

    public boolean addExp(int amount){
        if(progress.add(amount)){
            curLevel++;
            return true;
        }
        return false;
    }

    public static String getLevelColour(int level) {
        if (level >= 480) return "§4";
        if (level >= 440) return "§c";
        if (level >= 400) return "§6";
        if (level >= 360) return "§5";
        if (level >= 320) return "§d";
        if (level >= 280) return "§9";
        if (level >= 240) return "§3";
        if (level >= 200) return "§b";
        if (level >= 160) return "§2";
        if (level >= 120) return "§a";
        if (level >= 80)  return "§e";
        if (level >= 40)  return "§f";
        return "§7";
    }

    public static Map<String, ItemStack> getRewards(int oldLevel,int curLevel) {
        Map<String, ItemStack> rewards = new LinkedHashMap<>();
        for(int level = oldLevel; level <= curLevel; level++) {

            switch (level) {
                case 3:
                    rewards.put("§bAccess To Community Shop", ItemBuilder.getHead("4e495b103ddd47701449ad7a34d908e8d2f08e0bd9476653d433c3bfc7c1b055"));
                    break;

                case 5:
                    rewards.put("§2Access To the Garden", ItemBuilder.getHead("4778b434a258f7991825cabc965a56403c4d772e9628ce60164927e94b79d17"));
                    rewards.put("§aAccess To Wardrobe", new ItemBuilder(Material.LEATHER_CHESTPLATE).setGlowing(true).build());
                    break;

                case 6:
                    rewards.put("§aAuto-pickup block and mob drops", ItemStack.of(Material.DIAMOND_SWORD));
                    break;

                case 7:
                    rewards.put("§6Access To Bazaar", ItemBuilder.getHead("c232e3820897429157619b0ee099fec0628f602fff12b695de54aef11d923ad7"));
                    break;

                case 12:
                    rewards.put("§dAccess To Wizard Portal", ItemBuilder.getHead("838564e28aba98301dbda5fafd86d1da4e2eaeef12ea94dcf440b883e559311c"));
                    break;

                case 25:
                    rewards.put("§65B Auction House Bid Limit", ItemStack.of(Material.GOLDEN_HORSE_ARMOR));
                    break;

                case 40:
                    rewards.put("§fWhite Level Prefix", null);
                    break;

                case 50:
                    rewards.put("§610B Auction House Bid Limit", ItemStack.of(Material.GOLDEN_HORSE_ARMOR));
                    rewards.put("§aDaily Coins Trading Limit of 1B", ItemBuilder.getHead("740d6e362bc7eee4f911dbd0446307e7458d1050d09aee538ebcb0273cf75742"));
                    break;

                case 80:
                    rewards.put("§eYellow Level Prefix", null);
                    break;

                case 100:
                    rewards.put("§625B Auction House Bid Limit", ItemStack.of(Material.GOLDEN_HORSE_ARMOR));
                    rewards.put("§aDaily Coins Trading Limit of 10B", ItemBuilder.getHead("c43f12c8369f9c3888a45aaf6d7761578402b4241958f7d4ae4eceb56a867d2a"));
                    break;

                case 120:
                    rewards.put("§aGreen Level Prefix", null);
                    break;

                case 160:
                    rewards.put("§2Dark Green Level Prefix", null);
                    break;

                case 200:
                    rewards.put("§bAqua Level Prefix", null);
                    rewards.put("§650B Auction House Bid Limit", ItemStack.of(Material.GOLDEN_HORSE_ARMOR));
                    break;

                case 240:
                    rewards.put("§3Cyan Level Prefix", null);
                    break;

                case 280:
                    rewards.put("§9Blue Level Prefix", null);
                    break;

                case 320:
                    rewards.put("§dPink Level Prefix", null);
                    break;

                case 360:
                    rewards.put("§5Purple Level Prefix", null);
                    break;

                case 400:
                    rewards.put("§6Gold Level Prefix", null);
                    break;

                case 440:
                    rewards.put("§cRed Level Prefix", null);
                    break;

                case 480:
                    rewards.put("§4Dark Red Level Prefix", null);
                    break;
            }
        }
        return rewards;
    }

    public static int getHealthReward(int oldLevel,int curLevel) {
        int diff = curLevel - oldLevel;
        return (diff * 5);
    }

    public static int getStrengthReward(int oldLevel, int curLevel) {
        return (curLevel / 5) - (oldLevel / 5);
    }
}
