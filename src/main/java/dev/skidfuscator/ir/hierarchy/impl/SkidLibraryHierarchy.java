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

    /*@Override
    public KlassNode findClass(String name) {
        KlassNode klassNode = super.findClass(name);

        if (klassNode != null) {
            return klassNode;
        }

        ClassNode classNode = null;

        for (LibrarySource library : libraries) {
            classNode = library.getClass(name);

            if (classNode != null) {
                klassNode = create(classNode);
                if (!klassNode.isResolvedHierarchy())
                    klassNode.resolveHierarchy();
                if (!klassNode.isResolvedInternal())
                    klassNode.resolveInternal();
                klassNode.resolveInstructions();
                klassNode.lock();

                return klassNode;
            }
        }

        return null;
    }*/

    @Override
    public void resolveClasses(Collection<ClassNode> classes) {
        for (LibrarySource library : libraries) {
            for (ClassNode value : library.getClasses().values()) {
                if (findClass(value.name) != null) {
                    continue;
                }

                create(value, false);
            }
        }

        super.resolveClasses(classes);
    }
}
