package fun.ascent.skyblock.hotm;

import java.util.HashMap;
import java.util.Map;

public class HotmData {
    public int level = 0;
    public int xp = 0;
    public int tokens = 0;
    public String activeAbilityId = null;
    public Map<String, Integer> upgradeLevels = new HashMap<>();
    public Map<String, Boolean> upgradeEnabled = new HashMap<>();
    public Map<String, Integer> powder = new HashMap<>();

    public void postLoad() {
        if (upgradeLevels == null) upgradeLevels = new HashMap<>();
        if (upgradeEnabled == null) upgradeEnabled = new HashMap<>();
        if (powder == null) powder = new HashMap<>();
    }
}
