package dev.skidfuscator.ir.hierarchy.klass;

import dev.skidfuscator.ir.klass.KlassNode;

public interface KlassInheritanceEdge {
    KlassNode getNode();

    KlassNode getInherits();
}
