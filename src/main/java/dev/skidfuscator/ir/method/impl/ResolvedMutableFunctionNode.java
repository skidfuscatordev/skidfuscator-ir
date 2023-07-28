package dev.skidfuscator.ir.method.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.method.FunctionNode;
import dev.skidfuscator.ir.util.Descriptor;
import org.objectweb.asm.tree.MethodNode;

public class ResolvedMutableFunctionNode extends ResolvedAbstractFunctionNode {
    private FunctionNode parent;
    private String name;
    // TODO: Add support for types
    private String desc;

    public ResolvedMutableFunctionNode(Hierarchy hierarchy, Descriptor descriptor, MethodNode node, FunctionNode parent) {
        super(hierarchy, descriptor, node);
        this.parent = parent;

        this.name = super.getName();
        this.desc = super.getDesc();
    }

    @Override
    public boolean isSynthetic() {
        return this.node == null && this.getInstructions().isEmpty();
    }

    @Override
    public void resolveHierarchy() {
        if (owner.getParent() != null) {
            try {
                final FunctionNode parent = owner.getParent().getMethod(
                        originalDescriptor.getName(),
                        originalDescriptor.getDesc()
                );

                this.parent = parent;

                if (parent != null && !parent.isResolved()) {
                    parent.resolveHierarchy();
                }
            } catch (IllegalStateException e) {
                // no-op
            }

        }

        super.resolveHierarchy();
    }

    @Override
    public String getName() {
        return parent == null ? name : parent.getName();
    }

    @Override
    public void setName(String name) {
        if (!mutable)
            throw new IllegalStateException("Cannot modify name of immutable function node");

        if (parent == null) {
            this.name = name;
        } else {
            parent.setName(name);
        }
    }

    @Override
    public String getDesc() {
        return parent == null ? desc : parent.getDesc();
    }
}
