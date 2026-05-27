package fun.ascent.skyblock.player.level;

import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.common.util.Progress;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.LinkedHashMap;
import java.util.Map;

public class SkyblockLevel {

    public int xp = 0;
    public int curLevel = 0;
    public Progress progress = new Progress(0, 100);

    public int getXp() {
        if (xp == 0 && (curLevel > 0 || progress.curProgress > 0)) {
            xp = curLevel * 100 + progress.curProgress;
        }
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
        this.curLevel = xp / 100;
        this.progress.curProgress = xp % 100;
    }

    public int getLevel() {
        return curLevel;
    }

    public void setLevel(int level) {
        this.curLevel = level;
        this.xp = level * 100 + this.progress.curProgress;
    }

    public void addXP(int amount) {
        setXp(getXp() + amount);
    }

    public void addExp(int amount) {
        addXP(amount);
    }

    public boolean removeXP(int xpAmount) {
        if (getXp() - xpAmount >= 0) {
            setXp(getXp() - xpAmount);
            return true;
        }
        return false;
    }

    public void addLevel(int levelAmount) {
        setLevel(getLevel() + levelAmount);
    }

    public boolean removeLevel(int levelAmount) {
        if (getLevel() - levelAmount >= 0) {
            setLevel(getLevel() - levelAmount);
            return true;
        }
        return false;
    }

    public static String getLevelColour(int level) {
        if (level >= 480) return "<dark_red>";
        if (level >= 440) return "<red>";
        if (level >= 400) return "<gold>";
        if (level >= 360) return "<dark_purple>";
        if (level >= 320) return "<light_purple>";
        if (level >= 280) return "<blue>";
        if (level >= 240) return "<dark_aqua>";
        if (level >= 200) return "<aqua>";
        if (level >= 160) return "<dark_green>";
        if (level >= 120) return "<green>";
        if (level >= 80)  return "<yellow>";
        if (level >= 40)  return "<white>";
        return "<gray>";
    }

    public static Map<String, ItemStack> getRewards(int oldLevel, int curLevel) {
        Map<String, ItemStack> rewards = new LinkedHashMap<>();
        for (int level = oldLevel; level <= curLevel; level++) {

            switch (level) {
                case 3:
                    rewards.put("<aqua>Access To Community Shop", ItemStackCreator.getStackHead("4e495b103ddd47701449ad7a34d908e8d2f08e0bd9476653d433c3bfc7c1b055").build());
                    break;

                case 5:
                    rewards.put("<dark_green>Access To the Garden", ItemStackCreator.getStackHead("4778b434a258f7991825cabc965a56403c4d772e9628ce60164927e94b79d17").build());
                    rewards.put("<green>Access To Wardrobe", ItemStackCreator.enchant(ItemStackCreator.createNamedItemStack(Material.LEATHER_CHESTPLATE)).build());
                    break;

                case 6:
                    rewards.put("<green>Auto-pickup block and mob drops", ItemStack.of(Material.DIAMOND_SWORD));
                    break;

                case 7:
                    rewards.put("<gold>Access To Bazaar", ItemStackCreator.getStackHead("c232e3820897429157619b0ee099fec0628f602fff12b695de54aef11d923ad7").build());
                    break;

                case 12:
                    rewards.put("<light_purple>Access To Wizard Portal", ItemStackCreator.getStackHead("838564e28aba98301dbda5fafd86d1da4e2eaeef12ea94dcf440b883e559311c").build());
                    break;

                case 25:
                    rewards.put("<gold>5B Auction House Bid Limit", ItemStack.of(Material.GOLDEN_HORSE_ARMOR));
                    break;

                case 40:
                    rewards.put("<white>White Level Prefix", null);
                    break;

                case 50:
                    rewards.put("<gold>10B Auction House Bid Limit", ItemStack.of(Material.GOLDEN_HORSE_ARMOR));
                    rewards.put("<green>Daily Coins Trading Limit of 1B", ItemStackCreator.getStackHead("740d6e362bc7eee4f911dbd0446307e7458d1050d09aee538ebcb0273cf75742").build());
                    break;

                case 80:
                    rewards.put("<yellow>Yellow Level Prefix", null);
                    break;

                case 100:
                    rewards.put("<gold>25B Auction House Bid Limit", ItemStack.of(Material.GOLDEN_HORSE_ARMOR));
                    rewards.put("<green>Daily Coins Trading Limit of 10B", ItemStackCreator.getStackHead("c43f12c8369f9c3888a45aaf6d7761578402b4241958f7d4ae4eceb56a867d2a").build());
                    break;

                case 120:
                    rewards.put("<green>Green Level Prefix", null);
                    break;

                case 160:
                    rewards.put("<dark_green>Dark Green Level Prefix", null);
                    break;

                case 200:
                    rewards.put("<aqua>Aqua Level Prefix", null);
                    rewards.put("<gold>50B Auction House Bid Limit", ItemStack.of(Material.GOLDEN_HORSE_ARMOR));
                    break;

                case 240:
                    rewards.put("<dark_aqua>Cyan Level Prefix", null);
                    break;

                case 280:
                    rewards.put("<blue>Blue Level Prefix", null);
                    break;

                case 320:
                    rewards.put("<light_purple>Pink Level Prefix", null);
                    break;

                case 360:
                    rewards.put("<dark_purple>Purple Level Prefix", null);
                    break;

                case 400:
                    rewards.put("<gold>Gold Level Prefix", null);
                    break;

                case 440:
                    rewards.put("<red>Red Level Prefix", null);
                    break;

                case 480:
                    rewards.put("<dark_red>Dark Red Level Prefix", null);
                    break;
            }
        }
        return rewards;
    }

    public static int getHealthReward(int oldLevel, int curLevel) {
        int diff = curLevel - oldLevel;
        return (diff * 5);
    }

    public static int getStrengthReward(int oldLevel, int curLevel) {
        return (curLevel / 5) - (oldLevel / 5);
    }
}
