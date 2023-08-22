package dev.skidfuscator.ir.hierarchy.klass;

import dev.skidfuscator.ir.klass.Klass;

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
        if (this == o) return true;
        if (!(o instanceof KlassAbstractEdge)) return false;

        KlassAbstractEdge that = (KlassAbstractEdge) o;

        if (node != null ? !node.equals(that.node) : that.node != null) return false;
        if (inherits != null ? !inherits.equals(that.inherits) : that.inherits != null) return false;

        return true;
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
