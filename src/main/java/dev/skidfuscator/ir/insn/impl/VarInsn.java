package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class VarInsn extends AbstractInsn<VarInsnNode> {
    public VarInsn(Hierarchy hierarchy, VarInsnNode node) {
        super(hierarchy, node);
    }
}
