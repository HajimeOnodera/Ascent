package fun.ascent.skyblock.world;

import net.hollowcube.polar.AnvilPolar;
import net.hollowcube.polar.PolarLoader;
import net.hollowcube.polar.PolarWorld;
import net.hollowcube.polar.PolarWriter;
import net.minestom.server.instance.ChunkLoader;
import net.minestom.server.instance.anvil.AnvilLoader;
import org.jspecify.annotations.NonNull;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class WorldLoaderUtils {

    public static ChunkLoader setupTemplateWorld(Path templatePath, Path savePath) throws IOException {
        System.out.println("[WorldLoader] ========================================");
        System.out.println("[WorldLoader] Setup Request Received:");
        System.out.println("[WorldLoader] Template: " + templatePath.toAbsolutePath());
        System.out.println("[WorldLoader] Save Tgt: " + savePath.toAbsolutePath());

        boolean saveTargetIsPolar = savePath.toString().toLowerCase().endsWith(".polar");

        if (Files.exists(savePath)) {
            System.out.println("[WorldLoader] Save target already exists. Loading directly.");
            return saveTargetIsPolar ? new PolarLoader(savePath) : new AnvilLoader(savePath);
        }

        if (savePath.getParent() != null) {
            Files.createDirectories(savePath.getParent());
        }

        String templateStr = templatePath.toString().toLowerCase();

        if (saveTargetIsPolar) {
            PolarWorld convertedWorld;

            if (templateStr.endsWith(".polar")) {
                System.out.println("[WorldLoader] Action: Copying existing .polar file.");
                Files.copy(templatePath, savePath);
                return new PolarLoader(savePath);

            } else if (templateStr.endsWith(".zip")) {
                System.out.println("[WorldLoader] Action: Unzipping .zip for Polar conversion.");
                Path tempDir = Files.createTempDirectory("ascent_unzip_");
                try {
                    unzip(templatePath, tempDir);
                    System.out.println("[WorldLoader] Executing AnvilPolar.anvilToPolar() on temp dir...");
                    convertedWorld = AnvilPolar.anvilToPolar(tempDir);
                    System.out.println("[WorldLoader] Conversion Successful!");
                } catch (Exception e) {
                    System.err.println("[WorldLoader] CRITICAL ERROR during ZIP -> Polar conversion!");
                    System.err.println("[WorldLoader] Failed File: " + templatePath.toAbsolutePath());
                    throw e;
                } finally {
                    deleteFolder(tempDir);
                }
            } else if (Files.isDirectory(templatePath)) {
                System.out.println("[WorldLoader] Action: Converting Anvil directory to Polar.");
                try {
                    System.out.println("[WorldLoader] Executing AnvilPolar.anvilToPolar() on template...");
                    convertedWorld = AnvilPolar.anvilToPolar(templatePath);
                    System.out.println("[WorldLoader] Conversion Successful!");
                } catch (Exception e) {
                    System.err.println("[WorldLoader] CRITICAL ERROR during Anvil -> Polar conversion!");
                    System.err.println("[WorldLoader] Failed Directory: " + templatePath.toAbsolutePath());
                    throw e;
                }
            } else {
                throw new IllegalArgumentException("Unknown template world format: " + templatePath);
            }

            System.out.println("[WorldLoader] Writing Polar file to disk...");
            Files.write(savePath, PolarWriter.write(convertedWorld));
            return new PolarLoader(savePath);
        }

        else {
            if (templateStr.endsWith(".polar")) {
                throw new UnsupportedOperationException("Cannot natively extract a .polar file back into an Anvil folder. Please use a .polar save target.");

            } else if (templateStr.endsWith(".zip")) {
                System.out.println("[WorldLoader] Action: Unzipping .zip to Anvil save directory.");
                unzip(templatePath, savePath);

            } else if (Files.isDirectory(templatePath)) {
                System.out.println("[WorldLoader] Action: Copying Anvil directory to Anvil save directory.");
                copyDirectory(templatePath, savePath);

            } else {
                throw new IllegalArgumentException("Unknown template world format: " + templatePath);
            }

            return new AnvilLoader(savePath);
        }
    }

    private static void unzip(Path zipFilePath, Path destDir) throws IOException {
        Files.createDirectories(destDir);
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath.toFile()))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path newFilePath = destDir.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(newFilePath);
                } else {
                    Files.createDirectories(newFilePath.getParent());
                    try (OutputStream os = Files.newOutputStream(newFilePath)) {
                        zis.transferTo(os);
                    }
                }
                zis.closeEntry();
            }
        }
    }

    private static void copyDirectory(Path source, Path target) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<>() {
            @Override
            public @NonNull FileVisitResult preVisitDirectory(@NonNull Path dir, @NonNull BasicFileAttributes attrs) throws IOException {
                Files.createDirectories(target.resolve(source.relativize(dir)));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public @NonNull FileVisitResult visitFile(@NonNull Path file, @NonNull BasicFileAttributes attrs) throws IOException {
                Files.copy(file, target.resolve(source.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void deleteFolder(Path folder) throws IOException {
        if (Files.exists(folder)) {
            try (Stream<Path> paths = Files.walk(folder)) {
                paths.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        }
    }
}
