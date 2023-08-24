package dev.skidfuscator.ir.hierarchy.method;

import dev.skidfuscator.ir.Method;

import java.util.Objects;

public class FunctionInheritanceEdge {
    private final Method parent;
    private final Method child;

    public FunctionInheritanceEdge(Method parent, Method child) {
        this.parent = parent;
        this.child = child;
    }

    public Method getParent() {
        return parent;
    }

    public Method getChild() {
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
