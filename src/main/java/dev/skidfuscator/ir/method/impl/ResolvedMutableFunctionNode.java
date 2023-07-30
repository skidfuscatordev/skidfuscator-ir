package dev.skidfuscator.ir.method.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.method.FunctionNode;
import dev.skidfuscator.ir.type.TypeWrapper;
import dev.skidfuscator.ir.util.Descriptor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

public class ResolvedMutableFunctionNode extends ResolvedAbstractFunctionNode {
    private FunctionNode parent;
    private String name;
    // TODO: Add support for types
    private TypeWrapper desc;

    public ResolvedMutableFunctionNode(Hierarchy hierarchy, Descriptor descriptor, MethodNode node, FunctionNode parent) {
        super(hierarchy, descriptor, node);
        this.parent = parent;

        this.name = super.getName();
        this.desc = new TypeWrapper(Type.getMethodType(super.getDesc()), hierarchy);
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

        if (!this.desc.isResolved()) {
            this.desc.resolveHierarchy();
        }

        super.resolveHierarchy();
    }

    @Override
    public void resolveInternal() {
        // TODO: fix this shit
        super.resolveInternal();
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
        return parent == null
                ? desc.isResolved() ? desc.dump().getDescriptor() : super.getDesc()
                : parent.getDesc();
    }
}
