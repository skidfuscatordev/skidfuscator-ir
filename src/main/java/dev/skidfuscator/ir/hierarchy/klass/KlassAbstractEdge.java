package dev.skidfuscator.ir.hierarchy.klass;

import dev.skidfuscator.ir.klass.KlassNode;

import java.util.Objects;

public abstract class KlassAbstractEdge implements KlassInheritanceEdge {
    private final KlassNode node;
    private final KlassNode inherits;

    public KlassAbstractEdge(KlassNode node, KlassNode inherited) {
        this.node = node;
        this.inherits = inherited;
    }

    @Override
    public KlassNode getNode() {
        return node;
    }

    @Override
    public KlassNode getInherits() {
        return inherits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KlassAbstractEdge that)) return false;

        if (!Objects.equals(node, that.node)) return false;
        return Objects.equals(inherits, that.inherits);
    }

    @Override
    public int hashCode() {
        int result = node != null ? node.hashCode() : 0;
        result = 31 * result + (inherits != null ? inherits.hashCode() : 0);
        return result;
    }
}
