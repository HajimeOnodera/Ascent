package fun.ascent.common.world;

import net.hollowcube.polar.AnvilPolar;
import net.hollowcube.polar.ChunkSelector;
import net.hollowcube.polar.PolarLoader;
import net.hollowcube.polar.PolarReader;
import net.hollowcube.polar.PolarWriter;
import net.hollowcube.polar.PolarWorld;
import net.minestom.server.instance.ChunkLoader;
import net.minestom.server.instance.InstanceContainer;
import fun.ascent.database.MapRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class PolarWorlds {

    private PolarWorlds() {
    }

    /**
     * Loads a world into memory using Polar.
     * Useful for transient instances like minigame maps or Skyblock islands.
     */
    public static ChunkLoader setupMemoryPolarWorld(Path templatePath, int radius) throws IOException {
        PolarWorld world;
        if (Files.isDirectory(templatePath)) {
            // Convert Anvil directory to Polar world in memory
            if (radius > 0) {
                world = AnvilPolar.anvilToPolar(templatePath, ChunkSelector.radius(radius));
            } else {
                world = AnvilPolar.anvilToPolar(templatePath);
            }
        } else if (templatePath.toString().endsWith(".polar")) {
            // Read Polar file directly into memory
            world = PolarReader.read(Files.readAllBytes(templatePath));
        } else {
            throw new IllegalArgumentException("Unsupported template format for memory polar world: " + templatePath);
        }
        
        return new PolarLoader(world);
    }

    public static boolean loadOrConvert(InstanceContainer instance, Path polarPath, Path anvilPath, String logPrefix) {
        try {
            String worldId = polarPath.getFileName().toString().replace(".polar", "");
            
            // 1. Try to load from MongoDB first
            byte[] mongoData = MapRepository.loadWorld(worldId);
            if (mongoData != null) {
                PolarWorld world = PolarReader.read(mongoData);
                stripEntities(world); // Extra safeguard
                instance.setChunkLoader(new PolarLoader(world).setLoadLighting(true));
                System.out.println(logPrefix + " Loaded Polar world '" + worldId + "' from MongoDB");
                return true;
            }

            // 2. Fallback to local file or conversion
            ensurePolarWorld(polarPath, anvilPath, logPrefix);
            if (!Files.isRegularFile(polarPath)) {
                System.err.println(logPrefix + " No Polar world found at " + polarPath.toAbsolutePath());
                return false;
            }

            byte[] fileData = Files.readAllBytes(polarPath);
            PolarWorld world = PolarReader.read(fileData);
            
            // 3. Strip entities and save to MongoDB for future use
            stripEntities(world);
            byte[] strippedData = PolarWriter.write(world);
            MapRepository.saveWorld(worldId, strippedData);
            
            instance.setChunkLoader(new PolarLoader(world).setLoadLighting(true));
            System.out.println(logPrefix + " Loaded, stripped entities, and saved Polar world '" + worldId + "' to MongoDB");
            return true;
        } catch (Exception e) {
            System.err.println(logPrefix + " Failed to load/convert Polar world from " + polarPath.toAbsolutePath());
            e.printStackTrace();
            return false;
        }
    }

    public static void stripEntities(PolarWorld world) {
        try {
            // Polar API varies between versions (getChunks vs chunks, entities() vs getEntities())
            // Using reflection to stay compatible and avoid compilation failures.
            java.lang.reflect.Method getChunks = world.getClass().getMethod("chunks");
            Iterable<?> chunks = (Iterable<?>) getChunks.invoke(world);
            
            for (Object chunk : chunks) {
                try {
                    java.lang.reflect.Method entitiesMethod = chunk.getClass().getMethod("entities");
                    java.util.Collection<?> entities = (java.util.Collection<?>) entitiesMethod.invoke(chunk);
                    entities.clear();
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            // Fallback for different method names
            try {
                java.lang.reflect.Method getChunks = world.getClass().getMethod("getChunks");
                Iterable<?> chunks = (Iterable<?>) getChunks.invoke(world);
                for (Object chunk : chunks) {
                    try {
                        java.lang.reflect.Method entitiesMethod = chunk.getClass().getMethod("getEntities");
                        java.util.Collection<?> entities = (java.util.Collection<?>) entitiesMethod.invoke(chunk);
                        entities.clear();
                    } catch (Exception ignored) {}
                }
            } catch (Exception ignored) {}
        }
    }

    public static void ensurePolarWorld(Path polarPath, Path anvilPath, String logPrefix) throws IOException {
        if (Files.isRegularFile(polarPath) || anvilPath == null || !Files.isDirectory(anvilPath)) {
            return;
        }

        Files.createDirectories(polarPath.getParent());
        System.out.println(logPrefix + " Converting Anvil world to Polar: " + anvilPath.toAbsolutePath());
        PolarWorld world = AnvilPolar.anvilToPolar(anvilPath);
        Files.write(polarPath, PolarWriter.write(world));
        System.out.println(logPrefix + " Wrote Polar world to " + polarPath.toAbsolutePath());
    }
}
