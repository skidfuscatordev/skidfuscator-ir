package dev.skidfuscator.ir.insn.impl;

import dev.skidfuscator.ir.hierarchy.Hierarchy;
import dev.skidfuscator.ir.insn.AbstractInsn;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;

//TODO
public class InvokeDynamicInsn extends AbstractInsn {

    private final InvokeDynamicInsnNode node;

    public InvokeDynamicInsn(Hierarchy hierarchy, InvokeDynamicInsnNode node) {
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
