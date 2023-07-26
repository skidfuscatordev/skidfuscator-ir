package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;

public class SimpleInsn extends AbstractInsn {
    private final InsnNode node;

    public SimpleInsn(Hierarchy hierarchy, InsnNode node) {
        super(hierarchy, node);
        this.node = node;
    }

    @Override
    public AbstractInsnNode dump() {
        return node;
    }
}
