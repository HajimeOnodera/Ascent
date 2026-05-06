package fun.ascent.common.world;

import net.hollowcube.polar.AnvilPolar;
import net.hollowcube.polar.ChunkSelector;
import net.hollowcube.polar.PolarLoader;
import net.hollowcube.polar.PolarReader;
import net.hollowcube.polar.PolarWriter;
import net.hollowcube.polar.PolarWorld;
import net.minestom.server.instance.ChunkLoader;
import net.minestom.server.instance.InstanceContainer;

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
            ensurePolarWorld(polarPath, anvilPath, logPrefix);
            if (!Files.isRegularFile(polarPath)) {
                System.err.println(logPrefix + " No Polar world found at " + polarPath.toAbsolutePath());
                return false;
            }

            instance.setChunkLoader(new PolarLoader(polarPath).setLoadLighting(true));
            System.out.println(logPrefix + " Loaded Polar world from " + polarPath.toAbsolutePath());
            return true;
        } catch (Exception e) {
            System.err.println(logPrefix + " Failed to load Polar world from " + polarPath.toAbsolutePath());
            e.printStackTrace();
            return false;
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
