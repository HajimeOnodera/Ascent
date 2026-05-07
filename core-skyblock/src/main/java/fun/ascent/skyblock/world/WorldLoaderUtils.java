package fun.ascent.skyblock.world;

import net.hollowcube.polar.AnvilPolar;
import net.hollowcube.polar.PolarLoader;
import net.hollowcube.polar.PolarWorld;
import net.hollowcube.polar.PolarWriter;
import net.minestom.server.instance.ChunkLoader;
import net.minestom.server.instance.anvil.AnvilLoader;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class WorldLoaderUtils {

    public static ChunkLoader setupTemplateWorld(Path templatePath, Path savePath) throws IOException {

        boolean saveTargetIsPolar = savePath.toString().toLowerCase().endsWith(".polar");
        if (Files.exists(savePath)) {
            return saveTargetIsPolar ? new PolarLoader(savePath) : new AnvilLoader(savePath);
        }

        if (savePath.getParent() != null) {
            Files.createDirectories(savePath.getParent());
        }

        String templateStr = templatePath.toString().toLowerCase();

        if (saveTargetIsPolar) {
            PolarWorld convertedWorld;

            if (templateStr.endsWith(".polar")) {
                Files.copy(templatePath, savePath);
                return new PolarLoader(savePath);

            } else if (templateStr.endsWith(".zip")) {
                Path tempDir = Files.createTempDirectory("ascent_unzip_");
                try {
                    unzip(templatePath, tempDir);
                    convertedWorld = AnvilPolar.anvilToPolar(tempDir);
                } finally {
                    deleteFolder(tempDir);
                }
            } else if (Files.isDirectory(templatePath)) {
                convertedWorld = AnvilPolar.anvilToPolar(templatePath);
            } else {
                throw new IllegalArgumentException("Unknown template world format: " + templatePath);
            }
            Files.write(savePath, PolarWriter.write(convertedWorld));
            return new PolarLoader(savePath);
        }

        else {
            if (templateStr.endsWith(".polar")) {
                throw new UnsupportedOperationException("Cannot natively extract a .polar file back into an Anvil folder. Please use a .polar save target.");

            } else if (templateStr.endsWith(".zip")) {
                unzip(templatePath, savePath);

            } else if (Files.isDirectory(templatePath)) {
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
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Files.createDirectories(target.resolve(source.relativize(dir)));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
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