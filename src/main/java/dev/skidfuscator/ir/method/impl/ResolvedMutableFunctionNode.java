package dev.skidfuscator.ir.method.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.method.FunctionNode;
import dev.skidfuscator.ir.util.Descriptor;
import org.objectweb.asm.tree.MethodNode;

public class ResolvedMutableFunctionNode extends ResolvedAbstractFunctionNode {
    private FunctionNode parent;
    private String name;
    private String desc;

    public ResolvedMutableFunctionNode(Hierarchy hierarchy, Descriptor descriptor, MethodNode node, FunctionNode parent) {
        super(hierarchy, descriptor, node);
        this.parent = parent;

        this.name = super.getName();
        this.desc = super.getDesc();
    }

    @Override
    public void resolveHierarchy() {
        if (owner.getParent() != null) {
            final FunctionNode parent = owner.getParent().getMethod(
                    originalDescriptor.getName(),
                    originalDescriptor.getDesc()
            );

            // TODO: Create a root definition cache
            /*if (parent == null) {
                throw new IllegalStateException(String.format(
                        "Could not find parent method %s%s in %s",
                        descriptor.getName(),
                        descriptor.getDesc(),
                        owner.getParent().getName()
                ));
            }*/

            this.parent = parent;

            if (parent != null && !parent.isResolved()) {
                parent.resolveHierarchy();
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
