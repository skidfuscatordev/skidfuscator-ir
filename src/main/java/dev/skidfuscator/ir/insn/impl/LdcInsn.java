package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.ConstantInsn;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;

public class LdcInsn extends ConstantInsn {
    private final LdcInsnNode node;

    public LdcInsn(Hierarchy hierarchy, LdcInsnNode node) {
        super(hierarchy, node);
        this.node = node;
    }

    @Override
    public void resolve() {
        this.constant = node.cst;
    }

    @Override
    public AbstractInsnNode dump() {
        this.node.cst = constant;
        return node;
    }
}
