package fun.ascent.skyblock.data;

import fun.ascent.skyblock.data.impl.*;
import fun.ascent.skyblock.hotm.HotmData;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.player.level.SkyblockLevel;
import fun.ascent.skyblock.player.skill.SkillType;
import lombok.Getter;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.item.ItemStack;
import org.bson.Document;

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

        ENDERCHEST("enderchest", DatapointInventory.class, new DatapointInventory("enderchest", new ArrayList<>()),
            (player, dp) -> {
                // TODO: Load enderchest items when system is implemented
            },
            (player) -> {
                // TODO: Save enderchest items
                return new DatapointInventory("enderchest", new ArrayList<>());
            }),

        VISITED_REGIONS("visited_regions", DatapointStringList.class, new DatapointStringList("visited_regions", new ArrayList<>())),
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
