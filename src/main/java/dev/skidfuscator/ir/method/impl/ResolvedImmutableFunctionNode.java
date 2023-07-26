package dev.skidfuscator.ir.method.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.util.Descriptor;
import org.objectweb.asm.tree.MethodNode;

public class ResolvedImmutableFunctionNode extends ResolvedAbstractFunctionNode {
    public ResolvedImmutableFunctionNode(Hierarchy hierarchy, Descriptor descriptor, MethodNode node) {
        super(hierarchy, descriptor, node);
    }
}
