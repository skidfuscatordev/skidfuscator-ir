package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IntInsnNode;

public class IntInsn extends AbstractInsn<IntInsnNode> {
    public IntInsn(Hierarchy hierarchy, IntInsnNode node) {
        super(hierarchy, node);
    }

    @Override
    public String toString() {
        return "push(int) " + node.operand;
    }
}
