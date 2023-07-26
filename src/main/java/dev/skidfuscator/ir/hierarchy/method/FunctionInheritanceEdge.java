package dev.skidfuscator.ir.hierarchy.method;

import dev.skidfuscator.ir.method.impl.ResolvedAbstractFunctionNode;

import java.util.Objects;

public abstract class FunctionInheritanceEdge {
    private final ResolvedAbstractFunctionNode parent;
    private final ResolvedAbstractFunctionNode child;

    public FunctionInheritanceEdge(ResolvedAbstractFunctionNode parent, ResolvedAbstractFunctionNode child) {
        this.parent = parent;
        this.child = child;
    }

    public ResolvedAbstractFunctionNode getParent() {
        return parent;
    }

    public ResolvedAbstractFunctionNode getChild() {
        return child;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FunctionInheritanceEdge that)) return false;

        if (!Objects.equals(parent, that.parent)) return false;
        return Objects.equals(child, that.child);
    }

    @Override
    public int hashCode() {
        int result = parent != null ? parent.hashCode() : 0;
        result = 31 * result + (child != null ? child.hashCode() : 0);
        return result;
    }
}
