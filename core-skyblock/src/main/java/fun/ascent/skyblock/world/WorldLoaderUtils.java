package fun.ascent.skyblock.world;

import net.hollowcube.polar.PolarLoader;
import net.minestom.server.instance.ChunkLoader;
import net.minestom.server.instance.anvil.AnvilLoader;
import org.jspecify.annotations.NonNull;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class WorldLoaderUtils {

    /**
     * Creates a ChunkLoader pointing to the savePath.
     * If the savePath does not exist, it automatically copies or unzips the templatePath.
     */
    public static ChunkLoader setupTemplateWorld(Path templatePath, Path savePath) throws IOException {

        if(!Files.isSameFile(templatePath,savePath)) {
            if (!Files.exists(savePath)) {
                Files.createDirectories(savePath.getParent());

                if (Files.isDirectory(templatePath)) {
                    copyDirectory(templatePath, savePath);
                } else if (templatePath.toString().endsWith(".zip")) {
                    unzip(templatePath, savePath);
                } else if (templatePath.toString().endsWith(".polar")) {
                    Files.copy(templatePath, savePath);
                } else {
                    throw new IllegalArgumentException("Unknown template world format: " + templatePath);
                }
            }
        }

        if (savePath.toString().endsWith(".polar")) {
            return new PolarLoader(savePath);
        } else {
            return new AnvilLoader(savePath);
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
}