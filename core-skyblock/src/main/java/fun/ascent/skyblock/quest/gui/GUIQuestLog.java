package fun.ascent.skyblock.quest.gui;

import fun.ascent.common.gui.InventoryGUI;
import fun.ascent.common.item.GUIClickableItem;
import fun.ascent.common.item.ItemStackCreator;
import fun.ascent.skyblock.player.SkyblockPlayer;
import fun.ascent.skyblock.quest.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.text.SimpleDateFormat;
import java.util.*;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public class GUIQuestLog extends InventoryGUI {
    private static final int[] QUEST_SLOTS = {
            11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    private final boolean showCompleted;

    public GUIQuestLog() {
        this(false);
    }

    public GUIQuestLog(boolean showCompleted) {
        super("Quest Log " + (showCompleted ? "§8(Completed)" : ""), InventoryType.CHEST_6_ROW);
        this.showCompleted = showCompleted;
    }

    public static void open(SkyblockPlayer player) {
        new GUIQuestLog(false).open((Player) player);
    }

    private String getFormattedDateTime(long ms) {
        if (ms <= 0) return "Never";
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy h:mm a");
        return sdf.format(new Date(ms));
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        // Disallow moving items around in bottom inventory when UI is open
        e.setCancelled(true);
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        fill(FILLER_ITEM);
        set(GUIClickableItem.getCloseItem(49));

        // Go Back Button
        set(new GUIClickableItem(48) {
            @Override
            public void run(InventoryPreClickEvent event, Player player) {
                if (player instanceof SkyblockPlayer sp) {
                    fun.ascent.skyblock.menus.SkyblockMenu.open(sp);
                }
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                List<Component> lore = new ArrayList<>();
                lore.add(miniMessage().deserialize("<gray>To SkyBlock Menu").decoration(TextDecoration.ITALIC, false));
                return ItemStackCreator.getStack(
                        miniMessage().deserialize("<green>Go Back"),
                        Material.ARROW,
                        1,
                        lore
                );
            }
        });

        // Quest Log Info
        set(new GUIClickableItem(4) {
            @Override public void run(InventoryPreClickEvent event, Player player) {}
            @Override
            public ItemStack.Builder getItem(Player player) {
                List<Component> lore = new ArrayList<>();
                lore.add(miniMessage().deserialize("<gray>View your active and completed").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>Quest chains here.").decoration(TextDecoration.ITALIC, false));
                return ItemStackCreator.getStack(
                        miniMessage().deserialize("<green>Quest Log " + (showCompleted ? "§8(Completed)" : "")),
                        Material.WRITABLE_BOOK,
                        1,
                        lore
                );
            }
        });

        // Fairy Souls Info
        set(new GUIClickableItem(10) {
            @Override public void run(InventoryPreClickEvent event, Player player) {}
            @Override
            public ItemStack.Builder getItem(Player player) {
                List<Component> lore = new ArrayList<>();
                lore.add(miniMessage().deserialize("<gray>Fairy Souls are hidden all").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<gray>over the world! Bring them to").decoration(TextDecoration.ITALIC, false));
                lore.add(miniMessage().deserialize("<light_purple>Tia the Fairy <gray>for permanent stats!").decoration(TextDecoration.ITALIC, false));
                return ItemStackCreator.getStack(
                        miniMessage().deserialize("<light_purple>Fairy Souls"),
                        Material.PLAYER_HEAD,
                        1,
                        lore
                );
            }
        });

        // Toggle Completed / Ongoing
        set(new GUIClickableItem(50) {
            @Override
            public void run(InventoryPreClickEvent event, Player player) {
                if (player instanceof SkyblockPlayer sp) {
                    new GUIQuestLog(!showCompleted).open((Player) sp);
                }
            }

            @Override
            public ItemStack.Builder getItem(Player player) {
                List<Component> lore = new ArrayList<>();
                lore.add(miniMessage().deserialize("<gray>Click to show " + (showCompleted ? "<green>Ongoing" : "<red>Completed") + " quests!").decoration(TextDecoration.ITALIC, false));
                return ItemStackCreator.getStack(
                        miniMessage().deserialize("<green>" + (showCompleted ? "Show Ongoing Quests" : "Show Completed Quests")),
                        Material.BOOK,
                        1,
                        lore
                );
            }
        });

        SkyblockPlayer player = (SkyblockPlayer) e.player();
        if (player.getActiveProfileData() == null) return;

        QuestData questData = player.getActiveProfileData().getQuestData();
        List<QuestSet> completedQuests = new ArrayList<>();
        List<QuestSet> activeQuests = new ArrayList<>();

        for (QuestSet set : QuestSet.values()) {
            boolean completedSet = true;
            for (Class<? extends Quest> q : set.getQuests()) {
                if (questData.getQuest(q) == null || !questData.getQuest(q).getValue()) {
                    completedSet = false;
                    break;
                }
            }
            if (completedSet) {
                completedQuests.add(set);
            } else {
                activeQuests.add(set);
            }
        }

        List<QuestSet> toShow = showCompleted ? completedQuests : activeQuests;

        // Clear quest slots
        for (int slot : QUEST_SLOTS) {
            set(new GUIClickableItem(slot) {
                @Override public void run(InventoryPreClickEvent event, Player player) {}
                @Override
                public ItemStack.Builder getItem(Player player) {
                    return ItemStack.builder(Material.AIR);
                }
            });
        }

        for (int i = 0; i < toShow.size() && i < QUEST_SLOTS.length; i++) {
            QuestSet questSet = toShow.get(i);
            int slot = QUEST_SLOTS[i];

            set(new GUIClickableItem(slot) {
                @Override public void run(InventoryPreClickEvent event, Player player) {}

                @Override
                public ItemStack.Builder getItem(Player p) {
                    List<Component> lore = new ArrayList<>();
                    lore.add(Component.empty());

                    Arrays.stream(questSet.getQuests()).forEach(qClass -> {
                        Map.Entry<ActiveQuest, Boolean> activeQuest = questData.getQuest(qClass);
                        if (activeQuest == null) {
                            try {
                                Quest temp = qClass.getDeclaredConstructor().newInstance();
                                lore.add(miniMessage().deserialize("<red>✖ <yellow>" + temp.getName() + ".").decoration(TextDecoration.ITALIC, false));
                            } catch (Exception ex) {
                                lore.add(miniMessage().deserialize("<red>✖ <yellow>Unknown Quest.").decoration(TextDecoration.ITALIC, false));
                            }
                            return;
                        }

                        Quest questObj = QuestData.getQuestClass(activeQuest.getKey().getQuestID());
                        if (questObj == null) return;

                        boolean isProgress = questObj instanceof QuestProgress;
                        String progressSuffix = "";
                        if (isProgress) {
                            int maxProg = ((QuestProgress) questObj).getMaxProgress();
                            progressSuffix = " <gray>(<aqua>" + activeQuest.getKey().getProgress() + "<gray>/<aqua>" + maxProg + "<gray>)";
                        }

                        if (activeQuest.getValue()) {
                            lore.add(miniMessage().deserialize("<green>✔ <white>" + questObj.getName() + progressSuffix).decoration(TextDecoration.ITALIC, false));
                        } else {
                            lore.add(miniMessage().deserialize("<red>✖ <yellow>" + questObj.getName() + progressSuffix).decoration(TextDecoration.ITALIC, false));
                        }
                    });

                    lore.add(Component.empty());
                    Map.Entry<ActiveQuest, Boolean> firstEntry = questData.getQuest(questSet.getQuests()[0]);
                    if (firstEntry != null) {
                        ActiveQuest firstQuest = firstEntry.getKey();
                        lore.add(miniMessage().deserialize("<gray>Started:").decoration(TextDecoration.ITALIC, false));
                        lore.add(miniMessage().deserialize("<white>  " + getFormattedDateTime(firstQuest.getStartedTime())).decoration(TextDecoration.ITALIC, false));

                        if (showCompleted) {
                            lore.add(Component.empty());
                            lore.add(miniMessage().deserialize("<gray>Completed:").decoration(TextDecoration.ITALIC, false));
                            lore.add(miniMessage().deserialize("<white>  " + getFormattedDateTime(firstQuest.getEndedTime())).decoration(TextDecoration.ITALIC, false));
                        }
                    } else {
                        lore.add(miniMessage().deserialize("<red>Not Started").decoration(TextDecoration.ITALIC, false));
                    }

                    // Prettify name
                    String cleanName = questSet.name().replace("_", " ").toLowerCase();
                    cleanName = cleanName.substring(0, 1).toUpperCase() + cleanName.substring(1);

                    return ItemStackCreator.getStack(
                            miniMessage().deserialize("<green>" + cleanName),
                            Material.PAPER,
                            1,
                            lore
                    );
                }
            });
        }
    }
}
