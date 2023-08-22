package dev.skidfuscator.ir.hierarchy.klass;

import dev.skidfuscator.ir.klass.Klass;

public interface KlassInheritanceEdge {
    Klass getNode();

    Klass getInherited();
}
