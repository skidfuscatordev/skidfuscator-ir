package dev.skidfuscator.ir.hierarchy.klass;

import dev.skidfuscator.ir.Klass;

public interface KlassInheritanceEdge {
    Klass getNode();

    Klass getInherited();
}
