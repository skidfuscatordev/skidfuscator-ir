package dev.skidfuscator.ir.method.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.util.Descriptor;
import org.objectweb.asm.tree.MethodNode;

public class ResolvedImmutableFunctionNode extends ResolvedAbstractFunctionNode {
    public ResolvedImmutableFunctionNode(Hierarchy hierarchy, Descriptor descriptor, MethodNode node) {
        super(hierarchy, descriptor, node);
    }

    @Override
    public boolean isSynthetic() {
        return true;
    }

    @Override
    public void setOwner(KlassNode node) {
        // Remove back sorting logic
        this.owner = node;
    }

    @Override
    protected void _checkResolve() {
        throw new IllegalStateException(String.format(
                "Cannot resolve function node %s.%s%s as it is immutable",
                this.getOwner().getName(),
                this.getName(),
                this.getDesc()
        ));
    }
}
