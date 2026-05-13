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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PolarWorlds {
    private static final Logger LOGGER = LoggerFactory.getLogger(PolarWorlds.class);

    private PolarWorlds() {
    }

    /**
     * Loads a world into memory using Polar.
     * Useful for transient instances like minigame maps or Skyblock islands.
     */
    public static ChunkLoader setupMemoryPolarWorld(Path templatePath, int radius) {
        PolarWorld world;
        try {
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
        } catch (Exception e) {
            LOGGER.error("CRITICAL ERROR: Failed to convert/load world from {}", templatePath);
            LOGGER.error("Error detail: {}", e.getMessage());
            if (e instanceof IllegalArgumentException && e.getMessage().contains("grindstone")) {
                LOGGER.warn("DETECTED GRINDSTONE PROPERTY ERROR. This is likely a world compatibility issue.");
            }
            // Fallback to empty world to prevent crash
            world = new PolarWorld();
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
                LOGGER.info("{} Loaded Polar world '{}' from MongoDB", logPrefix, worldId);
                return true;
            }

            // 2. Fallback to local file or conversion
            ensurePolarWorld(polarPath, anvilPath, logPrefix);
            if (!Files.isRegularFile(polarPath)) {
                LOGGER.error("{} No Polar world found at {}", logPrefix, polarPath.toAbsolutePath());
                return false;
            }

            byte[] fileData = Files.readAllBytes(polarPath);
            PolarWorld world = PolarReader.read(fileData);
            
            // 3. Strip entities and save to MongoDB for future use
            stripEntities(world);
            byte[] strippedData = PolarWriter.write(world);
            MapRepository.saveWorld(worldId, strippedData);
            
            instance.setChunkLoader(new PolarLoader(world).setLoadLighting(true));
            LOGGER.info("{} Loaded, stripped entities, and saved Polar world '{}' to MongoDB", logPrefix, worldId);
            return true;
        } catch (Exception e) {
            LOGGER.error("{} Failed to load/convert Polar world from {}", logPrefix, polarPath.toAbsolutePath(), e);
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
        LOGGER.info("{} Converting Anvil world to Polar: {}", logPrefix, anvilPath.toAbsolutePath());
        PolarWorld world = AnvilPolar.anvilToPolar(anvilPath);
        Files.write(polarPath, PolarWriter.write(world));
        LOGGER.info("{} Wrote Polar world to {}", logPrefix, polarPath.toAbsolutePath());
    }
}
