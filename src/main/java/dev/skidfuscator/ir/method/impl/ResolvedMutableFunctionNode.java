package dev.skidfuscator.ir.method.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.klass.KlassNode;
import dev.skidfuscator.ir.method.FunctionNode;
import dev.skidfuscator.ir.signature.SignatureWrapper;
import dev.skidfuscator.ir.type.TypeWrapper;
import dev.skidfuscator.ir.util.Descriptor;
import dev.skidfuscator.ir.variable.LocalVariable;
import org.jgrapht.traverse.DepthFirstIterator;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class ResolvedMutableFunctionNode extends ResolvedAbstractFunctionNode {
    private String name;
    // TODO: Add support for types
    private TypeWrapper desc;
    private SignatureWrapper sig;

    public ResolvedMutableFunctionNode(Hierarchy hierarchy, Descriptor descriptor, MethodNode node, Set<FunctionNode> parent) {
        super(hierarchy, descriptor, node);

        this.name = super.getName();
        this.desc = new TypeWrapper(Type.getMethodType(super.getDesc()), hierarchy);
        this.sig = new SignatureWrapper(super.getSignature(), hierarchy);
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

        if (!this.sig.isResolved()) {
            this.sig.resolveHierarchy();
        }

        super.resolveHierarchy();
    }

    @Override
    public void resolveInternal() {
        // TODO: fix this shit
        super.resolveInternal();
    }

    @Override
    public boolean isMutable() {
        final Iterator<FunctionNode> iterator = new DepthFirstIterator<>(hierarchy.getFunctionGraph());
        return Stream.iterate(iterator.next(), e -> iterator.hasNext(), e -> iterator.next())
                .noneMatch(FunctionNode::isLocked);
    }

    @Override
    public boolean isLocked() {
        return !mutable;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        if (!mutable)
            throw new IllegalStateException("Cannot modify name of immutable function node");

        this.name = name;

        final Iterator<FunctionNode> iterator = new DepthFirstIterator<>(hierarchy.getFunctionGraph());
        Stream.iterate(iterator.next(), e -> iterator.hasNext(), e -> iterator.next())
                .forEach(e -> {
                    if (e instanceof ResolvedMutableFunctionNode) {
                        final ResolvedMutableFunctionNode func = (ResolvedMutableFunctionNode) e;
                        func.name = name;
                    } else {
                        throw new IllegalStateException(String.format(
                           "Function %s is not of mutable state (type: %s)",
                                e,
                                e.getClass().getName()
                        ));
                    }
                });
    }

    @Override
    public String getDesc() {
        return desc.isResolved() ? desc.dump().getDescriptor() : super.getDesc();
    }

    @Override
    public String getSignature() {
        return sig.isResolved() ? sig.dump() : super.getSignature();
    }
}
