package dev.skidfuscator.ir.library;

import org.objectweb.asm.tree.ClassNode;

import java.util.Map;

public class LibrarySource {
    private final int priority;
    private final Map<String, ClassNode> classes;

    public LibrarySource(int priority, Map<String, ClassNode> classes) {
        this.priority = priority;
        this.classes = classes;
    }

    public int getPriority() {
        return priority;
    }

    public Map<String, ClassNode> getClasses() {
        return classes;
    }

    public ClassNode getClass(String name) {
        return classes.get(name);
    }
}
