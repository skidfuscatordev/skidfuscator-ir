package dev.skidfuscator.ir.asm.hierachy;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.hierarchy.HierarchyConfig;
import dev.skidfuscator.ir.hierarchy.HierarchyContext;
import dev.skidfuscator.ir.klass.Klass;
import org.objectweb.asm.tree.ClassNode;

import java.util.function.Function;

public class SkidHierarchyContext implements HierarchyContext {
    private final HierarchyConfig config;
    private final Function<String, ClassNode> classProvider;

    public SkidHierarchyContext(HierarchyConfig config, Function<String, ClassNode> classProvider) {
        this.config = config;
        this.classProvider = classProvider;
    }

    @Override
    public Klass create(String name) {
        final ClassNode node = classProvider.apply(name);
        return null;
    }

    @Override
    public HierarchyConfig config() {
        return null;
    }
}
