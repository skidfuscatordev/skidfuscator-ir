package dev.skidfuscator.util;

import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.jar.JarFile;

/**
 * A helper class for file operations.
 * @author narumii
 */
public final class FileHelper {


    private FileHelper() {
    }

    public static void loadFilesFromZip(Path path, BiConsumer<String, byte[]> consumer) {
        try (JarFile zipFile = new JarFile(path.toFile())) {
            zipFile.entries().asIterator().forEachRemaining(zipEntry -> {
                try {
                    consumer.accept(zipEntry.getName(), zipFile.getInputStream(zipEntry).readAllBytes());
                } catch (Exception e) {
                    System.err.printf("Could not load ZipEntry: %s\n", zipEntry.getName());
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            System.err.printf("Could not load file: %s\n", path);
            e.printStackTrace();
        }
    }
}