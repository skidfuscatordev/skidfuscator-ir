package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IincInsnNode;

public class IincInsn extends AbstractInsn<IincInsnNode> {
    public IincInsn(Hierarchy hierarchy, IincInsnNode node) {
        super(hierarchy, node);
    }

    @Override
    public String toString() {
        return "var" + node.var + " += " + node.incr;
    }
}
