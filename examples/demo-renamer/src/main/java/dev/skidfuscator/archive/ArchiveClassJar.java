package dev.skidfuscator.archive;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.hierarchy.impl.SkidLibraryHierarchy;
import dev.skidfuscator.util.ClassHelper;
import dev.skidfuscator.util.FileHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ArchiveClassJar {
    private final Map<String, byte[]> originalClasses;
    private final Map<String, ClassNode> classes;
    private final Map<String, byte[]> files;
    private final boolean library;

    private final File inputFile;
    private final File outputFile;

    public ArchiveClassJar(File inputFile, boolean library) {
        this.originalClasses = new HashMap<>();
        this.classes = new HashMap<>();
        this.files = new HashMap<>();
        this.inputFile = inputFile;
        this.outputFile = new File(inputFile.getAbsolutePath() + "-out.jar");
        this.library = library;
    }



    public Map<String, byte[]> getOriginalClasses() {
        return originalClasses;
    }

    public Map<String, ClassNode> getClasses() {
        return classes;
    }

    public Map<String, byte[]> getFiles() {
        return files;
    }

    public void load() {
        System.out.printf("Loading input file: %s\n", inputFile);

        FileHelper.loadFilesFromZip(inputFile.toPath(), (name, bytes) -> {
            try {
                if (ClassHelper.isClass(bytes)) {
                    ClassHelper.loadClass(bytes, ClassReader.EXPAND_FRAMES).ifPresentOrElse(classWrapper -> {
                        if (classes.containsKey(classWrapper.name)) {
                            throw new IllegalStateException(String.format(
                                    "Duplicate class: %s\n" +
                                            "First: %s\n" +
                                            "Second: %s\n",
                                    classWrapper.name,
                                    classes.get(classWrapper.name).sourceFile,
                                    classWrapper.sourceFile
                            ));
                        }

                        classes.put(classWrapper.name, classWrapper);
                        originalClasses.put(classWrapper.name, bytes);
                    }, () -> {
                        if (library)
                            return;

                        if (files.containsKey(name)) {
                            throw new IllegalStateException(String.format(
                                    "Duplicate file: %s",
                                    name
                            ));
                        }

                        files.put(name, bytes);
                    });
                } else {
                    if (library)
                        return;

                    if (files.containsKey(name)) {
                        throw new IllegalStateException(String.format(
                                "Duplicate file: %s",
                                name
                        ));
                    }

                    files.put(name, bytes);
                }
            } catch (Exception e) {
                System.err.println(String.format(
                        "Could not load class: %s, adding as file", name)
                );
                e.printStackTrace();
                if (library)
                    return;

                files.put(name, bytes);
            }
        });

        System.out.printf("Loaded input file: %s\n", inputFile);
    }

    public void save() {
        if (library) {
            throw new IllegalStateException("Cannot save library jar");
        }
        System.out.printf("Saving output file: %s\n", outputFile);

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(outputFile))) {
            zipOutputStream.setLevel(9);

            new HashMap<>(classes).forEach((ignored, classWrapper) -> {
                try {
                    ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
                    classWrapper.accept(classWriter);

                    byte[] data = classWriter.toByteArray();
                    zipOutputStream.putNextEntry(new ZipEntry(classWrapper.name + ".class"));
                    zipOutputStream.write(data);
                } catch (Exception e) {
                    System.err.printf(
                            "Could not save class, saving original class instead of deobfuscated: %s\n",
                            classWrapper.name
                    );
                    e.printStackTrace();
                    e.printStackTrace();
                    try {
                        byte[] data = originalClasses.get(classWrapper.name);

                        zipOutputStream.putNextEntry(new ZipEntry(classWrapper.name + ".class"));
                        zipOutputStream.write(data);
                    } catch (Exception e2) {
                        System.err.printf("Could not save original class: %s\n", classWrapper.name);
                        e2.printStackTrace();
                    }
                }

                originalClasses.remove(classWrapper.name);
                classes.remove(classWrapper.name);
            });

            new HashMap<>(files).forEach((name, data) -> {
                try {
                    zipOutputStream.putNextEntry(new ZipEntry(name));
                    zipOutputStream.write(data);
                } catch (Exception e) {
                    System.err.printf("Could not save file: %s\n", name);
                    e.printStackTrace();
                }

                files.remove(name);
            });

        } catch (Exception e) {
            System.err.printf("Could not save output file:%s\n", outputFile);
            e.printStackTrace();
        }

        System.out.printf("Saved output file: {}\n", outputFile);
    }
}
