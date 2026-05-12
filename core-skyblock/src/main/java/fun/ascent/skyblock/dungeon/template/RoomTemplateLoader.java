package fun.ascent.skyblock.dungeon.template;

import fun.ascent.skyblock.dungeon.generation.RoomShape;
import fun.ascent.skyblock.dungeon.generation.RoomType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class RoomTemplateLoader {

    private final Map<String, RoomTemplate> allTemplates = new HashMap<>();
    private final Map<RoomShape, List<RoomTemplate>> byShape = new EnumMap<>(RoomShape.class);
    private final Map<RoomType, List<RoomTemplate>> byType = new EnumMap<>(RoomType.class);

    public void loadAll(Path directory) throws IOException {
        allTemplates.clear();
        byShape.clear();
        byType.clear();

        for (RoomShape shape : RoomShape.values()) byShape.put(shape, new ArrayList<>());
        for (RoomType type : RoomType.values()) byType.put(type, new ArrayList<>());

        try (var stream = Files.list(directory)) {
            for (Path file : stream.filter(p -> p.toString().endsWith(".droom")).toList()) {
                try {
                    RoomTemplate template = RoomTemplate.load(file);
                    allTemplates.put(template.name(), template);
                    categorize(template);
                } catch (IOException e) {
                    System.err.println("[Dungeon] Failed to load template: " + file.getFileName() + " - " + e.getMessage());
                }
            }
        }

        System.out.printf("[Dungeon] Loaded %d room templates: 1x1=%d 1x2=%d 1x3=%d 1x4=%d 2x2=%d L=%d puzzle=%d miniboss=%d trap=%d%n",
                allTemplates.size(),
                byShape.get(RoomShape.SINGLE).size(),
                byShape.get(RoomShape.DOUBLE).size(),
                byShape.get(RoomShape.TRIPLE).size(),
                byShape.get(RoomShape.QUAD).size(),
                byShape.get(RoomShape.SQUARE).size(),
                byShape.get(RoomShape.L_SHAPE).size(),
                byType.get(RoomType.PUZZLE).size(),
                byType.get(RoomType.MINIBOSS).size(),
                byType.get(RoomType.TRAP).size());
    }

    private void categorize(RoomTemplate template) {
        String name = template.name();

        if (name.startsWith("Puzzle-"))         { byType.get(RoomType.PUZZLE).add(template); return; }
        if (name.startsWith("Miniboss-"))       { byType.get(RoomType.MINIBOSS).add(template); return; }
        if (name.startsWith("Special-Trap"))    { byType.get(RoomType.TRAP).add(template); return; }
        if (name.startsWith("Special-") || name.startsWith("Fairy")) return;
        if (name.endsWith("Door") || name.contains("Door-")) return;

        if (name.startsWith("1x1-"))      byShape.get(RoomShape.SINGLE).add(template);
        else if (name.startsWith("1x2-")) byShape.get(RoomShape.DOUBLE).add(template);
        else if (name.startsWith("1x3-")) byShape.get(RoomShape.TRIPLE).add(template);
        else if (name.startsWith("1x4-")) byShape.get(RoomShape.QUAD).add(template);
        else if (name.startsWith("2x2-")) byShape.get(RoomShape.SQUARE).add(template);
        else if (name.startsWith("L-"))    byShape.get(RoomShape.L_SHAPE).add(template);
    }

    public RoomTemplate get(String name) {
        return allTemplates.get(name);
    }

    public RoomTemplate pickForShape(Random random, RoomShape shape, Set<String> used) {
        return pickUnique(random, byShape.get(shape), used);
    }

    public RoomTemplate pickForType(Random random, RoomType type, Set<String> used) {
        return pickUnique(random, byType.get(type), used);
    }

    public List<RoomTemplate> getByShape(RoomShape shape) {
        return byShape.getOrDefault(shape, List.of());
    }

    private RoomTemplate pickUnique(Random random, List<RoomTemplate> pool, Set<String> used) {
        if (pool == null || pool.isEmpty()) return null;

        List<RoomTemplate> available = pool.stream()
                .filter(t -> !used.contains(t.name()))
                .toList();

        if (available.isEmpty()) {
            return pool.get(random.nextInt(pool.size()));
        }

        RoomTemplate picked = available.get(random.nextInt(available.size()));
        used.add(picked.name());
        return picked;
    }
}
