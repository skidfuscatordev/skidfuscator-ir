package dev.skidfuscator.ir.hierarchy.klass;

import dev.skidfuscator.ir.Klass;

import java.util.Objects;

public abstract class KlassAbstractEdge implements KlassInheritanceEdge {
    private final Klass node;
    private final Klass inherits;

    public KlassAbstractEdge(Klass node, Klass inherited) {
        this.node = node;
        this.inherits = inherited;
    }

    @Override
    public Klass getNode() {
        return node;
    }

    @Override
    public Klass getInherited() {
        return inherits;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof KlassAbstractEdge edge
                && Objects.equals(node, edge.node)
                && Objects.equals(inherits, edge.inherits));
    }

    @Override
    public int hashCode() {
        int result = node != null ? node.hashCode() : 0;
        result = 31 * result + (inherits != null ? inherits.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return inherits + "\n   |\n   \\-> " + node + "\n";
    }
}
