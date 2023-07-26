package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import org.objectweb.asm.tree.VarInsnNode;

public class VarInsn extends AbstractInsn {

    private final VarInsnNode node;

    public VarInsn(Hierarchy hierarchy, VarInsnNode node) {
        super(hierarchy, node);
        this.node = node;
    }

    @Override
    public void resolve() {

    }

    @Override
    public void dump() {

    }
}
