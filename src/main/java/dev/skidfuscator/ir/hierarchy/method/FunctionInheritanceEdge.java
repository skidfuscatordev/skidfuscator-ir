package dev.skidfuscator.ir.hierarchy.method;

import dev.skidfuscator.ir.method.impl.ResolvedFunctionNode;

import java.util.Objects;

public abstract class FunctionInheritanceEdge {
    private final ResolvedFunctionNode parent;
    private final ResolvedFunctionNode child;

    public FunctionInheritanceEdge(ResolvedFunctionNode parent, ResolvedFunctionNode child) {
        this.parent = parent;
        this.child = child;
    }

    public ResolvedFunctionNode getParent() {
        return parent;
    }

    public ResolvedFunctionNode getChild() {
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
