package fun.ascent.skyblock.data;

import fun.ascent.skyblock.data.impl.*;
import fun.ascent.skyblock.hotm.HotmData;
import fun.ascent.skyblock.player.bestiary.BestiaryProgress;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.level.SkyblockLevel;
import fun.ascent.skyblock.player.skill.SkillType;
import fun.ascent.skyblock.player.stats.StatBuilder;
import fun.ascent.skyblock.player.stats.Stats;
import fun.ascent.skyblock.quest.QuestData;
import lombok.Getter;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.item.ItemStack;
import org.bson.Document;
import fun.ascent.skyblock.player.profiles.SkyblockProfile.BankTransaction;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SkyblockDataHandler {
    @Getter protected UUID playerUuid;
    @Getter protected UUID profileId;
    protected final Map<String, Datapoint<?>> datapoints = new ConcurrentHashMap<>();

    public SkyblockDataHandler(UUID playerUuid, UUID profileId) {
        this.playerUuid = playerUuid;
        this.profileId = profileId;
        initDefaults();
    }

    private void initDefaults() {
        for (Data data : Data.values()) {
            datapoints.put(data.getKey(), data.getDefaultDatapoint().deepClone());
        }
    }

    public void load(Document document) {
        if (document == null) return;
        for (Data data : Data.values()) {
            String key = data.getKey();
            if (document.containsKey(key)) {
                Datapoint<?> dp = datapoints.get(key);
                String serialized = document.getString(key);
                if (serialized != null) {
                    dp.deserialize(serialized);
                }
            }
        }
    }

    public Document save() {
        Document doc = new Document();
        doc.put("_owner", playerUuid.toString());
        doc.put("_id", profileId.toString());
        for (Data data : Data.values()) {
            Datapoint<?> dp = datapoints.get(data.getKey());
            if (dp != null) {
                doc.put(data.getKey(), dp.serialize());
            }
        }
        return doc;
    }

    public Datapoint<?> get(Data data) {
        return datapoints.get(data.getKey());
    }

    public <T extends Datapoint<?>> T get(Data data, Class<T> clazz) {
        return clazz.cast(datapoints.get(data.getKey()));
    }

    /**
     * Syncs data from the live player to the datapoints (onQuit)
     */
    public void syncFromPlayer(SkyblockPlayer player) {
        for (Data data : Data.values()) {
            if (data.onQuit != null) {
                Datapoint<?> target = get(data);
                Datapoint<?> produced = data.onQuit.apply(player);
                if (produced != null) {
                    // Set value without triggering onChange if we had it
                    ((Datapoint<Object>) target).setValue(produced.getValue());
                }
            }
        }
    }

    /**
     * Syncs data from the datapoints to the live player (onLoad)
     */
    public void syncToPlayer(SkyblockPlayer player) {
        for (Data data : Data.values()) {
            if (data.onLoad != null) {
                data.onLoad.accept(player, get(data));
            }
        }
    }

    public enum Data {
        PROFILE_NAME("profile_name", DatapointString.class, new DatapointString("profile_name", "Apple")),
        
        COINS("coins", DatapointDouble.class, new DatapointDouble("coins", 0.0),
            (player, dp) -> player.setCoins((Double) dp.getValue()),
            (player) -> new DatapointDouble("coins", player.getCoins())),
            
        BITS("bits", DatapointDouble.class, new DatapointDouble("bits", 0.0),
            (player, dp) -> player.setBits((Double) dp.getValue()),
            (player) -> new DatapointDouble("bits", player.getBits())),

        GOLD("gold", DatapointDouble.class, new DatapointDouble("gold", 0.0),
            (player, dp) -> player.setGold((Double) dp.getValue()),
            (player) -> new DatapointDouble("gold", player.getGold())),

        STATS("stats", DatapointMapDouble.class, new DatapointMapDouble("stats", new HashMap<>()),
            (player, dp) -> {
                Map<String, Double> map = (Map<String, Double>) dp.getValue();
                if (player.getActiveProfileData() == null || map == null) return;
                for (Map.Entry<String, Double> entry : map.entrySet()) {
                    try {
                        Stats statType = Stats.valueOf(entry.getKey().toUpperCase(Locale.ROOT));
                        var stat = StatBuilder.build(statType);
                        stat.setMaxValue(entry.getValue());
                        stat.setCurValue(entry.getValue());
                        player.getActiveProfileData().stats.put(stat.id, stat);
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            },
            (player) -> {
                if (player.getActiveProfileData() == null) return null;
                Map<String, Double> map = new HashMap<>();
                player.getActiveProfileData().stats.forEach((id, stat) -> map.put(id, stat.getCurValue()));
                return new DatapointMapDouble("stats", map);
            }),

        SKILLS("skills", DatapointMapDouble.class, new DatapointMapDouble("skills", new HashMap<>()),
            (player, dp) -> {
                Map<String, Double> map = (Map<String, Double>) dp.getValue();
                var skillData = player.getSkillData();
                if (skillData == null) return;
                for (SkillType type : SkillType.values()) {
                    if (map.containsKey(type.name())) {
                        skillData.setRawXp(type, map.get(type.name()));
                    }
                }
            },
            (player) -> {
                var skillData = player.getSkillData();
                if (skillData == null) return null;
                Map<String, Double> map = new HashMap<>();
                for (SkillType type : SkillType.values()) {
                    map.put(type.name(), skillData.getRawXp(type));
                }
                return new DatapointMapDouble("skills", map);
            }),

        LEVEL("level", DatapointObject.class, new DatapointObject<>("level", new SkyblockLevel(), SkyblockLevel.class),
            (player, dp) -> player.setSkyblockLevel((SkyblockLevel) dp.getValue()),
            (player) -> {
                var level = player.getSkyblockLevel();
                if (level == null) return null;
                return new DatapointObject<>("level", level, SkyblockLevel.class);
            }),

        HOTM("hotm", DatapointObject.class, new DatapointObject<>("hotm", new HotmData(), HotmData.class),
            (player, dp) -> player.setSkyblockHotmData((HotmData) dp.getValue()),
            (player) -> {
                var hotm = player.getSkyblockHotmData();
                if (hotm == null) return null;
                return new DatapointObject<>("hotm", hotm, HotmData.class);
            }),

        BESTIARY("bestiary", DatapointObject.class, new DatapointObject<>("bestiary", new BestiaryProgress(), BestiaryProgress.class),
            (player, dp) -> {
                if (player.getActiveProfileData() == null) return;
                BestiaryProgress progress = (BestiaryProgress) dp.getValue();
                player.getActiveProfileData().bestiaryProgress = progress != null ? progress : new BestiaryProgress();
            },
            (player) -> {
                if (player.getActiveProfileData() == null) return null;
                return new DatapointObject<>("bestiary", player.getActiveProfileData().getBestiaryProgress(), BestiaryProgress.class);
            }),

        INVENTORY("inventory", DatapointInventory.class, new DatapointInventory("inventory", new ArrayList<>()),
            (player, dp) -> {
                List<ItemStack> items = (List<ItemStack>) dp.getValue();
                for (int i = 0; i < items.size(); i++) {
                    player.getInventory().setItemStack(i, items.get(i));
                }
            },
            (player) -> new DatapointInventory("inventory", Arrays.asList(player.getInventory().getItemStacks()))),

        ARMOR("armor", DatapointInventory.class, new DatapointInventory("armor", new ArrayList<>()),
            (player, dp) -> {
                List<ItemStack> items = (List<ItemStack>) dp.getValue();
                if (items.size() >= 4) {
                    player.setEquipment(EquipmentSlot.BOOTS, items.get(0));
                    player.setEquipment(EquipmentSlot.LEGGINGS, items.get(1));
                    player.setEquipment(EquipmentSlot.CHESTPLATE, items.get(2));
                    player.setEquipment(EquipmentSlot.HELMET, items.get(3));
                }
            },
            (player) -> new DatapointInventory("armor", List.of(
                player.getEquipment(EquipmentSlot.BOOTS),
                player.getEquipment(EquipmentSlot.LEGGINGS),
                player.getEquipment(EquipmentSlot.CHESTPLATE),
                player.getEquipment(EquipmentSlot.HELMET)
            ))),

        VISITED_REGIONS("visited_regions", DatapointStringList.class, new DatapointStringList("visited_regions", new ArrayList<>())),

        QUESTS("quests", DatapointObject.class, new DatapointObject<>("quests", new QuestData(), QuestData.class),
            (player, dp) -> {
                QuestData qd = (QuestData) dp.getValue();
                if (qd == null) qd = new QuestData();
                qd.setSkyblockPlayer(player);
                if (player.getActiveProfileData() != null) {
                    player.getActiveProfileData().questData = qd;
                }
            },
            (player) -> {
                if (player.getActiveProfileData() == null) return null;
                QuestData qd = player.getActiveProfileData().getQuestData();
                return new DatapointObject<>("quests", qd, QuestData.class);
            }),

        DROP_ALERTS_DISABLED("drop_alerts_disabled", DatapointBoolean.class, new DatapointBoolean("drop_alerts_disabled", false),
            (player, dp) -> player.setDropAlertsDisabled(dp.getValue() != null && (Boolean) dp.getValue()),
            (player) -> new DatapointBoolean("drop_alerts_disabled", player.hasDropAlertsDisabled())),

        BANK_DATA("bank_data", DatapointBankData.class, new DatapointBankData("bank_data", new BankData()),
            (player, dp) -> {
                BankData bd = (BankData) dp.getValue();
                if (bd == null) bd = new BankData();
                if (player.getActiveProfile() != null) {
                    player.getActiveProfile().bankCoins = bd.getAmount();
                    player.getActiveProfile().bankLimit = bd.getBalanceLimit();
                    player.getActiveProfile().lastClaimedInterestMonth = bd.getLastClaimedInterest();
                    
                    player.getActiveProfile().bankTransactions.clear();
                    for (BankData.Transaction tx : bd.getTransactions()) {
                        player.getActiveProfile().bankTransactions.add(new BankTransaction(
                            tx.timestamp, tx.amount, tx.originator, tx.type
                        ));
                    }
                }
            },
            (player) -> {
                if (player.getActiveProfile() == null) return null;
                List<BankData.Transaction> txs = new ArrayList<>();
                for (BankTransaction tx : player.getActiveProfile().bankTransactions) {
                    txs.add(new BankData.Transaction(tx.timestamp(), tx.amount(), tx.originator(), tx.type()));
                }
                BankData bd = new BankData(
                    player.getActiveProfile().lastClaimedInterestMonth,
                    txs,
                    player.getActiveProfile().bankCoins,
                    player.getActiveProfile().bankLimit
                );
                return new DatapointBankData("bank_data", bd);
            }),

        BOOSTER_COOKIE_EXPIRES("booster_cookie_expires", DatapointLong.class, new DatapointLong("booster_cookie_expires", 0L),
            (player, dp) -> {
                if (player.getActiveProfileData() != null) {
                    player.getActiveProfileData().boosterCookieExpires = (Long) dp.getValue();
                }
            },
            (player) -> {
                if (player.getActiveProfileData() == null) return null;
                return new DatapointLong("booster_cookie_expires", player.getActiveProfileData().boosterCookieExpires);
            }),

        CLAIMED_FREE_COOKIE("claimed_free_cookie", DatapointBoolean.class, new DatapointBoolean("claimed_free_cookie", false),
            (player, dp) -> {
                if (player.getActiveProfileData() != null) {
                    player.getActiveProfileData().claimedFreeCookie = (Boolean) dp.getValue();
                }
            },
            (player) -> {
                if (player.getActiveProfileData() == null) return null;
                return new DatapointBoolean("claimed_free_cookie", player.getActiveProfileData().claimedFreeCookie);
            }),


        BITS_MULTIPLIER("bits_multiplier", DatapointDouble.class, new DatapointDouble("bits_multiplier", 1.0),
            (player, dp) -> {
                if (player.getActiveProfileData() != null) {
                    player.getActiveProfileData().bitsMultiplier = (Double) dp.getValue();
                }
            },
            (player) -> {
                if (player.getActiveProfileData() == null) return null;
                return new DatapointDouble("bits_multiplier", player.getActiveProfileData().bitsMultiplier);
            }),

        COOKIE_BITS("cookie_bits", DatapointDouble.class, new DatapointDouble("cookie_bits", 0.0),
            (player, dp) -> {
                if (player.getActiveProfileData() != null) {
                    player.getActiveProfileData().cookieBits = (Double) dp.getValue();
                }
            },
            (player) -> {
                if (player.getActiveProfileData() == null) return null;
                return new DatapointDouble("cookie_bits", player.getActiveProfileData().cookieBits);
            }),

        BITS_TIMER_SECONDS("bits_timer_seconds", DatapointLong.class, new DatapointLong("bits_timer_seconds", 0L),
            (player, dp) -> {
                if (player.getActiveProfileData() != null) {
                    player.getActiveProfileData().bitsTimerSeconds = dp.getValue() != null ? ((Long) dp.getValue()).intValue() : 0;
                }
            },
            (player) -> {
                if (player.getActiveProfileData() == null) return null;
                return new DatapointLong("bits_timer_seconds", (long) player.getActiveProfileData().bitsTimerSeconds);
            }),
        ;

        @Getter private final String key;
        @Getter
        private final Class<?> type;
        @Getter private final Datapoint<?> defaultDatapoint;
        
        public BiConsumer<SkyblockPlayer, Datapoint<?>> onLoad;
        public Function<SkyblockPlayer, Datapoint<?>> onQuit;

        Data(String key, Class<?> type, Datapoint<?> defaultDatapoint) {
            this.key = key;
            this.type = type;
            this.defaultDatapoint = defaultDatapoint;
        }

        Data(String key, Class<?> type, Datapoint<?> defaultDatapoint, 
             BiConsumer<SkyblockPlayer, Datapoint<?>> onLoad, Function<SkyblockPlayer, Datapoint<?>> onQuit) {
            this.key = key;
            this.type = type;
            this.defaultDatapoint = defaultDatapoint;
            this.onLoad = onLoad;
            this.onQuit = onQuit;
        }
    }
}
