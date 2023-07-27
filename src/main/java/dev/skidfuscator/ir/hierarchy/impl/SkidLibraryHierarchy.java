package dev.skidfuscator.ir.hierarchy.impl;

import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.library.LibrarySource;
import org.objectweb.asm.tree.ClassNode;

import java.util.*;
import java.util.function.ToIntFunction;

public class SkidLibraryHierarchy extends SkidHierarchy {
    private final Queue<LibrarySource> libraries = new PriorityQueue<>(
            Comparator.comparingInt(LibrarySource::getPriority)
    );

    public void addLibrary(LibrarySource library) {
        libraries.add(library);
    }

    @Override
    public void resolveClasses(Collection<ClassNode> classes) {
        for (LibrarySource library : libraries) {
            for (ClassNode value : library.getClasses().values()) {
                if (findClass(value.name) != null) {
                    continue;
                }

                create(value);
            }
        }

        super.resolveClasses(classes);
    }
}
