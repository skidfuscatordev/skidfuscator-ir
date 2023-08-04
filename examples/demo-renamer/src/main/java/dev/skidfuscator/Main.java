package dev.skidfuscator;

import dev.skidfuscator.archive.ArchiveClassJar;
import dev.skidfuscator.ir.hierarchy.impl.SkidLibraryHierarchy;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.library.LibrarySource;
import dev.skidfuscator.util.MiscHelper;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java -jar skid-obfuscator.jar <input>");
            return;
        }
        final String input = args[0];
        final File inputFile = new File(input);

        // Load the JVM libs
        final SkidLibraryHierarchy hierarchy = new SkidLibraryHierarchy();
        loadLibs(hierarchy);

        // Load the input jar
        final ArchiveClassJar inputJar = new ArchiveClassJar(inputFile, false);
        inputJar.load();

        // Resolve the classes
        hierarchy.resolveClasses(inputJar.getClasses().values());

        // Rename all classes
        for (KlassNode node : hierarchy.iterateKlasses()) {
            System.out.printf("Renaming %s\n", node.getName());
            node.setName("Renamed_" + node.getName());
        }

        // Dump the classes
        for (KlassNode node : hierarchy.iterateKlasses()) {
            node.dump();
        }

        // Save the jar
        inputJar.save();
    }

    private static void loadLibs(final SkidLibraryHierarchy hierarchy) {
        final String home = System.getProperty("java.home");
        final File runtimeFile = new File(
                home,
                MiscHelper.isJmod()
                        ? "jmods"
                        : "lib/rt.jar"
        );
        if (MiscHelper.isJmod()) {
            final File[] libFiles = runtimeFile.listFiles();

            for (File file : libFiles) {
                if (!file.getAbsolutePath().endsWith(".jmod")) {
                    continue;
                }

                final ArchiveClassJar runtime = new ArchiveClassJar(file, true);
                runtime.load();
                final LibrarySource librarySource = new LibrarySource(
                        1, runtime.getClasses()
                );
                hierarchy.addLibrary(librarySource);
            }

        } else {
            final ArchiveClassJar runtime = new ArchiveClassJar(runtimeFile, true);
            runtime.load();
            final LibrarySource librarySource = new LibrarySource(
                    1, runtime.getClasses()
            );
            hierarchy.addLibrary(librarySource);
        }
    }
}