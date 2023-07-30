package dev.skidfuscator.ir.method.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.method.FunctionNode;
import dev.skidfuscator.ir.type.TypeWrapper;
import dev.skidfuscator.ir.util.Descriptor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

import java.util.Set;

public class ResolvedMutableFunctionNode extends ResolvedAbstractFunctionNode {
    private String name;
    // TODO: Add support for types
    private TypeWrapper desc;
    private TypeWrapper sig;

    public ResolvedMutableFunctionNode(Hierarchy hierarchy, Descriptor descriptor, MethodNode node, Set<FunctionNode> parent) {
        super(hierarchy, descriptor, node);

        this.name = super.getName();
        this.desc = new TypeWrapper(Type.getMethodType(super.getDesc()), hierarchy);
        //this.sig = new TypeWrapper(Type.getMethodType(super.getSignature()), hierarchy);
    }

    @Override
    public boolean isSynthetic() {
        return this.node == null && this.getInstructions().isEmpty();
    }

    @Override
    public void resolveHierarchy() {
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
        return name;
    }

    @Override
    public void setName(String name) {
        if (!mutable)
            throw new IllegalStateException("Cannot modify name of immutable function node");

        for (FunctionNode functionNode : hierarchy.getFunctionGraph().childrenOf(this)) {
            functionNode.setName(name);
        }

        for (FunctionNode functionNode : hierarchy.getFunctionGraph().parentsOf(this)) {
            functionNode.setName(name);
        }
    }

    @Override
    public String getDesc() {
        return desc.isResolved() ? desc.dump().getDescriptor() : super.getDesc();
    }
}
