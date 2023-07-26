package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;

public class JumpInsn extends AbstractInsn {

    private final JumpInsnNode node;

    public JumpInsn(Hierarchy hierarchy, JumpInsnNode node) {
        super(hierarchy, node);
        this.node = node;
    }

    @Override
    public AbstractInsnNode dump() {
        return node;
    }

    @Override
    public String toString() {
        return "jump " + node.label.getLabel();
    }
}
