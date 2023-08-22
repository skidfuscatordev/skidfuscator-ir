package dev.skidfuscator.ir.asm.hierachy;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.hierarchy.HierarchyConfig;
import dev.skidfuscator.ir.klass.Klass;

import java.util.Collection;

public class SkidHierarchy implements Hierarchy {
    @Override
    public HierarchyConfig getConfig() {
        return null;
    }

    @Override
    public Iterable<Klass> iterateKlasses() {
        return null;
    }

    @Override
    public void resolveClasses(Collection<Klass> classes) {

    }

    @Override
    public Klass resolveClass(String name) {
        return null;
    }

    @Override
    public KlassGraph getKlassGraph() {
        return null;
    }
}
