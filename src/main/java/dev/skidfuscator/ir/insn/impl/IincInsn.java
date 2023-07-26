package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IincInsnNode;

public class IincInsn extends AbstractInsn {

    private final IincInsnNode node;

    public IincInsn(Hierarchy hierarchy, IincInsnNode node) {
        super(hierarchy, node);
        this.node = node;
    }

    @Override
    public void resolve() {

    }

    @Override
    public AbstractInsnNode dump() {
        return node;
    }
}
