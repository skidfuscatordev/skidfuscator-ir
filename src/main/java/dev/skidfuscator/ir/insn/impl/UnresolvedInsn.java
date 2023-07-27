package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import org.objectweb.asm.tree.AbstractInsnNode;

public class UnresolvedInsn extends AbstractInsn<AbstractInsnNode> {
    public UnresolvedInsn(Hierarchy hierarchy, AbstractInsnNode node) {
        super(hierarchy, node);
    }
}
