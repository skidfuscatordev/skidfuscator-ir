package dev.skidfuscator.ir.hierarchy.method;

import dev.skidfuscator.ir.JavaMethod;

import java.util.Objects;

public class FunctionInheritanceEdge {
    private final JavaMethod parent;
    private final JavaMethod child;

    public FunctionInheritanceEdge(JavaMethod parent, JavaMethod child) {
        this.parent = parent;
        this.child = child;
    }

    public JavaMethod getParent() {
        return parent;
    }

    public JavaMethod getChild() {
        return child;
    }


    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof FunctionInheritanceEdge edge
                && Objects.equals(parent, edge.parent)
                && Objects.equals(child, edge.child));
    }

    @Override
    public int hashCode() {
        int result = parent != null ? parent.hashCode() : 0;
        result = 31 * result + (child != null ? child.hashCode() : 0);
        return result;
    }
}
